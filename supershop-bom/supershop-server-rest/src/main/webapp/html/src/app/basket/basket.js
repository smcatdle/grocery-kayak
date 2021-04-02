angular.module( 'basket', [
  'ui.router',
  'plusOne',
  'ui.bootstrap',
  'account',
  'search'
])

/**
 * Each section or module of the site can also have its own routes. AngularJS
 * will handle ensuring they are all available at run-time, but splitting it
 * this way makes each module more "self-contained".
 */
.config(['$stateProvider', function config( $stateProvider ) {
  $stateProvider.state( 'basket', {
    url: '/basket',
    views: {
    	'main': {
            templateUrl:'html/basket.html',
            controller: 'BasketCtrl'
        }
    },
    /*resolve: {
        baskets: function(basketService) {
            return basketService.getBasketsForAccount();
        }
    },*/
    data:{ pageTitle: 'Basket' }
  });
}])
.factory('basketService', ['$rootScope', '$resource', '$q', '$http', '$state', 'sharedService', function($rootScope, $resource, $q, $http, $state, sharedService) {
      var service = {};
      
      service.getBasketsForAccount = function() {

          var Basket = $resource($rootScope.BASE_URL + "/rest/baskets");
          return Basket.get().$promise;
      };
      
      service.getBasketsItemsForAccount = function(index) {
          
      	$http.get($rootScope.SECURE_BASE_URL + "/rest/baskets/basketitems?basketId=" + index)
    	.success(function(response){sharedService.updateBasket(response)}).error(function(response){console.log('Access Denied : ' + response);localStorage.clear();sharedService.updateBasket([]);$state.go('login');});
      };
      
      service.addBasketItem = function(product, productId, success, failure) {
    	  
    	  var res = $http.post($rootScope.SECURE_BASE_URL + '/rest/baskets/item', productId);
    	  
    	  res.success(function(response){sharedService.updateBasket(response);});
    	  
    	  res.error(function(data, status, headers, config) {
  			  console.log('Access Denied : ' + data);
          localStorage.clear();
          sharedService.updateBasket([]);
          $state.go('login');
    	  });
    	  
      };
      
      service.updateBasketItem = function(basketItem, success, failure) {
    	  
        console.log('Patching basket item with id [' + basketItem.id + ']');
        
    	  var res = $http.patch($rootScope.SECURE_BASE_URL + '/rest/baskets/item?basketItemId=' + basketItem.id, JSON.stringify(basketItem) );
    	  
    	  res.success(function(response){});
    	  
    	  res.error(function(data, status, headers, config) {
  			  console.log('Access Denied : ' + data);
          localStorage.clear();
          sharedService.updateBasket([]);
          $state.go('login');
    	  });
    	  
      };
      
      service.swapBasketItem = function(productId, basketId, basketItemId, success, failure) {
    	  
    	  var res = $http.patch($rootScope.SECURE_BASE_URL + '/rest/baskets/' + basketId +'/items/' + basketItemId, productId);
    	  
    	  res.success(function(response){sharedService.updateBasket(response);});
    	  
    	  res.error(function(data, status, headers, config) {
  			  console.log('Access Denied : ' + data);
          localStorage.clear();
          sharedService.updateBasket([]);
          $state.go('login');
    	  });
    	  
      };
   
      service.deleteBasketItem = function(basketId, basketItemId, success, failure) {
    	  
        console.log('deleteBasketItem() basketId [' + basketId + '] basketItemId [' + basketItemId + ']');

    	  //TODO: Update the shared basket model with servers view of the selected store basket.
    	  $http.delete($rootScope.SECURE_BASE_URL + "/rest/baskets/" + basketId +"/items/" + basketItemId).success(function(response){sharedService.updateBasket(response);}).error(function(response){console.log('Access Denied : ' + response);localStorage.clear();sharedService.updateBasket([]);$state.go('login');});
    	  
      };	
      
      return service;   
}])


.controller("BasketCtrl", ['$rootScope', '$scope', '$state', '$http', 'basketService', 'sessionService', 'sharedService', function($rootScope, $scope, $state, $http, basketService, sessionService, sharedService) {

	$scope.baskets = [];
	$scope.basketitems = [];
	
    $scope.getBaskets = function() {
  	  
    	$http.get($rootScope.BASE_URL + "/rest/baskets")
    	.success(function(response) {$scope.baskets = response; });
    };

    $scope.getBaskets();
    
    $rootScope.$on('broadcast:resume', function() {
      $scope.getBaskets();
    });
              
     $scope.selectStore= function(index) { 
    	 basketService.getBasketsItemsForAccount(index);
       sharedService.selectedStore=index;
     }
     
     $scope.deleteFromBasket = function(index) { 
     	var basketItemId = $scope.basketitems[index].id;
     	var basketId = $scope.basketitems[index].basketId;
     	basketService.deleteBasketItem(basketId, basketItemId, function() {$scope.selectStore($scope.selectedStore);}, function(){});
     }
     
     $scope.viewStore = function() {
     	$state.go('store');
     }
     
     $scope.isLoggedIn = function() {
     	return sessionService.isLoggedIn();
     }
}])

.controller('BasketTabsController', ['$scope', '$rootScope', '$http', 'sharedService', 'sessionService', 'basketService', function($scope, $rootScope, $http, sharedService, sessionService, basketService) {
	 
	$scope.baskets = [];
	
    $scope.getBaskets = function() {
  	  
    	// Load the baskets if not already retrieved
    	if ($scope.baskets instanceof Array && $scope.baskets.length < 1) {
	    	$http.get($rootScope.BASE_URL + "/rest/baskets")
	    	.success(function(response) {$scope.baskets = response; 
        })
        .error(function(response) {sharedService.loadingStoreTabs = false;
        
        });
    	}
    };
	  
    $scope.selectStore= function(chain, index) { 
      
      console.log('selectStore()');
      
      // selectionInProgress flag is to prevent re-selection when tabs are re-enabled after selectio completed (bit of a hack)
      if (!$rootScope.tabsDisabled && !sharedService.selectionInProgress) {
        sharedService.selectionInProgress  = true;
        
        console.log('Selected Store : ' + index);
          sharedService.setSelectedChain(chain, index);
          //$scope.getDepartments(sharedService.selectedChain);
      }
        
    }
    
    $scope.getBaskets();

  }

])


;