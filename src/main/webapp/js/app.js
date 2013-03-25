'use strict';

/* App Module */

angular.module('nestorshop', ['nestorshopServices'], function($httpProvider) {
	  // Use x-www-form-urlencoded Content-Type
	$httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
	 
	  // Override $http service's default transformRequest
	$httpProvider.defaults.transformRequest = [function(data) {
	    return angular.isObject(data) && String(data) !== '[object File]' ? jQuery.param(data) : data;
	  }];	
	
  }).
  config(['$routeProvider', function($routeProvider) {
  $routeProvider.
      when('/products', {templateUrl: 'partials/product-list.html'+preventCache(),   controller: ProductListCtrl}).
      when('/products/:productId', {templateUrl: 'partials/product-detail.html'+preventCache(), controller: ProductDetailCtrl}).
      when('/shoplists', {templateUrl: 'partials/shoplist-list.html'+preventCache(),   controller: ShopListsCtrl}).
      when('/shoplists/:shoplistId', {templateUrl: 'partials/shoplist-detail.html'+preventCache(),   controller: ShopListDetailCtrl}).
      otherwise({redirectTo: '/products'});

}]);

