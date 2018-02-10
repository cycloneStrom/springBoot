/**
 * Created by yineng on 2016/12/21.
 */
myApp.directive("fileUploader", function () {
    return {
        /*定义的标签的类型  A 属性 E 元素 C 类   M 注释*/
        restrict: "AE",
        /*模板地址，相对于应用界面的的位置*/
        templateUrl: basePath + "/components/08-fileuploader/template/fileUploader.html",
        /*是不是替换*/
        replace: true,
        /*作用域  */
        scope: {
            "config": "="
        },
        controller: ["$scope", "$http", "$location", "localStorageService", "fileUploader","notification", function ($scope, $http, $location, localStorageService, fileUploader,notification) {

            /**
             * 默认配置管理
             * @type {{url: string, deleteUrl: string, accept: (any)}}
             */
            $scope.options = {
                url: $scope.config.url,
                deleteUrl: $scope.config.url,
                accept:$scope.config.accept,
                defaultShow:$scope.config.defaultShow == null ? true:$scope.config.defaultShow,
                multiple:!$scope.config.multiple ? true:false
            };
            /**
             * 全局控制
             * @type {Array}
             */
            $scope.fastDfsIdList = [];
            /**
             * 获取上传的文件的控件
             * @type {Element}
             */
            /*获取基础*/
            var mainContainer = document.getElementById('uploadContainer');

            /** 创建一个文件上传控件*/
            var fileInput = document.createElement("input");
            fileInput.type = "file";
            if($scope.options.multiple){
                fileInput.setAttribute('multiple', 'multiple');
            }
            fileInput.setAttribute("accept",$scope.options.accept.join(","));
            mainContainer.appendChild(fileInput);

            if (!$scope.options.url) {
                throw new URIError("the url is not null!");
            }
            /**
             * 初始化
             */
            fileUploader.init({
                url: $scope.options.url,
                deleteUrl: $scope.options.url
            });
            /**
             * 添加监控
             */
            fileInput.addEventListener("change", function (e) {
                fileUploader.uploadFile(e.target, function (fastDfsId) {
                    var _tempList = [];
                    if (angular.isArray(fastDfsId)) {
                        _tempList = angular.copy(fastDfsId)
                    } else {
                        _tempList = angular.copy([fastDfsId])
                    }
                    var idsList = [];
                    angular.forEach(_tempList, function (id) {
                        if (id != -1 && !$scope.fastDfsIdList.some(function (_id) {
                                return _id === id
                            })) {
                            idsList.push(id);
                        }
                    });
                    $scope.$apply(function(){
                        $scope.fastDfsIdList = idsList;
                    });
                    /**回调函数*/
                    if($scope.config.callback){
                        $scope.config.callback( $scope.fastDfsIdList);
                    }
                }, function () {
                    console.log("上传失败！")
                });
            });

            /**
             * 删除对象
             */
            $scope.deleteFile = function (fastDfsId) {
                notification.confirm("warinng","确定要删除？" ,function(){
                    $http.get(basePath + "/fileManger/delete?fastDFSId=" + fastDfsId).success(function (data) {
                        if (data.status == 0) {/*删除成功*/
                            var index = -1;
                            angular.forEach($scope.fastDfsIdList,function(id,ind){
                                if(id === fastDfsId){
                                    index = ind;
                                }
                            });
                            if(-1 != index){
                                $scope.fastDfsIdList.splice(index,1);
                                /**回调函数*/
                                if($scope.config.callback){
                                    $scope.config.callback( $scope.fastDfsIdList);
                                }
                            }
                        } else {
                            //删除失败
                        }
                    }).error(function (data) {
                        //删除失败
                    })
                },function(){
                    console.log();
                });
            }
        }]
    }
});