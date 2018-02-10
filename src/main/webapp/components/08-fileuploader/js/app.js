/**
 * Created by yineng on 2016/12/21.
 */
var myApp = angular.module("myApp", ["ngMessages", "ui.router", "LocalStorageModule"]);
myApp.controller("fileUploaderController", ["$scope", "$http", "$location", "$document", "localStorageService", function ($scope, $http, $location, $document, localStorageService) {

    /**
     * file-upload config
     * @type {{url: string}}
     */
    $scope.config = {
        url:basePath + "/fileManger/upload",
        callback:function(fastDfsIds){
            console.log("完成！");
            angular.forEach(fastDfsIds,function(i){
                console.log(i);
            })
        },
        accept:["image/gif", "image/x-ms-bmp", "image/bmp"]
    }
}]);