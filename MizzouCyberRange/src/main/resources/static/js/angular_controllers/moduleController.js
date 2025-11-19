app.controller('moduleController', function($scope, $location, $rootScope, mainService) {
    $scope.loadModuleBasedDifficultyLevel = function(level) {

        var input = {};
        input['difficultyLevel'] = level;
        input['userId'] = $rootScope.userSessionVO.userId;

        input = angular.toJson(input);
        mainService.callPostRestAPI("modules/getModulesFromDifficultyLevel", input).then(function(modules) {
            $scope.modules = modules;

        });
    }

    $scope.viewIntro = function(module) {
        $location.path("/lab_intro/" + module.moduleId);

    }
    $scope.viewCourse = function(module) {
        $location.path("/lab_main/" + module.moduleId);

    }

    $scope.loadModuleBasedDifficultyLevel('NEUTRAL');
});

app.controller('moduleDisplayController', function($scope, $location, $rootScope, mainService, sharedService, $routeParams) {
    //$scope.modules=sharedService.getSharedData();
    $scope.loadModuleBasedDifficultyLevel = function(level) {

        var input = {};
        input['difficultyLevel'] = level;
        input['userId'] = $rootScope.userSessionVO.userId;

        input = angular.toJson(input);
        mainService.callPostRestAPI("modules/getModulesFromDifficultyLevel", input).then(function(modules) {
            $scope.modules = modules;

        });
    }

    $scope.viewIntro = function(module) {
        $location.path("/lab_intro/" + module.moduleId);

    }
    $scope.viewCourse = function(module) {
        $location.path("/lab_main/" + module.moduleId);

    }

    $scope.loadModuleBasedDifficultyLevel("NEUTRAL");
});

app.controller('moduleDeleteController', function($scope, $location, $rootScope, mainService, sharedService, $routeParams) {

    //$scope.modules=sharedService.getSharedData();
    /*$scope.loadModuleBasedDifficultyLevel = function(level) {

        var input = {};
        input['difficultyLevel'] = level;
        input['userId'] = $rootScope.userSessionVO.userId;

        input = angular.toJson(input);
        mainService.callPostRestAPI("modules/getModulesFromDifficultyLevel", input).then(function(modules) {
            $scope.modules = modules;

        });
    }*/

    $scope.deleteModule = function(module) {
        console.log("Delete Module Funciton");
        if (confirm("Do you really want to delete this module?\nModule Name : " + module.moduleTitle + "\n\nNote: This action is not reversible.")) {
            console.log("Deleting " + module.moduleId)
            var input = {};
            input['moduleId'] = module.moduleId;
            input['moduleTitle'] = module.moduleTitle;

            input = angular.toJson(input);
            mainService.callPostRestAPI("modules/deleteModule", input).then(function(modules) {
                console.log(modules);
                $location.reload();

            });
        }
    }




});

app.controller('labintroController', function($scope, $rootScope, $location, $routeParams, mainService) {

    $scope.callLocation = function callLocation() {
        console.log('explorer/' + $scope.moduleDetails.difficultyLevel)
        $location.path('explorer/' + $scope.moduleDetails.difficultyLevel)
    }
    $scope.callHome = function callHome() {
        console.log('explorer/' + $scope.moduleDetails.difficultyLevel)
        $location.path('')
    }
    $scope.getLabInfo = function(moduleId) {
        var input = {};
        input['userId'] = $rootScope.userSessionVO.userId;
        input['moduleId'] = moduleId;
        input = angular.toJson(input);

        mainService.callPostRestAPI("lab/getLabStepWithStatus", input).then(function(data) {

            $scope.moduleSteps = data.labSteps;
            $scope.moduleDetails = data.labSteps[0].module;
            if (data.userModule != null) {
                $scope.labStatus = 'true';
            } else {
                $scope.labStatus = 'false';
            }

        });
    }

    var moduleId = $routeParams.moduleId;
    $scope.getLabInfo(moduleId);

    $scope.startLab = function(moduleId, userId, viewName) {
        var input = {};
        input['moduleId'] = moduleId;
        input['userId'] = userId;
        input = angular.toJson(input);

        mainService.callPostRestAPI("lab/startLab", input).then(function(data) {

            console.log("LAB is started.");
            $scope.labStatus = 'true';
            alert("You have enrolled for this module. Please go to My Courses to start working on module.");

        });
    }

    $scope.endLab = function(moduleId, userId, viewName) {
        var input = {};
        input['moduleId'] = moduleId;
        input['userId'] = userId;
        input = angular.toJson(input);
        console.log('is', input)
        mainService.callPostRestAPI("lab/endLab", input).then(function(modules) {

            $scope.userModules = modules;
            $location.path('mycourses');
            console.log("LAB is ended.");
            //$scope.labStatus = 'false';
            alert("deleted the module.");
        });
    }

});

app.controller('mycoursesController', function($scope, $location, $rootScope, mainService, sharedService) {

    $scope.loadMyCourses = function() {

        var input = {};
        input['userId'] = $rootScope.userSessionVO.userId;

        input = angular.toJson(input);

        mainService.callPostRestAPI("modules/getModulesForUser", input).then(function(modules) {
            console.log("mycoursesController: here");
            console.log(modules);
            $scope.userModules = modules;
            $location.path('mycourses');

        });

    }
    $scope.loadMyCourses();

    $scope.openCourse = function(viewName, userModule) {
        sharedService.setSharedData(userModule);
        $location.path('/lab_main/' + userModule.moduleId);


    }

    $scope.uneroll = function(viewName, userModule) {
        console.log(userModule)
            //	sharedService.setSharedData(userModule);
            //	$location.path('/lab_main/'+userModule.moduleId);


    }
    $scope.endLab = function(element, moduleId, userId, viewName) {
        var input = {};
        element.disabled = true;
        if (confirm("Do you really want to UnEnroll from this course? \nThis action is NOT REVERSIBLE")) {
            element.disabled = true;
            console.log(moduleId)
            input['moduleId'] = moduleId;
            input['userId'] = userId;
            input = angular.toJson(input);
            console.log('herer', input)
            mainService.callPostRestAPI("lab/endLab", input).then(function(modules) {
                console.log('lol', modules);
                //$scope.userModules=modules;
                $scope.loadMyCourses();
                $location.path('mycourses');
                console.log("LAB is ended.");
                //$scope.labStatus = 'false';
                alert("You are un-enrolled from the module");

            });
        } else {
            element.disabled = false;
        }
    }

});
// --------------------------- Course Edit Controller ------------------------ //
app.controller('courseEditorController', function($scope, $location, $http, $rootScope, mainService, $routeParams) {

    $scope._refreshData = function() {
        console.log("All Courses");
        mainService.callPostRestAPI("modules/getAllModules").then(function(courses) {
            $scope.disableCreate = true;
            $scope.courses = courses;
            console.log(courses);
            console.log("Course Editor Controller : Got Courses");

        });
    }
    $scope.disableCreate = true;

    $scope.uploadResult = "";

    $scope.myForm = {
        description: "",
        files: []
    }

    $scope.labs = {
        files: []
    }

    $scope.labGettingStarted = {
        files: []
    }

    $scope.module = {
        evaluation: {
            preSrvyNo: 1,
            techAsmtNo: 1,
            preSrvyQuestions: {
                "1": ""
            },
            techAsmtQuestions: {
                "1": {}
            }
        }
    }

    $scope.deleteQuestion = function(type, x) {
        console.log(type + " , No :  " + x);
        if (type == "techAsmtNo") {
            delete $scope.module.evaluation.techAsmtQuestions[x];

            for (i = parseInt(x) + 1; i <= $scope.module.evaluation.techAsmtNo; i++) {
                //console.log($scope.module.evaluation.techAsmtQuestions[i]);
                $scope.module.evaluation.techAsmtQuestions[i - 1] = $scope.module.evaluation.techAsmtQuestions[i];
                delete $scope.module.evaluation.techAsmtQuestions[i];
            }

            $scope.module.evaluation.techAsmtNo -= 1;
        } else {

            delete $scope.module.evaluation.preSrvyQuestions[x];

            for (i = parseInt(x) + 1; i <= $scope.module.evaluation.preSrvyNo; i++) {
                //console.log($scope.module.evaluation.preSrvyQuestions[i]);
                $scope.module.evaluation.preSrvyQuestions[i - 1] = $scope.module.evaluation.preSrvyQuestions[i];
                delete $scope.module.evaluation.preSrvyQuestions[i];
            }


            $scope.module.evaluation.preSrvyNo -= 1;
        }
    }

    $scope.insertQuestionBelow = function(type, x) {
        console.log("Inserting Below " + type + " , No :  " + x);
        var index = parseInt(x);
        if (type == "techAsmtNo") {
            // For tech Question
            $scope.module.evaluation.techAsmtNo += 1;
            var n = $scope.module.evaluation.techAsmtNo;
            $scope.module.evaluation.techAsmtQuestions[n] = {};
            for (i = n; i > index + 1; i--) {
                //console.log($scope.module.evaluation.preSrvyQuestions[i] + "<-" + $scope.module.evaluation.preSrvyQuestions[i - 1]);
                $scope.module.evaluation.techAsmtQuestions[i] = $scope.module.evaluation.techAsmtQuestions[i - 1];
            }
            $scope.module.evaluation.techAsmtQuestions[index + 1] = {}
        } else {
            // For pre Question
            $scope.module.evaluation.preSrvyNo += 1;
            var n = $scope.module.evaluation.preSrvyNo;
            $scope.module.evaluation.preSrvyQuestions[n] = "";
            for (i = n; i > index + 1; i--) {
                //console.log($scope.module.evaluation.preSrvyQuestions[i] + "<-" + $scope.module.evaluation.preSrvyQuestions[i - 1]);
                $scope.module.evaluation.preSrvyQuestions[i] = $scope.module.evaluation.preSrvyQuestions[i - 1];
            }
            $scope.module.evaluation.preSrvyQuestions[index + 1] = ""
        }
    }

    $scope.increasePreSrvyNo = function() {
        $scope.module.evaluation.preSrvyNo += 1;
        $scope.module.evaluation.preSrvyQuestions[$scope.module.evaluation.preSrvyNo] = "";
    }

    $scope.increaseTechAsmtNo = function() {
        $scope.module.evaluation.techAsmtNo += 1;
        $scope.module.evaluation.techAsmtQuestions[$scope.module.evaluation.techAsmtNo] = {};
    }

    $scope.getAllCourses = function() {

        $scope._refreshData();
    }
    $scope.resetLabs = function() {
        $scope.labs.files = [];
        $scope.modules = {
            labNames: []
        };
    };
    $scope.resetImage = function() {
        $scope.myForm.files = [];
    };
    $scope.range = function(min, max, step) {
        step = step || 1;
        var input = [];
        for (var i = min; i <= max; i += step) {
            input.push(i);
        }
        return input;
    };

    $scope.coursecreate = function() {
        $location.path("/coursecreate/");
    }

    $scope.courseeditor = function() {
        $location.path("/courseeditor/");
    }




    //console.log($scope.module);
    $scope.mastermodule = {}
    $scope.emptymodule = {}
    $scope.update = function(module) {
        if (module) {
            if (module == null) {
                console.log("Its empty");
            }
            $scope.mastermodule = angular.copy(module);
            $scope.mastermodule["moduleImage"] = ""
                //console.log($scope.mastermodule);
            $scope.disableCreate = false;
        }

    };
    $scope.reset = function() {
        console.log("reset");
        $scope.module = angular.copy($scope.emptymodule);
        $scope.mastermodule = angular.copy($scope.emptymodule);
        $scope.disableCreate = true;

    };
    $scope.createcourse = function(element) {
        $scope.uploadResult = "";
        element.disabled = true;
        $scope.disableCreate = true;
        console.log("Disable create " + $scope.disableCreate);
        if (confirm("Do you confirm the details of the module?")) {
            element.disabled = true;
            $scope.disableCreate = true;

            var length = Object.keys($scope.mastermodule).length;
            console.log("Creating course--- MasterModule Length = " + length);

            if (length == 10) {
                console.log("Creation Initiated");
                console.log($scope.mastermodule);
                mainService.callPostRestAPI("modules/createModule", $scope.mastermodule).then(function(result) {


                    console.log(result);
                    if (result > 0) {
                        console.log("Upload FIles");
                        $scope.myForm.description = $scope.mastermodule.moduleTitle + "@" + result;


                        _uploadFiles($scope.myForm);

                        setTimeout(function() {
                            _uploadFiles($scope.labs);
                        }, 2000);


                        alert("Course Creation is complete. You'll be redirected to Course Editor Main Page");

                        setTimeout(function() {
                            $location.path("/courseeditor/");
                        }, 1000);



                    } else {
                        alert("Error Occured. You'll be redirected to Course Editor Main Page");
                        $location.path("/courseeditor/");
                        console.log("Error Occured");
                    }
                    console.log("Course Editor Course Creation Complete");

                });


            }

        } else {
            console.log("Enable create");
            $scope.disableCreate = false;
            element.disabled = false;
        }

    };


    $scope.deleteModule = function(element, module) {

        element.disabled = true;

        if (confirm("Do you really want to delete this module : " + module.moduleTitle + "?\nNote: This action will delete the data which is dependent on this Module. \n1. Labsteps\n2. Evaluation data\n3. User Module\nWARNING: This action is not reversible.")) {
            var user_input = prompt("Please enter this:\n\n \" DELETE \" ")
            if (user_input == "DELETE") {

                console.log("Deleting " + module.moduleId)
                var input = {};
                input['moduleId'] = module.moduleId;
                input['moduleTitle'] = module.moduleTitle;

                input = angular.toJson(input);
                mainService.callPostRestAPI("modules/deleteModule", input).then(function(modules) {
                    //console.log(modules);
                    //console.log("Yes..");
                    //$location.reload();
                    //$scope._refreshData();
                    alert("Module Delete Successfull");
                    $location.path("/courseeditor/");

                });


            } else {

                alert("Invalid entry");
                element.disabled = false;
            }
        } else {
            element.disabled = false;
        }

    }

    function _uploadFiles(fileform) {
        //var root = "https://www.mizzoucyberrange.net/CyberRangeAPISandBox/";
        // var root = "http://35.226.197.88:8080/CyberRangeAPISandBox/";
        var root = "http://127.0.0.1:8080/CyberRangeAPISandBox/";
        var url = root + "upload/rest/uploadMultiFiles";

        var data = new FormData();
        console.log("Upload for Batch files started for : " + $scope.myForm.description);
        data.append("description", $scope.myForm.description);
        for (i = 0; i < fileform.files.length; i++) {
            data.append("files", fileform.files[i]);
        }
        var config = {
            transformRequest: angular.identity,
            transformResponse: angular.identity,
            headers: {
                'Content-Type': undefined
            }
        }


        $http.post(url, data, config).then(
            // Success
            function(response) {
                $scope.uploadResult += response.data;
            },
            // Error
            function(response) {
                $scope.uploadResult = response.data;
            });
    }


});

app.directive('username', function($q, $timeout) {
    return {
        require: 'ngModel',
        link: function(scope, elm, attrs, ctrl) {
            var usernames = ['Jim', 'John', 'Jill', 'Jackie'];

            ctrl.$asyncValidators.username = function(modelValue, viewValue) {

                if (ctrl.$isEmpty(modelValue)) {
                    // consider empty model valid
                    return $q.resolve();
                }
                var def = $q.defer();

                $timeout(function() {
                    // Mock a delayed response
                    if (modelValue.match(/^[A-Za-z0-9\s-]+$/m)) {
                        // The username is available
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

app.directive('bgcolor', function($q, $timeout) {
    return {
        require: 'ngModel',
        link: function(scope, elm, attrs, ctrl) {
            var usernames = ['Jim', 'John', 'Jill', 'Jackie'];

            ctrl.$asyncValidators.bgcolor = function(modelValue, viewValue) {

                if (ctrl.$isEmpty(modelValue)) {
                    // consider empty model valid
                    return $q.resolve();
                }
                var def = $q.defer();

                $timeout(function() {
                    // Mock a delayed response
                    if (modelValue.match(/^[0-9a-zA-Z]*$/) && modelValue.length == 6) {
                        // The bgcolor is available
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

app.directive('tags', function($q, $timeout) {
    return {
        require: 'ngModel',
        link: function(scope, elm, attrs, ctrl) {
            var usernames = ['Jim', 'John', 'Jill', 'Jackie'];

            ctrl.$asyncValidators.tags = function(modelValue, viewValue) {

                if (ctrl.$isEmpty(modelValue)) {
                    // consider empty model valid
                    return $q.resolve();
                }
                var def = $q.defer();

                $timeout(function() {
                    // Mock a delayed response
                    if (modelValue.match(/^[A-Za-z0-9\s,]+$/m)) {
                        // The tags is available
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


app.directive('fileModel', ['$parse', function($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function() {
                scope.$apply(function() {
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };

}]);

app.controller('UploadFileController', function($scope, $http) {

    $scope.uploadResult = "";

    $scope.form = {
        description: "",
        files: []
    }

    $scope.doUploadFile = function() {

        var root = "http://localhost:8080/CyberRangeAPISandBox/";
        // var root = "http://35.226.197.88:8080/CyberRangeAPISandBox/";
        // var root = "https://www.mizzoucyberrange.net/CyberRangeAPISandBox/";
        var url = root + "upload/rest/uploadMultiFiles";

        var data = new FormData();

        data.append("description", $scope.myForm.description);
        for (i = 0; i < $scope.myForm.files.length; i++) {
            data.append("files", $scope.myForm.files[i]);
        }
        console.log("Here data");
        console.log(data);

        var config = {
            transformRequest: angular.identity,
            transformResponse: angular.identity,
            headers: {
                'Content-Type': undefined
            }
        }


        $http.post(url, data, config).then(
            // Success
            function(response) {
                $scope.uploadResult = response.data;
            },
            // Error
            function(response) {
                $scope.uploadResult = response.data;
            });
    };

});
app.controller('GetFilesController', function($scope, $http) {

    $scope.allFiles = [];


    $scope.getAllFiles = function() {

        // REST URL:
        // var root = "https://www.mizzoucyberrange.net/CyberRangeAPISandBox/";
        // var root = "http://35.226.197.88:8080/CyberRangeAPISandBox/";
        var root = "http://127.0.0.1:8080/CyberRangeAPISandBox/";
        var url = root + "upload/rest/uploadMultiFiles";
        $http.get(url).then(
            // Success
            function(response) {
                alert("OK");
                $scope.allFiles = response.data;
            },
            // Error
            function(response) {
                alert("Error: " + response.data);
            }
        );
    };
});

//---------------------- //file upload example ------------------------------------------------