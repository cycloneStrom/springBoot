/**
 * Created by lyw on 2016/12/21.
 * 资源文件的上传管理，全部使用JavaScript控制
 */
'use strict';

myApp.service("fileUploader",function(){
    /**
     * 定义对象
     * @type {{}}
     */
    var fileUploader = new Object();

    /**
     * 全局
     * @type {Array}
     */
    var fastDfsId = [];
    /**
     * 获取XHR请求
     */
    var getXHR = function(){
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
        return xhr;
    };

    /**
     *  上传文件
     * @param file 上传的文件信息
     */
    var up = function(file,callback,errorCallback){
        var formData = new FormData();
        formData.append("file", file);
        var reader = new FileReader();
        /**文件读取*/
        reader.onloadend = function(e){
            /** 文件读取完成*/
            if(e.target.readyState == FileReader.DONE){
                var xhr = getXHR();
                /**同步请求*/
                xhr.open("POST",fileUploader.url,false);
                /**请求信息处理*/
                xhr.onreadystatechange = function(){
                    if(xhr.readyState == 4){
                        /**  上传返回状态不正常*/
                        if(xhr.status != 200){
                            console.error("上传错误！");
                            return false;
                        }
                        /** 上传请求返回信息*/
                        if(xhr.response){
                            return false;
                        }
                    }
                };
                /*发送文件*/
                try {
                    xhr.send(formData);
                    var id = xhr.responseText;
                    if(id != "-1"){
                        callback(id);
                    }else{
                        errorCallback()
                    }
                } catch (e) {
                    console.error(e);
                }
            }
        };
        /*将文件读取为二进制编码 读取块*/
        reader.readAsBinaryString(slice(file,0,file.size));
    };

    /**
     *  取文件的二进制码
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
     * 文件信息
     * @param referenceToFileElement
     */
    var exteactFile = function(referenceToFileElement,callback,errorCallback){
        var newFiles = [],index = 0;
        fastDfsId = [];
        /**提取文件的信息*/
        var files = referenceToFileElement.files;
        if(files.length){
            for (var fileKey in files) {
                var file = files[fileKey];
                if(file.name && file.size){
                    up(file,function(id){
                        if(!fastDfsId.some(function(fsId){
                                return fsId === id;
                            })){
                            fastDfsId.push(id);
                            index++;
                            /**传完了才调用回调函数*/
                            if(index == files.length){
                                callback(fastDfsId);
                            }
                        }
                    },errorCallback);
                }
            }
        }else{
            console.log("no file");
        }
        return newFiles;
    };

    /**
     * 初始化
     * @param config
     */
    fileUploader.init =  function(config){
        if(!config.url){
            throw new Error("url is not null!")
        }
        fileUploader.url = config.url;
    };

    /**
     * 上传文件
     * @param referenceToFileElement 上传的文件
     * @param callback 成功的回调函数
     * @param errorCalback 失败回调函数
     */
    fileUploader.uploadFile = function (referenceToFileElement, callback,errorCallback) {
        exteactFile(referenceToFileElement,callback,errorCallback);
    };

    return fileUploader;
});