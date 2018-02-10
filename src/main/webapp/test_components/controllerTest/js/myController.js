/**
 * Created by poem on 2016/3/11.
 */
myApp.controller("myController",["$scope","myServer",function($scope,myServer){
    /**
     *  来实现一个对象
     * @type {{}}
     */
    $scope.itemObject = {

    };

    /**
     * 这儿的监听服务，监听出
     * 监听传入的第一个，如果发生变化，则会调用后面的相关的方法
     */
    $scope.$watch("emailBody",function(newValue){
        /**调用的方法，用注入的服务去操作相关的应用*/
        $scope.previewText = myServer.parse(newValue,{
            to:$scope.email
        });
    })
}]);