/**
 * Created by YN on 2016/5/28.
 */
myApp.service("modal", ["$http", "$rootScope", "$compile", function ($http, $rootScope, $compile) {
    return {
        /**
         * 弹出框
         * @param url 弹出框需要显示的页面
         * @param config 弹出框配置
         * @param scope angular作用域
         */
        showModal: function (url, config, scope) {
            /*弹出框对象作用域*/
            var modalScope = null;

            /*弹出框对象*/
            var model = null;


            /*弹出框默认配置*/
            var defaultConfig = {
                title: "弹出框", /*标题*/
                content: "", /*中间显示的内容*/
                ok: false,
                cancel: false, /*不显示关闭按钮*/
                quickClose: false, /*点击空白的部分时，关闭*/
                statusbar: "", /*给对话框左下脚消息*/
                button: [], /*自定义按钮*/
                padding: 0, /*边框*/
                zIndex: 2048
            };
            /*合并配置，生成相应的配置*/
            config = angular.extend(defaultConfig, config);

            /*1、首先，管理angular的作用域，如果没有就新建一个*/
            if (!scope) {
                modalScope = $rootScope.$new();
            } else if (!scope.$new && !scope.$watch && !scope.$digest && !scope.$destroy) {
                modalScope = $rootScope.$new();
                angular.forEach(scope, function (value, key) {
                    modalScope[key] = value;
                });
            } else {
                modalScope = scope;
            }


            /*生成弹出框*/
            model = dialog(config);

            /*页面管理*/
            if (/\.html$|\.htm$/.test(url)) {
                $http.get(url).success(function (data, status) {
                    if (status == 200) {
                        model.content($compile($(data))(modalScope));
                    }
                });
            } else {
                model.content($compile($(content))(modalScope));
            }

            /*打来页面*/
            model.showModal();

            /*返回给页面*/
            return model;
        }
    }
}]);