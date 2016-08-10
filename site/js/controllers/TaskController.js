app.controller('TaskController', ['$scope', 'TaskService', function($scope, TaskService){

	$scope.currentTask = '';
	$scope.filter = 'all';
	TaskService.getTasks().then(function(response){
		$scope.tasks = response.data;
	});
	$scope.currentTaskId = '';

	$scope.createTask = function() {
		console.log($scope.currentTask);
	};

	$scope.setFilter = function(filter) {
		$scope.filter = filter;
	};

	$scope.setCurrentTask = function(taskId) {
		$scope.currentTaskId = taskId;
		console.log($scope.currentTaskId);
	};

}]);