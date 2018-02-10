/**
 * Created by YN on 2016/5/25.
 */
myApp.controller("notifyController", ["$scope", "$http", "$location", "$document", "notification",
    function ($scope, $http, $location, $document, notification) {

        var stomClient = null;
        /**
         * 消息提示
         */
        $scope.notifyWarinng = function () {
            notification.notify("warinng", "警告！");
        };

        /**
         * 失败提示
         */
        $scope.notifyError = function () {
            notification.notify("error", "错误！");
        };

        /**
         * 成功
         */
        $scope.notifySucess = function () {
            notification.notify("sucess", "成功！");
        };


        /**************************************************************     交互 ********************************************************/
        /**
         * 消息提示
         */
        $scope.confirmWarinng = function () {
            notification.confirm("warinng", "警告！",function(){
                console.log("确定");
            },function(){
                console.log("取消");
            });
        };

        /**
         * 失败提示
         */
        $scope.confirmError = function () {
            notification.confirm("error", "错误！",function(){
                console.log("确定");
            },function(){
                console.log("取消");
            });
        };

        /**
         * 成功
         */
        $scope.confirmSucess = function () {
            notification.confirm("sucess", "成功！",function(){
                console.log("确定");
            },function(){
                console.log("取消");
            });
        };

        $scope.loading = function(){
            notification.loading();
        };


        $scope.connection = function(){
           var socket = new SockJS("/endpointWisely");
            stomClient = Stomp.over(socket);
            stomClient.connect({},function(frame){
                setConnected(true);
                console.log("Connected:" + frame);
                stomClient.subscribe("/topic/getResponse",function(resonse){
                    console.log(JSON.parse(resonse.body).responseMessage);
                })
            })
        };


        function disconnect(){
            if(stomClient != null){
                stomClient.disconnect();
            }
            setConnected(false);
            console.log("DISConnected");
        }

        $scope.sendName = function(){
            if(stomClient == null){
                $scope.connection();
            }
            stomClient.send("/welcome",{},JSON.stringify({'name':"我靠"}));
        }
    }]);