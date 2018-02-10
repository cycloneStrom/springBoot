/*
 * Constructor 
 */
function JavaLargeFileUploader() {
    var basePath = "/fs";
    var globalServletMapping = "uploader/largeFileUploader.htm";
    /*回传文件的对象*/
    var pendingFiles = new Object();
    var bytesPerChunk;

    var javaLargeFileUploaderHost = "";
    var progressPollerRefreshRate = 1000;
    var maxNumberOfConcurrentUploads = 5;
    /*是否自动重试*/
    var autoRetry = true;
    var autoRetryDelay = 5000;
    var errorMessages = new Object();
    errorMessages[0] = "Request failed for an unknown reason, please contact an administrator if the problem persists.";
    errorMessages[1] = "The request is not multipart.";
    errorMessages[2] = "No file to upload found in the request.";
    errorMessages[3] = "CRC32 Validation of the part failed.";
    errorMessages[4] = "The request cannot be processed because a parameter is missing.";
    errorMessages[5] = "Cannot retrieve the configuration.";
    errorMessages[6] = "No files have been selected, please select at least one file!";
    errorMessages[7] = "Resuming file upload with previous slice as the last part is invalid.";
    errorMessages[8] = "Error while uploading a slice of the file";
    errorMessages[9] = "Maximum number of concurrent uploads reached, the upload is queued and waiting for one to finish.";
    errorMessages[10] = "An exception occurred. Retrying ...";
    errorMessages[11] = "Connection lost. Automatically retrying in a moment.";
    errorMessages[12] = "You do not have the permission to perform this action.";
    errorMessages[13] = "FireBug is enabled, you may experience issues if you do not disable it while uploading.";
    errorMessages[14] = "File corrupted. An unknown error has occured and the file is corrupted. The usual cause is that the file has been modified during the upload. Please clear it and re-upload it.";
    errorMessages[15] = "File is currently locked, retrying in a moment...";
    errorMessages[16] = "Uploads are momentarily disabled, retrying in a moment...";
    var exceptionsRetryable = [0, 3, 7, 8, 10, 11, 15, 16];

    this.setJavaLargeFileUploaderHost = function (javaLargeFileUploaderHostI) {
        javaLargeFileUploaderHost = javaLargeFileUploaderHostI;
    };

    this.setMaxNumberOfConcurrentUploads = function (maxNumberOfConcurrentUploadsI) {
        maxNumberOfConcurrentUploads = maxNumberOfConcurrentUploadsI;
    };

    this.getErrorMessages = function () {
        return errorMessages;
    };

    this.setProgressPollerRefreshRate = function (progressPollerRefreshRateI) {
        progressPollerRefreshRate = progressPollerRefreshRateI;
    };

    this.setAutoRetry = function (autoRetryBoolean, autoRetryDelayI) {
        autoRetry = autoRetryBoolean;
        autoRetryDelay = autoRetryDelayI;
    };

    /**
     *  初始化上传的信息
     * @param initializationCallback 回调函数
     * @param exceptionCallback    错误信息的处理
     * @param optionalClientOrJobIdentifier
     */
    this.initialize = function (initializationCallback, exceptionCallback, optionalClientOrJobIdentifier) {
        var appended = "";
        if (optionalClientOrJobIdentifier) {
            appended = "&clientId=" + optionalClientOrJobIdentifier;
        }
        /*如果可以console，则显示controller*/
        //manageFirebug(exceptionCallback);
        /*获取文件的相关的配置*/
        $.get(javaLargeFileUploaderHost + globalServletMapping + "?action=getConfig" + appended, function (data) {
            if (data) {
                bytesPerChunk = data.inByte;
                /*如果返回值是一个对象*/
                if (!jQuery.isEmptyObject(data.pendingFiles)) {
                    pendingFiles = data.pendingFiles;
                    $.each(data.pendingFiles, function (key, pendingFile) {
                        pendingFile.id = key;
                        pendingFile.fileCompletion = getFormattedSize(pendingFile.fileCompletionInBytes);
                        pendingFile.originalFileSize = getFormattedSize(pendingFile.originalFileSizeInBytes);
                        pendingFile.percentageCompleted = format(pendingFile.fileCompletionInBytes * 100 / pendingFile.originalFileSizeInBytes);
                        /*文件上传是否开始*/
                        pendingFile.started = false;
                    });
                }
                /*执行回调函数*/
                initializationCallback(pendingFiles);
            } else {
                /** 如果出现错误，则显示出异常*/
                if (exceptionCallback) {
                    exceptionCallback(errorMessages[5]);
                }
            }
        });

        // launch the progress poller
        startProgressPoller();

        //initialize the pause all file uploads on close
        $(window).bind('unload', function () {
            pauseAllFileUploadsI(false);
        });


    };

    /**
     * 清理文件的上传
     * @param callback
     */
    this.clearFileUpload = function (callback) {
        $.each(pendingFiles, function (key, pendingFile) {
            pendingFile.cancelled = true;
        });
        pendingFiles = new Object();
        $.get(javaLargeFileUploaderHost + globalServletMapping + "?action=clearAll", function (e) {
            if (callback) {
                callback();
            }
        });
    };

    this.setRateInKiloBytes = function (fileId, rate) {
        if (fileId && rate) {
            $.get(javaLargeFileUploaderHost + globalServletMapping + "?action=setRate&rate=" + rate + "&fileId=" + fileId);
        }
    };

    /**
     *  清除上传的文件
     * @param fileIdI 文件的ID
     * @param callback 回调函数
     */
    this.cancelFileUpload = function (fileIdI, callback) {
        var fileId = fileIdI;
        if (fileId && pendingFiles[fileId]) {
            pendingFiles[fileId].cancelled = true;
            $.get(javaLargeFileUploaderHost + globalServletMapping + "?action=clearFile&fileId=" + fileId, function (e) {
                abort(pendingFiles[fileId], false);
                if (callback) {
                    callback(fileId);
                }
                delete pendingFiles[fileId];
                processNextInQueue();
            });
        }
    };

    this.pauseFileUpload = function (fileIdI, callback) {
        pauseFileUploads(true, [fileIdI], callback);
    };


    /*********************************************** 暂停上传 ********************************************************************/
    /**
     * 暂停所有上传的文件
     * @param callback 回调函数
     */
    this.pauseAllFileUploads = function (callback) {
        pauseAllFileUploadsI(true, callback);
    };

    /**
     * 暂定所有的文件
     * @param async 是不是同步
     * @param callback 回调的函数
     */
    function pauseAllFileUploadsI(async, callback) {
        var fileIds = [];
        for (var fileId in pendingFiles) {
            fileIds.push(fileId);
        }
        pauseFileUploads(async, fileIds, callback);
    }

    /**
     * 暂定文件的上传
     * @param async 是不是同步
     * @param fileIds 文件的IDList
     * @param callback 暂停文件上传的回调的函数
     */
    function pauseFileUploads(async, fileIds, callback) {
        var filesToSend = [];
        for (var i in fileIds) {
            var fileId = fileIds[i];
            /*pendingFiles 正在上传的文件*/
            if (fileId && pendingFiles[fileId] && isFilePaused(pendingFiles[fileId]) === false && pendingFiles[fileId].resuming === false) {
                if (pendingFiles[fileId].queued === true) {
                    pendingFiles[fileId].paused = true;
                } else {
                    pendingFiles[fileId].pausing = true;
                    filesToSend.push(fileId);
                }
            }
        }
        /*给后台发送数据 暂停*/
        if (filesToSend.length > 0) {
            $.ajax({
                url: javaLargeFileUploaderHost + globalServletMapping + "?action=k&fileId=" + filesToSend,
                success: function () {
                    for (var i in fileIds) {
                        var fileId = fileIds[i];
                        var pendingFile = pendingFiles[fileId];
                        pendingFile.pausedCallback = callback;
                        abort(pendingFile, true);
                    }
                },
                async: async
            });
        }
    }

    /**
     * 终止计划
     * @param pendingFile 上传的文件
     * @param forPauseBool
     */
    function abort(pendingFile, forPauseBool) {
        if (pendingFile.xhr) {
            pendingFile.xhr.abort();
        }
        if (forPauseBool) {
            setTimeout(function () {
                //if still paused after a certain delay, we unblock it
                if (pendingFile.paused === false) {
                    notifyPause(pendingFile);
                }
            }, 2000);
        }
    }

    /**
     * 标注上传的文件
     * @param pendingFile
     */
    function notifyPause(pendingFile) {
        if (pendingFile.pausing) {
            uploadEnd(pendingFile, false);
            pendingFile.paused = true;
            if (pendingFile.pausedCallback) {
                pendingFile.pausedCallback(pendingFile);
            }
        }
    }

    /**
     * 文件是不是已经暂停
     * @param pendingFile
     * @returns {boolean|*}
     */
    function isFilePaused(pendingFile) {
        return pendingFile.paused || pendingFile.pausing;
    }


    /*********************************************** 暂停上传 ********************************************************************/
    /*********************************************** 重置上传 ********************************************************************/

    this.resumeFileUpload = function (fileIdI, callback) {
        this.resumeFileUploads([fileIdI], callback);
    };
    /**
     * 重置所有的文件
     * @param callback
     */
    this.resumeAllFileUploads = function (callback) {
        var fileIds = [];
        for (var fileId in pendingFiles) {
            fileIds.push(fileId);
        }
        this.resumeFileUploads(fileIds, callback);
    };

    /**
     * 重置上传的文件
     * @param fileIds 文件的ID
     * @param callback 回调的函数
     */
    this.resumeFileUploads = function (fileIds, callback) {
        for (var i in fileIds) {
            var fileIdI = fileIds[i];
            if (fileIdI && pendingFiles[fileIdI]) {
                if (pendingFiles[fileIdI].paused === true && pendingFiles[fileIdI].resuming === false) {
                    pendingFiles[fileIdI].resuming = true;
                    resumeFileUploadInternal(pendingFiles[fileIdI], callback);
                }
            }
        }
    };

    this.retryFileUpload = function (fileIdI, callback) {
        if (fileIdI && pendingFiles[fileIdI]) {
            resumeFileUploadInternal(pendingFiles[fileIdI], null, callback);
        }
    };

    /**
     * 抛出异常 并且显示出来
     * @param pendingFile 文件
     * @param errorMessageId 错误的信息的ID
     */
    function displayException(pendingFile, errorMessageId) {
        /* 显示错误的信息*/
        console.log(errorMessages[errorMessageId]);
        if (pendingFile.exceptionCallback) {
            pendingFile.exceptionCallback(errorMessages[errorMessageId], pendingFile.referenceToFileElement, pendingFile);
        }
    }

    function retryStart(pendingFile) {
        setTimeout(retryRecursive, autoRetryDelay, pendingFile);
    }

    function retryRecursive(pendingFile) {
        if (pendingFile) {
            displayException(pendingFile, 10);
            resumeFileUploadInternal(pendingFile, null, function (ok) {
                if (ok === false) {
                    displayException(pendingFile, 11);
                    retryStart(pendingFile);
                }
            });
        }
    }

    /**
     * 重置问价
     * @param pendingFile 重置的文件的ID
     * @param callback 回调函数
     * @param retryCallback 再次操作的回调函数
     */
    function resumeFileUploadInternal(pendingFile, callback, retryCallback) {
        if (pendingFile) {
            //and restart flow
            $.get(javaLargeFileUploaderHost + globalServletMapping + "?action=resumeFile&fileId=" + pendingFile.id, function (data) {
                if (callback) {
                    callback(pendingFile);
                }
                //populate crc data
                pendingFile.crcedBytes = data.crcedBytes;
                pendingFile.fileCompletionInBytes = data.fileCompletionInBytes;

                //try to validate the unvalidated chunks and resume it
                fileResumeProcessStarter(pendingFile);
            }).success(function (e) {
                if (retryCallback) {
                    retryCallback(true);
                }
            }).error(function (e) {
                if (retryCallback) {
                    retryCallback(false);
                }
            });
        }
    }

    /********************************************************************************************/
                                    /********/
                                    /********/
                           /****  这儿是文件上传的入口    ****/
                                    /********/
                                    /********/
    /********************************************************************************************/

    /**
     * 开始上传
     * @param referenceToFileElement 文件控件的元素
     * @param startCallback 开始的回调函数
     * @param progressCallback 显示上传进度方法
     * @param finishCallback 完成的调用的方法
     * @param exceptionCallback 错误调用的方法
     */
    this.fileUploadProcess = function (referenceToFileElement, startCallback, progressCallback,
                                       finishCallback, exceptionCallback) {
        /*从上传的文件元素中读取文件的信息*/
        var allFiles = extractFilesInformation(referenceToFileElement, startCallback, progressCallback,finishCallback, exceptionCallback);
        /*将它复制到另一个数组中，它将包含新的文件来处理*/
        var potentialNewFiles = allFiles.slice(0);

        //try to corrolate information with our pending files
        //corrolate with filename  size and crc of first chunk
        //start resuming if we have a match
        //if we dont have any name/size math, we process an upload
        var potentialResumeCounter = new Object();
        potentialResumeCounter.counter = 0;
        for (var fileKey in allFiles) {
            var pendingFile = allFiles[fileKey];
            /*在文件中查找他自己的验证信息*/
            for (var pendingFileToCheckKey in pendingFiles) {
                var pendingFileToCheck = pendingFiles[pendingFileToCheckKey];
                if (pendingFileToCheck.originalFileName == pendingFile.originalFileName &&
                    pendingFileToCheck.originalFileSizeInBytes == pendingFile.originalFileSizeInBytes) {

                    /*我们可能会有一场匹配，增加一个匹配的记录*/
                    potentialResumeCounter.counter++;

                    /*验证文件的切片*/
                    /*当所有完成时，该方法将照顾新文件处理*/
                    processCrcFirstSlice(potentialNewFiles, pendingFile, pendingFileToCheck, potentialResumeCounter);
                }
            }
        }
        /*如果没有文件碎片，则重新开始新的文件*/
        if (potentialResumeCounter.counter === 0 && potentialNewFiles.length > 0) {
            processNewFiles(potentialNewFiles);
        }

    };

    /**
     *文件的切片
     * @param blob 需要切片的文件
     * @param callback 回调函数
     */
    function extractCrcFirstSlice(blob, callback) {
        /*计算切片*/
        var end = 8192;
        if (blob.size < 8192) {
            end = blob.size;
        }
        /*文件读取*/
        var reader = new FileReader();
        reader.onloadend = function (e) {
            /*如果文件分块读取完成，调用回调的方法*/
            if (e.target.readyState == FileReader.DONE) { // DONE == 2
                callback(decimalToHexString(crc32(e.target.result)), blob);
            }
        };
        reader.readAsBinaryString(slice(blob, 0, end));
    }


    /**
     * 生成文件碎片
     * @param potentialNewFiles 完整的文件
     * @param pendingFileI  连接的文件
     * @param pendingFileToCheckI   验证文件
     * @param potentialResumeCounter 碎片数量
     */
    function processCrcFirstSlice(potentialNewFiles, pendingFileI, pendingFileToCheckI, potentialResumeCounter) {
        /*准备切片的校验*/
        var pendingFile = pendingFileI;
        var pendingFileToCheck = pendingFileToCheckI;
        /*已经切片*/
        extractCrcFirstSlice(pendingFile.blob, function (crc, blob) {
            /*如果当前的文件还没有完*/
            if (potentialNewFiles.indexOf(pendingFile) != -1) {
                /*计算并且比较文件，继续读取*/
                /*如果是正确的文件，则继续读取*/
                if (crc == pendingFileToCheck.firstChunkCrc) {
                    /*删除它从新的文件系统（因为我们现在肯定它不是一个新的文件）*/
                    potentialNewFiles.splice(potentialNewFiles.indexOf(pendingFile), 1);
                    /*如果该文件未被上传*/
                    if (!pendingFileToCheck.started) {
                        /*如果该文件上传暂停*/
                        if (isFilePaused(pendingFileToCheck)) {
                            /*重新上传文件*/
                            resumeFileUploadInternal(pendingFileToCheck);
                        } else {
                            /*跟新文件的信息，在初始化中检索*/
                            pendingFile.fileCompletionInBytes = pendingFileToCheck.fileCompletionInBytes;
                            pendingFile.crcedBytes = pendingFileToCheck.crcedBytes;
                            pendingFile.firstChunkCrc = pendingFileToCheck.firstChunkCrc;
                            pendingFile.started = pendingFileToCheck.started;
                            pendingFile.id = pendingFileToCheck.id;
                            /*把它放进等待文件数组*/
                            pendingFiles[pendingFileToCheck.id] = pendingFile;
                            /*开始上传文件*/
                            fileResumeProcessStarter(pendingFile);
                        }
                    }
                } else {
                    console.log("Invalid resume crc for " + pendingFileToCheck.originalFileName + ". processing as a new file.");
                }
                /*减量潜力恢复计数器*/
                potentialResumeCounter.counter--;
                if (potentialResumeCounter.counter === 0 && potentialNewFiles.length > 0) {
                    /*如果是最后一个，处理新的文件*/
                    processNewFiles(potentialNewFiles);
                }

            }
        });
    }


    /**
     * 从上传的文件元素中读取文件的信息
     * @param referenceToFileElements 文件控件的元素
     * @param startCallback 开始的回调函数
     * @param progressCallback 显示上传进度方法
     * @param finishCallback 完成的调用的方法
     * @param exceptionCallback 错误调用的方法
     * @returns {Array} 文件数组
     */
    function extractFilesInformation(referenceToFileElements, startCallback, progressCallback, finishCallback, exceptionCallback) {
        var retArray = [];
        /*如果当前是一个文件的数组*/
        if ($.isArray(referenceToFileElements)) {
            for (var i = 0; i < referenceToFileElements.length; i++) {
                retArray = retArray.concat(extractSingleElementFilesInformationProcess(referenceToFileElements[i], startCallback, progressCallback, finishCallback, exceptionCallback));
            }
        } else {
            retArray = extractSingleElementFilesInformationProcess(referenceToFileElements, startCallback, progressCallback, finishCallback, exceptionCallback);
        }
        return retArray;
    }


    /**
     * 获取文件的信息
     * @param referenceToFileElement 当前的文件所在的元素
     * @param startCallback  开始的回调函数
     * @param progressCallback 显示上传进度方法
     * @param finishCallback 完成的调用的方法
     * @param exceptionCallback 错误调用的方法
     * @returns {Array}
     */
    function extractSingleElementFilesInformationProcess(referenceToFileElement, startCallback, progressCallback,
                                                         finishCallback, exceptionCallback) {
        var newFiles = [];

        /**提取文件的信息*/
        var files = referenceToFileElement.files;
        if (!files.length) {
            /*如果没有文件，则跑出异常*/
            if (exceptionCallback) {
                exceptionCallback(errorMessages[6], referenceToFileElement);
            }
        } else {
            for (var fileKey in files) {
                var file = files[fileKey];
                if (file.name && file.size) {
                    /*初始化一个文件的基本信息*/
                    var pendingFile = new Object();

                    pendingFile.originalFileName = file.name;
                    pendingFile.originalFileSizeInBytes = file.size;
                    pendingFile.originalFileSize = getFormattedSize(pendingFile.originalFileSizeInBytes);
                    pendingFile.blob = file;
                    pendingFile.progressCallback = progressCallback;
                    pendingFile.referenceToFileElement = referenceToFileElement;
                    pendingFile.startCallback = startCallback;
                    pendingFile.finishCallback = finishCallback;
                    pendingFile.exceptionCallback = exceptionCallback;
                    pendingFile.paused = false;
                    pendingFile.pausing = false;
                    pendingFile.resuming = false;
                    /*把它放到临时的新文件数组中，因为每一个文件都有可能成为一个新文件，直到它被证明它不是一个新文件*/
                    newFiles.push(pendingFile);
                }
            }
        }
        return newFiles;
    }

    /**
     * 上传一个新的文件
     * @param newFiles
     */
    function processNewFiles(newFiles) {
        /*为新文件留下，准备开始*/
        var jsonVersionOfNewFiles = [];
        var newFilesIds = 0;
        var crcsCalculated = 0;
        for (var pendingFileId in newFiles) {
            var pendingFile = newFiles[pendingFileId];
            /*准备文件请求对象*/
            var fileForPost = new Object();

            fileForPost.tempId = newFilesIds;
            fileForPost.fileName = pendingFile.originalFileName;
            fileForPost.size = pendingFile.originalFileSizeInBytes;
            jsonVersionOfNewFiles[fileForPost.tempId] = fileForPost;
            pendingFiles[fileForPost.tempId] = pendingFile;
            newFilesIds++;

            /*提取第一条数据信息*/
            pendingFile.blob.i = fileForPost.tempId;
            extractCrcFirstSlice(pendingFile.blob, function (crc, blob) {
                /*blob  原始文件       crc 碎片*/
                jsonVersionOfNewFiles[blob.i].crc = crc;
                pendingFiles[blob.i].firstChunkCrc = crc;
                crcsCalculated++;
                if (crcsCalculated == jsonVersionOfNewFiles.length) {
                    /*发送后台检验上传的信息*/
                    $.getJSON(javaLargeFileUploaderHost + globalServletMapping + "?action=prepareUpload", {newFiles: JSON.stringify(jsonVersionOfNewFiles)}, function (data) {
                        /*现在用新的数据来填充本地条目*/
                        $.each(data, function (tempIdI, fileIdI) {
                            /*现在我们有文件标识，我们可以分配对象*/
                            var fileId = fileIdI;
                            pendingFile = pendingFiles[tempIdI];
                            pendingFile.id = fileId;
                            pendingFile.fileComplete = false;
                            pendingFile.fileCompletionInBytes = 0;
                            pendingFiles[fileId] = pendingFile;
                            delete pendingFiles[tempIdI];
                            /*调用回调函数  进度条*/
                            if (pendingFile.startCallback) {
                                pendingFile.startCallback(pendingFile, pendingFile.referenceToFileElement);
                            }
                            /*开始上传文件*/
                            fileUploadProcessStarter(pendingFile);
                        });
                    });
                }
            });

        }
    }

    /**
     * 第一次执行文件的上传
     * @param pendingFile 等待上传的文件
     */
    function fileResumeProcessStarter(pendingFile) {
        /*必须确保未经验证的最后一个块更新是正确的*/
        var bytesToValidates = pendingFile.fileCompletionInBytes - pendingFile.crcedBytes;
        /*如果还有没有验证的*/
        if (bytesToValidates > 0) {
            /*切片未验证的部分*/
            var chunk = slice(pendingFile.blob, pendingFile.crcedBytes, pendingFile.fileCompletionInBytes);
            /*添加一个表格数据块 这是数据上传所使用的表单信息，import*/
            var formData = new FormData();
            /*需要上传的文件*/
            formData.append("file", chunk);
            console.log(formData);
            /*准备切片的校验*/
            var reader = new FileReader();
            reader.onloadend = function (e) {
                if (e.target.readyState == FileReader.DONE) { // DONE == 2
                    /*计算块读的文件块*/
                    var digest = crc32(e.target.result);
                    /*发送文件*/
                    console.log("开始文件碎片验证。");
                    /************************************ 发送文件 *************************************/
                    $.get(javaLargeFileUploaderHost + globalServletMapping + "?action=verifyCrcOfUncheckedPart&fileId=" + pendingFile.id + "&crc=" + decimalToHexString(digest), function (data) {
                        /*检查我们是否有异常*/
                        if (data.value) {
                            displayException(pendingFile, data.value);
                            if (autoRetry && isExceptionRetryable(data.value)) {
                                /*重新提交*/
                                retryStart(pendingFile);
                            }
                        } else {
                            /*数据验证*/
                            if (data === false) {
                                displayException(pendingFile, 7);
                                console.log("crc verification failed for unchecked chunk, filecompletion is truncated to " + pendingFile.crcedBytes + " (was " + pendingFile.fileCompletionInBytes + ")");
                                pendingFile.fileCompletionInBytes = pendingFile.crcedBytes;
                            }
                            /*然后处理上传*/
                            fileUploadProcessStarter(pendingFile);
                        }
                    });
                }
            };
            //read the chunk to calculate the crc
            reader.readAsBinaryString(chunk);
        }
        //if we dont have bytes to validate, process
        else {
            //if everything is good, resume it:
            fileUploadProcessStarter(pendingFile);
        }
    }

    /**
     * 比较文件队列的大小
     * @returns {boolean}
     */
    function canUploadBeProcessed() {
        var numberOfUploadsCurrentlyBeingProcessed = 0;
        for (fileId in pendingFiles) {
            var pendingFile = pendingFiles[fileId];
            if (pendingFile.started) {
                numberOfUploadsCurrentlyBeingProcessed++;
            }
        }
        //we can process only if we are under the capacity
        return numberOfUploadsCurrentlyBeingProcessed < maxNumberOfConcurrentUploads;
    }


    /**
     * 开始文件上传的进程
     * @param pendingFile 上传的文件
     */
    function fileUploadProcessStarter(pendingFile) {
        /*如果文件不完整*/
        if (pendingFile.fileCompletionInBytes < pendingFile.originalFileSizeInBytes) {
            /*重置一些标签*/
            pendingFile.paused = false;
            pendingFile.pausing = false;
            pendingFile.resuming = false;
            /*检查我们是否可以处理上传*/
            if (canUploadBeProcessed() === true) {
                pendingFile.end = pendingFile.fileCompletionInBytes + bytesPerChunk;
                pendingFile.started = true;
                pendingFile.queued = false;
                //console.log("处理文件" + pendingFile.id + "，碎片大小为：" + pendingFile.fileCompletionInBytes + " - " + pendingFile.end);
                go(pendingFile);
            } else {
                /*queue it*/
                pendingFile.queued = true;
                //specify to user
                displayException(pendingFile, 9);
            }
        }
        else {
            /*标记，文件上传完成*/
            pendingFile.fileComplete = true;
        }
    }

    /**
     * 文件碎片
     * @param blob
     * @param start
     * @param end
     * @returns {*}
     */
    function slice(blob, start, end) {

        if (blob.slice) {
            return blob.slice(start, end);
        } else if (blob.mozSlice) {
            return blob.mozSlice(start, end);
        } else {
            return blob.webkitSlice(start, end);
        }
    }


    /**
     * 开始文件的上传
     */
    function go(pendingFile) {
        /*每一次上传，都要去检查一个bug，这样好展示一些新的信息*/
        manageFirebug(pendingFile.exceptionCallback);
        /*如果文件标识在正在挂起的文件中*/
        var chunk = slice(pendingFile.blob, pendingFile.fileCompletionInBytes, pendingFile.end);
        /*数据表单   模拟上传的数据 */
        var formData = new FormData();
        formData.append("file", chunk);
        /*准备切片的校验*/
        var reader = new FileReader();
        /*读取完成，无论成功失败*/
        reader.onloadend = function (e) {
            if (e.target.readyState == FileReader.DONE) { // DONE == 2
                /*计算块读的结*/
                var digest = crc32(e.target.result);
                /*准备XHR请求*/
                var xhr;
                if(window.XMLHttpRequest){
                    xhr=new XMLHttpRequest();
                }else if(window.ActiveXObject){
                    var activeName=["MSXML2.XMLHTTP","Microsoft.XMLHTTP"];
                    for(var i=0;i<activeName.length;i++){
                        try{
                            xhr=new ActiveXObject(activeName[i]);
                            break;
                        }catch(e){
                            console.error(e);
                        }
                    }
                }
                pendingFile.xhr = xhr;

                /*分配暂停回调*/
                xhr.addEventListener("abort", function (event) {
                    notifyPause(pendingFile);
                }, false);
                /*开始文件上传，异步请求*/
                xhr.open(
                    'POST',
                    basePath + '/uploadServletAsync/async/async?action=upload&fileId=' + pendingFile.id + '&crc=' + decimalToHexString(digest),
                    true
                );
                /*处理请求的信息*/
                xhr.onreadystatechange = function () {
                    if (xhr.readyState == 4) {
                        /*如果我们暂停或取消，我们只是回归*/
                        if (pendingFile.pausing || pendingFile.cancelled) {
                            return;
                        }
                        /*如果有错误，就即系执行*/
                        if (xhr.status != 200) {
                            displayException(pendingFile, 8);
                            if (autoRetry) {
                                //submit retry
                                retryStart(pendingFile);
                            }
                            uploadEnd(pendingFile, true);
                            return;
                        }
                        /*如果出现错误*/
                        if (xhr.response) {
                            var resp = JSON.parse(xhr.response);
                            displayException(pendingFile, resp.value);
                            if (autoRetry && isExceptionRetryable(resp.value)) {
                                /*重新提交数据*/
                                retryStart(pendingFile);
                            }
                            uploadEnd(pendingFile, true);
                            return;
                        }
                        // progress
                        pendingFile.fileCompletionInBytes = pendingFile.end;
                        pendingFile.end = pendingFile.fileCompletionInBytes + bytesPerChunk;

                        /*检查我们是否需要继续*/
                        if (pendingFile.fileCompletionInBytes < pendingFile.originalFileSizeInBytes) {
                            /*递归调用 */
                            window.setTimeout(go, 5, pendingFile);
                        } else {
                            pendingFile.fileComplete = true;
                            uploadEnd(pendingFile, false);
                            /*完成回调*/
                            if (pendingFile.finishCallback) {
                                pendingFile.finishCallback(pendingFile, pendingFile.referenceToFileElement);
                            }
                        }
                    }
                };

                /*发送请求*/
                try {
                    /*只有发送，如果它是悬而未决的，因为它可以被要求取消，而我们正在阅读的文件！*/
                    if (pendingFiles[pendingFile.id]) {
                        /*如果我们不暂停或取消*/
                        if (!isFilePaused(pendingFile) && !pendingFile.cancelled) {
                            xhr.send(formData);
                        }
                    }
                } catch (e) {
                    console.error(e);
                    uploadEnd(pendingFile, true);
                    displayException(pendingFile, 8);
                    if (autoRetry) {
                        retryStart(pendingFile);
                    }
                }
            }
        };
        /*将文件读取为二进制编码 读取块*/
        reader.readAsBinaryString(chunk);
    }


    /**
     * uploadEnd
     * @param pendingFile 上传的文件的对象
     * @param withException 是否跑出异常
     */
    function uploadEnd(pendingFile, withException) {

        //the file is not started anymore
        pendingFile.started = false;

        //process the queue if it was not an exception and if there is no auto retry
        if (withException === false) {
            processNextInQueue();
        }
    }

    /**
     * 队列进程
     */
    function processNextInQueue() {
        for (fileId in pendingFiles) {
            if (pendingFiles[fileId].queued && !pendingFiles[fileId].paused) {
                fileUploadProcessStarter(pendingFiles[fileId]);
                return;
            }
        }
    }


    /*
     * inspired from http://codeaid.net/javascript/convert-seconds-to-hours-minutes-and-seconds-(javascript)
     */
    function getFormattedTime(secs) {
        if (secs < 1) {
            return "";
        }
        /*小时*/
        var hours = Math.floor(secs / (60 * 60));
        /*分钟*/
        var divisor_for_minutes = secs % (60 * 60);
        var minutes = Math.floor(divisor_for_minutes / 60);
        /*秒*/
        var divisor_for_seconds = divisor_for_minutes % 60;
        var seconds = Math.ceil(divisor_for_seconds);

        var returned = '';
        if (hours > 0) {
            returned += hours + ":";
        }
        if (minutes > 0) {
            returned += minutes + ":";
        }else{
            returned +=  "0:";
        }

        returned += seconds + "";

        return returned;
    }

    function getFormattedSize(size) {
        if (size < 1024) {
            return format(size) + 'B';
        } else if (size < 1048576) {
            return format(size / 1024) + 'KB';
        } else if (size < 1073741824) {
            return format(size / 1048576) + 'MB';
        } else if (size < 1099511627776) {
            return format(size / 1073741824) + 'GB';
        } else if (size < 1125899906842624) {
            return format(size / 1099511627776) + 'TB';
        }
    }

    function format(size) {
        return Math.ceil(size * 100) / 100;
    }

    function uploadIsActive(pendingFile) {
        //process only if we have this id in the pending files and if the file is incomplete and if the file is not paused and if the file is started!
        return pendingFile && pendingFiles[pendingFile.id] && !isFilePaused(pendingFile) && !pendingFile.fileComplete && pendingFile.started;
    }

    function isExceptionRetryable(errorId) {
        return (exceptionsRetryable.indexOf(parseInt(errorId)) != -1);
    }

    function manageFirebug(exceptionCallback) {
        //if firebug is enabled, show exception
        if (window.console && (window.console.firebug || window.console.exception)) {
            if (exceptionCallback) {
                exceptionCallback(errorMessages[13]);
            } else {
                alert(errorMessages[13]);
            }
        }
    }

    /**
     * 获取文件的上传的进程
     */
    function startProgressPoller() {
        /*文件数组*/
        var fileIds = [];
        for (var fileId in pendingFiles) {
            var pendingFile = pendingFiles[fileId];
            if (uploadIsActive(pendingFile) && pendingFile.progressCallback) {
                fileIds.push(fileId);
            }
        }
        if (fileIds.length > 0) {
            $.getJSON(javaLargeFileUploaderHost + globalServletMapping + "?action=getProgress", {fileId: JSON.stringify(fileIds)}, function (data) {
                $.each(data, function (fileId, progress) {
                    //console.log(progress);
                    var pendingFile = pendingFiles[fileId];
                    /*如果在查询时未删除该等待文件状态*/
                    if (uploadIsActive(pendingFile)) {
                        /*如果有关于传输效率的信息*/
                        if (progress.uploadRate != undefined) {
                            var uploadRate = getFormattedSize(progress.uploadRate);
                        }
                        /*如果有剩下的文件的信息*/
                        if (progress.estimatedRemainingTimeInSeconds != undefined) {
                            var estimatedRemainingTimeInSeconds = getFormattedTime(progress.estimatedRemainingTimeInSeconds);
                        }
                        //keep progress
                        pendingFile.percentageCompleted = format(progress.progress);
                        // specify progress
                        pendingFile.progressCallback(pendingFile, pendingFile.percentageCompleted, uploadRate, estimatedRemainingTimeInSeconds,
                            pendingFile.referenceToFileElement);
                    }
                });
            }).complete(function () {
                //reschedule when the have the answer
                window.setTimeout(startProgressPoller, progressPollerRefreshRate);
            });
        } else {
            window.setTimeout(startProgressPoller, progressPollerRefreshRate);
        }
    }
    /*
     ===============================================================================
     Crc32 is a JavaScript function for computing the CRC32 of a string
     ...............................................................................

     Version: 1.2 - 2006/11 - http://noteslog.com/post/crc32-for-javascript/

     -------------------------------------------------------------------------------
     Copyright (c) 2006 Andrea Ercolino
     http://www.opensource.org/licenses/mit-license.php
     ===============================================================================
     */

    var strTable = "00000000 77073096 EE0E612C 990951BA 076DC419 706AF48F E963A535 9E6495A3 0EDB8832 79DCB8A4 E0D5E91E 97D2D988 09B64C2B 7EB17CBD E7B82D07 90BF1D91 1DB71064 6AB020F2 F3B97148 84BE41DE 1ADAD47D 6DDDE4EB F4D4B551 83D385C7 136C9856 646BA8C0 FD62F97A 8A65C9EC 14015C4F 63066CD9 FA0F3D63 8D080DF5 3B6E20C8 4C69105E D56041E4 A2677172 3C03E4D1 4B04D447 D20D85FD A50AB56B 35B5A8FA 42B2986C DBBBC9D6 ACBCF940 32D86CE3 45DF5C75 DCD60DCF ABD13D59 26D930AC 51DE003A C8D75180 BFD06116 21B4F4B5 56B3C423 CFBA9599 B8BDA50F 2802B89E 5F058808 C60CD9B2 B10BE924 2F6F7C87 58684C11 C1611DAB B6662D3D 76DC4190 01DB7106 98D220BC EFD5102A 71B18589 06B6B51F 9FBFE4A5 E8B8D433 7807C9A2 0F00F934 9609A88E E10E9818 7F6A0DBB 086D3D2D 91646C97 E6635C01 6B6B51F4 1C6C6162 856530D8 F262004E 6C0695ED 1B01A57B 8208F4C1 F50FC457 65B0D9C6 12B7E950 8BBEB8EA FCB9887C 62DD1DDF 15DA2D49 8CD37CF3 FBD44C65 4DB26158 3AB551CE A3BC0074 D4BB30E2 4ADFA541 3DD895D7 A4D1C46D D3D6F4FB 4369E96A 346ED9FC AD678846 DA60B8D0 44042D73 33031DE5 AA0A4C5F DD0D7CC9 5005713C 270241AA BE0B1010 C90C2086 5768B525 206F85B3 B966D409 CE61E49F 5EDEF90E 29D9C998 B0D09822 C7D7A8B4 59B33D17 2EB40D81 B7BD5C3B C0BA6CAD EDB88320 9ABFB3B6 03B6E20C 74B1D29A EAD54739 9DD277AF 04DB2615 73DC1683 E3630B12 94643B84 0D6D6A3E 7A6A5AA8 E40ECF0B 9309FF9D 0A00AE27 7D079EB1 F00F9344 8708A3D2 1E01F268 6906C2FE F762575D 806567CB 196C3671 6E6B06E7 FED41B76 89D32BE0 10DA7A5A 67DD4ACC F9B9DF6F 8EBEEFF9 17B7BE43 60B08ED5 D6D6A3E8 A1D1937E 38D8C2C4 4FDFF252 D1BB67F1 A6BC5767 3FB506DD 48B2364B D80D2BDA AF0A1B4C 36034AF6 41047A60 DF60EFC3 A867DF55 316E8EEF 4669BE79 CB61B38C BC66831A 256FD2A0 5268E236 CC0C7795 BB0B4703 220216B9 5505262F C5BA3BBE B2BD0B28 2BB45A92 5CB36A04 C2D7FFA7 B5D0CF31 2CD99E8B 5BDEAE1D 9B64C2B0 EC63F226 756AA39C 026D930A 9C0906A9 EB0E363F 72076785 05005713 95BF4A82 E2B87A14 7BB12BAE 0CB61B38 92D28E9B E5D5BE0D 7CDCEFB7 0BDBDF21 86D3D2D4 F1D4E242 68DDB3F8 1FDA836E 81BE16CD F6B9265B 6FB077E1 18B74777 88085AE6 FF0F6A70 66063BCA 11010B5C 8F659EFF F862AE69 616BFFD3 166CCF45 A00AE278 D70DD2EE 4E048354 3903B3C2 A7672661 D06016F7 4969474D 3E6E77DB AED16A4A D9D65ADC 40DF0B66 37D83BF0 A9BCAE53 DEBB9EC5 47B2CF7F 30B5FFE9 BDBDF21C CABAC28A 53B39330 24B4A3A6 BAD03605 CDD70693 54DE5729 23D967BF B3667A2E C4614AB8 5D681B02 2A6F2B94 B40BBE37 C30C8EA1 5A05DF1B 2D02EF8D".split(' ');

    var table = new Array();
    for (var i = 0; i < strTable.length; ++i) {
        table[i] = parseInt("0x" + strTable[i]);
    }

    /* Number */
    function crc32(/* String */ str) {
        var crc = 0;
        var n = 0; //a number between 0 and 255
        var x = 0; //an hex number

        crc = crc ^ (-1);
        for (var i = 0, iTop = str.length; i < iTop; i++) {
            n = ( crc ^ str.charCodeAt(i) ) & 0xFF;
            crc = ( crc >>> 8 ) ^ table[n];
        }
        return crc ^ (-1);
    }

    /**
     *  文件编码
     * @param number
     * @returns {string}
     */
    function decimalToHexString(number) {
        if (number < 0) {
            number = 0xFFFFFFFF + number + 1;
        }

        return number.toString(16).toLowerCase();
    }
}