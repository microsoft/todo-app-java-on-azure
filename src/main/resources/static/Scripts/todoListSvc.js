/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

'use strict';
angular.module('todoApp')
    .factory('todoListSvc', ['$http', function ($http) {
        return {
            getItems: function () {
                return $http.get('api/todolist/all');
            },
            getItem: function (id) {
                return $http.get('api/todolist?id=' + id);
            },
            postItem: function (id, item) {
                return $http.post('api/todolist?id=' + id, item);
            },
            putItem: function (id, item) {
                return $http.put('api/todolist?id=' + id, item);
            },
            deleteItem: function (id) {
                return $http({
                    method: 'DELETE',
                    url: 'api/todolist?id=' + id
                });
            }
        };
    }]);
