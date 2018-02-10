/**
 * Created by yineng on 2016/12/26.
 *  时间控件
 */
(function () {
    "use strict";

    angular
        .module("myApp")
        .factory("datePickerService",datePickerService);
    datePickerService.$inject = ["$parse","$compile"];
    function datePickerService($parse,$compile) {
        
    }
})()
(function(){
    "use strict";

    angular
        .module("myApp")
        .directive("datePicker",datePicker);

    datePicker.$inject = ["$parse","$compile"];
    function datePicker($parse,$compile) {
        
    }
})();
