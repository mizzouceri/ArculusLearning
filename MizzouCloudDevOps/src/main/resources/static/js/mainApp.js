var app = angular.module("mainApp", ["ngRoute", 'chart.js']);



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

function showStep(id) {
    $("#" + id).toggle("slow");
    //$("#lab_step_1_1").css("display","");

}

(function(ChartJsProvider) {
    ChartJsProvider.setOptions({ colors: ['#803690', '#00ADF9', '#DCDCDC', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'] });
});

app.controller("BarCtrl", function($scope) {
    $scope.labels = ['Lab 2', 'Lab 3', 'Lab 4'];
    $scope.series = ['Level 1', 'Level 2', 'Level 3'];

    $scope.data = [
        [65, 59, 100],
        [28, 48, 40],
        [48, 78, 49]
    ];

    $scope.labels1 = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'];

    $scope.data1 = [
        [65, 59, 80, 81, 56, 55, 40, 55, 32, 56]

    ];

    $scope.colors = ['#28a745'];



});

app.controller("MainChartCtrl", function($scope, $location, $http, $rootScope, mainService, $routeParams) {
    $scope.date = new Date();
    $scope.courseLists = [
        ["MyCoursination", 50],
        ["YourCoursination of euro", 30],
        ["Data Science", 50],
        ["Machine Learning", 30],
        ["Cyber Security", 50],
        ["Heo", 30],
        ["DCA", 30],
    ];
    $scope.chartsFor = "All Courses";

    function _refreshData() {

        //console.log("All Courses");
        /*mainService.callPostRestAPI("modules/getAllModules").then(function(courses) {
            $scope.disableCreate = true;
            $scope.courses = courses;
            console.log(courses);
            console.log("Course Editor Controller : Got Courses");

        });*/

        mainService.callPostRestAPI("modules/getUserModuleCount").then(function(courseData) {

            $scope.courseEnrollment = courseData;

            var sum = 0;
            courseData.forEach(ai => {
                sum += ai.count;
            });
            $scope.totalEnrollments = sum;

            console.log("Course Editor Controller : Got Courses Data");

        });

        mainService.callPostRestAPI("modules/getUserModuleDataMonthWise").then(function(graphStats) {


            //console.log(graphStats);
            $scope.graphStats = graphStats;

            console.log("getUserModuleDataMonthWise : Got Courses Data");
            //select COUNT(u.module_id), u.module_id, m.module_title, {fn MONTHNAME(u.enrollment_dt)} as Month, YEAR(u.enrollment_dt) as Year from user_module u, modules m where m.module_id = u.module_id group by {fn MONTHNAME(u.enrollment_dt) }, YEAR(u.enrollment_dt), u.module_id, m.module_title order by u.module_id;
        });

    }

    $scope.getData = function() {
        //console.log("Hey");
        _refreshData();
        var element = document.querySelector("#allCourses");
        //var result = angular.element(element);

        //console.log(result);
        //console.log(document.querySelector("#allCourses"))

        // result.css({
        //     'background': '#46BFBD',
        //     'color': 'white'
        // });


        setTimeout(function() {
            $scope.go(element, "All Courses", 0);
        }, 1000);
    }

    function setPieData() {
        var high = 0;
        var med = 0;
        var low = 0;
        var least = 0;
        var totalSum = 0;
        $scope.highPoints = -1;
        $scope.lowPoints = 101;
        $scope.courseAvg = 0;
        var length = Object.keys($scope.ranks).length;
        $scope.assestmentCompleted = length;
        for (var i = 0; i < length; i++) {
            var points = $scope.ranks[i].points;
            //console.log(points)
            totalSum += points;
            if (points > $scope.highPoints) {
                $scope.highPoints = points;
            }
            if (points < $scope.lowPoints) {
                $scope.lowPoints = points;
            }
            if (points >= 90 && points <= 100) {
                high += 1;
            } else if (points >= 70 && points < 90) {
                med += 1;
            } else if (points >= 50 && points < 70) {
                low += 1;
            } else if (points < 50) {
                least += 1;
            }
        }
        $scope.courseAvg = totalSum / length;
        //console.log(high, med, low, least);
        $scope.pie.data = [high, med, low, least];

    }

    function setBarData() {
        $scope.bar.labels = [];
        $scope.bar.data[0] = [];
        //console.log($scope.stepData[0]["Step-0"]);
        var length = $scope.stepData[0]["totalSteps"];
        for (let index = 0; index <= length; index++) {
            $scope.bar.labels.push("STEP " + index);
            //console.log($scope.stepData[0]["Step-" + index]);
            $scope.bar.data[0].push($scope.stepData[0]["Step-" + index]);
        }
        if (length > 0) {
            $scope.bar.labels.push("COMPLETED");
            $scope.bar.data[0].push($scope.stepData[0]["Completed"]);

        }
        //$scope.bar.data = [$scope.stepData[0]["Step-0"],$scope.stepData[0]["Step-0"]]
    }

    $scope.loadRank = function(moduleId) {

        $scope.ranks = null;
        $scope.pie.data = [];
        var input = {};
        input['moduleId'] = moduleId;

        input = angular.toJson(input);
        //console.log(input);

        mainService.callPostRestAPI("evaluation/getPeerRanks", input).then(function(ranks) {
            //getStudentRank, getPeerRanks
            //console.log("these are the ranks");
            //console.log(ranks);
            $scope.ranks = ranks;
            setPieData();
        });



    }

    $scope.loadStepData = function(moduleId) {
        $scope.stepData = null;
        var input = {};
        input['moduleId'] = moduleId;

        input = angular.toJson(input);
        //console.log(input);
        mainService.callPostRestAPI("modules/getUserModuleStepData", input).then(function(stepData) {
            //console.log(stepData);
            $scope.stepData = stepData;

            setBarData();
        });
    }

    $scope.setLineChartData = function(moduleId) {
        $scope.line.data[0] = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
        $scope.line.series = [""];

        if (moduleId > 0) {

            var courseslength = Object.keys($scope.courseEnrollment).length;
            for (let index = 0; index < courseslength; index++) {
                if ($scope.courseEnrollment[index].moduleId == moduleId) {
                    $scope.line.series[0] = $scope.courseEnrollment[index].moduleTitle;
                }

            }
            //console.log($scope.graphStats[moduleId]);
            var length = Object.keys($scope.graphStats[moduleId]).length;
            for (let index = 0; index < length; index++) {
                console.log("Data -- " + $scope.graphStats[moduleId][index]);
                var m = parseInt($scope.graphStats[moduleId][index].Month);
                var count = $scope.graphStats[moduleId][index].Count;
                $scope.line.data[0][m - 1] = count;
            }
            //console.log($scope.line.data[0]);
        } else if (moduleId == 0) {
            $scope.line.series = ["All Courses"];
            $scope.line.data[0] = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];

            for (id in $scope.graphStats) {
                var length = Object.keys($scope.graphStats[id]).length;
                //console.log("length : " + length);
                for (let index = 0; index < length; index++) {
                    //console.log("Data -- " + $scope.graphStats[id][index]);
                    var m = parseInt($scope.graphStats[id][index].Month);
                    var count = $scope.graphStats[id][index].Count;
                    $scope.line.data[0][m - 1] += count;
                    //$scope.line.data[1][m - 5] += count + 10;

                }
                //console.log($scope.line.series);

            }
            //console.log("For alll... Done ");
        }
    }

    $scope.go = function(element, moduleTitle, moduleId) {
        //console.log(element);
        $scope.chartsFor = moduleTitle;
        $scope.moduleId = moduleId;
        var result = angular.element(document.querySelectorAll(".table-row"));
        result.css({
            'background': '#f5f5f5',
            'color': 'black'
        });

        element.style.background = "#46BFBD";
        element.style.color = "white";


        //console.log("Hey")

        $scope.loadRank(moduleId);
        $scope.loadStepData(moduleId);
        $scope.setLineChartData(moduleId);
    };


    $scope.line = {};
    $scope.pie = {};
    $scope.bar = {};
    $scope.averages = {}

    //----- Line chart -- //
    $scope.line.labels = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    $scope.line.series = ['Series A', 'Series B'];

    $scope.line.data = [
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    ];

    $scope.onClick = function(points, evt) {
        console.log(points, evt);
    };

    $scope.line.datasetOverride = [{ yAxisID: 'y-axis-1' }];
    $scope.line.options = {
        scales: {
            yAxes: [{
                id: 'y-axis-1',
                type: 'linear',
                display: true,
                position: 'left'
            }, ]
        },
        responsive: true,
        maintainAspectRatio: false,
    };

    //----- End of Line chart -- //

    //----- Pie chart -- //
    $scope.pie.labels = ["Score(90-100)", "Score(70-89)", "Score(50-69)", "Score(0-49)"];
    $scope.pie.data = [30, 20, 40, 10];
    $scope.pie.options = {
        responsive: true,
        maintainAspectRatio: true,
        layout: {
            padding: {
                left: -10
            },
            margin: {
                left: 0
            },
        },

        legend: {
            display: false
        }
    };
    $scope.pie.colors = ['#ffd700', '#C0C0C0', '#CD7F32', '#FF0000'];
    //----- End of Pie chart -- //

    //----- Bar chart -- //

    //$scope.bar.series = ['Series A', 'Series B', 'Series C'];
    //$scope.bar.series = ['Series A', 'Series B', 'Series C'];
    //$scope.bar.labels = ['STEP 1', 'STEP 2', 'STEP 3', 'STEP 4', 'COMPLETED'];
    $scope.bar.labels = [];


    //set bar.data


    $scope.bar.data = [
        [],
        //[65, 59, 80, 81],
        //[65, 59, 80, 81],
    ];



    //----- End of Bar chart -- //


});