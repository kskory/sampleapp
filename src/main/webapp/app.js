var app = angular.module('myApp', []);

app.controller('ctrl', function ($scope, $http) {
    var apiUrl = 'http://sample-env.s2u2q23k5p.us-east-1.elasticbeanstalk.com';
    $scope.wallet = {};
    $scope.transactions = {};
    $scope.transactionToAdd = {};

    var loadData = function () {
        $http.get(apiUrl + '/wallet').then(function (result) {
            $scope.wallet = result.data;
        });

        $http.get(apiUrl + '/transactions').then(function (result) {
            $scope.transactions = result.data;
        });
    };

    loadData();

    $scope.add = function () {
        var transactionToAdd = $scope.transactionToAdd;
        transactionToAdd.date = $scope.transactionToAdd.date.getTime();
        $http.post(apiUrl + '/transactions', transactionToAdd).then(function () {
            loadData();
        });
        $scope.transactionToAdd = {};
    };

    $scope.remove = function (id) {
        $http.delete(apiUrl + '/transactions/' + id).then(function () {
            loadData();
        });
    };
});