app.service('mainService', function($http, $q, $location) {
    //var serverURL = "http://localhost:8080/CyberRangeAPISandBox/";
    var serverURL = "https://www.mizzouclouddevops.net/CyberRangeAPISandBox/";
    return {
        callPostRestAPI: function(url, data) {

            var url = serverURL + url;

            return $http({
                method: "POST",
                url: url,
                data: data,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(function(response) {
                console.log(response);
                return response.data;
            }, function(response) {
                console.log(response);
                //				console.log("Error While Calling "+url+" API ");
                alert("Error While Calling API " + url);
                throw new Error("Error While Calling " + url + " API ");
            });
        }
    }

});

app.service('sharedService', function() {


    var _sharedData = {};

    return {
        getSharedData: function() {
            return _sharedData;
        },
        setSharedData: function(value) {
            _sharedData = value;
        }
    };

});

app.service('fileUpload', function($http) {
    this.uploadFileToUrl = function(file, uploadUrl) {
        var fd = new FormData();
        fd.append('file', file);

        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined }
        })

        .success(function() {})

        .error(function() {});
    }
});