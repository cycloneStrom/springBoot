/**
 * Created by poem on 2016/5/7.
 * 本地数据缓存
 */
myApp.service("myLocalStorageServer", function ($rootScope, $http, $location, localStorageService) {

    /*左边菜单保存的ID*/
    var LEFT_MENU = "poem.storage.leftMenu.key";
    /*路径*/
    var path = $location.path().toUpperCase();
    /*默认key*/
    var DEFAULT_PATH_KEY = "poem.storage.default.key" + "_" + path;
    /**
     * 初始化
     */
    var init = function () {
        /**路径*/
        var path = $location.path().toUpperCase();
        /**默认key*/
        var DEFAULT_PATH_KEY = "poem.storage.default.key" + "_" + path;
    };
    init();
    /**
     * 检验传入的数据是不是为空
     * @param item
     * @returns {boolean}
     */
    var isNotBlank = function (item) {
        if (null == item) {
            return false;
        }
        if (angular.isUndefined(item)) {
            return false;
        }
        if (angular.isString(item)) {
            return item.length != 0;
        }
        return true;
    };


    /**
     * 检验key
     * @param key
     */
    var keyToStr = function (key) {
        if (isNotBlank(key)) {
            return DEFAULT_PATH_KEY;
        }
        if (angular.isArray(key) || angular.isObject(key)) {
            key = angular.toJson(key);
        }
        return key;
    };

    /**
     * 删除对象
     * @param key
     */
    var removeAction = function (key) {
        localStorageService.remove(keyToStr(key));
    };
    /**
     * 保存菜单
     * @param item
     */
    var saveLeftMenuAction = function (item) {
        localStorageService.set(LEFT_MENU, item)
    };

    /**
     * 获取左边菜单的值
     */
    var getLeftMenuAction = function () {
        var $_ = localStorageService.get(LEFT_MENU);
        removeAction(LEFT_MENU);
        return angular.copy($_);
    };

    /**
     * 接口管理
     */
    return {
        saveLeftMenu: saveLeftMenuAction,
        getLeftMenu: getLeftMenuAction
    }
});