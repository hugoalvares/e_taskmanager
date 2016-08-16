app.controller('TaskController', ['$scope', '$http', function($scope, $http){

	this.serverUrl = 'http://localhost:8080/';
	this.doneStatus = 'D';
	this.pendingStatus = 'P';

	this.getTasks = function() {
		$http.get(this.serverUrl + 'gettasks').then(function(response) {
			$scope.tasks = response.data;
		});
	};

	$scope.deleteTask = function(idTask) {
		$http.get(this.serverUrl + 'deletetask?idtask=' + idTask).then(function(response) {
			$scope.tasks = response.data;
		});
	}.bind(this);

	$scope.createTask = function() {
		$http.get(this.serverUrl + 'createtask?title=' + $scope.currentTaskTitle).then(function(response) {
			$scope.tasks = response.data;
		});
	}.bind(this);

	$scope.currentTaskTitle = '';
	$scope.filter = 'all';
	$scope.tasks = '';
	this.getTasks();
	$scope.currentIdTask = '';	

	$scope.setFilter = function(filter) {
		$scope.filter = filter;
	};

	$scope.setCurrentTask = function(idTask) {
		$scope.currentIdTask = idTask;
	};

	$scope.checkFilter = function(status) {
		if ($scope.filter == 'all') {
			return true;
		} else if ($scope.filter == 'pending') {
			return status == this.pendingStatus;
		} else if ($scope.filter == 'done') {
			return status == this.doneStatus;
		}
	}.bind(this);

}]);