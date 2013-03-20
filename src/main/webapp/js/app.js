'use strict';

/* App Module */

angular.module('nestorshop', ['nestorshopServices']).
  config(['$routeProvider', function($routeProvider) {
  $routeProvider.
      when('/products', {templateUrl: 'partials/product-list.html',   controller: ProductListCtrl}).
      when('/products/:productId', {templateUrl: 'partials/product-detail.html', controller: ProductDetailCtrl}).
      when('/shoplists', {templateUrl: 'partials/shoplist-list.html',   controller: ShopListsCtrl}).
      when('/shoplists/:shoplistId', {templateUrl: 'partials/shoplist-detail.html',   controller: ShopListDetailCtrl}).
      otherwise({redirectTo: '/products'});
}]);
