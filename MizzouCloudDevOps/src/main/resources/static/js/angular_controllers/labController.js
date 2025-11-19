app.controller('labController', function ($scope, $location, $routeParams, mainService, $rootScope, $sce) {
    $scope.launchInfra = null;
    $scope.statusModules = {};
    $scope.locations = [];
    $scope.startEvaluationFlag = [];
    $scope.resourceJSON = null;
    $scope.deleteStatus = "SHOW";
    $scope.hasSurvey = true;

    $scope.checkHasSurvey = function (moduleId, evaluationType) {
        console.log("Checking if module has a survey");
        console.log($scope.statusModules);
        var input = {};
        input['moduleCd'] = moduleId;
        input['evaluationCd'] = evaluationType;
        input = angular.toJson(input);

        mainService.callPostRestAPI("evaluation/getQuestionForEvaluation", input).then(function (data) {
            if (data.length > 0) {
                $scope.hasSurvey = true;
            } else {
                $scope.hasSurvey = false;
                startEvaluationFlag['PRE_SRVY'] = false;
                $scope.launchInfra = 'LAUNCH';
                $scope.selectTabOnLoad();
            }
        });
    }

    $scope.loadEvalutation = function (moduleId, evaluationType) {

        var input = {};
        input['moduleCd'] = moduleId;
        input['evaluationCd'] = evaluationType;
        input = angular.toJson(input);

        mainService.callPostRestAPI("evaluation/getQuestionForEvaluation", input).then(function (data) {
            console.log("Load Eval: mainService :getQuestionForEvaluation ")
            console.log(data);
            if (data.length > 0) {
                data[0]['class_active'] = 'active';

                var data_;
                for (var x = 0; x < data.length; x++) {
                    data_ = data[x];
                    var feedbackanswers = data_['evaluationAnswer'];
                    data_['evaluationQuesId'] = data_['id'];
                    // if (evaluationType === "FDBK" || evaluationType === "PRE_SRVY") {
                    feedbackanswersJSON = JSON.parse(feedbackanswers);
                    data_['evaluationAnswers'] = feedbackanswersJSON;
                    // }
                    data_['evaluationAnswer'] = '';
                }

                $scope.questions = data;
                $scope.startEvaluationFlag[evaluationType] = true;
                $("#test-nl-1").addClass("active dstepper-block");
            } else {
                console.log("Disabling survey because there are no questions in database");
                //alert("Sorry, there are no surveys available for this course. Please continue with the labs.");
                $("#preSurvayModal").modal("hide");
                $scope.startEvaluationFlag[evaluationType] = false;
                $scope.launchInfra = 'LAUNCH';
                $scope.hasSurvey = false;
                $scope.selectTab('nav-launch');
                $scope.selectTabOnLoad();
            }
        });
    }

    /*$scope.loadFeedback = function(moduleId, evaluationType) {

        var input = {};
        input['moduleCd'] = moduleId;
        input['evaluationCd'] = evaluationType;
        input = angular.toJson(input);

        mainService.callPostRestAPI("evaluation/getQuestionForEvaluation", input).then(function(data) {
            console.log("Load Eval: mainService :getQuestionForEvaluation ")
            console.log(data);
            if (data.length > 0) {
                data[0]['class_active'] = 'active';
                var data_;
                for (var x = 0; x < data.length; x++) {
                    data_ = data[x];
                    var feedbackanswers = data_['evaluationAnswer'];
                    data_['evaluationQuesId'] = data_['id'];
                    // if (evaluationType === "FDBK" || evaluationType === "PRE_SRVY") {
                    feedbackanswersJSON = JSON.parse(feedbackanswers);
                    data_['evaluationAnswers'] = feedbackanswersJSON;
                    // }
                    data_['evaluationAnswer'] = '';
                }
                $scope.questions = data;
                $scope.startEvaluationFlag[evaluationType] = true;

                $("#test-nl-1").addClass("active dstepper-block");
            } else {
                console.log("Disabling survey because there are no questions in database");
                //alert("Sorry, there are no surveys available for this course. Please continue with the labs.");
                $("#postAssessment").modal("hide");
                $scope.startEvaluationFlag[evaluationType] = false;
                //$scope.launchInfra = 'LAUNCH';
                //$scope.hasSurvey = false;
                //$scope.selectTab('nav-launch');
                //$scope.selectTabOnLoad();
            }
        });

    } */

    $scope.loadEvalutationFeedback = function (moduleId, evaluationType) {
        delete $scope.startEvaluationFlag['TECH_ASMT_SUCCESS'];
        $scope.loadEvalutation(moduleId, evaluationType);
    }

    $scope.checkForAssessment = function (moduleId, userId) {
        var input = {};
        input['moduleId'] = moduleId;
        input['userId'] = userId;
        input['evaluationCd'] = 'TECH_ASMT';
        inputStr = angular.toJson(input);

        mainService.callPostRestAPI("evaluation/getStatusofEvaluation", inputStr).then(function (labStatusAssessment) {
            if (labStatusAssessment == '') {
                $scope.loadEvalutation(moduleId, 'TECH_ASMT');
                $scope.startEvaluationFlag['TECH_ASMT'] = true;
            } else {
                var percentage = ((labStatusAssessment.points / labStatusAssessment.totalPoints) * 100).toFixed(2);
                $scope.percentage = percentage;
                //alert(percentage);
                input['evaluationCd'] = 'FDBK';
                inputStr = angular.toJson(input);

                mainService.callPostRestAPI("evaluation/getStatusofEvaluation", inputStr).then(function (labStatusFDBK) {
                    if (labStatusFDBK == '') {
                        $scope.startEvaluationFlag['TECH_ASMT_SUCCESS'] = true;
                    } else {
                        $scope.startEvaluationFlag['ALL_EVAL_DONE'] = true;
                    }
                });

            }
        });
    }

    $scope.callProgress = function callProgress() {
        console.log('explorer/' + $scope.statusModules.module.difficultyLevel)
        // $location.path('explorer/'+$scope.statusModules.module.difficultyLevel)
        $location.path('progress/')
    }
    $scope.callLocation = function callLocation() {
        console.log('explorer/' + $scope.statusModules.module.difficultyLevel)
        // $location.path('explorer/'+$scope.statusModules.module.difficultyLevel)
        $location.path('explorer/BEGINNER')
    }
    $scope.callHome = function callHome() {
        console.log('explorer/mycourses' + $scope.statusModules.module.difficultyLevel)
        $location.path('mycourses')
    }
    $scope.copyInputMessage = function copyInputMessage(inputElement) {
        navigator.clipboard.writeText(inputElement);
        alert('IP Address is copied to clipboard')
    }
    $scope.checkStatusofLab = function (moduleId, userId) {
        var input = {};
        input['moduleId'] = moduleId;
        input['userId'] = userId;
        input['evaluationCd'] = 'PRE_SRVY';
        input = angular.toJson(input);

        mainService.callPostRestAPI("lab/getLabStepWithStatus", input).then(function (labStepsStatus) {

            console.log("checking status..");
            console.log(labStepsStatus);
            console.log("LAB was started earlier, call API to check subjective pre evaluation");
            $scope.labSteps = labStepsStatus.labSteps;
            console.log(labStepsStatus.labSteps[0].viewName);
            $scope.lab_url = $sce.trustAsResourceUrl(labStepsStatus.labSteps[0].viewName);
            $scope.statusModules = labStepsStatus.userModule;

            $scope.gettingStarted = "view/modules/cyber_security/lab_" + $scope.statusModules.moduleId + "/lab" + $scope.statusModules.moduleId + "_getting_start.html";
            if ($scope.statusModules.moduleId > 10) {
                console.log($scope.statusModules.module.moduleTitle);
                var module_path_name = angular.lowercase($scope.statusModules.module.moduleTitle).replace(' ', '_')
                $scope.gettingStarted = "view/modules/" + module_path_name + "/labs/" + module_path_name + "_lab_getting_start.html";
            }

            var stepsDone = parseInt($scope.statusModules.stepsCompleted);

            upadteLabStatusonHTML(stepsDone + 1);

            if ($scope.statusModules.labCompleted == 'Y') {
                $scope.selectStep(1);

            } else {
                if ($scope.statusModules.stepsCompleted == '0') {
                    mainService.callPostRestAPI("evaluation/getStatusofEvaluation", input).then(function (data) {

                        if (data == '') {

                            $scope.startEvaluationFlag['PRE_SRVY'] = true;

                        } else {

                            if ($scope.statusModules.sliceCreated && $scope.statusModules.sliceCreated != '' &&
                                $scope.statusModules.resourcesReserved && $scope.statusModules.resourcesReserved != '') {
                                resourcesReservedJSON = JSON.parse($scope.statusModules.resourcesReserved);

                                var showLaunchButton = false;
                                for (i in resourcesReservedJSON) {
                                    resourcesReserved = resourcesReservedJSON[i];
                                    if (resourcesReserved['status'] == false) {
                                        showLaunchButton = true;
                                        break;
                                    }
                                }

                                if (showLaunchButton) {
                                    $scope.launchInfra = 'LAUNCH';
                                } else {
                                    $scope.launchInfraNext = true;
                                }


                            } else {
                                $scope.launchInfra = 'LAUNCH';
                            }
                            $scope.selectStep(1);
                        }
                    });
                } else {
                    var final_step = parseInt($scope.statusModules.stepsCompleted) + 1;
                    $scope.selectStep(final_step);

                }
            }

        });
    }


    function upadteLabStatusonHTML(stepsDone) {
        var allStepDone = true;
        for (index in $scope.labSteps) {

            labStep = $scope.labSteps[index];
            currentLab = parseInt(labStep['stepId']);

            if (currentLab < stepsDone) {
                labStep['stepStatus'] = "done";
            } else {
                allStepDone = false;
                var stepCompleted = parseInt($scope.statusModules.stepsCompleted) + 1;
                if (currentLab == stepCompleted) {
                    labStep['stepStatus'] = "edit active";

                } else {
                    labStep['stepStatus'] = "notdone";
                }
            }

        }

        if (allStepDone) {
            labStep = $scope.labSteps[0];
            labStep['stepStatus'] = "done active";
        }
    }




    $scope.luanchInfrastructureGoogle = function (moduleId, userId) {
        $scope.launchInfra = 'WORKING';
        $scope.resourceJSON = 'CREATING';
        var input = {};
        input['geniUsername'] = $rootScope.userSessionVO.userName;
        input['moduleId'] = moduleId;
        input['userId'] = userId;
        inputJSON = angular.toJson(input);

        mainService.callPostRestAPI("goolgeApi/createInstanceForLab1", inputJSON).then(function (data) {

            console.log(data.resourcesReserved);
            $scope.statusModules['sliceCreated'] = data.sliceCreated;
            $scope.statusModules['resourcesReserved'] = data.resourcesReserved;
            $scope.getResourcesReserved(data.resourcesReserved);

            //alert(data.resourcesReserved.length);
            $scope.launchInfra = 'SUCCESS';
            $scope.launchInfraNext = true;
        });
    }
    /*----------------------------------------*/
    $scope.copyToClipboard = function (name) {
        var element = document.getElementById(name);
        var textToCopy = element.innerText; // Use innerText instead of innerHTML
        console.log(textToCopy);
        
        var copyElement = document.createElement("textarea");
        copyElement.style.position = 'fixed';
        copyElement.style.opacity = '0';
        copyElement.textContent = textToCopy;
        
        var body = document.getElementsByTagName('body')[0];
        body.appendChild(copyElement);
        
        copyElement.select();
        document.execCommand('copy');
        
        body.removeChild(copyElement);
    }    


    $scope.convertStringToNumber = function (str) {
        if (str.trim().length == 0) {
            return NaN;
        }
        return Number(str);
    }
    /*----------------------------------------*/

    $scope.luanchInfrastructure = function (geniUserName, moduleId, userId) {

        $scope.launchInfra = 'WORKING';
        var input = {};

        var sliceName = $scope.statusModules.sliceCreated;
        var geniSliceStatus = $scope.statusModules.geniSliceStatus;

        input['moduleId'] = moduleId;
        input['userId'] = userId;
        input['geniUsername'] = geniUserName;
        input['geniSliceStatus'] = geniSliceStatus;
        input['resourcesReserved'] = $scope.statusModules.resourcesReserved;
        input['sliceCreated'] = sliceName;
        inputJSON = angular.toJson(input);

        if (!geniSliceStatus || geniSliceStatus != 'SUCCESS') {

            mainService.callPostRestAPI("slice/createSlice", inputJSON).then(function (data) {
                sliceName = data.sliceCreated;
                geniSliceStatus = data['geniSliceStatus'];
                $scope.statusModules['sliceCreated'] = sliceName;
                $scope.statusModules['geniSliceStatus'] = geniSliceStatus;
                if (geniSliceStatus == 'SUCCESS') {
                    reserveResources(input, sliceName, moduleId);
                } else {
                    $scope.launchInfra = 'LAUNCH';
                    if (geniSliceStatus == 'NO_IN_PROJECT') {
                        alert("You have not joined 'VIMAN-lab' project. Please join \"VIMAN-lab\" project on GENI portal and wait till approval to try again.");

                    } else {
                        alert("Please enter valid GENI user name.");
                    }
                }
            });

        } else {
            reserveResources(input, sliceName, moduleId);
        }

    }

    function reserveResources(input, sliceName, moduleId) {

        input['sliceCreated'] = sliceName;
        //alert("reserveResources");
        var apiName = 'slice/reserveResources';
        if (moduleId == 1) {
            apiName = 'slice/reserveResourcesLab1';
        }

        inputJSON = angular.toJson(input);


        mainService.callPostRestAPI(apiName, inputJSON).then(function (resourcesReservedJSON) {
            console.log("DONE");

            $scope.statusModules.resourcesReserved = angular.toJson(resourcesReservedJSON);
            var showLaunchButton = false;
            if (resourcesReservedJSON && resourcesReservedJSON != '') {
                for (i in resourcesReservedJSON) {
                    resourcesReserved = resourcesReservedJSON[i];
                    if (resourcesReserved['status'] == false) {
                        showLaunchButton = true;
                        break;
                    }
                }

                if (showLaunchButton) {
                    $scope.launchInfra = 'LAUNCH';
                    alert("Error-101 while reserving resources on GENI Portal. Please try again.");

                } else {
                    $scope.launchInfra = 'SUCCESS';
                    $scope.launchInfraNext = true;
                }

            } else {
                $scope.launchInfra = 'LAUNCH';
                alert("Error-201 while reserving resources on GENI Portal through application. Please try again.");
            }


        });
    }

    $scope.checkResourceStatus = function () {

        var input = {};
        input = $scope.statusModules;
        inputJSON = angular.toJson(input);

        $scope.checkResourcesStatusBtn = true;

        var moduleId = $scope.statusModules.moduleId;
        var checkStatusApiName = 'slice/checkStatusSlice';
        var deleteResourcesApiName = 'slice/deleteResoucesFromSlice';
        if (moduleId == 1) {
            checkStatusApiName = 'slice/checkStatusSliceForLab1';
            deleteResourcesApiName = 'slice/deleteResoucesFromSliceForLab1';
        }

        mainService.callPostRestAPI(checkStatusApiName, inputJSON).then(function (data) {
            resourceStatus = data['resourceStatus'];
            if (resourceStatus == 'UNKNOWN') {
                $scope.checkResourcesStatusBtn = false;
                alert("Resources of GENI slice are not ready yet. Please try after sometime. Thank You.");
            } else if (resourceStatus == 'FAIL') {
                //alert("Resources of GENI slice are failed. Please contact Mizzou Cyber Range Team. Thank You.");
                $scope.checkResourcesStatusBtn = false;

                mainService.callPostRestAPI(deleteResourcesApiName, inputJSON).then(function (userModule) {
                    deleteStatus = userModule.deleteStatus;
                    $scope.statusModules['resourcesReserved'] = userModule.resourcesReserved
                    $scope.launchInfra = 'LAUNCH';
                    $scope.launchInfraNext = false;
                    alert("Resources of GENI slice are failed. We have deleted Resources. Please try again to reserve Infrastructure.");
                });


            } else {
                $scope.checkResourcesStatusBtn = false;

                $scope.updateStatus($scope.statusModules.moduleId, $scope.statusModules.userId, $scope.statusModules.stepsCompleted);
            }
        });

    }


    $scope.updateStatus = function (moduleId, userId, stepsCompleted) {
        var input = {};
        stepsDone = parseInt(stepsCompleted) + 1;
        input['moduleId'] = moduleId;
        input['userId'] = userId;
        input['stepsCompleted'] = stepsDone;
        input = angular.toJson(input);

        mainService.callPostRestAPI("lab/updateStatusofLab", input).then(function (data) {

            console.log("LAB is started, call API to for Evaluation");
            $scope.statusModules.stepsCompleted = stepsDone;

            var final_step = stepsDone + 1;
            upadteLabStatusonHTML(final_step);
            $scope.selectStep(final_step);

        });
    }

    $scope.updateStatusAndCompleteLab = function (moduleId, userId, stepsCompleted) {
        var input = {};
        stepsDone = parseInt(stepsCompleted) + 1;
        input['moduleId'] = moduleId;
        input['userId'] = userId;
        input['stepsCompleted'] = stepsDone;
        input = angular.toJson(input);

        mainService.callPostRestAPI("lab/updateStatusAndCompleteLab", input).then(function (data) {

            console.log("LAB is started, call API to for Evaluation");
            $scope.statusModules.stepsCompleted = stepsDone;

            var final_step = stepsDone + 1;
            upadteLabStatusonHTML(final_step);
            $scope.statusModules.labCompleted = 'Y';

        });
    }


    $scope.nextQuestionPreSurvay = function (moduleId, userId, answer, index, size, evaluation) {
        if (answer == null || answer == "" || answer == undefined) {
            alert("Please enter answer to move forward to next question");
        } else {

            if (evaluation == 'PRE_SRVY' || evaluation == 'FDBK_DONE') {
                $(".bs-stepper-pane").removeClass("active dstepper-block");
                $("#test-nl-" + (index + 1)).addClass("active dstepper-block");


            } else {
                $(".bs-stepper-pane").removeClass("active dstepper-block");
                $("#" + evaluation + "-" + (index + 1)).addClass("active dstepper-block");

                $(".bs-stepper-circle.header-circle").removeClass("highlight ");
                $("#step_header_" + (index + 1)).addClass("highlight");
            }

            if ((index + 1) === size) {
                if (evaluation == 'PRE_SRVY') {
                    $("#preSurvayModal").modal("hide");
                } else if (evaluation == 'FDBK_DONE') {
                    $("#postAssessment").modal("hide");
                }
                $scope.submitEvaluation(moduleId, userId, evaluation, $scope.questions);

            }
        }
    }

    /*$scope.nextQuestionFeedback = function(moduleId, userId, answer, index, size, evaluation) {
        if (answer == null || answer == "" || answer == undefined) {
            alert("Please enter answer to move forward to next question");
        } else {
            if (evaluation == 'FDBK_DONE') {
                $(".bs-stepper-pane").removeClass("active dstepper-block");
                $("#test-nl-" + (index + 1)).addClass("active dstepper-block");
            } else {
                $(".bs-stepper-pane").removeClass("active dstepper-block");
                $("#" + evaluation + "-" + (index + 1)).addClass("active dstepper-block");
                $(".bs-stepper-circle.header-circle").removeClass("highlight ");
                $("#step_header_" + (index + 1)).addClass("highlight");
            }
            if ((index + 1) === size) {
                if (evaluation == 'FDBK_DONE') {
                    $("#postAssessment").modal("hide");
                }
                $scope.submitEvaluation(moduleId, userId, evaluation, $scope.questions);
            }
        }
    }*/

    /*$scope.nextQuestionFeedback = function(moduleId, userId, answer, index, size, evaluation) {
        if (answer == null || answer == "" || answer == undefined) {
            alert("Please enter answers to all questions");
        } 			
        $("#postAssessment").modal("hide");
        $scope.submitEvaluation(moduleId, userId, evaluation, $scope.questions);
    }*/

    $scope.viewQuestion = function (index, type) {

        $(".bs-stepper-pane").removeClass("active dstepper-block");
        $("#" + type + "-" + index).addClass("active dstepper-block");

        $(".bs-stepper-circle.header-circle").removeClass("highlight ");
        $("#step_header_" + index).addClass("highlight");
    }

    $scope.submitEvaluation = function (moduleId, userId, evaluation, ans) {

        var input = {};
        input['moduleCd'] = moduleId;
        input['userId'] = userId;
        input['evaluationCd'] = evaluation;

        //resolving wrong answer value ambiguity.
        for (let i = 0; i < ans.length; i++) {
            if (ans[i].evaluationAnswer == "0.0" || ans[i].evaluationAnswer == "0.00") {
                ans[i].evaluationAnswer = "0";
            }
        }

        input['answers'] = ans;

        console.log(ans);
        input = angular.toJson(input);
        //console.log(input);
        mainService.callPostRestAPI("evaluation/submitEvaluation", input).then(function (data) {

            delete $scope.startEvaluationFlag[evaluation];
            console.log(data);
            if (evaluation == 'PRE_SRVY') {

                $scope.launchInfra = 'LAUNCH';
                $scope.selectTabOnLoad();

            } else if (evaluation == 'TECH_ASMT') {

                $scope.percentage = data.persentage;

                $scope.startEvaluationFlag['TECH_ASMT_SUCCESS'] = true;
            } else {

                $scope.startEvaluationFlag['FDBK_DONE'] = true;
            }
        });
    }


    $scope.selectTab = function (tab_id) {
        $(".lab-tab .nav-link").removeClass("active show");
        $(".lab-tab #" + tab_id + "-tab").addClass("active show");
        $(".lab-tab .tab-pane").removeClass("active show");
        $(".lab-tab #" + tab_id).addClass("active show");
    }

    $scope.selectStep = function (tab_id) {
        var tab_id_update = 'step-' + tab_id;

        $(".tab-content-steps .tab-pane-step").removeClass("active show");
        $(".tab-content-steps .tab-pane-step").css("display", 'none');
        $(".tab-content-steps #" + tab_id_update + "-tab").css("display", "");
        $(".tab-content-steps #" + tab_id_update + "-tab").addClass("active show");

        $(".lab_steps .md-step").removeClass("active");
        $(".lab_steps #step-" + tab_id + "-title").addClass("active");
    }


    $scope.selectTabOnLoad = function () {
        var final_step = parseInt($scope.statusModules.stepsCompleted) + 1;
        if ($scope.statusModules.labCompleted == 'Y') {
            $scope.selectStep(1);

        } else {
            $scope.selectStep(final_step);
        }
    }

    $scope.checkStatusofLab($routeParams.moduleId, $rootScope.userSessionVO.userId);

    $scope.getResourcesReserved = function (resources) {
        var resourceJSON;
        if (resources && resources != '') {
            resourceJSON = JSON.parse(resources);
        }

        $scope.resourceJSON = resourceJSON;
    }
    $scope.deleteCompletedResourcesForLab = function (element, moduleId, userId) {
        element.disabled = true;
        if (confirm("Are you sure, you want to delete all VM Instances?\nNote: This action is NOT REVERSIBLE")) {
            element.disabled = true;
            console.log("Deleting Virutal Instances");
            var input = {};
            input['moduleId'] = moduleId;
            input['userId'] = userId;

            input = angular.toJson(input);
            console.log(input);
            mainService.callPostRestAPI("goolgeApi/deleteCompletedResourcesForLab", input).then(function (data) {
                //$scope.launchInfra = 'LAUNCH';
                $scope.statusModules['sliceCreated'] = null;
                $scope.statusModules['resourcesReserved'] = null;
                console.log("Delete Sucess but..");
                console.log($scope.statusModules.geniSliceStatus);
                $scope.statusModules['geniSliceStatus'] = null;

                $scope.getResourcesReserved(null);

                //alert(data.resourcesReserved.length);
                $scope.launchInfra = 'LAUNCH';
                $scope.resourceJSON = null;
                //$scope.launchInfraNext = false;
                alert("All VM Instances are deleted.");
            });

        } else {
            element.disabled = false;
        }
    }
});

app.controller('labViewController', function ($scope, $location, $routeParams, mainService, $rootScope, $sce) {



    $scope.checkStatusofLab = function (moduleId, role) {

        console.log("In labbire Controller");
        console.log("ModuleID =" + moduleId);
        if (angular.uppercase(role) == "INSTRUCTOR") {
            var input = {};
            input['moduleId'] = moduleId;
            // input['userId'] = userId;
            input['evaluationCd'] = 'PRE_SRVY';
            input = angular.toJson(input);

            mainService.callPostRestAPI("lab/getLabDetails", input).then(function (labStepsStatus) {

                console.log("checking labDetails");
                console.log(labStepsStatus);
                console.log("LAB was started earlier, call API to check subjective pre evaluation");
                $scope.labSteps = labStepsStatus.labSteps;
                //console.log(labStepsStatus.labSteps[0].viewName);
                ///$scope.lab_url = $sce.trustAsResourceUrl(labStepsStatus.labSteps[0].viewName);
                $scope.statusModules = labStepsStatus.module;
                $scope.evaluationQuestions = labStepsStatus.evaluationQuestions;


                $scope.gettingStarted = "view/modules/cyber_security/lab_" + $scope.statusModules.moduleId + "/lab" + $scope.statusModules.moduleId + "_getting_start.html";
                if ($scope.statusModules.moduleId > 10) {
                    console.log($scope.statusModules.moduleTitle);
                    var module_path_name = angular.lowercase($scope.statusModules.moduleTitle).replace(' ', '_')

                    $scope.gettingStarted = "view/modules/" + module_path_name + "/labs/" + module_path_name + "_lab_getting_start.html";
                }

                var stepsDone = parseInt($scope.statusModules.stepsCompleted);
                $scope.selectStep(1);
                //upadteLabStatusonHTML(stepsDone + 1);

                /*if ($scope.statusModules.labCompleted == 'Y') {
                    $scope.selectStep(1);

                } else {
                    if ($scope.statusModules.stepsCompleted == '0') {
                        mainService.callPostRestAPI("evaluation/getStatusofEvaluation", input).then(function(data) {

                            if (data == '') {

                                $scope.startEvaluationFlag['PRE_SRVY'] = true;

                            } else {

                                if ($scope.statusModules.sliceCreated && $scope.statusModules.sliceCreated != '' &&
                                    $scope.statusModules.resourcesReserved && $scope.statusModules.resourcesReserved != '') {
                                    resourcesReservedJSON = JSON.parse($scope.statusModules.resourcesReserved);

                                    var showLaunchButton = false;
                                    for (i in resourcesReservedJSON) {
                                        resourcesReserved = resourcesReservedJSON[i];
                                        if (resourcesReserved['status'] == false) {
                                            showLaunchButton = true;
                                            break;
                                        }
                                    }

                                    if (showLaunchButton) {
                                        $scope.launchInfra = 'LAUNCH';
                                    } else {
                                        $scope.launchInfraNext = true;
                                    }


                                } else {
                                    $scope.launchInfra = 'LAUNCH';
                                }
                                $scope.selectStep(1);
                            }
                        });
                    } else {
                        var final_step = parseInt($scope.statusModules.stepsCompleted) + 1;
                        $scope.selectStep(final_step);

                    }
                }*/

            });
        } else {
            console.log("Not Instructor");
            $location.path("/");
        }
    }
    $scope.selectTab = function (tab_id) {
        if (tab_id == "nav-launch") {
            //console.log("Yes select step 1");
            $scope.selectStep(1);
        }
        $(".lab-tab .nav-link").removeClass("active show");
        $(".lab-tab #" + tab_id + "-tab").addClass("active show");
        $(".lab-tab .tab-pane").removeClass("active show");
        $(".lab-tab #" + tab_id).addClass("active show");

    }

    $scope.selectStep = function (tab_id) {
        //console.log("Tab Id =" + tab_id);
        var tab_id_update = 'step-' + tab_id;

        $(".tab-content-steps .tab-pane-step").removeClass("active show");
        $(".tab-content-steps .tab-pane-step").css("display", 'none');
        $(".tab-content-steps #" + tab_id_update + "-tab").css("display", "");
        $(".tab-content-steps #" + tab_id_update + "-tab").addClass("active show");

        $(".lab_steps .md-step").removeClass("active");
        $(".lab_steps #step-" + tab_id + "-title").addClass("active");
    }

    $scope.callLocation = function callLocation() {
        console.log('explorer/' + $scope.statusModules.module.difficultyLevel)
        // $location.path('explorer/'+$scope.statusModules.module.difficultyLevel)
        $location.path('/courseeditor')
    }
    $scope.callHome = function callHome() {
        //console.log('explorer/mycourses' + $scope.statusModules.module.difficultyLevel)
        $location.path('/')
    }

    $scope.mySplit = function (string, nb) {
        var array = string.split(',');

        var ans = "";
        for (i in array) {
            ans = ans + array[i];
            if (i < array.length - 1) {
                ans = ans + " ,";
            }
        }
        return ans;
    }

    $scope.checkStatusofLab($routeParams.moduleId, $rootScope.userSessionVO.role);
});
