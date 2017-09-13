'use strict';
angular.module('todoApp', ['ngRoute'])
    .config(['$routeProvider',  function ($routeProvider) {
        $routeProvider.when('/Home', {
            controller: 'homeCtrl',
            templateUrl: '/Views/Home.html',
        }).when('/TodoList', {
            controller: 'todoListCtrl',
            templateUrl: '/Views/TodoList.html',
        }).otherwise({redirectTo: '/Home'});
    }]);
