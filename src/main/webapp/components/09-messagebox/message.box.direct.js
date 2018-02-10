/**
 * Created by poem on 2016/12/24.
 */
myApp.directive("messageBoxDirect", ["notification", function (notification) {
    return {
        replace: true,
        /*定义的标签的类型  A 属性 E 元素 C 类   M 注释*/
        restrict: "AE",
        scope: {
            options: "="
        },
        templateUrl: basePath + "/components/09-messagebox/template/messageBoxDirect.html",
        controller: ["$scope", "$http", "$location", "myLocalStorageServer", "localStorageService", function ($scope, $http, $location, myLocalStorageServer, localStorageService) {
            $scope.basePath = basePath;
            /**
             * 默认配置
             * @type {{}}
             */
            $scope.config = {
                pageSize: 20,
                pageNumber: 0,
                userId: $scope.config ? $scope.config.userId:""
            };
            /**
             * 当前的用户信息
             */
            $scope.user = localStorageService.get(platformSystemUser);
            /**
             * 所有的消息实体
             * @type {Array}
             */
            $scope.shortMessages = [];

            /**
             *  查询用户的信息
             */
            $scope.getShortMsg = function () {
                $http.get(basePath + "/business/platformShortMessageInfo/queryShortMsg?pageSize=" + $scope.config.pageSize +
                    "&pageNumber=" + $scope.config.pageNumber + "&userId=" + $scope.config.userId)
                    .success(function (data, status, headers, config) {
                        if (data.status == 0) {
                            $scope.shortMessages = data.result.content;
                        } else {
                            notification.notify("error", "查询失败！");
                        }
                    }).error(function (data) {
                    notification.confirm("error", data, function () {
                        return true
                    }, function () {
                        return true
                    });
                });
            };

            /**
             * 外部接口
             * 刷新数据
             */
            $scope.options.refresh = function (userId) {
                if (isNotBlankStr(userId)) {
                    $scope.config.userId = userId;
                }
                $scope.getShortMsg()
            };

            /**
             * 打开用户管理
             */
            $scope.consumerManageMain = function (id) {
                var url = basePath + "/pages/platform/consumer/index.html?user=" + id +"&our="+(id == localStorageService.get(platformSystemUserId))+"&time="+new Date().getTime();
                window.open(url)
            };


            /**
             * 删除消息
             * @param item
             */
            $scope.deleteInfo = function (item) {
                notification.confirm("warinng", "确定要删除吗？", function () {
                    $http.post(basePath + "/business/platformShortMessageInfo/deleteMsg?", {infoId: item.id})
                        .success(function (data, status, headers, config) {
                            if (data.status == 0) {
                                notification.notify("sucess", "删除成功！");
                                $scope.getShortMsg();
                            } else {
                                notification.notify("error", "删除失败！");
                            }
                        }).error(function (data) {
                        notification.notify("error", data)
                    });
                })

            };

            /**
             * 开始执行
             */
            $scope.getShortMsg();
        }]
    };
}]);