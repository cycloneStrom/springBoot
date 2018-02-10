/**
 * Created by poem on 2016/3/13.
 * my directive
 *   指令的生命周期开始与$compile方法，并且结束于link方法
 */

myApp.directive("myDirective",function(){
    return {
        /*定义的标签的类型  A 属性 E 元素 C 类   M 注释*/
        restrict:"AE",
        /*模板地址，相对于应用界面的的位置*/
        templateUrl:"templete/myDirectve.html",
        /*是不是替换*/
        replace:true,
        /*作用域  */
        scope:{
            someProperty:'@', //或者是'@someProperty',显示绑定，someProperty是标签中的属性
            model:"=",
            myModel:"="
        },
        require:["?ngModel","?^myValidate"],
        /*控制器*/
        controller:function($scope,$element,$attrs,$transclude){

            //console.log($scope.someProperty);
            /**
             * 因为在上面作用域中，已经定义了model属性在自定义的标签里面和标签外面是双向绑定的
             * 在标签里改变了model的值，在外面显示的时候也会去跟着改变值
             * @type {*[]}
             */
            $scope.model = [
                {
                    id:"1",
                    typeList:[
                        {
                            id:"1-1",
                            name:"标题"
                        },
                        {
                            id:"1-2",
                            name:"显示类型"
                        }
                    ]
                },
                {
                    id:"2",
                    typeList:{
                        id:$scope.id,
                        termId: '',//采购学期
                        orderOrgNoName: '',//订单机构名称
                        orderOrgNo: '',//订单机构
                        orderName: '',//订单名称
                        applyOrgNosName: '',//订单的采购机构名称
                        applyOrgNos: ''//采购机构
                    }
                },
                {
                    id: "3",
                    typeList: [
                        {field: "orderName", displayName: "订单名称"},
                        {field: "orderOrgName", displayName: "订单机构"},
                        {field: "termName", displayName: "采购学期"},
                        {field: "applyOrgNames", displayName: "采购机构"},
                        {field: "applyTime", displayName: "申请时间"},
                        {field: "approveUserName", displayName: "审核人", columnSelectFlag: 'no'},
                        {field: "approveTime", displayName: "审核时间", columnSelectFlag: 'no'}
                    ]
                }
            ];

            /**
             * 页面上的表单的信息
             * @type {{name: string, phone: string, password: string}}
             */
            $scope.myFormDate = {
                name:"",
                phone:"",
                password:""
            };

            /**
             * 提交
             * @param index 1、正式提交，2取消
             */
            $scope.submit = function(index){
                /*表单验证失败*/
                if($scope.myForm.$invalid){
                    console.log("错误");
                    return false;
                }
                if(index == 1){
                    console.log($scope.myFormDate);
                }else{
                    console.log("取消");
                }
            };

            /**
             * 出现在￥scope中
             * @type {{name: string}}
             */
            $scope.myModel = {
                value:"myValue"
            }
        },
        link:function(tElement,tAttrs,transclude,injectControllers){
            /**显示的是定义在当前的标签上的ngModel属性对象的一个对象，在ngModel中还有很多其他的属性*/
            console.log(injectControllers);
        }
    }
});