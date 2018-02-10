/**
 * Created by poem on 2016/5/8.
 * 右边用户信息管理
 */
myApp.directive("rightMenu", function () {
    return {
        replace: true,
        /*定义的标签的类型  A 属性 E 元素 C 类   M 注释*/
        restrict: "AE",
        templateUrl: basePath + "/components/04-rightmenu/template/rightMenu.html",
        controller: ["$scope","$element","$attrs","localStorageService",function ($scope, $element, $attrs,localStorageService) {
            $scope.user = localStorageService.get(platformSystemUser);
            console.log($scope.user);

            /**
             * 打开用户管理
             */
            $scope.consumerManage = function () {
                self.location = basePath + "/pages/platform/consumer/index.html";
            };
        }],
        link: function (tElement, tAttrs, transclude) {
        }
    }
});