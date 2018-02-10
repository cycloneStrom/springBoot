/**
 * Created by YN on 2016/3/18.
 */
angular.module("myApp","ngRoute").config(["$routeProvider",function($routeProvider){

    /**
     * 第一个的控制器
     */
    var view1Controller = function(){

    };

    /**
     * 第二个的控制器
     */
    var view2Controller = function(){

    };

    /**
     *  路由的配置信息
     */
    $routeProvider.when(
        /*路由的路径 会和$location.path匹配，还可以有参数，参数以冒号开头*/
        "/",
        /*配置对象  controller,template,templateUrl,resolve,redirectTo,reloadOnSearch*/
        {
            /**
             * 会根据templateUrl属性所指定的路径通过XHR读取师徒(或者是从$templatCache中读取)
             */
            templateUrl:"view/view1.html:name",

            /**
             * 如果配置对象中设置了controller属性，那么就指定的控制器与路由所创建的新作用域关联，
             * 如果是字符模型，会在模块中所有注册的控制器中找
             * 如果参数是个函数，这个函数会作为模板DOM元素的控制器并且与模板关联
             */
            controller:view1Controller,

            /**
             * 会将配置的对象的HTML模板渲染到具有ng-view指令的DOM元素中
             */
            template:"<div class='active'></div>",

            /**
             * 如果设置了resolve angular会将列表的元素都注入到控制器中
             * 如果是promise对象，他们会在控制器加载以及$routeChangeSuccess出发之前，会被resolve设置成一个值
             */
            resolve:{
                "data":["$http",function($http){
                    return $http.get("/api").then(
                        function success(response) {return response.data;},
                        function error(reason) {return false;}
                    )
                }]
            },

            /**
             * 如果是个字符串，那么路径会被替换成这个字符串，并且根据这个目标出发路由变化
             * 如果是个函数，那么路径会这转换成函数的返回值
             *     函数的参数：
             *              第一个 从当前路径中取出的路由的参数
             *              第二个 当前的路径
             *              第三个 当前URL中的字符串
             */
            redirectTo:"/home",//function(route,path,search){}

            /**
             * 这个默认是true，当$location.search()发生变化的时候，会重新加载路由
             * 如果被设置成false，在URL中查询串部分变化时就不会重新加载路由
             */
            reloadOnSearch:true
        }).when("/1",{
        templateUrl:"view/view1.htm2",
        controller:view2Controller
    })
}]);
