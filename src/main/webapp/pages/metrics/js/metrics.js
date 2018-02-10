/**
 * Created by YN on 2016/7/6.
 */
myApp.controller("metricsController",["$scope","$http","$location",function($scope,$http,$location){

    /**
     * 发送请求
     */
    $scope.sendMetris = function(){
        $http.get(basePath + "/metrics/metrics").then(function(then){
            $scope.metrics = then.data;
        })
    };

    $scope.sendMetris();
}]);