package com.cyberrange.api.google;

import java.io.FileInputStream;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.http.HttpTransport;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.Operation;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;

@Component
@PropertySource("classpath:googleCloudProp.properties")
public class GoogleLib {
	

	@Value("${projectId}")
    private String projectId;
	
	
	@Value("${serviceAPIKeypath}")
	private String jsonPath;
	
	GoogleCredentials credentials;
	
	HttpTransport httpTransport;

	
	public GoogleCredentials authExplicit() throws IOException {
        
		if(null == credentials) {
		  credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath))
	          .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
		  credentials.refreshIfExpired();
		}else {
			 credentials.refreshIfExpired();
		}
		
		return credentials;
		 
	  }
	
	public String googleCloudRestClient(String input, String url, HttpMethod methodType)throws Exception {


		String URL = url;

		String response = null;
		ResponseEntity<String> result = null;
		RestTemplate restTemplate = new RestTemplate();
		
		GoogleCredentials credentials = authExplicit();

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		requestHeaders.add("Authorization", "Bearer " + credentials.getAccessToken().getTokenValue() );

		HttpEntity<String> entity = null;

		if (methodType == HttpMethod.POST) {
			
			entity = new HttpEntity<String>(input, requestHeaders);
			result = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);

		} else if (methodType == HttpMethod.GET) {
			entity = new HttpEntity<String>(requestHeaders);
			result = restTemplate.exchange(URL, HttpMethod.GET, entity, String.class);

		} else if(methodType == HttpMethod.DELETE){
			entity = new HttpEntity<String>(requestHeaders);
			result = restTemplate.exchange(URL, HttpMethod.DELETE, entity, String.class);
		}

		if (result.getStatusCode().equals(HttpStatus.OK)) {
			response = result.getBody();
		}

		return response;

		
	}
	
	public String createInstance(String zoneName, String instanceName, String machineImage) throws Exception {
		instanceName= instanceName.toLowerCase();

		String url = "https://compute.googleapis.com/compute/beta/projects/"+projectId+"/zones/"+zoneName+"/instances";
		
		String input = "{\"kind\":\"compute#instance\",\"name\":\""+instanceName+"\",\"zone\":\"projects/mizzoucyberrange/zones/"+zoneName+"\","
				+ "\"minCpuPlatform\":\"Automatic\",\"machineType\":\"projects/mizzoucyberrange/zones/"+zoneName+"/machineTypes/n1-standard-1\","
				+ "\"displayDevice\":{\"enableDisplay\":false},\"metadata\":{\"kind\":\"compute#metadata\",\"items\":[]},"
				+ "\"tags\":{\"items\":[\"http-server\",\"https-server\"]},\"disks\":[],\"canIpForward\":false,"
				+ "\"networkInterfaces\":[{\"kind\":\"compute#networkInterface\",\"subnetwork\":\"projects/mizzoucyberrange/regions/us-central1/subnetworks/default\","
				+ "\"accessConfigs\":[{\"kind\":\"compute#accessConfig\",\"name\":\"External NAT\",\"type\":\"ONE_TO_ONE_NAT\",\"networkTier\":\"PREMIUM\"}],"
				+ "\"aliasIpRanges\":[]}],\"description\":\"\",\"labels\":{},\"scheduling\":{\"preemptible\":false,\"onHostMaintenance\":\"MIGRATE\",\"automaticRestart\":true,\"nodeAffinities\":[]},\"deletionProtection\":false,\"reservationAffinity\":{\"consumeReservationType\":\"ANY_RESERVATION\"},"
				+ "\"serviceAccounts\":[{\"email\":\"709593634312-compute@developer.gserviceaccount.com\",\"scopes\":[\"https://www.googleapis.com/auth/devstorage.read_only\",\"https://www.googleapis.com/auth/logging.write\",\"https://www.googleapis.com/auth/monitoring.write\",\"https://www.googleapis.com/auth/servicecontrol\",\"https://www.googleapis.com/auth/service.management.readonly\",\"https://www.googleapis.com/auth/trace.append\"]}],"
				+ "\"sourceMachineImage\":\"projects/mizzoucyberrange/global/machineImages/"+machineImage+"\"}";
		
		return googleCloudRestClient(input, url, HttpMethod.POST);
		  
		 
	  }
	
	  public String deleteInstance(String zoneName, String instanceName, String machineImage) throws Exception {
		instanceName= instanceName.toLowerCase();

		String url = "https://compute.googleapis.com/compute/beta/projects/"+projectId+"/zones/"+zoneName+"/instances/"+instanceName;
		
		String input = "DELETE";
		
		return googleCloudRestClient(null, url, HttpMethod.DELETE);
		//return null;
		  
		 
	}
	
	public String getInstanceDetails(String zoneName, String instanceName) throws Exception {

		String url = "https://compute.googleapis.com/compute/v1/projects/"+projectId+"/zones/"+zoneName+"/instances/"+instanceName;
		
		return googleCloudRestClient(null, url, HttpMethod.GET);
	}
	
	
	 public Operation.Error blockUntilComplete(
		      Compute compute, Operation operation, long timeout) throws Exception {
		    long start = System.currentTimeMillis();
		    final long pollInterval = 5 * 1000;
		    String zone = operation.getZone();  // null for global/regional operations
		    if (zone != null) {
		      String[] bits = zone.split("/");
		      zone = bits[bits.length - 1];
		    }
		    String status = operation.getStatus();
		    String opId = operation.getName();
		    while (operation != null && !status.equals("DONE")) {
		      Thread.sleep(pollInterval);
		      long elapsed = System.currentTimeMillis() - start;
		      if (elapsed >= timeout) {
		        throw new InterruptedException("Timed out waiting for operation to complete");
		      }
		      System.out.println("waiting...");
		      if (zone != null) {
		        Compute.ZoneOperations.Get get = compute.zoneOperations().get(projectId, zone, opId);
		        operation = get.execute();
		      } else {
		        Compute.GlobalOperations.Get get = compute.globalOperations().get(projectId, opId);
		        operation = get.execute();
		      }
		      if (operation != null) {
		        status = operation.getStatus();
		      }
		    }
		    return operation == null ? null : operation.getError();
		  }
		  // [END wait_until_complete]

	
}
