/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

'use strict';
angular.module('todoApp')
    .controller('todoListCtrl', ['$scope', '$location', 'todoListSvc', function ($scope, $location, todoListSvc) {
        $scope.error = '';
        $scope.loadingMessage = '';
        $scope.todoList = null;
        $scope.editingInProgress = false;
        $scope.newTodoCaption = '';

        $scope.editInProgressTodo = {
            description: '',
            id: 0,
            finish: false
        };

        $scope.finishSwitch = function (id, todo) {
            todoListSvc.putItem(id, todo).error(function (err) {
                todo.finished = !todo.finished;
                $scope.error = err;
                $scope.loadingMessage = '';
            })
        };

        $scope.editSwitch = function (todo) {
            todo.edit = !todo.edit;
            if (todo.edit) {
                $scope.editInProgressTodo.description = todo.description;
                $scope.editInProgressTodo.id = todo.id;
                $scope.editInProgressTodo.finished = todo.finished;
                $scope.editingInProgress = true;
            } else {
                $scope.editingInProgress = false;
            }
        };

        $scope.populate = function () {
            todoListSvc.getItems().success(function (results) {
                $scope.todoList = results;
            }).error(function (err) {
                $scope.error = err;
                $scope.loadingMessage = '';
            })
        };

        $scope.delete = function (id) {
            todoListSvc.deleteItem(id).success(function (results) {
                $scope.populate();
                $scope.loadingMessage = results;
                $scope.error = '';
            }).error(function (err) {
                $scope.error = err;
                $scope.loadingMessage = '';
            })
        };

        $scope.update = function (todo) {
            todoListSvc.putItem($scope.editInProgressTodo.id, $scope.editInProgressTodo).success(function (results) {
                $scope.populate();
                $scope.editSwitch(todo);
                $scope.loadingMessage = results;
                $scope.error = '';
            }).error(function (err) {
                $scope.error = err;
                $scope.loadingMessage = '';
            })
        };
        
        $scope.add = function () {
            function getUser() {
                var user = localStorage.getItem('user') || 'unknown';
                localStorage.setItem('user', user);
                return user;
            }
            function guid() {
                function s4() {
                return Math.floor((1 + Math.random()) * 0x10000)
                    .toString(16)
                    .substring(1);
                }
                return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
            }
            var uuid = guid();
            todoListSvc.postItem(uuid, {
                'id': uuid,
                'description': $scope.newTodoCaption,
                'owner': getUser(),
                'finish': 'false'
            }).success(function (results) {
                $scope.newTodoCaption = '';
                $scope.populate();
                $scope.loadingMessage = results;
                $scope.error = '';
            }).error(function (err) {
                $scope.error = err;
                $scope.loadingMsg = '';
            })
        };
    }]);
