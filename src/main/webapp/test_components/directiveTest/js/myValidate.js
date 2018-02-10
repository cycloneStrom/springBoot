/**
 * Created by YN on 2016/3/18.
 * 自定义验证
 */

myApp.directive("myValidate",function(){
    return {
        require:"?ngModel",
        scope:{
            ngModel:"=",
            myValidate:"="
        },
        restrict:"A",
        controller:function($scope,$element,$attrs){
            /**
             * 定义在这儿，可以在其他的嵌套指令中复用
             * 但是其他的指令必须使用require注入当前的指令
             *
             * 当前的验证策略是
             *        为每一个元素添加点击事件、按钮事件
             */
            $scope.$myValidate = function(){
                console.log("myValidate - " + $scope.$myValidate);
            };
        },
        link:function($scope,$element,$attrs,ngModel){
            /**
             * 在这儿，可以在require中的注入的指令调用其定义在controller的各种方法
             */
            $scope.$watch(function(){
                return $scope.ngModel
            },function(){
                $scope.getRequireValue();
            });
            /**
             * 获取必填项
             */
            $scope.getRequireValue = function(){
                /* 在当前的标签中有require属性，则表示必填项*/
                if($attrs.required){
                    if($scope.ngModel == undefined || $scope.ngModel == ""){
                        console.log($scope.myValidate.name  + "必填！！！！");
                    }
                }
                $scope.getLength();
            };


            /**这儿依赖ngModel指令*/
            var error = ngModel.$error;

            /**
             * 输入的长度的验证
             */
            $scope.getLength = function(){
                /*如果是验证长度失败*/
                if(error.minlength ||  error.maxlength){
                    if($attrs.maxlength && $attrs.minlength){
                        console.log($scope.myValidate.name  + "只能输入介于" + $attrs.minlength + "-" + $attrs.maxlength + "个字符！");
                    }
                    if($attrs.maxlength){
                        console.log($scope.myValidate.name  + "长度不能超过" +  $attrs.maxlength + "个字符！");
                    }
                }
            }
        }
    }
});