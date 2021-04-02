angular
		.module(
				'GroceryKayak',
				[ 'templates-app', 'templates-common', 'store', 'account',
						'basket', 'ui.router', 'ngBoilerplate.about',
						'kendo.directives', 'ngMaterial' ])

		.config(
				[
						'$stateProvider',
						'$urlRouterProvider',
						'$mdThemingProvider',
						function($stateProvider, $urlRouterProvider,
								$mdThemingProvider /*
													 * , $idleProvider,
													 * $keepaliveProvider,
													 * HateoasInterceptorProvider
													 */) {
							$urlRouterProvider.otherwise('store');
							// $idleProvider.idleDuration(1*60); // 10 minutes
							// idle
							// $idleProvider.warningDuration(30); // 30 second
							// warning
							// $keepaliveProvider.interval(1*60); // 5 minute
							// keep-alive ping
							// HateoasInterceptorProvider.transformAllResponses();

							var customBlueMap = $mdThemingProvider
									.extendPalette('blue', {
										'contrastDefaultColor' : 'light',
										'contrastDarkColors' : [ '50' ],
										'50' : 'ffffff'
									});
							$mdThemingProvider.definePalette('customBlue',
									customBlueMap);
							$mdThemingProvider.theme('default').primaryPalette(
									'customBlue', {
										'default' : '500',
										'hue-1' : '50'
									}).accentPalette('pink');
							$mdThemingProvider.theme('input', 'default')
									.primaryPalette('grey');
						} ])

		// Override AngularJS’s default names (XSRF instead of CSRF)
		/*
		 * .config(['$httpProvider', function($httpProvider) { //fancy random
		 * token, losely after https://gist.github.com/jed/982883 function
		 * b(a){return
		 * a?(a^Math.random()*16>>a/4).toString(16):([1e16]+1e16).replace(/[01]/g,b)};
		 * 
		 * $httpProvider.interceptors.push(function() { return { 'request':
		 * function(response) { // put a new random secret into our CSRF-TOKEN
		 * Cookie before each request document.cookie = 'CSRF-TOKEN=' + b();
		 * return response; } }; }); }])
		 */

		.run(function run() {

		})
		.run(['$rootScope', function ($rootScope) {
		  $rootScope.$on('$locationChangeStart', function (event, newUrl, oldUrl) {
		        console.log('newUrl :' + newUrl); 
		        console.log('oldUrl :' + oldUrl); 
		        //event.preventDefault(); // This prevents the navigation from happening
		      }
		    );
		  }])
		
		 /* .directive('idle', function($document, $idle, $timeout, $interval){
		 * return { restrict: 'A', link: function(scope, elem, attrs) { var
		 * timeout; var timestamp = localStorage.lastEventTime;
		 *  // Watch for the events set in ng-idle's options // If any of them
		 * fire (considering 500ms debounce), update localStorage.lastEventTime
		 * with a current timestamp elem.on($idle._options().events, function(){
		 * if (timeout) { $timeout.cancel(timeout); } timeout =
		 * $timeout(function(){ localStorage.setItem('lastEventTime', new
		 * Date().getTime()); }, 500); });
		 *  // Every 5s, poll localStorage.lastEventTime to see if its value is
		 * greater than the timestamp set for the last known event // If it is,
		 * reset the ng-idle timer and update the last known event timestamp to
		 * the value found in localStorage $interval(function() { if
		 * (localStorage.lastEventTime > timestamp) { $idle.watch(); timestamp =
		 * localStorage.lastEventTime; } }, 5000); } } })
		 */


		
		.controller(
				'AppCtrl',
				[
						'$rootScope',
						'$scope',
						'$state',
						'$location',
						'sessionService',
						'sharedService',
						'basketService',
						'$mdBottomSheet',
						'$mdToast',
						'$animate',
						'$mdSidenav',
						'$mdUtil',
						'$log',
						function AppCtrl($rootScope, $scope, $state, $location,
								sessionService, sharedService, basketService,
								$mdBottomSheet, $mdToast, $animate, $mdSidenav,
								$mdUtil, $log) {

							$scope.showSearch = false;
							$scope.isMobileApp = window._cordovaNative;
							$scope.toggleRight = buildToggler('right');
							$scope.toggleLeft = buildToggler('left');

							// TODO: Need to move the scope of the basket items
							$scope.selectedBasketItemId = 0;
							$scope.basketItems = [];
							$rootScope.selectedChain = '';
							$scope.selectedStore = 0;
							$rootScope.tabsDisabled = false;
							
							// Product Caching
							$scope.defaultProducts = [ [] ];
							$scope.defaultProductTimestamps = [ [] ]; // Timestamps
																		// to
																		// record
																		// last
																		// refresh.
																		// Update
																		// the
																		// defaultProducts
																		// if
																		// date
																		// changed.

							// Configuration Variables
							$rootScope.BASE_URL = '';
							
							$scope.clearBasket = function() {
								$scope.selectedBasketItemId = 0;
								$scope.basketItems = [];
								sharedService.basket = [];
							}
							
							$scope.setBaseUrl = function() {
								if (window._cordovaNative) {
									$rootScope.BASE_URL = 'https://my-grocerykayak.rhcloud.com';
									$rootScope.SECURE_BASE_URL = 'https://my-grocerykayak.rhcloud.com';
								} else {
									$rootScope.BASE_URL = 'https://my-grocerykayak.rhcloud.com';
									$rootScope.SECURE_BASE_URL = 'https://my-grocerykayak.rhcloud.com';
									//$rootScope.BASE_URL = 'https://compare-thesupermarkets.rhcloud.com';
									//$rootScope.SECURE_BASE_URL = 'https://compare-thesupermarkets.rhcloud.com';
									//$rootScope.BASE_URL = '';
									//$rootScope.SECURE_BASE_URL = '';
								}

							}

							// Set the position of the Toast message
							$scope.toastPosition = {
								bottom : false,
								top : true,
								left : false,
								right : true
							};

							$scope.getToastPosition = function() {
								return Object.keys($scope.toastPosition)
										.filter(function(pos) {
											return $scope.toastPosition[pos];
										}).join(' ');
							};

							// Show the Toast message
							$scope.showSimpleToast = function(message, theme) {
								$mdToast.show($mdToast.simple()
										.content(message).position(
												$scope.getToastPosition())
										.hideDelay(3000));
							};

							// Initialise the prefix to be used navigating URI's
							$scope.setBaseUrl();

							$scope
									.$on(
											'$stateChangeSuccess',
											function(event, toState, toParams,
													fromState, fromParams) {
												if (angular
														.isDefined(toState.data.pageTitle)) {
													$scope.pageTitle = toState.data.pageTitle;
												}

												$scope.showListBottomSheet = function(
														$event) {
													$scope.alert = '';

													if (sharedService.selectedProduct != '') {

														$mdBottomSheet
																.show(
																		{
																			templateUrl : 'html/alternatives.html',
																			controller : 'AlternativesCarouselCtrl',
																			targetEvent : $event
																		})
																.then(
																		function(
																				clickedItem) {
																			$scope.alert = clickedItem.name
																					+ ' clicked!';
																		});

													}
												};
											});

							/**
							 * Build handler to open/close a SideNav; when
							 * animation finishes report completion in console
							 */
							function buildToggler(navID) {

								var debounceFn = $mdUtil
										.debounce(
												function() {

													// If right sidenav, then
													// load the basket (Only
													// want to load basket items
													// when basket displayed!)
													if (navID == 'right') {
														sharedService.selectedBasketItem = null;
														$scope.clearBasket();
														basketService
																.getBasketsItemsForAccount(sharedService.selectedBasketId);
													}

													// Only display cart if user
													// logged in, always allow menu to open
													if ((sessionService
															.isLoggedIn() && navID == 'right') || navID == 'left') {
														
														$mdSidenav(navID)
																.toggle()
																.then(
																		function() {
																			$log
																					.debug("toggle "
																							+ navID
																							+ " is done");
																		});
													} else {
														$state.go('login');
													}

												}, 300);

								return debounceFn;
							}

							/*
							 * $scope.setSelectedChain = function() {
							 * console.log('Opening cart for chain : ' +
							 * sharedService.selectedChain);
							 * $scope.selectedChain =
							 * sharedService.selectedChain; }
							 */

							$scope.setShowSearch = function(status) {
								$scope.showSearch = status;
							}

							$scope.toggleSearchDisplay = function() {
								$scope.showSearch = !$scope.showSearch;
							}

							/*
							 * $scope.$on('$idleStart', function() { // the user
							 * appears to have gone idle
							 * console.log('$idleStart'); });
							 * 
							 * $scope.$on('$idleWarn', function(e, countdown) { //
							 * follows after the $idleStart event, but includes
							 * a countdown until the user is considered timed
							 * out // the countdown arg is the number of seconds
							 * remaining until then. // you can change the title
							 * or display a warning dialog from here. // you can
							 * let them resume their session by calling
							 * $idle.watch() console.log('$idleWarn');
							 * //alert('User will be logged out in 30 seconds');
							 * });
							 * 
							 * $scope.$on('$idleTimeout', function() { // the
							 * user has timed out (meaning idleDuration +
							 * warningDuration has passed without any activity) //
							 * this is where you'd log them
							 * console.log('$idleTimeout');
							 * sessionService.logout(); alert('User Logged
							 * out'); })
							 * 
							 * $scope.$on('$idleEnd', function() { // the user
							 * has come back from AFK and is doing stuff. if you
							 * are warning them, you can use this to hide the
							 * dialog console.log('$idleEnd'); });
							 * 
							 * $scope.$on('$keepalive', function() { // do
							 * something to keep the user's session alive
							 * console.log('$keepalive'); })
							 */

						} ])

.directive('uiFocus', [ '$timeout', function($timeout) {
	return {
		link : function(scope, elem, attr) {
			console.log('Setting focus');
			var focusTarget = attr['uiFocus'];
			scope.$watch(focusTarget, function(value) {
				$timeout(function() {
					elem[0].focus();
				});
			});
		}
	}
} ])

.controller(
		'RightCtrl',
		[ '$scope', '$timeout', '$mdSidenav', '$log',
				function($scope, $timeout, $mdSidenav, $log) {

					$scope.close = function() {

						$mdSidenav('right').close().then(function() {
							$log.debug("close RIGHT is done");
						});

					};

				} ])
						
.controller('MenuController', ['$scope', '$state', 'sessionService', function($scope, $state, sessionService) {
	  
    $scope.logout= function() { 
    	console.log('Logging Out');
        sessionService.logout();
    }

    $scope.isLoggedIn = function() {
    	return sessionService.isLoggedIn();
    }
	
	$scope.gotoScreen = function(state) {
    	$state.go(state);
    }
	
  }

])		
;
