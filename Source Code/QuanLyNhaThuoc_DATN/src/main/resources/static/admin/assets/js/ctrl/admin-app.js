const app = angular.module("admin-app",["ngRoute"]);
app.config(function($routeProvider){
    $routeProvider
        .when("/sell",{
            templateUrl:"/admin/assets/layout/sell.html",
            controller:"sell-ctrl"
        })
        .when("/account",{
            templateUrl:"/admin/assets/layout/account.html",
            controller:"account-ctrl"
        })
        .when("/category",{
        	templateUrl:"/admin/assets/layout/category.html",
        	controller:"category-ctrl"
        })
        .when("/sub-category",{
            templateUrl:"/admin/assets/layout/sub-category.html",
            controller:"subCategory-ctrl"
        })
        .when("/brand",{
        	templateUrl:"/admin/assets/layout/brand.html",
        	controller:"brand-ctrl"
        })
        .when("/util",{
            templateUrl:"/admin/assets/layout/util.html",
            controller:"util-ctrl"
        })
        .when("/product",{
        	templateUrl:"/admin/assets/layout/product.html",
        	controller:"product-ctrl"
        })
        .when("/promotion",{
        	templateUrl:"/admin/assets/layout/promotion.html",
        	controller:"promotion-ctrl"
        })
        .when("/input",{
            templateUrl:"/admin/assets/layout/input.html",
            controller:"input-ctrl"
        })
        .when("/order",{
            templateUrl:"/admin/assets/layout/order.html",
            controller:"order-ctrl"
        })
        .otherwise({
            templateUrl:"/admin/assets/layout/dashboard.html",
            controller:"dashboard-ctrl"
        });
});
const REGEX_DAY = /([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]))/;
app.directive('validateDay', function(){
	return {
		require: 'ngModel',
		link: function (scope, element, attr, mCtrl){
			function fnValidate(value){
				if(!value.match(REGEX_DAY)){
					mCtrl.$setValidity('charE', false);
				}else{
					mCtrl.$setValidity('charE', true);
				}
				return value;
			}
			mCtrl.$parsers.push(fnValidate);
		}
	};
});
const REGEX_SDT = /^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$/;
app.directive('validateSdt', function(){
	return {
		require: 'ngModel',
		link: function (scope, element, attr, mCtrl){
			function fnValidate(value){
				if(!value.match(REGEX_SDT)){
					mCtrl.$setValidity('charE', false);
				}else{
					mCtrl.$setValidity('charE', true);
				}
				return value;
			}
			mCtrl.$parsers.push(fnValidate);
		}
	};
});
