'use strict';

/* Services */

angular.module('nestorshopServices', ['ngResource']).
    factory('product', function($resource){
  return $resource('product/:productId.json', {}, {
    query: {method:'GET', params:{productId:'products'}, isArray:true}
  });
});
