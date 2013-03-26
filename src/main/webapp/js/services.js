'use strict';

/* Services */

angular.module('nestorshopServices', ['ngResource']).
    factory('productService', function($resource){
  return $resource('s/product/:productId'+preventCache(), {productId: '@id'}, {
    query: {method:'GET', params:{productId:'products'}, isArray:true}
  });	
});


