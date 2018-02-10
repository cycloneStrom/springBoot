/**
 * Created by poem on 2016/3/12.
 * 我的过滤器测试版本
 */
myApp.controller("myFilterController", ["$scope", function ($scope) {

    /**
     * 显示过滤的字符数组
     * @type {Array}
     */
    $scope.filterList = [];

    $scope.objectList = [];

    /*字符串*/
    for (var i = 0; i < 5; i++) {
        $scope.filterList.push(myScript.getCharacters(10));
    }

    /*对象*/
    angular.forEach($scope.filterList, function (item, index) {
        $scope.objectList.push({
            'index': index,
            'name': item,
            'like same': myScript.getCharacters(20)
        })
    });

    /**
     * 过滤器 filter后面的函数
     */
    var filterFun = function () {
        return function (charcaters) {
            return charcaters + " --- filterFun";
        };
    };
    /**
     * 自定义拦截器
     * @type {{}}
     */
    $scope.autoFilter = {
        currecy: 123456,
        date: {
            medium: new Date(),
            short: new Date(),
            fullDate: new Date(),
            longDate: new Date(),
            mediumDate: new Date(),
            shortDate: new Date(),
            mediumTime: new Date(),
            shortTime: new Date()
        },
        filter: $scope.filterList,
        json: $scope.filterList,
        lintTo: $scope.filterList,
        lowercase: $scope.filterList,
        orderBy: $scope.filterList
    }
}]);
