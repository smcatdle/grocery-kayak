angular
		.module('search', [ 'ui.router', 'plusOne', 'ui.bootstrap', 'account' ])

		.factory(
				'sharedService',
				[
						'$rootScope', 'sessionService',
						function($rootScope, sessionService) {
							var sharedService = {};

							sharedService.products = [];
							sharedService.basket = [];
							sharedService.selectedProduct = {};
							sharedService.selectedBasketItem = {};
							sharedService.selectedChain = 'x';
							sharedService.selectedBasketId = 0;
							sharedService.lastSelectedDataItems = {};
							sharedService.loadingStoreTabs = false;
							sharedService.selectionInProgress = false;
							
							sharedService.selectProduct = function(product) {
								this.selectedProduct = product;
								this.emitMessage('emit:selectProduct');
							};

							sharedService.selectBasketItem = function(
									basketItem) {
								this.selectedBasketItem = basketItem;
								this.emitMessage('emit:selectBasketItem');
							};

							sharedService.updateProducts = function(products) {
								this.products = products;
								this.emitMessage('emit:productsUpdated');
							};

							sharedService.updateBasket = function(basket) {

								if (basket instanceof Array
										&& basket.length > 0) {
									this.basket = basket;
									//sharedService.selectedChain = basket[0].chain;
									// sharedService.selectedBasketId =
									// basket[0].basketId;
								} else {
									this.basket = [];
								}

								console.log('sharedService.updateBasket : '
										+ sharedService.selectedChain);
								this.emitMessage('emit:basketUpdated');
							};

							sharedService.setSelectedChain = function(chain,
									index) {
								this.selectedChain = chain;
								sharedService.selectedBasketId = index;
								console.log('setSelectedChain()');
								this.emitMessage('emit:chainSelected');
							}

							sharedService.emitMessage = function(message) {
								$rootScope.$emit(message);
							};

							return sharedService;
						} ])

		.controller(
				'TypeaheadCtrl',
				[
						'$rootScope',
						'$scope',
						'$http',
						'sharedService',
						function($rootScope, $scope, $http, sharedService) {

							$scope.searchStringEntered = '';

							// Any function returning a promise object can be
							// used to load values asynchronously
							$scope.getTypeAheadProducts = function(val) {
								return $http.get(
										$rootScope.BASE_URL
												+ '/rest/products/search', {
											params : {
												searchString : val
											}
										}).then(
										function(response) {
											var products = [];
											angular.forEach(response.data,
													function(item) {
														products.push(item);
													});
											return products;
										});
							};

							$scope.selectTypeAheadItem = function(item, model,
									label) {
								$scope.search(label);
							}

							$scope.searchUserEnteredString = function() {
								angular.element('#thumbnails').focus();
								$scope.search($scope.searchStringEntered);
							}

							$scope.search = function(searchString) {

								$http
										.get(
												$rootScope.BASE_URL
														+ "/rest/products/search?searchString="
														+ window
																.encodeURIComponent(searchString))
										.success(
												function(response) {
													$scope.products = response;
													sharedService
															.updateProducts(response);
													$scope.searchStringEntered = '';
													$scope.setShowSearch(false);
												});
							};

						} ]);
