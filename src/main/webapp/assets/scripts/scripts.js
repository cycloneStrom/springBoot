/**
 * Created by YN on 2016/6/30.
 */
function isActivitStorage() {
    if (window.localStorage) return true;
    return false;
}
/*保存本地值*/
function setStorage(value, key) {
    window.localStorage.setItem(value, key);
}
/*获取值*/
function getStorage(key) {
    return window.localStorage.getItem(key);
}
/*获取cookie*/
function getCookie(key) {
    var $value = "";
    if (isActivitStorage()) {
        $value =  getStorage(key)
    }
    var name = key + "=";
    var values = document.cookie.split(';');
    for (var i = 0; i < values.length; i++) {
        var value = values[i];
        while (value.charAt(0) == ' ') {
            value = value.substring(1);
        }
        if (value.indexOf(name) != -1) {
            $value = decodeURI(value.substring(name.length, value.length));
        }
    }
    return $value;
}
/*保存*/
function setCookie(value, key) {
    if(isActivitStorage()){
        setStorage(value,key);
    }else{
        var d = new Date();
        d.setTime(d.getTime() + (365 * 24 * 60 * 60 * 1000));
        var expires = "expires=" + d.toUTCString();
        document.cookie = key + "=" + encodeURI(value) + "; " + expires + ";path=/";
    }
}
/*修改皮肤*/
var setSkin = function (style) {
    if (style) {
        var url =basePath +"/assets/style/", node = $("#scriptTheme"), href = node.attr("href");
        node.attr("href", url + style + ".css");
        setCookie(myAppSkinStyle,style);
    } else {
        throw Error("the skin file is not null.")
    }
};
/*设置皮肤*/
if(getCookie(myAppSkinStyle)){
    setSkin(getCookie(myAppSkinStyle));
}