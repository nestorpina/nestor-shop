'use strict';

/* Controllers */
var currentSL;
/**
 * Product List
 * Methods : 
 * - addProduct - add a product to the selected shopping list
 */
function ProductListCtrl($scope, $http, $location) {
	$http.get('/s/product/all' + preventCache())
	.success(function(data) {
		$scope.products = data;
	});

	$scope.addProduct = function(productId) {
		if(!currentSL) {
			showError('listNotSelectedAlert');
			return false;
		}
		
		$http.post('/s/shoplist/item/',{listId : currentSL, productId : productId}).success(function(data) {
			// Update list counter on navbar
			var newValue = parseInt($("#listBadge").html())+1;
			$("#listBadge").html(newValue).effect("highlight", {color : "cyan" }, 1000);
		});
	};
	
	$scope.orderProp = 'name';
}

/**
 * Product Detail
 * Methods :  
 */
function ProductDetailCtrl($scope, $routeParams, $http, productService) {

	$http.get('/s/product/' + $routeParams.productId + preventCache()).success(function(data) {
		$scope.product = data;
	});

}

/**
 * Product Add Form
 * Methods :  
 * - cancelAdd : returns to previos page
 * - addNewProduct : adds a product to the bbdd
 */
function ProductNewCtrl($scope, $routeParams, $http, $window, productService) {

	$scope.cancelAdd = function() {
		$window.history.back();
	};	

	
	$scope.addNewProduct = function() {
		$('#saveButton').button('loading');
		$http.post('/s/product/', $scope.product).success(function(data) {
			alert('Product saved!');
		}).error(function() {
			alert('Duplicate product name');
		}).
		then(function() {
			$('#saveButton').button('reset');
		});
	};
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
	$http.get('/s/shoplist/all' + preventCache()).success(function(data) {
		if(!data) data = [];
		$scope.shoplists = data;
		
	});
	
    $scope.deleteSL = function(p_list) {
    	console.log(p_list);
    	console.log($scope.shoplist);
		$http.delete('/s/shoplist/'+p_list.id).success(function(data) {
			 removeFromModel($scope.shoplists, p_list) 
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
    	var value = "Current list: " + shoplist.name + '<span class="badge badge-info" id="listBadge" style="margin-left:5px">'+shoplist.itemsTotal+'</span>';
    	$("#currentSL").html(value).prop("href","#shoplists/"+currentSL).stop().effect("highlight", {color : "cyan"}, 1000);
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

	$http.get('/s/shoplist/' + $routeParams.shoplistId + preventCache()).success(function(data) {
		$scope.shoplist = data;
	});
	$http.get('/s/shoplist/' + $routeParams.shoplistId + '/items' + preventCache()).success(function(data) {
		$scope.items = data;
	});	
	
	$scope.buyItem = function(item) {
		var shoplistId = $scope.shoplist.id;
		$http.post('/s/shoplist/item/buy',{listId : shoplistId, itemId : item.id}).success(function(data) {
			updateModel($scope.items,data)
			$http.get('/s/shoplist/' + shoplistId + preventCache()).success(function(data) {
				$scope.shoplist = data;
			});			
		});
	};
	
	$scope.removeItem = function(item) {
		var shoplistId = $scope.shoplist.id;
		$http.post('/s/shoplist/item/remove',{listId : shoplistId, itemId : item.id}).success(function(data) {
			removeFromModel($scope.items,item)
			$http.get('/s/shoplist/' + shoplistId + preventCache()).success(function(data) {
				$scope.shoplist = data;
			});
			if(shoplistId == currentSL) {
				// Update list counter on navbar
				var newValue = parseInt($("#listBadge").html())-item.quantity;
				$("#listBadge").html(newValue).stop().effect("highlight", {color : "cyan" }, 1000);
			}
		});
	};	
	
}


