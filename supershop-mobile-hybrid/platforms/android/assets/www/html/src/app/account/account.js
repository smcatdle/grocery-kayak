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
    	
       /**
        * to save the username and password in the shared preferences (if mobile app)
        */
        if (window._cordovaNative) {
            try {
                
                window.arkPref("save", "email", data.email, function (prefObj) {
                    //alert(JSON.stringify(prefObj));
                });
                
                window.arkPref("save", "password", data.password, function (prefObj) {
                    //alert(JSON.stringify(prefObj));
                });

            } catch (ex) {
                alert(ex);
            }
        }
        
 
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
    session.relogin = function() {
    	
        if (window._cordovaNative && localStorage.getItem("session") == null) {
            try {
                var data = {'email': '', 'password': ''};
                
                window.arkPref("get", "email", " ", function (prefObj) {
                    //alert(JSON.stringify(prefObj));
                    data.email = prefObj.email;
                });

                window.arkPref("get", "password", " ", function (prefObj) {
                    //alert(JSON.stringify(prefObj));
                    data.password = prefObj.password;
                    session.login(data);
                });

            } catch (ex) {
                alert(ex);
            }
        } 	
    	
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