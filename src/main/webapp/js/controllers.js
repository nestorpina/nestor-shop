'use strict';

/* Controllers */

function ProductListCtrl($scope, $http) {
	$http.get('/s/product/all').success(function(data) {
		$scope.products = data;
	});

	$scope.addProduct = function(productId) {
		$http.post('/s/shoplist/item/',{listId : 1, productId : productId}).success(function(data) {
		});
	};
	
	$scope.orderProp = 'name';
}

function ProductDetailCtrl($scope, $routeParams, $http) {

	$http.get('/s/product/id/' + $routeParams.productId).success(function(data) {
		$scope.product = data;
	});
}

function ShopListsCtrl($scope, $http) {
	$http.get('/s/shoplist/all').success(function(data) {
		if(!data) data = [];
		$scope.shoplists = data;
		
	});
	
   $scope.deleteSL = function(index) {
	   	var shoplist = $scope.shoplists[index];
		$http.delete('/s/shoplist/'+shoplist.id).success(function(data) {
			$scope.shoplists.splice(index,1);
		});

    };
    
    
    $scope.newSL = function() {
        $scope.newFormEnabled = true;
    };
    
    $scope.addSL = function() {
    	$http.post('/s/shoplist',{name : $scope.name}).success(function(data) {
            $scope.shoplists.push(data);
		});
        
        $scope.cancelAdd();
    };
    
    $scope.cancelAdd = function() {
        $scope.name = '';
        $scope.newFormEnabled = false;
    };	

	$scope.orderProp = 'name';
}

function ShopListDetailCtrl($scope, $routeParams, $http) {

	$http.get('/s/shoplist/id/' + $routeParams.shoplistId).success(function(data) {
		$scope.shoplist = data;
	});
	
	$scope.buyItem = function(itemId) {
	    alert("buy item " + itemId);
	};
	
	$scope.removeItem = function(index) {
		var item = $scope.shoplist.items[index];
		$http.post('/s/shoplist/item/remove',{listId : 1, itemId : item.id}).success(function(data) {
			$scope.shoplist.items.splice(index,1);
		});
	};	
	
}
