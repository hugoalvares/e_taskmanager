app.service('TaskService', ['$http', function($http){

	wsUrl = 'http://localhost:8080';

	this.getTasks = function() {
		/*
		return [
			{
				"id": '1',
				"title": "test",
				"image": "images/test.png"
			}
		];
		*/
		/*
		$http.get(wsUrl).then(function(response) {
			return response.data;
		});

.then(function(response) {
			console.log("Your name is: " + response.data);
		}, function(error) {
			console.log("The request failed: " + error);
		});


		*/
		return $http.get(wsUrl);
	};

}]);