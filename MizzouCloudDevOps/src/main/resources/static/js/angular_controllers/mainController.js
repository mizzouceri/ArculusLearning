app.controller('loginController', function($scope, $window, $rootScope, $location) {

    $scope.doLogin = function() {

    // Local Github Client
	//$window.location = "https://github.com/login/oauth/authorize?scope=user:&client_id=d5dc137d4fd8f971dd84";
    
	// Cloud Github Client
	$window.location = "https://github.com/login/oauth/authorize?scope=user:&client_id=2ce95615fba7b74b1736";
    }
	
    if ($rootScope.userSessionVO == null) {
        $location.path('home_page');
    }


});


app.controller('requestController', function($scope, $http, $rootScope, mainService, sharedService, $routeParams, $timeout) {
	$scope.approvalSent = false;
	
    $scope.requestApproval = function(element) {
        
		element.textContent = "Sending..";
		
        //Send Email
        var input = {};
        input['userName'] = $scope.userSessionVO.userName;
        input['githubId'] = $scope.userSessionVO.githubId;
        input = angular.toJson(input);
        console.log(input);
        mainService.callPostRestAPI("mailer/sendMailTo", input);

        element.textContent = "Request Sent as " + $scope.userSessionVO.userName + ". Once the instructor approves your request, you will receive an email to your github associated address. You may log out for now.";
        element.disabled = true;
		element.hidden = true;
    }

    $scope.generateCertificate = function(moduleId) {
        var genCerReq = {};
        genCerReq['userId'] = $scope.userSessionVO.userId;
        genCerReq['studentName'] = $scope.userSessionVO.studentName;
        genCerReq['moduleId'] = moduleId;
        if($scope.userSessionVO.studentName)
        {
            genCerReq = angular.toJson(genCerReq);
            console.log(genCerReq);
            mainService.callPostRestAPI("certificate/generateCertificate", genCerReq);
            document.getElementById("certificate-action-info").innerHTML = "Your certificate is available. You can now download or post it in LinkedIn";
            document.getElementById("certificate-action-info").style.color="green";
        }
        else
        {
            document.getElementById("certificate-action-info").innerHTML = "Please enter the name to be displayed on certificate";
            document.getElementById("certificate-action-info").style.color="red";
        }
        
        
    }

    $scope.downloadCertificate = function(moduleId) {
        var genCerReq = {};
        genCerReq['userId'] = $scope.userSessionVO.userId;
        genCerReq['moduleId'] = moduleId;
        genCerReq = angular.toJson(genCerReq);
        console.log(genCerReq);
        mainService.callPostRestAPI("certificate/downloadCertificate", genCerReq).then(function(data) {
            console.log(data)
            var file = new Blob([data], { type: 'application/pdf' })
            var fileURL = window.URL.createObjectURL(file);

            // if you want to open PDF in new tab
            var a         = document.createElement('a');
            a.href        = fileURL; 
            //a.target      = '_blank';
            a.download    = 'MCDCertificate.pdf';
            document.body.appendChild(a);
            a.click();
            document.getElementById("certificate-action-info").innerHTML = "Certificate downloaded successfully";
            document.getElementById("certificate-action-info").style.color="green";
        },
        (error) => {
            console.log('getPDF error: ',error);
        });     
    }

    $scope.addCertificateToLinkedIn = function(moduleId) {
        var genCerReq = {};
        genCerReq['userId'] = $scope.userSessionVO.userId;
        genCerReq['moduleId'] = moduleId;
        console.log(genCerReq);
        var certUrl = "http://localhost:8080/CyberRangeAPISandBox/certificate/viewCertificate/" + moduleId + "S" + $scope.userSessionVO.userId + ".html";
        var link = "https://www.linkedin.com/profile/add?startTask=CERTIFICATION_NAME&name=" + encodeURIComponent("Mizzou Cloud & DevOps Certificate") + "&organizationId=89759384&issueYear=2022&issueMonth=2&expirationYear=2027&expirationMonth=5&certUrl=" 
                    + encodeURIComponent(certUrl)
            console.log(link)

            // if you want to open PDF in new tab
            window.open(link); 
            var a         = document.createElement('a');
            a.href        = link; 
            a.target      = '_blank';
            document.body.appendChild(a);
            a.click();
            //console.log($scope.req_count);    
    }

    $scope.req_count = "NA";


    function _refreshData() {

        mainService.callPostRestAPI("requestapproval/getAllRequests").then(function(requests) {
            $scope.requests = requests;
            //console.log(requests);
            var count = 0;
            for (req in requests) {
                //console.log(requests[req]);
                if (requests[req].accessStatus == 'N') {
                    count += 1;
                }
            }
            if (count >= 10) {
                $scope.req_count = "9+";
            } else {
                $scope.req_count = "0" + count;
            }
            //console.log($scope.req_count);

        });
    }

    function getToAddress() {
        mainService.callPostRestAPI("mailer/getToEmailAddress").then(function(data) {
            $scope.getToEmailAddress = data.emailAddress;
            console.log(data.emailAddress);


        });

    }

    $scope.updateEmail = function() {
        console.log($scope.newEmail);
		if (confirm("Are you sure you want to update this Email?\nThis email will receive requests whenever user requests to access. ")) {
            var input = {}
            input["emailAddress"] = $scope.newEmail;
            mainService.callPostRestAPI("mailer/setToEmailAddress", input).then(function(data) {
                $scope.getToEmailAddress = data.emailAddress;
                console.log(data.emailAddress);
                setTimeout(function() {
                    document.getElementById('cancelEmailUpdate').click()
                }, 20);

            });
        }
    }

	$scope.addEmail = function() {
		var input = {}
		input['userName'] = $scope.userSessionVO.userName;
		input['githubId'] = $scope.userSessionVO.githubId;
		input['customEmail'] = $scope.userSessionVO.customEmail;
		input = angular.toJson(input);
		mainService.callPostRestAPI("login/updateEmail", input);
		$scope.approvalSent = true;
	}

    $scope.clearEmailModel = function() {
        $scope.newEmail = null;
    }

    function getAdminMailAddress() {
        mainService.callPostRestAPI("mailer/getAdminMailAddress").then(function(data) {
            $scope.adminMailAddress = data.emailAddress;
            console.log(data.emailAddress);

        });
    }

    $scope.getAllRequests = function() {

        //console.log("Getting requests");
        getToAddress();
        getAdminMailAddress();
        _refreshData();
    }



    $scope.grantAccess = function(requestId, githubId) {
        //console.log("Granting Access "+ requestId);
        var input = requestId;
        var input2 = {};
		input2['githubId'] = githubId;
		input2 = angular.toJson(input2);
		mainService.callPostRestAPI("mailer/grantAccessNotification", input2);
		mainService.callPostRestAPI("requestapproval/grantAccess", input).then(function(data) {

            _refreshData();

        });
		

    }

    $scope.denyAccess = function(element, requestId, githubId) {
        //console.log("Denying Access "+ requestId);
        element.textContent = "Denying..";
        var input = requestId;
        var input2 = {};
		input2['githubId'] = githubId;
		input2 = angular.toJson(input2);
		mainService.callPostRestAPI("mailer/denyAccessNotification", input2);
		mainService.callPostRestAPI("requestapproval/denyAccess", input).then(function(data) {

            _refreshData();

        }

		);
		element.textContent = "Deny Access";
    }

    $scope.grantAll = function() {
        mainService.callPostRestAPI("requestapproval/grantAll").then(function(data) {

            _refreshData();

        });
    };
    $scope.denyAll = function() {
        mainService.callPostRestAPI("requestapproval/denyAll").then(function(data) {

            _refreshData();

        });
    };
    $scope.deleteRequest = function(element, requestId) {
        //console.log("Delete Request "+ requestId);
        //element.textContent = "Denying..";
        var input = requestId;
        mainService.callPostRestAPI("requestapproval/deleteRequest", input).then(function(data) {

            _refreshData();

        });
        //element.textContent = "Deny Access";
    }

    $scope.toggleRole = function(element, requestId, studentName, githubId, studentRole) {
        element.disabled = true;
        console.log(githubId, " -- ", studentRole);
        var input = {};
        input['requestId'] = requestId;
        input['studentName'] = studentName;
        input['githubId'] = githubId;

        mainService.callPostRestAPI("login/toggleRole", input).then(function(data) {
            console.log(data);
            _refreshData();
        });
    }



});

app.controller('overviewController', function($scope, $http, $rootScope, mainService, sharedService, $routeParams) {

    function _refreshData() {
        //console.log("Overview");
        mainService.callPostRestAPI("overview/getAllStudentUsers").then(function(students) {
            $scope.students = students;
            //console.log(students);
            //console.log("Overview Controller : Got students");

        });
    }

    $scope.getStudentsCount = function () {
        mainService.callPostRestAPI("overview/getStudentsCount").then(function(students_count) {
            $scope.students_count = students_count;
        });
    }


    $scope.getAllStudents = function() {

        _refreshData();
    }

    $scope.range = function(min, max, step) {
        step = step || 1;
        var input = [];
        for (var i = min; i < max; i += step) {
            input.push(i);
        }
        return input;
    };



    $scope.getUserCourseDetails = function(element, userId) {
        $scope.student_course_details = null;
        var student_details = {};
        if (element.textContent == "View") {
            myEl = angular.element(document.querySelectorAll('.btn'));
            myEl.text('View');
            element.textContent = "Close";

            var input = userId;
            /*mainService.callPostRestAPI("overview/getStudeUserCourseDetails",input).then(function(details) {
            	//$scope.details = details;
            	console.log(details);
            	console.log("Overview Controller getUserCourseDetails : Got Details");
            	
            });*/
            mainService.callPostRestAPI("overview/getUserModuleDetails", input).then(function(details) {
                //$scope.moduleDetails = details;

                //console.log("Overview Controller getUserCourseDetails : Got Module Details");
                //console.log(details);

                var count_status = { 1: 'NA', 2: 'NA', 3: 'NA', 4: 'NA'}
                var count_steps = { 1: -1, 2: -1, 3: -1, 4: -1}
                for (var i in details) {


                    count_status[details[i].moduleId] = details[i].labCompleted;
                    count_steps[details[i].moduleId] = details[i].stepsCompleted;
                    student_details['status'] = count_status;
                    student_details['steps'] = count_steps;
                    //$scope.status = count_status;
                    //$scope.steps = count_steps;


                }


            });
            mainService.callPostRestAPI("overview/getEvaluatioinUserDetails", input).then(function(details) {

                //$scope.evaluationDetails = details;
                //console.log("Overview Controller getUserCourseDetails : Got Evaluation Details");
                //console.log(details);
                var count_points = { 1: 'NA', 2: 'NA', 3: 'NA', 4: 'NA'};
                var count_ranks = { 1: 'NA', 2: 'NA', 3: 'NA', 4: 'NA'};

                var count = 0;
                for (var i in details) {

                    count_points[details[i].module.moduleId] = details[0].points;
                    count_ranks[details[i].module.moduleId] = details[0].rank;
                    student_details['points'] = count_points;
                    student_details['ranks'] = count_ranks;
                    //$scope.points = count_points;
                    //$scope.ranks = count_ranks;
                }
                console.log(student_details);
                $scope.student_course_details = student_details;

            });

        } else {
            element.textContent = "View";
        }
    }
});
app.controller('menuStaticController', function($scope, $location) {

    $scope.loadMenu = function(menu_name) {
        $scope.menuStatic = {};
        $location.path(menu_name);
        $scope.menuStatic[menu_name] = 'active_menu';
    }


});


app.controller('logoutController', function($scope, $window, $http) {

    $scope.logout = function(cat_name) {

        $http.get("login/logoutUser").then(
            function successCallback(response) {
                $window.location.reload()
            },
            function errorCallback(response) {
                console.log("POST-ing of data failed");
            });

    }

});

app.controller('menuController', function($scope, $location, $rootScope) {
    $scope.menu = {};

    if ($location.$$path != '/') {
        var path = $location.$$path.substring(1, $location.$$path.length);
        $scope.menu[path] = 'active';
        $rootScope.menu_name = path;
    } else {

        $scope.menu['/'] = 'active';
        $rootScope.menu_name = '/';
    }

    $scope.loadMenu = function(menu_name, level) {
        $scope.menu = {};
        $location.path(menu_name);
        $scope.menu[menu_name] = 'active';

        if (menu_name == '/') {
            $rootScope.menu_name = '/';
        } else {
            $rootScope.menu_name = menu_name;
        }

    }

    $scope.setUserSession = function() {
        var user_session = document.getElementById("userSessionVO").value;
        user_session = angular.fromJson(user_session);
        $rootScope.userSessionVO = user_session;
        console.log("Inside set user session" + user_session.role);
        if (user_session.role == 'INSTRUCTOR') {
            console.log("inside if");
            //$location.path('studentProgress');
        }
    }

    $scope.setUserSession();
});

function learnMore() {
    var moreText = document.getElementById("learn-more");
    if (moreText.style.display === "none") {
        moreText.style.display = "block";
    } else {
        moreText.style.display = "none";
    }
};


function w3_open() {
    document.getElementById("mySidebar").style.display = "block";
}

function w3_close() {
    document.getElementById("mySidebar").style.display = "none";
}



app.directive('emailcheck', function($q, $timeout) {
    // directive name should be small.
    return {
        require: 'ngModel',
        link: function(scope, elm, attrs, ctrl) {
            var usernames = ['Jim', 'John', 'Jill', 'Jackie'];

            ctrl.$asyncValidators.emailcheck = function(modelValue, viewValue) {

                if (ctrl.$isEmpty(modelValue)) {
                    // consider empty model valid
                    return $q.resolve();
                }
                var def = $q.defer();

                $timeout(function() {
                    // Mock a delayed response
                    if (modelValue.match(/^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$/m)) {
                        // 17h61a0593@cvsr.ac.in
                        // The newEmailDirective is available
                        def.resolve();
                    } else {
                        def.reject();
                    }

                }, 2000);

                return def.promise;
            };
        }
    };
});