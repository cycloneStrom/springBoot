/**
 * Created by YN on 2016/3/18.
 * 控制器
 */
myApp.controller("routeController",["$scope","$routeParams","$location",function($scope,$routeParams,$location){

    /**
     * $routeParams
     * 这个用来访问参数
     * 详见 ../../angular-route.js 第一个的templateUrl值
     */
     $scope.locationObj = {
         path:$location.path(),
         replace:"",
         absUrl:$location.absUrl(),
         hash:$location.hash(),
         host:$location.host(),
         port:$location.port(),
         protocol:$location.protocol(),
         search:$location.search(),
         url:$location.url()
     }
}]);