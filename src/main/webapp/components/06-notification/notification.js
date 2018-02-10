/**
 * Created by YN on 2016/5/25.
 * 弹窗
 */
myApp.service("notification", ['$sce', "$compile", function ($sce, $compile) {
    /**
     * 对象管理
     * @type {{confirm: Function, alert: Function, notify: Function}}
     */

    var $dialog = null;

    /**
     * 自动关闭的瞬间
     * @type {number}
     */
    var outTime = 1000;

    /**
     * 超时
     * @type {string}
     */
    var timeOut = "0";

    /**
     * 弹出框
     * @type {{confirm: Function, alert: Function, notify: Function, loading: Function, remove: Function}}
     */
    var notify = {
        /**
         * 弹出式对话框
         * @param type 用于指定弹出框类型和控制图标样式,有三个值:warinng,error,sucess
         * @param content 显示的内容
         * @param okCallBack 点击ok的回调函数
         * @param cancelCallBack 点击取消的回调函数
         */
        confirm: function (type, content, okCallBack, cancelCallBack) {
            this.remove();
            if (type == "warinng") {
                $dialog = dialog({
                    title:"提示",
                    quickClose: false,
                    content: "<div class='confirm'><div class='confirm-warinng-img'></div>" + content + "</div>",
                    id: "confirm-warinng",
                    padding: 0,
                    skin: "confirm confirm-warinng",
                    align: "center",
                    okValue:"确定",
                    ok:okCallBack,
                    cancelValue:"取消",
                    cancel:cancelCallBack
                });
                $dialog.show();
            }
            if (type == "error") {
                $dialog = dialog({
                    title:"提示",
                    quickClose: false,
                    content: "<div class='confirm'><div class='confirm-error-img'></div>" + content + "</div>",
                    id: "confirm-error",
                    padding: 0,
                    skin: "confirm confirm-error",
                    align: "center",
                    okValue:"确定",
                    ok:okCallBack,
                    cancelValue:"取消",
                    cancel:cancelCallBack
                });
                $dialog.show();
            }
            else if(type == "sucess"){
                $dialog = dialog({
                    title:"提示",
                    quickClose: false,
                    content: "<div class='confirm'><div class='confirm-sucess-img'></div>" + content + "</div>",
                    id: "confirm-sucess",
                    padding: 0,
                    skin: "confirm confirm-sucess",
                    align: "center",
                    okValue:"确定",
                    ok:okCallBack,
                    cancelValue:"取消",
                    cancel:cancelCallBack
                });
                $dialog.show();
            }
        },
        /**
         * 失败的消息通知
         * @param type 用于指定弹出框类型和控制图标样式,有三个值:warinng,error,sucess
         * @param content 提示框的内容部分
         * @param okCallBack 点击ok的回调函数
         * @param cancelCallBack 点击取消的回调函数
         */
        alert: function (type, content, okCallBack, cancelCallBack) {

        },
        /**
         * 普通的消息通知
         * @param type 用于指定弹出框类型和控制图标样式,有三个值:info,error,sucess
         * @param content 提示框的内容部分
         */
        notify: function (type, content) {
            this.remove();
            if (type == "warinng") {
                $dialog = dialog({
                    quickClose: false,
                    content: content,
                    id: "notify-warinng",
                    padding: 0,
                    skin: "notify notify-warinng",
                    align: "top"
                });
                $dialog.show();
                timeOut = window.setTimeout(function () {
                    $dialog.close().remove()
                }, outTime);
            }
            if (type == "error") {
                $dialog = dialog({
                    quickClose: false,
                    content: content,
                    id: "notify-error",
                    padding: 0,
                    skin: "notify notify-error",
                    align: "top"
                });
                $dialog.show();
                timeOut = window.setTimeout(function () {
                    $dialog.close().remove()
                }, outTime);
            }
            else if(type == "success"){
                $dialog = dialog({
                    quickClose: false,
                    content: content,
                    id: "notify-sucess",
                    padding: 0,
                    skin: "notify notify-sucess",
                    align: "top"
                });
                $dialog.show();
                timeOut = window.setTimeout(function () {
                    $dialog.close().remove()
                }, outTime);
            }
        },
        loading:function(){
            this.remove();
            $dialog = dialog({});
            $dialog.showModal();
        },
        remove: function () {
            if(timeOut){
                window.clearTimeout(timeOut);
            }
            if ($dialog) {
                $dialog.close().remove();
            }
        }
    };
    return notify;
}]);