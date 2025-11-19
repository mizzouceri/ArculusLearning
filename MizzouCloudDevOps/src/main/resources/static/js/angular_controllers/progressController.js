app.controller('progressController', function($scope, mainService, $rootScope, $location) {

	
	$scope.loadProgress = function(userId, evaluation){
		
		var input = {};
		input['userId'] = userId;
		input['evaluationCd'] = evaluation;

		input = angular.toJson(input);
		console.log(input);
		
		mainService.callPostRestAPI("evaluation/getStudentProgress", input).then(function(data) {
			console.log(data);
			$scope.datas = data;
		});
		
	}

	$scope.viewRankPage = function(moduleId){
		$location.path('/rank/'+moduleId)
		
	}
	$scope.loadProgress($rootScope.userSessionVO.userId, 'TECH_ASMT');

});

app.controller('rankController', function($scope, $location, mainService, $rootScope, $routeParams) {

	$scope.callLocation=function callLocation(){
		console.log('explorer/'+$scope.statusModules.module.difficultyLevel)
		// $location.path('explorer/'+$scope.statusModules.module.difficultyLevel)
		$location.path('explorer/BEGINNER')
	}
	$scope.callHome=function callHome(){
		console.log('explorer/mycourses'+$scope.statusModules.module.difficultyLevel)
		$location.path('mycourses')
	}
	$scope.loadRank = function( moduleId){
		
		var input = {};
		input['moduleId'] = moduleId;

		input = angular.toJson(input);
		console.log(input);
		
		mainService.callPostRestAPI("evaluation/getStudentRank", input).then(function(ranks) {
			console.log(ranks);
			$scope.ranks = ranks;
		});
		
	}

	
	$scope.loadRank($routeParams.moduleId);

});