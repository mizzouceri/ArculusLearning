var app = angular.module("mainApp", [ "ngRoute",'chart.js' ]);



/*function selectTab(tab_id){
	
	
	$(".lab-tab .nav-link").removeClass("active show");
	$(".lab-tab #"+tab_id+"-tab").addClass("active show");
	$(".lab-tab .tab-pane").removeClass("active show");
	$(".lab-tab #"+tab_id).addClass("active show");
}
*/

$("#example-vertical").steps({
    headerTag: "h3",
    bodyTag: "section",
    transitionEffect: "slideLeft",
    stepsOrientation: "vertical"
});

function showStep(id){
	$("#"+id).toggle("slow");
	//$("#lab_step_1_1").css("display","");

}

(function (ChartJsProvider) {
	  ChartJsProvider.setOptions({ colors : [ '#803690', '#00ADF9', '#DCDCDC', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'] });
	}); 

app.controller("BarCtrl", function ($scope) {
	  $scope.labels = ['Lab 2', 'Lab 3', 'Lab 4'];
	  $scope.series = ['Level 1', 'Level 2','Level 3'];

	  $scope.data = [
	    [65, 59, 100],
	    [28, 48, 40],
	    [48, 78, 49]
	  ];
	  
	  $scope.labels1 = ['1', '2', '3', '4', '5', '6', '7','8','9','10'];

	    $scope.data1 = [
	      [65, 59, 80, 81, 56, 55, 40, 55,32,56]
	     
	    ];
	    
	    $scope.colors = ['#28a745'];
	    
	});
	              
