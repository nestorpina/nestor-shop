'use strict';

/* Controllers */

function ProductListCtrl($scope, $http) {
	$http.get('/s/product/all').success(function(data) {
		$scope.products = data;
	});

	$scope.orderProp = 'name';
}

function ProductDetailCtrl($scope, $routeParams, $http) {

	$http.get('/s/product/id/' + $routeParams.productId).success(function(data) {
		$scope.product = data;
	});
}

function ShopListsCtrl($scope, $http) {
	$http.get('/s/shoplist/all').success(function(data) {
		$scope.shoplists = data;
	});

	$scope.orderProp = 'name';
}

function ShopListDetailCtrl($scope, $routeParams, $http) {

	$http.get('/s/shoplist/id/' + $routeParams.shoplistId).success(function(data) {
		$scope.shoplist = data;
	});
}
