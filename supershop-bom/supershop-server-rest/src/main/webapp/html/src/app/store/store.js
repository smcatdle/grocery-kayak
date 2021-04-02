/**
 * Each section of the site has its own module. It probably also has submodules,
 * though this boilerplate is too simple to demonstrate it. Within
 * `src/app/home`, however, could exist several additional folders representing
 * additional modules that would then be listed as dependencies of this one. For
 * example, a `note` section could have the submodules `note.create`,
 * `note.delete`, `note.edit`, etc.
 * 
 * Regardless, so long as dependencies are managed correctly, the build process
 * will automatically take take of the rest.
 * 
 * The dependencies block here is also where component dependencies should be
 * specified, as shown below.
 */
angular
		.module(
				'store',
				[ 'ui.router', 'plusOne', 'ui.bootstrap', 'search', 'account',
						'basket', 'ngAnimate',
						/*
						 * 'ui.grid', 'ui.grid.edit', 'ui.grid.resizeColumns',
						 * 'rzModule',
						 */
						'kendo.directives' ])

		/**
		 * Each section or module of the site can also have its own routes.
		 * AngularJS will handle ensuring they are all available at run-time,
		 * but splitting it this way makes each module more "self-contained".
		 */
		.config([ '$stateProvider', function($stateProvider) {
			$stateProvider.state('store', {
				url : '/store',
				views : {
					'main' : {
						templateUrl : 'html/store.html',
						controller : 'StoreController'
					}
				},
				data : {
					pageTitle : 'Grocery Kayak Ireland (Beta)'
				}
			}).state('product_details', {
				url : '/product_details',
				views : {
					'main' : {
						templateUrl : 'html/product_details.html',
						controller : 'StoreController'
					}
				},
				data : {
					pageTitle : "Product Details"
				}
			}).state('contact_us', {
				url : '/contact_us',
				views : {
					'main' : {
						templateUrl : 'html/contact_us.html',
						controller : 'StoreController'
					}
				},
				data : {
					pageTitle : "Contact Us"
				}
			}).state('terms_and_cond', {
				url : '/terms_and_cond',
				views : {
					'main' : {
						templateUrl : 'html/terms_and_cond.html',
						controller : 'StoreController'
					}
				},
				data : {
					pageTitle : "Terms and Conditions"
				}
			}).state('privacy_conditions', {
				url : '/privacy_conditions',
				views : {
					'main' : {
						templateUrl : 'html/privacy_conditions.html',
						controller : 'StoreController'
					}
				},
				data : {
					pageTitle : "Privacy"
				}
			}).state('cookie_policy', {
				url : '/cookie_policy',
				views : {
					'main' : {
						templateUrl : 'html/cookie_policy.html',
						controller : 'StoreController'
					}
				},
				data : {
					pageTitle : "Cookies"
				}
			});
		} ])

		.controller(
				'StoreController',
				[
						'$scope',
						'$rootScope',
						'$http',
						'$state',
						'$mdToast',
						'$timeout',
						'$mdDialog',
						'$mdSidenav',
						'sharedService',
						'sessionService',
						'basketService',
						function($scope, $rootScope, $http, $state, $mdToast, $timeout, $mdDialog, $mdSidenav,
								sharedService, sessionService, basketService) {

							$scope.products = [];
							$scope.selectedProduct = sharedService.selectedProduct;
							$scope.searchString = '';
							$scope.selectedShelf = '';
							$scope.departments = [];
							$scope.shelves = [];
							$scope.maxSize = 6;
							$scope.productsPerPage = 24;
							$scope.totalItems = 0;
							$scope.currentPage = 1;
							$scope.loadingStoresSemaphore = false;

							$scope.status = {
								isopen : false
							};

							/* RZSlider Initialization */
							/*
							 * $scope.priceSlider = { min: 1, max: 10, ceil: 50,
							 * floor: 0 }
							 * 
							 * $scope.translate = function(value) { return
							 * '\u20AC' + value; };
							 */

							$rootScope.$on('emit:productsUpdated', function() {
								$scope.products = sharedService.products;
								$scope.totalItems = $scope.products.length;
								$timeout(function(){ sharedService.selectionInProgress = false; }, 500);
								$timeout(function(){ $rootScope.tabsDisabled = false; }, 500);
							});

							$rootScope.$on('broadcast:resume', function() {
								$scope.getDefaultProducts();
							});
							
							// Store Selected
							$rootScope
									.$on(
											'emit:chainSelected',
											function() {
												// $scope.getDepartments(sharedService.selectedChain);
												$rootScope.selectedChain = sharedService.selectedChain;
												console
														.log('Selected chain :'
																+ sharedService.selectedBasketId);
												$scope
														.getDefaultProducts(sharedService.selectedChain);
											});

							$rootScope
									.$on(
											'emit:basketUpdated',
											function() {
												console
														.log('Chain : '
																+ sharedService.selectedChain);
												$rootScope.selectedChain = sharedService.selectedChain;
												// $scope.baskets[sharedService.selectedBasketId].active
												// = true;

											});

							// Boolean check if product currently selected.
							$scope.isProductSelected = function() {
								console.log('isProductSelected'
										+ sharedService.selectedProduct);
								return sharedService.selectedProduct != '';
							}

							$scope.setPage = function(pageNo) {
								$scope.currentPage = pageNo;
							};

							$scope.pageChanged = function() {

							};

							/*$scope.viewBasket = function() {
								$state.go('basket');
							}*/

							$scope.getShelves = function(index) {

								// Load the shelves for a department
								$http
										.get(
												$rootScope.BASE_URL
														+ "/rest/products/department/shelves?chain="
														+ $rootScope.selectedChain
														+ "&department="
														+ window
																.encodeURIComponent($scope.departments[index]))
										.success(function(response) {
											$scope.shelves = response;
										});
							};

							$scope.getDefaultProducts = function() {


								if ($scope.loadingStoresSemaphore != true) {

									// Set a semaphore
									$scope.loadingStoresSemaphore = true;

									console
											.log('Retrieving default products for chain : '
													+ sharedService.selectedBasketId);
									var dateObject = new Date();
									var todaysDate = dateObject.getDate() + '-'
											+ dateObject.getMonth();
									$scope.currentPage = 1;

									// Load the default products if not already
									// retrieved for this chain
									if (($scope.defaultProducts[sharedService.selectedBasketId] == null)
											|| ($scope.defaultProducts[sharedService.selectedBasketId] instanceof Array && ($scope.defaultProducts[sharedService.selectedBasketId].length < 1 || $scope.defaultProductTimestamps[sharedService.selectedBasketId] != todaysDate))) {
										$rootScope.tabsDisabled = true;
										
										try {
											$http
													.get(
															$rootScope.BASE_URL
																	+ "/rest/products/default"
																	+ "?chain="
																	+ sharedService.selectedChain)
													.success(
															function(response) {
																
																$scope.defaultProducts[sharedService.selectedBasketId] = response;
																$scope.defaultProductTimestamps[sharedService.selectedBasketId] = todaysDate;
																console
																		.log('Updating '
																				+ $scope.defaultProducts[sharedService.selectedBasketId].length
																				+ ' defaultProducts with timestamp ['
																				+ $scope.defaultProductTimestamps[sharedService.selectedBasketId]
																				+ ']');
																sharedService
																		.updateProducts(response);
															})
													.error(
														function(response) {
															$rootScope.tabsDisabled = false;
															$mdSidenav('right').close();
														}
													);
										} catch (ex) {
											$rootScope.tabsDisabled = false;
										}
									} else {
										$scope.products = $scope.defaultProducts[sharedService.selectedBasketId];
										sharedService
												.updateProducts($scope.products);
									}

									$scope.loadingStoresSemaphore = false;
								}

							};
							
							$scope.showProductDetails = function(ev, index) {
								var product = $scope.products[(($scope.currentPage - 1) * 24)
										+ index];
								sharedService.selectProduct(product);
								$scope.selectedProduct = product;
								console.log('Product : ' + sharedService.selectedProduct.name);
								$scope.showProductDetailsDialog(ev, product);
							};
  
  							$scope.showProductDetailsDialog  = function(ev, product) {
								var dialogScope = $scope.$new();
								$mdDialog.show({
									scope: dialogScope,
									templateUrl: 'html/product_details.html',
									targetEvent: ev,
									parent: angular.element(document.querySelector('#mainContainer')),
									clickOutsideToClose:true
									})
									.then(function(answer) {
									
									}, function() {
									
								});
							  
							}
							
							
							$scope.hide = function() {
								$mdDialog.hide();
							};
							$scope.cancel = function() {
								$mdDialog.cancel();
							};
							$scope.answer = function(answer) {
								$mdDialog.hide(answer);
							};
							 
							$scope.addToBasket = function(index) {
								
								var product = null;
								var productId = {};
								
								// Close open dialogs
								$scope.cancel();
								
								if (typeof index !== 'undefined') {
									product = $scope.products[(($scope.currentPage - 1) * 24)
											+ index];
									sharedService.selectProduct(product);
									productId = {
										"id" : $scope.products[(($scope.currentPage - 1) * 24)
												+ index].id};

								} else {
									product = $scope.selectedProduct;
									productId = {
										"id" : product.id};
								}

								// Only add product if user logged in
								if (sessionService.isLoggedIn()) {

									$scope.showSimpleToast("Added '"
											+ product.name
											+ "' to Cart", null);
									basketService.addBasketItem(product,
											productId, function() {}, function() {
											});
								} else {
									$state.go('login');
								}

							}

							$scope.addProductToBasket = function(product) {
								
							}
							
							
							$scope.getDepartments = function(chain) {
								$http
										.get(
												$rootScope.BASE_URL
														+ "/rest/products/departments?chain="
														+ chain)
										.success(function(response) {
											$scope.departments = response;
										});
							}

							$scope.selectShelf = function(index) {
								$scope.selectedShelf = $scope.shelves[index];
								$http
										.get(
												$rootScope.BASE_URL
														+ "/rest/products/department/shelves/items?chain="
														+ $rootScope.selectedChain
														+ "&shelf="
														+ window
																.encodeURIComponent($scope.selectedShelf))
										.success(
												function(response) {
													$scope.products = response;
													$scope.totalItems = $scope.products.length;
												});
							}

							$scope.isLoggedIn = function() {
								return sessionService.isLoggedIn();
							}

							// $scope.getShelves();
							$scope.getDefaultProducts();
							
							// Close the splash screen
							$timeout(function(){ window.loading_screen.finish(); }, 3000);
							
							console.log('ReLoaded : ' + $scope.selectedProduct.name);
						}

				])

		.controller('DropdownCtrl', function($scope, $log) {

			$scope.toggled = function(open) {
				$log.log('Dropdown is now: ', open);
			};

		})

		.controller(
				'AlternativesCarouselCtrl',
				[
						'$scope',
						'$rootScope',
						'$timeout',
						'$http',
						'$mdBottomSheet',
						'$mdToast',
						'sharedService',
						'basketService',
						function($scope, $rootScope, $timeout, $http,
								$mdBottomSheet, $mdToast, sharedService, basketService) {

							$scope.alternatives = [];
							$scope.currentPage = 1;
							$scope.alternativesIncrementSize = 4;
							$scope.noAlternativesMax = 50;

							$rootScope.$on('emit:selectProduct', function() {
								// $scope.getAlternativeProducts(sharedService.selectedProduct.name);
							});

							$scope.getAlternativeProducts = function(
									productString) {

								// alert('getAlternativeProducts : ' +
								// productString);
								$http
										.get(
												$rootScope.BASE_URL
														+ "/rest/products/alternatives?productString="
														+ encodeURIComponent(productString))
										.success(function(response) {
											$scope.alternatives = response;
										});
							};

							$scope.scrollDownAlternatives = function() {
								console.log('Scrolling down : '
										+ $scope.currentPage);

								// Only scroll down if more items
								if (($scope.currentPage * $scope.alternativesIncrementSize) + 1 <= $scope.noAlternativesMax) {
									$scope.currentPage = $scope.currentPage + 1;
								}
							}

							$scope.scrollUpAlternatives = function() {
								console.log('Scrolling up : '
										+ $scope.currentPage);

								// Only scroll up if not at beginning of list.
								if ($scope.currentPage > 1) {
									$scope.currentPage = $scope.currentPage - 1;
								}
							}

							$scope.close = function() {
								$mdBottomSheet.hide();	
							}
							
							$scope.swapProduct = function(index) {
								product = $scope.alternatives[(($scope.currentPage - 1) * 4)
										+ index];
								productId = {
									"id" : $scope.alternatives[(($scope.currentPage - 1) * 4)
											+ index].id
								};

								try {
									basketService
											.swapBasketItem(
													productId,
													sharedService.selectedBasketId,
													sharedService.selectedBasketItem.id,
													function() {}, function() {
													});
									/*$scope.showSimpleToast("Swapped for product '"
											+ sharedService.basket.name + "'", null);*/

								} catch (err) {
								}
								$mdBottomSheet.hide();

							}

							$scope
									.getAlternativeProducts(sharedService.selectedProduct.name);

						} ])

		.controller(
				'BasketViewCtrl',
				[
						'$scope',
						'$rootScope',
						'$mdToast',
						'$timeout',
						'$mdDialog',
						'sharedService',
						'basketService',
						function($scope, $rootScope, $mdToast, $timeout, $mdDialog, sharedService,
								basketService) {

							
							$scope.basketTotal = 0;
							$scope.basketItemCount = 0;
							$scope.lastSelectedDataItem = {};
							$scope.basketItemStyle = [];
							$scope.strikeStyle = "no-strikethrough";


							$scope.initialiseStrikeThrough = function() {
								
								$scope.selectedBasketItemId = 0;
								
								// Initialse the strikethrough items
								for (i=0; i<sharedService.basket.length; i++) {
									$scope.basketItemStyle[sharedService.basket[i].id] = (sharedService.basket[i].strikethrough == true) ? "strikethrough" : "no-strikethrough";
									console.log('name' + sharedService.basket[i].name + 'id ' + sharedService.basket[i].id + ' strikethrough ' + $scope.basketItemStyle[sharedService.basket[i].id] );
								}
							}
							
							$scope.calculateBasketTotal = function() {
								var total = 0;
								$scope.basketItemCount = 0;

								for (i=0; i<sharedService.basket.length; i++) {
									total = total + (sharedService.basket[i].price * sharedService.basket[i].amount);
									$scope.basketItemCount = $scope.basketItemCount + sharedService.basket[i].amount;
								}
								$scope.basketTotal = total;
							}

							$rootScope.$on('emit:basketUpdated', function() {
								$scope.viewBasket = sharedService.basket;
								$scope.calculateBasketTotal();
								$scope.initialiseStrikeThrough();

							});

							$scope.deleteFromBasket = function() {
								basketService
										.deleteBasketItem(
												sharedService.selectedBasketId,
												$scope.selectedBasketItemId,
												function() {}, null);
								$scope.showSimpleToast("Removed '"
												+ sharedService.selectedBasketItem.name
												+ "' from Cart", null);
							}

							$scope.handleSelect = function(data, dataItem,
									columns, index) {
								$scope.selectedBasketItemId = dataItem.id;
								
								// Remember the last selected dataItem (for updates etc.)
								sharedService.lastSelectedDataItem = sharedService.selectedBasketItem;
								
								sharedService.selectedBasketItem = data;
								console.log('Selected row : '
										+ data.amount);
								sharedService.selectProduct(data);
							};
							
							$scope.getClassForRow = function(dataItem) {

								if (dataItem.available == true) {
									return 'grid-row-available';
								} else {
									dataItem.price = 0;
									return 'grid-row-not-available';
								}
							}

							$scope.getClassForImage = function(dataItem) {

								return 'product-icon-' + dataItem.chain;
							}

							$scope.onSave = function(kendoEvent) {
								
								$timeout(function(){ $scope.synchServer(); }, 500);
							}
							
							$scope.synchServer = function() {
								var basketItemIndex = $scope.findItemByName(sharedService.lastSelectedDataItem.name);

								sharedService.basket[basketItemIndex].amount = sharedService.lastSelectedDataItem.amount;
								$scope.calculateBasketTotal();

								basketService
									.updateBasketItem(
											sharedService.lastSelectedDataItem,
											function() {}, null);
							}
							
							$scope.findItemByName = function(name) {
								for (i=0; i<sharedService.basket.length; i++) {
									if (name == sharedService.basket[i].name) {
										return i;
									}
								}
							}

							$scope.onSwipeRight = function(data, dataItem, index) {
								
								//if (dataItem.strikethrough = false) {
									console.log('strike off! index : ' + index);
									$scope.basketItemStyle[index] = "strikethrough";
									dataItem.strikethrough = true;
									sharedService.lastSelectedDataItem = dataItem;
									$scope.synchServer();
								//}
							}
							
							$scope.onSwipeLeft = function(data, dataItem, index) {
								
								//if (dataItem.strikethrough = true) {
									console.log('strike on! index : ' + index);
									$scope.basketItemStyle[index] = "no-strikethrough";
									dataItem.strikethrough = false;
									sharedService.lastSelectedDataItem = dataItem;
									$scope.synchServer();
								//}
							}
							
							$scope.showItemDetails = function(ev, product) {
								sharedService.selectProduct(product);
								$scope.selectedProduct = product;	
								console.log('showItemDetails : ');
								$scope.showProductDetailsDialog(ev, product);
							}
							
  
  							$scope.showProductDetailsDialog  = function(ev, product) {
								var dialogScope = $scope.$new();
								$mdDialog.show({
									scope: dialogScope,
									templateUrl: 'html/product_details.html',
									targetEvent: ev,
									parent: angular.element(document.querySelector('#basket-grid')),
									clickOutsideToClose:true
									})
									.then(function(answer) {
									
									}, function() {
									
								});
							  
							}
							
							$scope.cancel = function() {
								$mdDialog.cancel();
							};
							
							
							$scope.detailGridOptions = function(dataItem) {
								return {
									dataSource : {
										data : $scope.viewBasket,
										schema : {
											model : {
												fields : {
													id : {
														type : "number",
														editable : false
													},
													imageURL: {
														type : "string",
														editable : false
													},
													amount : {
														type : "number",
														editable : true,
														validation : {
															required : true,
															min : 1
														},
													},
													name : {
														type : "string",
														editable : false
													},
													chain : {
														type : "string",
														editable : false
													},
													price : {
														type : "number",
														editable : false,
														format : "{0:c2}",
													}

												}
											}
										},
										pageSize : 200
									},
									selectable : "row",
									scrollable : true,
									height : "370px",
									sortable : false,
									filterable : false,
									pageable : false,
									rowTemplate : '<tr md-swipe-right="onSwipeRight(data, dataItem, dataItem.id)" md-swipe-left="onSwipeLeft(data, dataItem, dataItem.id)" data-uid="#: uid #" ng-class="getClassForRow(dataItem)"><td style="background: white;"><img style="align: left; margin: 1px; width: 55px; height: 55px;" class="getClassForImage(dataItem)" ng-click="showItemDetails($event, dataItem);" ng-src="#:imageURL #" /></td><td align="center" ng-blur="updateAmount(dataItem);">#:amount #</td><td class="basket-name-text {{basketItemStyle[dataItem.id]}}">#:name #</td><td>{{#:price # | currency:"€"}}</td></tr>',
									columns : [ {
										field : "imageURL",
										title : "Item",
										width : "22%"
									},{
										field : "amount",
										title : "#",
										width : "18%"
									}, {
										field : "name",
										title : "Product",
										width : "40%"
									}, {
										field : "price",
										title : 'Cost',
										width : "20%"
									}

									]
								};
							};

						} ])

.filter('shorten', function() {
	return function(input, maxSize) {
		var size = input.length;
		var newSize = maxSize - 1;

		if (size <= maxSize)
			return input.substring(0, size);

		return input.substring(0, newSize) + '...';
	}
})

// Image URL Decorator
.filter('decorateImageUrl', function() {
	return function(input, chain) {
        
        if (chain == 'm') {
		  return 'https://www.mace.ie' + input;
        } else {
            return input;
        }
	}
})

.filter('startFrom', function() {
	return function(input, start, blockSize) {
		start = start * blockSize - blockSize; //parse to int
		return input.slice(start);
	}
})

.filter('percent', function() {
	return function(input) {
		return parseFloat(Math.round(input * 100) / 100).toFixed(0);
	}
})
