/**
 * Created by YN on 2016/5/25.
 */
myApp.controller("modalController", ["$scope", "$http", "$location", "$document", "notification","modal",
    function ($scope, $http, $location, $document, notification,modal) {

        /**
         * 消息提示
         */
        $scope.notifyWarinng = function () {
            $scope.config = {
                title:"弹出框测试",
                button:[
                    {
                        value:"同意",
                        autofocus: true,
                        disabled:false,//	Boolean	(默认值: false) 是否禁用
                        callback:function(){
                            console.log("你同意了");
                            return true;
                        }
                    },
                    {
                        value:"不同意",
                        disabled:false,//	Boolean	(默认值: false) 是否禁用
                        callback:function(){
                            console.log("不同意");
                            return true;
                        }
                    }
                ]
            };

            var url = basePath + "/components/3-modal/template/template.html";
            modal.showModal(url,$scope.config,$scope);
        };
    }]);