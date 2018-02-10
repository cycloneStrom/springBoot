/**
 * Created by YN on 2016/3/18.
 *
 * $scope 作用域
 * localStorageServer 本地储存服务
 */
myApp.controller("serversController",["$scope","localStorageServer",function($scope,localStorageServer){

    /**
     * 用户名
     * @type {string}
     */
    $scope.username = "";
    /**
     * 密码
     * @type {string}
     */
    $scope.password = "";

    /**
     * 登录用户
     */
    $scope.login = function(){

    };

    /**
     * 取消用户登录
     */
    $scope.cancelLogin = function(){

    };
}]);