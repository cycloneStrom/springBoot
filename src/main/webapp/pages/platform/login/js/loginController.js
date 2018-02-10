/**
 * Created by lyw on 2016/4/14.
 * 登录controller
 */
var loginModule = angular.module("loginModule", ["ngMessages", "ui.router", "LocalStorageModule","ngAnimate"]);
/**
 * 配置管理
 */
loginModule.config(["$locationProvider","$httpProvider", function ($locationProvider,$httpProvider) {
    /*不使用html魔术*/
    $locationProvider.html5Mode(false);
    /*前缀 这儿先不配置这个，因为有一个默认的#*/
    //$locationProvider.hashPrefix("-#");
    /*hashbang語法，為的是使搜索引擎能夠檢索到，或者是使用<noscript>來包含我們的應用*/
    $locationProvider.hashPrefix("action");

    $httpProvider.defaults.xsrfCookieName = 'CSRF-TOKEN';
    $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';
}]);

/**
 * 登录页面前端路由管理
 */
loginModule.config(["$httpProvider","$stateProvider","$urlRouterProvider",function($httpProvider,$stateProvider,$urlRouterProvider){
    /**
     * 设置请求的路径
     * @type {string}
     */
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
    /**
     * The workhorse; converts an object to x-www-form-urlencoded serialization.
     * @param {Object} obj
     * @return {String}
     */
    var param = function (obj) {
        var query = '',
            name, value, fullSubName, subName, subValue, innerObj, i;

        for (name in obj) {
            value = obj[name];

            if (value instanceof Array) {
                for (i = 0; i < value.length; ++i) {
                    subValue = value[i];
                    fullSubName = name;
                    innerObj = {};
                    innerObj[fullSubName] = subValue;
                    query += param(innerObj) + '&';
                }
            } else if (value instanceof Object) {
                for (subName in value) {
                    subValue = value[subName];
                    fullSubName = name + '[' + subName + ']';
                    innerObj = {};
                    innerObj[fullSubName] = subValue;
                    query += param(innerObj) + '&';
                }
            } else if (value !== undefined && value !== null)
                query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
        }

        return query.length ? query.substr(0, query.length - 1) : query;
    };

    // Override $http service's default transformRequest
    $httpProvider.defaults.transformRequest = [

        function (data) {
            return angular.isObject(data) && String(data) !== '[object File]' ? param(data) : data;
        }
    ];


    /**
     * 页面路由管理
     * 路由生效  如果后面有，则增加在后面，今后优化
     */
        //    //TODO  今后优化任务
    $stateProvider
        .state("login", {
            url: "/login",
            templateUrl: basePath + "/pages/platform/login/template/login.html"
        }).state("register", {
            url: "/register",
            templateUrl: basePath + "/pages/platform/login/template/register.html"
        });

    /**
     * 默认路由
     */
    $urlRouterProvider.otherwise("/login");
}]);

loginModule.controller("loginModelController",["$scope","$http",function($scope,$http){

}]);
/**
 * 登录设置
 */
loginModule.controller("loginController", ["$scope", "$http", "$location", "$window", "$timeout",function ($scope, $http, $location, $window,$timeout) {

    $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
    /**
     * 登录的用户
     * @type {{username: string, password: string}}
     */
    $scope.login = {
        username: "",
        password: ""
    };

    /**
     * 登录按钮
     * @type {string}
     */
    $scope.login_buttom_message = "";

    /**
     * 是否发生全局的错误
     * @type {boolean}
     */
    $scope.error = true;

    /**
     * 验证用户名
     */
    $scope.validateUserName = function () {
        if (!$scope.login.username) {
            $scope.error = true;
            $scope.login_buttom_message = "请输入用户名";
            return false;
        } else {
            $scope.error = false;
            $scope.login_buttom_message = "";
            return true;
        }
    };

    /**
     * 验证密码
     */
    $scope.validatePassword = function () {
        if (!$scope.login.password) {
            $scope.error = true;
            $scope.login_buttom_message = "请输入密码";
            return false;
        } else {
            $scope.error = false;
            $scope.login_buttom_message = "";
            return true;
        }
    };

    /**
     * 登录信息
     */
    $scope.loginForm = function () {
        $scope.error = false;
        $scope.login_buttom_message = "登录";
        if (!$scope.validateUserName()) return false;
        if (!$scope.validatePassword()) return false;
        /*登录*/
        $scope.login.password = $scope.login.password.replace(/(^\s*)|(\s*$)/g, "");
        $http.post(basePath + "/api/login/loginForm", $.param({
            username: $scope.login.username,
            password: $scope.login.password
        })).success(function (data, status, headers, config) {
            if (status != 200) {
                delCookie("myApp_cookies");
            }
            if (data.status == 0) {
                $window.location.href = basePath + "/pages/platform/index.html?platform";
            } else {
                $scope.error = true;
                $scope.login_buttom_message = "用户和密码错误";
            }
        }).error(function (data) {
            $scope.error = true;
            $scope.login_buttom_message = "登录错误";
        })
    };

    /**
     * 注冊
     */
    $scope.Registe = function(){
        $location.path("/register");
    };

    $timeout(function () {
        angular.element('[ng-model="username"]').focus();
    });

    /**
     * 绑定
     */
    angular.element(document).bind("keyup", function (event) {
        if (event.keyCode == 13) {
            $scope.loginForm();
        }
    });
}]);

/**
 * 注册页面
 */
loginModule.controller("registerController",["$scope","$http","$location",function($scope,$http,$location){

    /**
     * 对象的建立
     * @type {{}}
     */
    $scope.register = {
        userName:"",
        password:"",
        sex:"1",
        invitationCode:""
    };

    /**
     * 錯错误消息
     * @type {{id: number, message: string}[]}
     */
    var registeErrorMssage = [
        {
            "id":1,"message":"请输入您的用户名"
        },{
            "id":3,"message":"请输入您的邀请码"
        },{
            "id":2,"message":"请输入您的密码"
        }
    ];
    /**
     * 注册
     */
    $scope.registe = function(){
        if (!isNotBlankStr($scope.register.userName)){
            return false;
        }
        if (!isNotBlankStr($scope.register.password)){
            return false;
        }
        if (!isNotBlankStr($scope.register.invitationCode)){
            return false;
        }
        $http.post(basePath + "/api/login/regiestAccount",$scope.register).success(function(data, status, headers, config){
            if(data.status == 0){
                console.log(data);
            }
        }).error(function(data, status, headers, config){
            console.log(data);
        });
    };
    /**
     * 去登录页面
     */
    $scope.loginPage = function(){
        $location.path("/login");
    }
}]);