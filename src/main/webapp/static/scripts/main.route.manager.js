/**
 * Created by yineng on 2016/12/23.
 */

/*************************************************** 路由管理 ********************************************************************/
myApp.config(["$httpProvider", "$stateProvider", "$urlRouterProvider", function ($httpProvider, $stateProvider, $urlRouterProvider) {

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
        .state("platform", {
            url: "/platform",
            templateUrl: basePath + "/pages/platform/main/main.html"
        }).state("welcome", {
        url: "/welcome",
        templateUrl: basePath + "/pages/platform/welcome/welcome.html"
    }).state("enjoy",{
        url:"/enjoy",
        templateUrl:basePath + "/pages/platform/enjoy/enjoy.html"
    }).state("favorite",{
        url:"/favorite",
        templateUrl:basePath + "/pages/platform/favorites/favorites.html"
    }).state("metrics",{
        url:"/metrics",
        templateUrl:basePath + "/pages/metrics/metrics.html"
    });

    /**
     * 默认路由
     */
    $urlRouterProvider.otherwise("/platform");
}]);


