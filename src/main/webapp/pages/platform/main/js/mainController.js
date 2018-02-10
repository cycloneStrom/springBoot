/**
 * Created by poem on 2016/4/28.
 */
myApp.controller("mainController", ["$scope", "$http", "$location", "$document", "notification", "localStorageService", function ($scope, $http, $location, $document, notification, localStorageService) {


    /**
     * 初始化一个新的消息实体
     * @type {{}}
     */
    $scope.initEmptyItem = function () {
        $scope.emptyItem = {
            messageInfo: "",
            fileList: []
        }
    };
    /**
     * 配置
     * @type {{}}
     */
    $scope.optionsBox = {};
    /**
     * 保存信息
     */
    $scope.saveShortMessage = function () {
        if ($scope.emptyItem.fileId == undefined) {
            $scope.emptyItem.fileId = [];
        }
        angular.forEach($scope.fastDfsIdList, function (file) {
            if (!$scope.emptyItem.fileId.some(function (f) {
                    return f.fileId == file;
                })) {
                $scope.emptyItem.fileId.push({
                    fileName: "",
                    fileId: file
                })
            }
        });
        $http.post(basePath + "/business/platformShortMessageInfo/saveShortMsg", parseSendParmToSpringMVC($scope.emptyItem))
            .success(function (data, status, headers, config) {
                if (data.status == 0) {
                    notification.notify("sucess", "保存成功！");
                    $scope.emptyItem.messageInfo = null;
                    $scope.fastDfsIdList = [];
                    $scope.emptyItem.fileId = [];
                    $scope.optionsBox.refresh();
                } else {
                    notification.notify("error", "保存失败！");
                }
            }).error(function (data) {
            notification.notify("error", data)
        });
    };

    /**
     * 文件上传
     * @type {{url: string, callback: $scope.config.callback, accept: string[]}}
     */
    $scope.config = {
        url: basePath + "/fileManger/upload",
        callback: function (fastDfsIds) {
            $scope.$apply(function () {
                $scope.fastDfsIdList = fastDfsIds;
            })
        },
        accept: ["image/gif", "image/x-ms-bmp", "image/bmp", "image/jpeg", "image/jpg", "image/png", "image/svg"],
        defaultShow: false
    };

    /**
     * 刪除附件
     * @param fastDfsId
     */
    $scope.deleteFile = function (fastDfsId) {
        notification.confirm("warinng", "确定要删除？", function () {
            $http.get(basePath + "/fileManger/delete?fastDFSId=" + fastDfsId).success(function (data) {
                if (data.status == 0) {/*删除成功*/
                    var index = -1;
                    angular.forEach($scope.fastDfsIdList, function (id, ind) {
                        if (id === fastDfsId) {
                            index = ind;
                        }
                    });
                    if (-1 != index) {
                        $scope.fastDfsIdList.splice(index, 1);

                    }
                } else {
                    //删除失败
                }
            }).error(function (data) {
                //删除失败
            })
        }, function () {
            console.log();
        });
    };

    (function () {
        $("#test").tooltip("show");
    })()
}]);