/**
 * Created by poem on 2016/12/24.
 */
myApp.controller("editInfoController", ["$scope", "$http", "$location", "$document", "notification", "localStorageService", function ($scope, $http, $location, $document, notification, localStorageService) {

    /**
     * the app basepath
     */
    $scope.basePath = basePath;

    /**
     * auto  don't show file upload directive
     * @type {boolean}
     */
    $scope.uploader  = false;

    /**
     * get current login  user id
     * it is not null
     */
    $scope.userId = localStorageService.get(platformSystemUserId);
    /**
     * getUserInfo
     * get user base info by this
     */
    $scope.getUserInfo = function () {
        $http.get(basePath + "/action/userInfo/queryPlatfromUserById?userId=" + $scope.userId).success(function (data) {
            if (data.status == 0) {
                $scope.platformSysUserVO = data.result;
            } else {
                notification.notify("error", "保存失败!");
            }
        }).error(function (data) {
            notification("error", data.message);
        })
    };

    /**
     *  save user base info
     */
    $scope.saveUserBaseInfo = function () {
        $http.post(basePath + "/action/userInfo/savePlatformSysUser",parseSendParmToSpringMVC($scope.platformSysUserVO)).success(function(data){
            if(data.status == 0){
                notification.notify("success","保存成功")
            }else{
                notification.notify("error","保存失败!")
            }
        }).error(function(data){
            notification.notify("error",data.message);
        })
    };

    /**
     * file upload config
     * @type {{}}
     */
    /**
     * file upload config
     * @type {{url: string, callback: $scope.config.callback, accept: string[]}}
     */
    $scope.config = {
        url: basePath + "/fileManger/upload",
        callback: function (fastDfsIds) {
            $scope.$apply(function () {
                if(angular.isArray(fastDfsIds)){
                    $scope.platformSysUserVO.headUrl = fastDfsIds[0];
                }else{
                    $scope.platformSysUserVO.headUrl = fastDfsIds;
                }
                /**show user new head image and close file upload directive*/
                $scope.uploader  = false;
            })
        },
        accept: ["image/gif", "image/x-ms-bmp", "image/bmp", "image/jpeg", "image/jpg", "image/png", "image/svg"],
        defaultShow: false,
        multiple:false// multiple file upload is false
    };
    /**
     * begin file upload
     */
    $scope.uploaderFile = function () {
        $scope.uploader = !$scope.uploader;
    };
    /**
     * begin query base info
     */
    $scope.getUserInfo();
}]);