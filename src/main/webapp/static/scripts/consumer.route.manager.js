/**
 * Created by poem on 2016/12/24.
 */
myApp.config(["$httpProvider", "$stateProvider", "$urlRouterProvider",function ($httpProvider, $stateProvider, $urlRouterProvider) {

    //从url获取请求所需要的参数
    var localUrl = decodeURI(window.location.href);
    var params = localUrl.split(/[?&]/);
    var our = params[2].replace("our=","");
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
    //TODO  今后优化任务
    $stateProvider
        .state("message", {
            url: "/message",
            templateUrl: basePath + "/pages/platform/consumer/message/message.html"
        }).state("editinfo", {
        url: "/editinfo",
        templateUrl: basePath + "/pages/platform/consumer/editinfo/editinfo.html"
    });

    /**
     * 默认路由
     */
    if(our == "true"){
        $urlRouterProvider.otherwise("/editinfo");
    }else{
        $urlRouterProvider.otherwise("/message");
    }
}]);