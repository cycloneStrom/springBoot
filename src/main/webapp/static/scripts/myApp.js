/**
 * Created by lyw on 2016/4/13.
 * angular App
 * 所有的关于angular myApp的都定义在这儿
 */
var myApp = angular.module("myApp", ["ngMessages", "ui.router", "LocalStorageModule"]);

/**************************************************** 配置  (身份验证) ********************************************************************/
/**
 * 对路由的配置
 */
myApp.config(["$locationProvider", function ($locationProvider) {
    /*不使用html魔术*/
    $locationProvider.html5Mode(false);
    /*前缀 这儿先不配置这个，因为有一个默认的#*/
    //$locationProvider.hashPrefix("-#");
    /*hashbang語法，為的是使搜索引擎能夠檢索到，或者是使用<noscript>來包含我們的應用*/
    $locationProvider.hashPrefix("!");
}]);


/**************************************************** 拦截器 ********************************************************************/
/*
 *  拦截器
 *      request angular通过$http设置对象来请求响应拦截器惊醒调用。他可以这是对象的修改，或者是创建一个新的对象，它需要返回一个更新过的设置对象
 *      response angular通过$http设置对象来对清秀的想用进行调用，它可以对设置进行修改，或者是创建一个新的响应，它需要返回一个更新过的响应，活着一个可以返回新的响应的promise
 *      requestError angular会在上已请求拦截器抛出错误，或者是promise被inject是调用此拦截器
 *      responseError 会在上一个响应拦截器抛出错误或者是promise被inject是调用次拦截器
 * */
myApp.factory("myInterceptor", ['$q', '$rootScope', '$window', '$injector', "$location",function ($q, $rootScope, $window, $injector,$location) {
    var isSameRequest = function (config, requestStack) {
        var method = config.method.toLocaleUpperCase(),
            index,
            isSame;
        if (method && method === 'POST') {
            isSame = requestStack.some(function (config_, index_) {
                index = index_;
                if (config_.url != config.url) {
                    return false;
                } else {
                    if (config.data == null || config_.data == null) {
                        return config.data == config_.data;
                    }
                    //对比传递参数中可枚举属性
                    var propertys = Object.keys(config.data),
                        propertys_ = Object.keys(config_.data);
                    if (propertys.length != propertys_.length) {
                        return false;
                    } else {
                        var len = propertys.length - 1,
                            isEqual = true;
                        while (len >= 0) {
                            var key = propertys[len];
                            if (config.data[key] != config_.data[key]) {
                                isEqual = false;
                                break;
                            }
                            len--;
                        }
                        return isEqual;
                    }
                }
            });
        } else {
            isSame = requestStack.some(function (config_, index_) {
                index = index_;
                return config.url.split("_templeTime")[0] == config_.url.split("_templeTime")[0]
                    && angular.equals(config_.params, config.params);
            });
        }
        return {isSame: isSame, index: index};
    };
    return {
        'request': function (config) {
            config.url = decodeURI(config.url);
            //相同菜单下进行拦截
            if ($location.path() !== $window.currentPath) {
                $window.currentPath = $location.path();
                /*请求栈队*/
                $window.requestStack = [];
            }
            var components = config.url.split('?');
            if (components.length > 1) {
                config.url = components[0] + "?" + decodeURIComponent(components[1]);
            }
            var url = config.url;
            /*附加参数 防止对请求的缓存*/
            if(config.method == "GET"){
                /*没有请求的参数*/
                if (url.indexOf('?') == -1){
                    if(url.split(".").length == 1){
                        config.url = url + "?_templeTime=" + new Date().getTime();
                    }
                } else {
                    if (url.split('?')[0].split('.').length == 1) {
                        config.url = url + "&_templeTime=" + new Date().getTime();
                    }
                }
            }
            /*成功请求的方法*/
            return config; // 或者是 $q.when(config);
        },
        'response': function (response) {
            var requestStack = $window.requestStack,
                isSameObj = isSameRequest(response.config, requestStack);
            if (isSameObj.isSame) {
                requestStack.splice(isSameObj.index, 1);
            }
            /*响应成功后*/
            return response; //或者是 $q.when(response)
        },
        'requestError': function (rejection) {
            $window.requestStack = [];
            /*请求发生错误，如果能从中回复，可以返回一个新的请求或者是一个promise*/
            //return response; 或者是一个新的promise对象
            /*或者是返货一个rejection来阻止下一步*/
            return $q.reject(rejection);
        },
        'responseError': function (rejection) {
            /*发生错误，如果可以从错误中恢复，可以返回一个新的响应活着是promise*/
            //return rejection
            /*通过放回一个rejection来阻止下一步*/
            if(rejection.status == 399){
                $window.requestStack = [];
                var relogin = $injector.get("reLoginService");
                if(relogin){
                    relogin.reLogin();
                }
                console.log("系统已关闭");
                return ;
            }
            if (rejection.status == 0) {
                if (!!window.ActiveXObject || "ActiveXObject" in window) {
                    var relogin = $injector.get("reLoginService");
                    if(relogin){
                        relogin.reLogin();
                    }
                    console.log("系统已关闭");
                    return;
                }
            }
            if (rejection.status == 404) {
                console.log('系统已关闭，请联系管理员！');
                return;
            }
            if (rejection.status == 500) {
                console.log('服务器忙，请刷新后重试！');
            }
            return $q.reject(rejection);
        }
    }
}]);

/**
 * 定义看一个新的拦截去，需要把拦截器注入到应用中
 */
myApp.config(["$httpProvider", "$cacheFactoryProvider",function ($httpProvider,$cacheFactoryProvider) {
    $httpProvider.interceptors.push("myInterceptor");
    /*取消IE中的緩存行為*/
    $httpProvider.defaults.headers.common["Cache-Control"] = "no-cache";
    // AngularJS将拦截器排入一个队列中，用于触发信息拦截
    $httpProvider.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";

    /*將使用$http默認的緩存機制*/
    $httpProvider.defaults.cache = $cacheFactoryProvider.$get();
    /*可以創建一個屬於自己的緩存
     var myCache = $cacheFactory("myCache");
     $httpProvider.defaults.cache = $cacheFactory("myCache",{capacity:20});/*這樣就生成了一個默認的$http緩存，緩存20條最新的緩存數目*/

    //整个的框架都支持CSRF
    $httpProvider.defaults.xsrfCookieName = 'CSRF-TOKEN';
    $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';
}]);

/**
 * 当前的登录人管理
 */
myApp.service("loginUserManager",["$http","$location",function($http,$location){
    var userInfo = null;
    var loginUserInfoMange = {};
    /*使用ajax angular不会拦截*/
    loginUserInfoMange.getLoginUserInfo = function() {
        $.ajax({
            type: "GET",
            url: basePath + "/commonController/queryForLoginInfoFactory?_templeTime=" + new Date().getTime(),
            dataType: "json",
            async: false
        }).done(function(data){
            userInfo = data.result;
        });
        return userInfo;
    };
    return loginUserInfoMange;
}]);
/**
 * 登录信息管理
 */
myApp.service("loginUserInfoStorage",["$rootScope","$injector","localStorageService",function($rootScope,$injector,localStorageService){
    var userId = null,
        getCurrentPlatformSysUserId = function(){
            var userVo = $rootScope.platformSysUserVO;
            if(userVo){
                return $rootScope.platformSysUserVO.id;
            }
            var loginUserManager =  $injector.get("loginUserManager");
            if(loginUserManager){
                return loginUserManager.getLoginUserInfo();
            }
        };
    return {
        /**
         * 重新管理当前的登录用户
         */
        manageUser:function(){
            var loginUserManager =  $injector.get("loginUserManager");
            if(loginUserManager){
                var currentUser = loginUserManager.getLoginUserInfo();
                if(currentUser && currentUser.id != userId){
                    userId = currentUser.id;
                    localStorageService.set(platformSystemUserId,userId);
                    localStorageService.set(platformSystemUser,currentUser.platformSysUserVO);
                    window.location.reload(true);
                }
            }
        },
        /*第一次登录的时候需要去获取当前的登录人的信息*/
        firstUserManage:function(){
            if(userId == null){
                var loginUserManager =  $injector.get("loginUserManager");
                if(loginUserManager){
                    var user = getCurrentPlatformSysUserId();
                    if(user){
                        userId = user.id;
                        localStorageService.set(platformSystemUserId,userId);
                        localStorageService.set(platformSystemUser,user.platformSysUserVO);
                    }else{
                        window.location.href = basePath + "/login";
                    }
                }
            }
        }
    }
}]);
/**
 * RUN模块，在整个angularJs的框架中优先执行
 */
myApp.run(function($rootScope, $location, $state,$injector) {
    /*获取当前的登录人的信息*/
    var loginUserInfoStorage = $injector.get("loginUserInfoStorage");
    if(loginUserInfoStorage){
        loginUserInfoStorage.firstUserManage();
    }
});