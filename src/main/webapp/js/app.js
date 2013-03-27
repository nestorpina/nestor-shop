'use strict';

/* App Module */

var myModule = angular.module('nestorshop', ['nestorshopServices'], function($httpProvider) {
	  // Use x-www-form-urlencoded Content-Type
	$httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
	 
	  // Override $http service's default transformRequest
	$httpProvider.defaults.transformRequest = [function(data) {
	    return angular.isObject(data) && String(data) !== '[object File]' ? jQuery.param(data) : data;
	  }];	
	
  }).
  // Routing Config
  config(['$routeProvider', function($routeProvider) {
  $routeProvider.
  	  when('/products/new', {templateUrl: 'partials/product-new.html'+preventCache(),   controller: ProductNewCtrl}).
      when('/products', {templateUrl: 'partials/product-list.html'+preventCache(),   controller: ProductListCtrl}).
      when('/product/:productId', {templateUrl: 'partials/product-detail.html'+preventCache(), controller: ProductDetailCtrl}).
      when('/shoplists', {templateUrl: 'partials/shoplist-list.html'+preventCache(),   controller: ShopListsCtrl}).
      when('/shoplists/:shoplistId', {templateUrl: 'partials/shoplist-detail.html'+preventCache(),   controller: ShopListDetailCtrl}).
      otherwise({redirectTo: '/products'});

}]);

/** 
 * Global $http interceptor to control session timeouts (redirect to login page)
 */
myModule.factory('myHttpInterceptor', function ($q) {
    return function (promise) {
        return promise.then(function (response) {
            return response;
        }, function (response) {

        	console.log(response);
        	if(response.status == 401) {
        		alert("Session timeout");
        		window.location.href="/_ah/login?continue=/";
        	} else if(response.status == 500) { 
        		alert("Unexpected error");
        	}
            return $q.reject(response);
        });
    };
});
myModule.config(function ($httpProvider) {
    $httpProvider.responseInterceptors.push('myHttpInterceptor');
});

