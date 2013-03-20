'use strict';

/* Controllers */

function ProductListCtrl($scope, $http) {
  $http.get('/s/product/all').success(function(data) {
    $scope.products = data;
  });

//	$scope.products = [{"id":1,"name":"Pack of 6 Eggs","category":{},"description":"Fresh eggs","unitType":"ITEM","units":6,"creationDate":"Mar 20, 2013 12:11:58 PM"},{"id":2,"name":"Rice","category":{},"description":"a pack of rice","unitType":"KG","units":1,"creationDate":"Mar 20, 2013 12:11:58 PM"},{"id":3,"name":"Ipad","category":{},"description":"latest apple ipad","unitType":"ITEM","units":1,"creationDate":"Mar 20, 2013 12:11:58 PM"}]

  $scope.orderProp = 'name';
}
