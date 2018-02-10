/**
 * Created by YN on 2016/6/27.
 */

/**
 * 重新登录服务
 */
myApp.service("reLoginService",["$rootScope","$http","$location","loginUserInfoStorage","$compile",function($rootScope,$http,$location,loginUserInfoStorage,$compile){
    return {
        reLogin:function(){
            /*创建新的管理属性*/
            var $scope = $rootScope.$new(),url = basePath + "/components/07-relogin/template/reLogin.html";

            /*弹出框*/
            var diogle = dialog({
                title:"",
                id: 'reLogin'
            });

            /**
             * 获取登录页面
             */
            $http.get(url).success(function (data, status) {
                if (status == 200) {
                    var content = $compile($(data))($scope);
                    diogle.content(content);
                } else {
                    diogle.content('加载失败');
                }
            });
            /**
             * 登录的用户
             * @type {{username: string, password: string}}
             */
            $scope.login = {
                username: "",
                password: ""
            };

            /**
             * 登录按钮
             * @type {string}
             */
            $scope.login_buttom_message = "";

            /**
             * 是否发生全局的错误
             * @type {boolean}
             */
            $scope.error = true;
            /**
             * 验证用户名
             */
            $scope.validateUserName = function () {
                if (!$scope.login.username) {
                    $scope.error = true;
                    $scope.login_buttom_message = "请输入用户名";
                    return false;
                } else {
                    $scope.error = false;
                    $scope.login_buttom_message = "";
                    return true;
                }
            };

            /**
             * 验证密码
             */
            $scope.validatePassword = function () {
                if (!$scope.login.password) {
                    $scope.error = true;
                    $scope.login_buttom_message = "请输入密码";
                    return false;
                } else {
                    $scope.error = false;
                    $scope.login_buttom_message = "";
                    return true;
                }
            };

            /**
             * 登录
             * @returns {boolean}
             */
            $scope.loginForm = function(){
                /*取消登录的时候*/
                $scope.error = false;
                $scope.login_buttom_message = "登录";
                if (!$scope.validateUserName()) return false;
                if (!$scope.validatePassword()) return false;
                /*登录*/
                $scope.login.password = $scope.login.password.replace(/(^\s*)|(\s*$)/g, "");
                $http.post(basePath + "/api/login/loginForm", parseSendParmToSpringMVC({
                    username: $scope.login.username,
                    password: $scope.login.password
                })).success(function (data, status, headers, config) {
                    if (status != 200) {
                        delCookie("myApp_cookies");
                    }
                    if (data.status == 0) {
                        $scope = null;
                        diogle.close().remove();
                        loginUserInfoStorage.manageUser();
                    } else {
                        $scope.error = true;
                        $scope.login_buttom_message = "用户和密码错误";
                    }
                }).error(function (data) {
                    $scope.error = true;
                    $scope.login_buttom_message = "登录错误";
                });
            };

            /**
             * 绑定
             */
            angular.element(document).bind("keyup", function (event) {
                if (event.keyCode == 13) {
                    $scope.loginForm();
                }
            });

            /**
             * 取消登录按钮事件
             */
            $scope.canCel = function(){
                diogle.close().remove();
            };

            /**
             * 弹出登录框
             */
            diogle.showModal();

            return null ;
        }
    }
}]);
