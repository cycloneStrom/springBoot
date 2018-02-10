
/**
 * Created by lyw on 2016/4/13.
 * 所有的通用方法都在这儿维护
 */
var myScript = {
    /*获取随机数*/
    myRound: function (arr) {
        if (arr <= 0) {
            return 0;
        }
        return parseFloat((100 * Math.random()) % arr).toFixed(0)
    },
    /**滴定仪 随机返回某一个字母*/
    getAllCharacter: function () {
        var character = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'];
        return character[myScript.myRound(51)];
    },
    /**滴定仪 随机返回某一个大写字母*/
    getUpperCharacter: function () {
        var character = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
        return character[myScript.myRound(25)];
    },
    /**滴定仪 随机返回某一个小写祖母*/
    getLowerCharacter: function () {
        var character = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'];
        return character[myScript.myRound(25)];
    },
    /**自定义，返回某一长度的字符串*/
    getCharacters: function (len, random) {
        var characters = "";
        if (isNaN(len)) {
            len = 1;
        }
        if (random) {
            for (var i = 0; i < len; i++) {
                characters += myScript.getAllCharacter();
            }
        } else {
            for (var i = 0; i < len; i++) {
                if (i == 0) {
                    characters += myScript.getUpperCharacter();
                } else {
                    characters += myScript.getLowerCharacter();
                }
            }
        }
        return characters;
    }
};

/**
 * 对象管理
 * @param obj
 * @returns {string}
 */
var param = function (obj) {
    var query = '',
        name, value, fullSubName, subName, subValue, innerObj, i;

    for (name in obj) {
        value = obj[name];

        if (value instanceof Array) {
            for (i = 0; i < value.length; ++i) {
                subValue = value[i];
                fullSubName = name;
                innerObj = {};
                innerObj[fullSubName] = subValue;
                query += param(innerObj) + '&';
            }
        } else if (value instanceof Object) {
            for (subName in value) {
                subValue = value[subName];
                fullSubName = name + '[' + subName + ']';
                innerObj = {};
                innerObj[fullSubName] = subValue;
                query += param(innerObj) + '&';
            }
        } else if (value !== undefined && value !== null)
            query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
    }

    return query.length ? query.substr(0, query.length - 1) : query;
};

/**
 * 把对象转化为一个JSon字符串
 * @param obj
 * @returns {{}}
 */
var parseSendParmToSpringMVC = function (obj) {
    //最终组合成功的对象
    var resultObj = {};
    //递归方法，用于深度转换每一个参数
    //key 对象时，是属性名
    //value 属性值
    //prefix（关键），和key进行组合相组合成想要的格式类型如name.age（对象）或name[0].age（数组）
    var deepParseParams = function (key, value, prefix) {
        //先判断是否是数组
        if (value instanceof Array) {
            for (var i in value) {
                deepParseParams("", value[i], prefix + key + "[" + i + "]");
            }
        }
        //再判断是否是对象
        else if (value instanceof Object) {
            for (var i in value) {
                deepParseParams("." + i, value[i], prefix + key);
            }
        }
        //如果不是数组或对象，到了此次递归的最后一次，将完成组合的这条最终数据放在最终组合对象中
        else {
            resultObj[prefix + key] = value;
        }

    };
    //因为传入的转换参数必须是对象,而且第一次传入和第二次开始组合“.”号是很特殊的地方，所有
    //第一次单独循环
    for (var i in obj) {
        deepParseParams("", obj[i], i);
    }

    //返回转换成功的对象集合
    return resultObj;
};

/**
 * 对传入的字符串检验
 *
 * 如果是 undefined 返回false
 * 如果是 null  返回false
 * 如果是 “” 返回false
 * 如果是非自负  抛出异常
 */
var isNotBlankStr = function (str) {
    if (typeof str == "undefined") {
        return false;
    }
    if (str == null) {
        return false;
    }
    /*判断是不是是字符串*/
    if (!angular.isString(str)) {
        throw new Error("this is not str");
    }
    return str.length != 0;
};

/**
 * 删除cookies
 * @param name
 */
var delCookie = function (name) {
    var date = new Date();
    date.setTime(date.getTime() - 10000);
    document.cookie = name + "=a; expires=" + date.toGMTString() + ";path=/";
};

/**
 * 获取cookie
 * @param name
 * @returns {*}
 */
var getCookie = function (name) {
    var strCookie = document.cookie;
    //将多cookie切割为多个名/值对
    var arrCookie = strCookie.split("; ");
    //遍历cookie数组，处理每个cookie对
    for (var i = 0; i < arrCookie.length; i++) {
        var arr = arrCookie[i].split("=");
        //找到名称为userId的cookie，并返回它的值
        if (name == arr[0]) {
            return arr[1];
        }
    }
    return null;
};

/**
 * 删除cookie
 * @param exclude
 */
var clearCookie = function (exclude) {
    var d = new Date();
    d.setTime(d.getTime() + (-1 * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    var arrCookie = document.cookie.split('; ');
    Array.prototype.S = String.fromCharCode(2);
    Array.prototype.in_array = function (e) {
        var r = new RegExp(this.S + e + this.S);
        return (r.test(this.S + this.join(this.S) + this.S));
    };
    for (var i = 0; i < arrCookie.length; i++) {
        var arr = arrCookie[i].split("=");
        if (typeof exclude == 'object') {
            if (!exclude.in_array(arr[0]))
                document.cookie = arr[0] + "=''; " + expires + ";path=/";
        } else if (arr[0] != exclude) {
            document.cookie = arr[0] + "=''; " + expires + ";path=/";
        }
    }
};