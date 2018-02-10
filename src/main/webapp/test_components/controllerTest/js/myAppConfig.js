/**
 * Created by poem on 2016/3/11.
 * 在这儿配置，可以改变默认的{{}}解析字符
 */
myApp.config(['$interpolateProvider',function($interpolateProvider){
    $interpolateProvider.startSymbol("__");
    $interpolateProvider.endSymbol("__");
}]);
/**
 * 定义新的服务，用来解析传入的字符串
 */
myApp.factory("myServer",['$interpolate',function($interpolate){
    /**自定义的服务的功能 实现插值运算
     * 及把text 中的满足变量要求，如上面定义的 __to__,默认为{{to}} 定义为一个变量，使用context来替换

     * 在context 中，使用的是对象{to : $scope.otherText} 就是使用$scope.otherText的值来换取在text中的{{to}}
     * 如，{myText:$scope.otherText} 表示使用$scope.otherText中的值来换取在text中的{{myText}}
     */
    return {
        parse:function(text,context){
            if(text){
                var template = $interpolate(text);
                return template(context)
            }
        }
    }
}]);