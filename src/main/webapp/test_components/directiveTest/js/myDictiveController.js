/**
 * Created by poem on 2016/3/13.
 * my directive controller
 */
myApp.controller("myController",["$scope",function($scope){

    /**
     * 定义一个对象
     * @type {{name: string, age: string}}
     */
    $scope.ngModelObj = {
        name:"12356",
        age:"25"
    };

    /**
     * 穆尼数据
     * @type {{}}
     */
    $scope.modelValue = {
        name:""
    };

    /**
     * 这个是自定义的标签，出现在scope中
     * @type {{name: string}}
     */
    $scope.myModel = {
        value:""
    };
    /**
     * 定义函数
     * 显示传入标签的对象
     */
    $scope.showDateByModelValue = function(){
        console.log($scope.myModel);
    }




}]);