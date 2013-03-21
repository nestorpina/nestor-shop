'use strict';

/* Controllers */
var currentSL;
/**
 * Product List
 * Methods : 
 * - addProduct - add a product to the selected shopping list
 */
function ProductListCtrl($scope, $http) {
	$http.get('/s/product/all').success(function(data) {
		$scope.products = data;
	});

	$scope.addProduct = function(productId) {
		if(!currentSL) {
			showError('listNotSelectedAlert');
			return false;
		}
		
		$http.post('/s/shoplist/item/',{listId : currentSL, productId : productId}).success(function(data) {
		});
	};
	
	$scope.orderProp = 'name';
}

/**
 * Product Detail
 * Methods :  
 */
function ProductDetailCtrl($scope, $routeParams, $http) {

	$http.get('/s/product/id/' + $routeParams.productId).success(function(data) {
		$scope.product = data;
	});
}

/**
 * Shopping Lists List
 * Methods : 
 * - deleteSL : delete a shopping list
 * - newSL : show form to create a shopping list
 * - addSL : create a shopping list
 * - cancelAdd : hide form to create a shopping list
 */
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
        $('input[ng-model=name]').focus();
    };
    
    $scope.addSL = function() {
    	$http.post('/s/shoplist',{name : $scope.name}).success(function(data) {
            $scope.shoplists.push(data);
		});
        
        $scope.cancelAdd();
    };
    
    $scope.selectSL = function(shoplist) {
    	currentSL = shoplist.id;
    	$("#currentSL").html("Current list: " + shoplist.name).prop("href","#shoplists/"+currentSL);
    	hideError('listNotSelectedAlert');
    };    
    
    $scope.cancelAdd = function() {
        $scope.name = '';
        $scope.newFormEnabled = false;
    };	

	$scope.orderProp = 'name';
}

/**
 * Shopping Lists Detail
 * Methods : 
 * - buyItem : marks as bought an item from the shopping list
 * - removeItem : deletes an item from the shopping list
 */
function ShopListDetailCtrl($scope, $routeParams, $http) {

	$http.get('/s/shoplist/id/' + $routeParams.shoplistId).success(function(data) {
		$scope.shoplist = data;
	});
	
	$scope.buyItem = function(item, index) {
		console.log(item);
		var shoplist = $scope.shoplist.shoplist;
		$http.post('/s/shoplist/item/buy',{listId : shoplist.id, itemId : item.id}).success(function(data) {
			$scope.shoplist.items[index] = data
		});
	};
	
	$scope.removeItem = function(index) {
		var shoplist = $scope.shoplist.shoplist;
		var item = $scope.shoplist.items[index];
		$http.post('/s/shoplist/item/remove',{listId : shoplist.id, itemId : item.id}).success(function(data) {
			$scope.shoplist.items.splice(index,1);
		});
	};	
	
}
