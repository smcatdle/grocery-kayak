angular.module('account', ['ui.router', 'ngResource', 'base64'])
.config(['$stateProvider', function($stateProvider) {
    $stateProvider.state('login', {
        url:'/login',
        views: {
            'main': {
                templateUrl:'account/login.tpl.html',
                controller: 'LoginCtrl'
            }
        },
        data : { pageTitle : "Login" }
    })
    .state('register', {
            url:'/register',
            views: {
                'main': {
                    templateUrl:'account/register.tpl.html',
                    controller: 'LoginCtrl'
                }
            },
            data : { pageTitle : "Registration" }
            }
    )

}])
.factory('sessionService', ['$rootScope', '$http', '$base64', '$state', function($rootScope, $http, $base64, $state) {
    var session = {};
    $rootScope.invalidLogin = false;
    
    session.login = function(data) {
    	
        return $http.post($rootScope.SECURE_BASE_URL + "/login", "username=" + window.encodeURIComponent(data.email) +
        "&password=" + window.encodeURIComponent(data.password), {
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        } ).then(function(data) {
        	localStorage.setItem("session", data.name);
            $rootScope.invalidLogin = false;
        	$state.go("store");
        }, function(response) {
            try {
                if (response.code == 401) {
                    $rootScope.invalidLogin = true;
                    //$scope.showSimpleToast("Invalid username or password : " + response.code, null); 
                } else {
                    $rootScope.invalidLogin = true;
                    //$scope.showSimpleToast("Invalid username or password : " + response.code, null);
                    console.log("error logging in : " + response);
                }
            } catch (ex) {
                $rootScope.invalidLogin = true;
                //$scope.showSimpleToast("Invalid username or password : " + response.code, null);
                console.log('Error authenticating user.');
            }
        });
    };
    session.logout = function() {
        localStorage.removeItem("session");
    	
    	$http.post($rootScope.SECURE_BASE_URL + "/logout", {}, {} ).then(function(data) {

    	        }, function(data) {
                    localStorage.clear();
    	        	$state.go('store');
    	        });  	
    	
    };
    session.isLoggedIn = function() {
        var loggedIn = localStorage.getItem("session") != null;
        console.log("User logged in : " + loggedIn);
        return localStorage.getItem("session") !== null;
    };
    return session;
}])
.factory('accountService', ['$resource', function($resource) {
    var service = {};
    service.register = function(account, success, failure) {
        var Account = $resource("/rest/accounts");
        Account.save({}, account, success, failure);
    };

    return service;
}])
.controller("LoginCtrl", ['$scope', '$rootScope', 'sessionService', 'accountService', '$state', function($scope, $rootScope, sessionService, accountService, $state) {
    
    $rootScope.invalidLogin = false;
    
    $scope.login = function() {

            sessionService.login($scope.account).then(function() {
            	$scope.loggedInUser = localStorage.getItem("session");
               
            });

    };

    
    $scope.register = function() {
    	
        accountService.register($scope.account,
        function(returnedData) {
        	
            sessionService.login($scope.account).then(function() {
            	$scope.loggedInUser = localStorage.getItem("session");
                $state.go("store");
            });
        },
        function() {
            alert("Error registering user");
        });
    };

    $scope.isLoggedIn = function() {
    	return sessionService.isLoggedIn();
    }
    
    $scope.logout = function() {
    	sessionService.logout();
    }

}]);