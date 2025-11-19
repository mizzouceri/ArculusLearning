app.config(function($routeProvider) {


    $routeProvider.when("/", {
            templateUrl: "view/common/dashboard.html"
        })
        .when("/home_page", {
            templateUrl: "view/static_site/home_page.html"
        })
        .when("/cloud_devops", {
            templateUrl: "view/static_site/cloud_devops.html"
        })
        .when("/education_catalog", {
            templateUrl: "view/static_site/education_catalog.html"
        })
        .when("/team", {
            templateUrl: "view/static_site/team.html"
        })
        .when("/progress", {
            templateUrl: "view/common/progress.html"
        })
        .when("/cyber_security_modules", {
            templateUrl: "view/common/cyber_security_modules.html"
        })
        .when("/cyber_security_lab_1", {
            templateUrl: "view/modules/cyber_security/lab_1/lab_1.html"
        })
        .when("/cyber_security_lab_2", {
            templateUrl: "view/modules/cyber_security/lab_2/lab_2.html"
        })
        .when("/mycourses", {
            templateUrl: "view/common/mycourses.html"
        })
        .when("/lab_main/:moduleId", {
            templateUrl: "view/modules/cyber_security/lab/lab_main.html"
        })
        .when("/rank/:moduleId", {
            templateUrl: "view/common/ranks.html"
        })
        .when("/studentProgress", {
            templateUrl: "view/common/studentProgress.html"
        })
        .when("/requests", {
            templateUrl: "view/common/requests.html"
        })
        .when("/overview", {
            templateUrl: "view/common/overview.html"
        })
        .when("/courseeditor", {
            templateUrl: "view/common/courseeditor.html"
        })
        .when("/coursecreate", {
            templateUrl: "view/common/coursecreate.html"
        })
        .when("/lab_intro/:moduleId", {
            templateUrl: "view/common/lab_intro.html"
        })
        .when("/explorer/:difficultyLevel", {
            templateUrl: "view/common/explorer.html"
        })





});