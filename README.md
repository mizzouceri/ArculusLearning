# This is the MizzouDevOps web portal repo
-----------------------------------------------



To run the portal in the Docker on a Cloud VM, do the following: 
-----------------------------------------------------------
#### Update the IP address of the server in the following files (change from localhost to the IP of the server): 

1.(1 place) /MizzouCloudDevOps/MizzouCloudDevOps/src/main/java/com/cyberrange/web/utility/RestClient.java

2.(3 places) /MizzouCloudDevOps/MizzouCloudDevOps/src/main/resources/static/js/angular_controllers/moduleController.js

3.(1 place) /MizzouCloudDevOps/MizzouCloudDevOps/src/main/resources/static/js/angularService/mainService.js

#### Change database source in the following file (replace the IP with the new database server IP):

/MizzouCloudDevOps/CyberRangeAPISandBox/src/main/resources/application.properties

#### Update the github authentication client ID/secret (replace both values with the appropriate ones):

1. /MizzouCloudDevOps/MizzouCloudDevOps/src/main/resources/github.properties

2. /MizzouCloudDevOps/MizzouCloudDevOps/src/main/resources/static/js/angular_controllers/mainController.js

#### Change three directory variables to use Docker ones

1. In googleCloudProp.properties file, change serviceAPIKeypath variable: use absolute Path for Google Cloud mizzoucyberrange.json 

2. In upload.properties file, change fileUploadDirectory variable : use absolute Path for uploading images for modules (../img/uploads)

3. In upload.properties file, change htmlfileUploadDirectory variable : Set path for uploading html files for modules (../../view/modules)%



To run the portal on the local, modify all these files above to local settings. 
-----------------------------------------------------------





