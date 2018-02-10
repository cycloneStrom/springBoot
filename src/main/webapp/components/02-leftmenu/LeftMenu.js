/**
 * Created by poem on 2016/4/17.
 * 左边菜单管理
 */
myApp.directive("leftMenu", ["notification",function (notification) {
    return {
        replace: true,
        /*定义的标签的类型  A 属性 E 元素 C 类   M 注释*/
        restrict:"AE",
        templateUrl: "../../components/02-leftmenu/template/LeftMenu.html",
        controller: ["$scope", "$http", "$location", "myLocalStorageServer", function ($scope, $http, $location, myLocalStorageServer) {
            /**
             * 查询菜单的信息
             */
            $http.get(basePath + "/business/classifyController/queryClassifyByCondition?pageSize=20&pageNumber=0")
                .success(function (data, status, headers, config) {
                    if (data.status == 0) {
                        $scope.leftMenuLists = data.result.content;
                        myLocalStorageServer.saveLeftMenu($scope.leftMenuLists);
                    } else {
                        notification.notify("error","查询菜单失败！");
                    }
                }).error(function (data) {
                    notification.confirm("error",data,function(){return true},function(){return true});
                });

            /**
             * 跳转到页面
             * @param item
             */
            $scope.selectMsg = function(item){
                if(item){
                    /*如果可以跳转，则发布广播*/
                    $scope.$root.$broadcast(broadcast,{key:item.id});
                }
            };

            /**
             * 页面跳转
             * @param index
             */
            $scope.jumpToTarget = function(info){
                if(info){
                    $location.path(info);
                }
            };
        }]
    };
}]);
