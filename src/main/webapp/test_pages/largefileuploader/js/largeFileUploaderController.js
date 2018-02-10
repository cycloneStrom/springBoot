/**
 * Created by YN on 2016/5/11.
 */
var fileUploaderApp = angular.module("fileUploaderApp", []);

/**
 * 大文件上传的controller
 */
fileUploaderApp.controller("fileUploaderController", ["$scope", "$http", "$location", function ($scope, $http, $location) {
    var jlfu = null;
    /**
     * 全局配置
     * @type {{}}
     */
    $scope.option = {
        usePauseAll: true,//使用全部暂停按钮
        useStarAll: true,//使用全部开始按钮
        useClassAll: true //使用清除全部按钮
    };

    /**
     * 文件初始化
     */
    $(document).ready(function () {
        /*初始化文件上传控件*/
        jlfu = new JavaLargeFileUploader();
        /*调用对象的初始化函数*/
        jlfu.initialize(function (pendingFiles) {
            /**如果在之前已经有文件，则循环显示出文件*/
            for (var key in pendingFiles) {
                var pendingFile = pendingFiles[key];
                /**为每一个回传的文件创建一个DOM元素*/
                appendUploadControls(pendingFile.id);
                /*文件上传完场*/
                if (pendingFile.fileComplete) {
                    document.getElementById(pendingFile.id).children[0].textContent = pendingFile.originalFileName + "  (" + (pendingFile.fileCompletion + "") + "/" + pendingFile.originalFileSize + ")";
                    deleteElementsButEmAndCancel(pendingFile.id);
                    /*进度条*/
                    $("#" + pendingFile.id).children(".progressbar").progressbar({
                        value: Math.floor(pendingFile.percentageCompleted)
                    });
                }
                /*文件没有完成*/
                else if (pendingFile.fileCompletion) {
                    /*描述文件，用户好重新上传*/
                    document.getElementById(pendingFile.id).children[0].textContent = pendingFile.originalFileName + "  (" + (pendingFile.fileCompletion + "") + "/" + pendingFile.originalFileSize + ")";
                    /*进度条*/
                    $("#" + pendingFile.id).children(".progressbar").progressbar({
                        value: Math.floor(pendingFile.percentageCompleted)
                    });
                    /*显示传输速率*/
                    if (pendingFile.rateInKiloBytes) {
                        document.getElementById(pendingFile.id).children[2].value = pendingFile.rateInKiloBytes;
                    }
                }
            }
        }, function (message) {
            alert(message);
        },"");
        /*当前上传的对象的个数*/
        jlfu.setMaxNumberOfConcurrentUploads(3);
        jlfu.getErrorMessages()[9] = "More than 3 pending upload, file is queued.";
        /*添加上传的文件*/
        appendFileInputElement();

    });

    /**
     * 获取文件的ID
     */
    function getFileId(element) {
        return element.parentElement.id;
    }

    /**
     * 创建文件上传的HTML元素
     *
     */
    function appendFileInputElement() {
        /*获取基础*/
        var mainContainer = document.getElementById('fileContainer');
        if (mainContainer.children.length > 0) {
            mainContainer.children[mainContainer.children.length - 1].hidden = true;
        }
        var imputfile = document.createElement("div");
        imputfile.setAttribute("class", "file-uploader-input-div");
        imputfile.textContent = "添加文件";
        /** 创建一个文件上传控件*/
        var fileInput = document.createElement("input");
        fileInput.type = "file";
        fileInput.setAttribute('multiple', 'multiple');
        fileInput.setAttribute("class", "file-uploader-file-input");
        imputfile.appendChild(fileInput);
        mainContainer.appendChild(imputfile);

        /** 文件上传控件添加监听事件*/
        fileInput.addEventListener("change", function (e) {
            appendFileInputElement();
            /*文件上传*/
            jlfu.fileUploadProcess(e.target,
                /*定义一个开始回调，以创建将与文件上传进行交互的用户界面*/
                function (pendingFile, origin) {
                    appendUploadControls(pendingFile.id);
                },

                /*定义文件的进度的展示条*/
                function (pendingFile, percentageCompleted, uploadRate, estimatedRemainingTime, origin) {
                    /*
                     pendingFile 文件信息
                     percentageCompleted 完成的进度
                     uploadRate 上传的速度
                     estimatedRemainingTime 剩余的时间
                     */
                    document.getElementById(pendingFile.id).children[0].textContent = pendingFile.originalFileName + "  (" +  pendingFile.originalFileSize + ")";
                    $("#" + pendingFile.id).children(".progressbar").progressbar({
                        value: Math.floor(percentageCompleted)
                    });
                    document.getElementById(pendingFile.id).children[2].textContent = estimatedRemainingTime;
                },

                /*显示完成的元素*/
                function (pendingFile, origin) {
                    document.getElementById(pendingFile.id).children[0].textContent = pendingFile.originalFileName + "  (" + pendingFile.originalFileSize + ")";
                    $("#" + pendingFile.id).children(".progressbar").progressbar({
                        value: Math.floor(100)
                    });
                    deleteElementsButEmAndCancel(pendingFile.id);
                },

                /*定义一个显示错误的元素*/
                function (message, origin, pendingFile /* pending file object that can be null! if null, it is a general error */) {
                    if (pendingFile) {
                        document.getElementById(pendingFile.id).children[0].textContent = message;
                    } else {
                        document.getElementById("error").textContent = message;
                    }
                });
        }, false);
    }

    /**
     * 文件上传完成
     * 删除文件的取消按钮
     */
    function deleteElementsButEmAndCancel(fileId) {
        var div = $('#' + fileId);
        ///*移除元素 detach() 会保留所有绑定的事件、附加的数据*/
        var fileNameDiv = div.children(":first").detach();
        var progressbar = div.children(".progressbar").detach();
        var cancel = div.children(".cancel").detach();
        /*清空*/
        div.empty();
        div.append(fileNameDiv);
        div.append(progressbar);
        div.append(cancel);
        //div.append(document.createElement('hr'));
    }

    /**
     * 创建文件上传的文件的信息
     * @param fileId
     */
    function appendUploadControls(fileId) {
        /*获取文件上传的信息*/
        var uiContainer = document.getElementById('uiContainer');

        /*整个文件区域*/
        var div = document.createElement("div");
        div.setAttribute('id', fileId);
        div.setAttribute("class", "file-uploader-div");
        uiContainer.appendChild(div);

        /*创建文件上传的区域信息*/
        //progress bar
        var progressBar = document.createElement("div");
        progressBar.setAttribute("class", "progressbar file-uploader-progressbar");

        /* 速度输入框*/
        var rateInput = document.createElement("input");
        rateInput.type = "text";

        /*速度按钮*/
        var rateButton = document.createElement("button");
        rateButton.addEventListener("click", function (e) {
            jlfu.setRateInKiloBytes(getFileId(this), this.parentElement.children[2].value);
        });
        rateButton.appendChild(document.createTextNode('Apply'));

        /*取消按钮*/
        var cancel = document.createElement("A");
        cancel.setAttribute("href", "javascript:void(0);");
        cancel.setAttribute("class", "cancel");
        cancel.addEventListener("click", function (e) {
            div.children[0].textContent = "删除中...";
            jlfu.cancelFileUpload(getFileId(this), function (pendingFileId) {
                var div = document.getElementById(pendingFileId);
                div.parentElement.removeChild(div);
            });
        });
        cancel.appendChild(document.createTextNode('删除'));

        /*暂停按钮*/
        var pause = document.createElement("A");
        pause.setAttribute("href", "javascript:void(0);");
        pause.addEventListener("click", function (e) {
            div.children[0].textContent = "暂停中...";
            jlfu.pauseFileUpload(getFileId(this), function (pendingFile) {
                div.children[0].textContent = pendingFile.originalFileName + " is paused.";
            });
        });
        pause.appendChild(document.createTextNode('暂停'));

        /*重新开始按钮*/
        var resume = document.createElement("A");
        resume.setAttribute("href", "javascript:void(0);");
        resume.addEventListener("click", function (e) {
            div.children[0].textContent = "重新开始中...";
            jlfu.resumeFileUpload(getFileId(this), function (pendingFile) {
                div.children[0].textContent = "Resuming " + pendingFile.originalFileName + " ...";
            });
        });
        resume.appendChild(document.createTextNode('重新开始'));

        /*剩余时间显示*/
        var leftTime = document.createElement("span");
        leftTime.setAttribute("class","file-uploader-time");

        /*上传的文件名称*/
        var fileNameSpan = document.createElement("span");
        fileNameSpan.setAttribute("class", "file-uploader-name-span");
        div.appendChild(fileNameSpan);


        div.appendChild(progressBar);
        div.appendChild(leftTime);
        div.appendChild(cancel);
    }

    /**
     * 全部暂停
     */
    $scope.pauseAllFile = function () {
        jlfu.pauseAllFileUploads(function () {
        });
    };

    /**
     * 全部开始
     */
    $scope.starAllFile = function () {
        jlfu.resumeAllFileUploads(function () {
        });
    };

    /**
     * 清除全部
     * @constructor
     */
    $scope.clearAllFile = function () {
        jlfu.clearFileUpload(function (e) {
            window.location.reload();
        });
    }
}]);