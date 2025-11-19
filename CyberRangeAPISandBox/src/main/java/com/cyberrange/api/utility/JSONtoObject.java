package com.cyberrange.api.utility;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.cyberrange.api.model.GoogleVMDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSONtoObject {

	public static <T> T  jsonToObject(String json, Class<?> classType ){

		Object map = null;
		try {
			ObjectMapper mapper = new ObjectMapper();

			map = mapper.readValue(json, classType);
			
		} catch (Exception e) {

			e.printStackTrace();
		} 

		return (T) map;
	
	}
	
	public static String ObjecttoJson(Object obj) {

		String output = null;
		
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			output = mapper.writeValueAsString(obj);
			
		} catch (Exception e) {

			e.printStackTrace();
		}


		return output;
		
	}



	public static GoogleVMDetails jsonToGoogleVMDetails(String responseAttacker, String responseDetails) throws Exception {
		JSONParser parser = new JSONParser();
		JSONObject json=(JSONObject) parser.parse(responseAttacker);
		
		JSONObject jsonDtl =(JSONObject) parser.parse(responseDetails);
		
		GoogleVMDetails details =new GoogleVMDetails();
		
		details.setId(String.valueOf(json.get("id")));
		details.setName(String.valueOf(jsonDtl.get("name")));
		
		JSONArray networkInterfaces =  (JSONArray) jsonDtl.get("networkInterfaces");
		JSONObject networkInterface = (JSONObject) networkInterfaces.get(0);
		JSONArray accessConfigs = (JSONArray) networkInterface.get("accessConfigs");
		JSONObject accessConfig = (JSONObject) accessConfigs.get(0);
		details.setExternalIP(String.valueOf(accessConfig.get("natIP")));
		
		return details;
	}
	
}
