/**
 * Created by YN on 2016/3/16.
 */
var  myApp = angular.model("myApp",[]);

myApp.directive("myDirective",function(){

    /**
     * 类型是String  字符串
     * 可选参数，告诉angular这个指令在DOM中以何种形式被声明
     * 默认的是A
     * E(元素) A(属性，默认值) M(注释) C(类名)
     *  forExample  :
     *        E(元素) <myDirective></myDirective>
     *        A(属性) <div my-directive="expression"></div>
     *        C(类名) <div class="my-directive:expression"></div>
     *        M(注释) <--directive:my-directive expression -->
     *
     * 可以混合使用
     * @type {string}
     */
    var restrict = "A";

    /**
     * Number
     * 优先级级别
     * 可以保证在同一元素上，优先调用
     * 默认 0  等级越高，越先被调用
     * @type {number}
     */
    var priority = 0;

    /**
     * 布尔类型
     * 可选参数 只能是true或者是false
     * 这个参数是告诉angularJs，停止执行当前元素上比本命令优先级低的指令
     * @type {boolean}
     */
    var terminal = true;



    /*
     函数或者是一段HTML代码
     var template = "<div></div>";
     */
    /**
     * 可选参数，是一段HTML代码或者是一个函数 返回的是带白哦模板的字符串
     * @param tElement  元素
     * @param tAttrs 元素的属性
     */
    var template = function(tElement,tAttrs){
        return "<div></div>";
    };


    /**
     * 字符串或者是一个函数
     *      字符串：一个带白哦外部HTML文件路径的字符串
     *      函数 ：返回一个外部HTML文件的路径的字符串
     *  模板的URL将会通过angularJs内置的安全层，特别是￥getTrustedResourceUrl，这样可以保护模板不会去加载一个不受信任的源加载
     *
     *  增加加载的速度，可以使用缓存
     *  模板记载完成后，angularJs会使用它的默认缓存$templateCache服务来缓存模板
     */
    var templateUrl = function(tElement,tAttrs){

    };


    /**
     * 可选 参数 默认为false
     * 如果设置这个属性必须是true
     * 默认的时候 模板会被当做子元素插入到该指令的内部
     * @type {boolean}
     */
    var replace = true;


    /**
     * 布尔值或者是对象
     *  布尔值  默认为false  ，如果是true，会从作用于集成并且创建一个新的作用域对象
     *  对象   如果是一个{}空对象，相当于是隔离作用域，指令就不能访问外部作用域
     * @type {{}}
     */
    var scope = {
        /*本地的作用域上的属性同父级的作用域上的属性进行双向绑定  或者是 ngModel:"=ngModel" 指定和父级作用域上的某一个属性双向绑定*/
        ngModel:"=",
        /*和父级作用域绑定，以便在其中运行函数，如果有参数，则需要传递一个对象，对象的名字是参数名,值是传递过来的参数内容*/
        noSend:"&",
        /*和本地的作用域同DOM属性的值进行绑定，指令内部作用域可以使用外部作用域的变量*/
        formName:"@"
    };


    /**
     * 可选参数，如果设置，必须是true
     * 将模板嵌入到另外一个模板中，包括其中的指令通过嵌入全部传入一个指令中
     * 为了实现这一效果，scope参数的值必须通过{}或者true设置成隔离作用域
     * 如果没有，指令内部的作用域将被设置成传入模板的作用域
     * @type {boolean}
     */
    var transclude = true;


    /**
     * 模板控制器
     * 字符串或者是一个函数
     *      字符串，注册是控制器的名字
     *      函数：匿名控制器
     *
     *  这个可以和后面的link函数交换。
     *      控制器主要用来定义指令间服用的行为
     *      链接函数只能在当前的指令内部中定义行为，无法再指令中间复用
     *
     *
     * @param $scope 指令的当前的作用域
     * @param $element 当前指令对应的元素
     * @param $attrs 当前元素的属性组成的对象
     * @param $transclude 嵌入连接函数     会与对应的潜入作用于进行绑定
     */
    var controller = function($scope,$element,$attrs,$transclude){

    };


    /**
     * 设置控制器的别名，可以用来发布控制器，并且作用域可以访问controllerAs
     * 如：
     *  angular.model("myApp").controller("myController",['$scope',function($scope){$scope.name = 'heh';}]);
     *  <div ng-appng-controller='myApp'> {{name}}</div>
     *
     *  而直接在标签中定义为
     *  angular.model('myApp').directive('myDirective',function(){
     *      return {
     *          restrict:"A",
     *          template:'<div>{{myController.name}}</div>',
     *          controllerAs:"myController",
     *          controller:function(){
     *            this.name = 'hehe';
     *          }
     *      }
     *  })
     * @type {string}
     */
    var controllerAs = "";


    /**
     * 字符串或者是数组
     *      字符串代表的是另外一个指令的名字
     *      require会将控制器注入到其值所指定的指令中，并且作为当前指令的链接函数的第四个参数
     *
     * ？ 如果当前的指令中没有找到所属的控制器，将会null作为参数传递给link的第四个参数
     * ^  指令会在上游德 指令链中查找require
     * ?^ 将前面两个组合起来
     *    如果没有，则在自身所提供的控制器中查找，如过没有，则返回跑出一个错误
     * @type {Array}
     */
    var require = ['?ngModel'];


    /**
     * 编译函数负责对模板DOM进行转换
     * 是一个可选的参数，和link函数是互斥的，如果定义了complie，会把compile返回的一个函数作为link函数，link函数会被忽略
     * @param tElement
     * @param tAttrs
     * @param transcludeFun
     */
    var complie = function(tElement,tAttrs,transcludeFun){

    };
    /**
     * 返回的操作对象
     */
    return {
        restrict:restrict,
        priority:priority,
        terminal:terminal,
        template:template,
        templateUrl:templateUrl,
        replace:replace,
        scope:scope,
        transclude:transclude,
        controller:controller,
        controllerAs:controllerAs,
        require:require,
        complie://返回一个对象或者是连接
            function(tElment,tAttrs,transclude){
                return{
                    pre:function(scope,iElment,iAttrs,controller){},
                    post:function(scope,iAttrs,comtroller){}
                };
                //或者是
                //return function postLink(...){}
            },
        link:function (scope,iElment,iAttrs,someControllers){
            //这儿可以操作DOM，可以访问require指定的控制器
        }
    }
});