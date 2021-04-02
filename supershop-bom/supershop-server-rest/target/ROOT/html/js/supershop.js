var supershopModule = angular.module('SuperShop',['ui.bootstrap', 'ui.router']);
  
  supershopModule.controller('productController', ['$scope', '$http', function($scope, $http) {
	  
    $scope.searchString = '';
    $scope.products = {};

    $scope.setPage = function (pageNo) {
     $scope.currentPage = pageNo;
    };  

    $scope.pageChanged = function() {

    };

    $scope.productsPerPage = 24;
    $scope.totalItems = 0;
    $scope.currentPage = 1;

    $scope.search = function() {

    	$http.get("/rest/products/search?searchString=" + $scope.searchString)
    	.success(function(response) {$scope.products = response; $scope.totalItems = $scope.products.length; });
    };
    
  }]);


	
  supershopModule.filter('startFrom', function() {
    return function(input, start) {
        start = start * 24 - 24; //parse to int
        return input.slice(start);
    }
  });


