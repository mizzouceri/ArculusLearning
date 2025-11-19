package com.cyberrange.web.utility;

import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class RestClient {

		// public static final String API_SANDBOX_URL = "http://localhost:8080/CyberRangeAPISandBox/";
		// public static final String API_SANDBOX_URL = "http://35.226.197.88:8080/CyberRangeAPISandBox/";
	    // public static final String API_SANDBOX_URL = "http://www.mizzoucyberrange.net/CyberRangeAPISandBox/";
		public static final String API_SANDBOX_URL = "http://127.0.0.1:8080/CyberRangeAPISandBox/";

		public static String callRestCyberRangeAPI(Object input, String url, HttpMethod methodType) {

		String URL = API_SANDBOX_URL + url;

		String response = null;
		ResponseEntity<String> result = null;
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<String> entity = null;

		if (methodType == HttpMethod.POST) {

			String inputJSON = JSONtoObject.ObjecttoJson(input);
			
			entity = new HttpEntity<String>(inputJSON, requestHeaders);
			result = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);

		} else if (methodType == HttpMethod.GET) {
			entity = new HttpEntity<String>(requestHeaders);
			result = restTemplate.exchange(URL, HttpMethod.GET, entity, String.class);
		}

		if (result.getStatusCode().equals(HttpStatus.OK)) {
			response = result.getBody();
		}

		return response;

	}

	public static String callRestGITHUBAPI(Object input, String url, HttpMethod methodType) {

		String result = null;

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(input));
		

		HttpEntity<String> entity = null;
		ResponseEntity<String> response = null;

		if (methodType == HttpMethod.POST) {

			String inputJSON = JSONtoObject.ObjecttoJson(input);
			entity = new HttpEntity<String>(inputJSON, headers);
			response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			System.out.println(gson.toJson(entity));

		} else if (methodType == HttpMethod.GET) {
			headers.add("Authorization", "token "+input);
			entity = new HttpEntity<String>(headers);
			System.out.println(gson.toJson(entity));
			response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		}

		if (response.getStatusCode().equals(HttpStatus.OK)) {
			result = response.getBody();
		}

		return result;

	}

}
