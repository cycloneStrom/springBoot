/**
 * Created by poem on 2016/3/22.
 * 定义的自定义标签，实现对输入的内容的验证
 */

myApp.directive("myValidate",function(){
    return {
        /*只能作为标签*/
        restrict:"A",
        /*比作任何的替换*/
        replace:false,
        /*作用域*/
        scope:{
            ngModel:"=",
            myValidate:"="
        },
        /*链接类型*/
        require:"^?ngModel",
        controller:function($scope,$element,$attrs){

        },
        link:function($scope,$element,$attrs,ngModel){
            /**
             * 这个验证主要是靠的是ngModel标签里面所定义的一些东西
             * 所以主要实在这儿写，在controller里面使用不到这个验证
             */
            /*监视每一个拥有这个标签的属性*/
            $scope.$watch(function(){
                return  $scope.ngModel;
            },function(){
                console.log("我靠，你大爷的，这个怎么搞？");
            });

            /**
             * 验证必填项
             */
            $scope.getRequire = function(){
                if($attrs.require && $scope.ngModel == ""){
                    console.log("必填项，你居然敢不填！")
                }
            }
        }
    }
});