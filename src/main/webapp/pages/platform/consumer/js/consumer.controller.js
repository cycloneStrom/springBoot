/**
 * Created by poem on 2016/12/24.
 */
"use strict";
angular
    .module("myApp")
    .controller("consumerController", consumerController);
consumerController.$inject = ["$scope", "$http", "$location", "notification", "localStorageService"];
function consumerController($scope, $http, $location, notification, localStorageService) {

    /**
     * app base path
     */
    $scope.basePath = basePath;
    /**
     * get user info from url
     * @type {string}
     */
    var localUrl = decodeURI(window.location.href);
    var params = localUrl.split(/[?&]/);
    var our = params[2].replace("our=", "");

    /**
     * get user id from url
     */
    $scope.userId = params[1].replace("user=", "");

    /**
     * get  current user id from cookie
     */
    $scope.currentUserId = localStorageService.get(platformSystemUserId);

    /**
     *  judgement this user if current user
     *  and  we  checked that left user info box is  displayed
     * @type {boolean}
     */
    $scope.isCurrent = $scope.userId == $scope.currentUserId;
    /**
     * 編輯消息
     */
    $scope.editBaseInfo = function () {
        $location.path("/editinfo")
    };

    /**
     * manage user base info
     * we will show it in left box
     */
    $scope.manageUserInfo = function () {
        $scope.genderName = ($scope.platformSysUserVO.gender == "1") ? "男" : "女";
        console.log($scope.genderName);
    };
    /**
     * get user base info bu user user id
     */
    $scope.getUserBaseInfo = function () {
        $http.get(basePath + "/action/userInfo/queryPlatfromUserById?userId=" + $scope.userId).success(function (data) {
            if (data.status == 0) {
                $scope.platformSysUserVO = data.result;
                $scope.manageUserInfo();
            } else {
                notification.notify("error", "query fail!");
            }
        }).error(function (data) {
            notification("error", data.message);
        })
    };

    /**
     * auto query user  base info
     */
    $scope.getUserBaseInfo();
}