/**
 * Created by poem on 2016/4/28.
 * 顶部的菜单
 */
myApp.directive("topMenu",function(){
    return {
        /*定义的标签的类型  A 属性 E 元素 C 类   M 注释*/
        restrict:"AE",
        /*模板地址，相对于应用界面的的位置*/
        templateUrl:"../../components/01-topmenu/template/topMenu.html",
        /*是不是替换*/
        replace:true,
        /*作用域  */
        scope:{},
        controller:["$scope","$http","$location","localStorageService",function($scope,$http,$location,localStorageService){

            /**
             * 顶部输入搜索框
             * @type {{info: string}}
             */
            $scope.mainSearch = {
                info:""
            };

            /**
             * 点击查询
             */
            $scope.search = function(){
                if(isNotBlankStr($scope.mainSearch.info)){
                    console.log($scope.mainSearch.info);
                }
            };

            /**
             * 对出登录
             */
            $scope.logOut = function(){
                $http.post(basePath + "/api/logout").success(function(date){
                    localStorageService.clearAll();
                    window.location.href = basePath + "/login.htm";
                })
            };

            /**
             * 监听事件的传播
             */
            $scope.$on(broadcast,function(event,data){
                console.log(data);
            })
        }],
        link:function(tElement,tAttrs,transclude,injectControllers){
            $(".theme-item").bind("click",function(event){
                setSkin($(event.currentTarget).attr("date-type"));
            });
        }
    }
});
