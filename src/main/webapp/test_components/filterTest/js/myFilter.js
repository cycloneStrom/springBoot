/**
 * Created by poem on 2016/3/12.
 * 我的过滤器测试版本
 * 可以使用|符号作为分割符来使用多个过滤器
 */
myApp.filter("myFilter",function(){
    /*过滤器反水的是一个改变的函数*/
    return function(input){
        if(input){
            return " -- " + input + " -- ";
        }
    }
});
