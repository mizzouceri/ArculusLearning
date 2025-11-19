package com.cyberrange.api.controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import com.cyberrange.api.service.CertificateService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/certificate")
public class CertificateAPI {

	@Autowired
	CertificateService certificateService;
	
	@Value("${cyberrange.certificate-template-area}")
	private String templateLocation;
	
	@Value("${cyberrange.certificate-storage-area}")
	private String certificateLocation;
	
	@PostMapping("/generateCertificate")
	public ResponseEntity generateCertificate(@RequestBody String request)
	{		
		try {
			JSONParser parser = new JSONParser();
			JSONObject json=(JSONObject) parser.parse(request);
			Long moduleId =  Long.parseLong(String.valueOf(json.get("moduleId")));
			Long userId =  Long.parseLong(String.valueOf(json.get("userId")));
			String studentName = String.valueOf(json.get("studentName"));
			certificateService.generateCertificate(moduleId, userId, studentName);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@PostMapping("/downloadCertificate")
	public ResponseEntity downloadCertificate(@RequestBody String request)
	{		
		try {
			JSONParser parser = new JSONParser();
			JSONObject json=(JSONObject) parser.parse(request);
			Long moduleId =  Long.parseLong(String.valueOf(json.get("moduleId")));
			Long userId =  Long.parseLong(String.valueOf(json.get("userId")));
			//InputStream certificate = certificateService.downloadCertificate(moduleId, userId);
			String type = null;
			byte[]out = null;

			File certificatePDF = new File(certificateLocation.toString() + "/" + moduleId + "/User_" + userId + "_Certificate.pdf");
			InputStream inputStream = new FileInputStream(certificateLocation.toString() + "/" + moduleId + "/User_" + userId + "_Certificate.pdf");
			type=certificatePDF.toURL().openConnection().guessContentTypeFromName("User_" + userId + "_Certificate.pdf");

	        
			out=org.apache.commons.io.IOUtils.toByteArray(inputStream);

	        HttpHeaders responseHeaders = new HttpHeaders();
	        responseHeaders.add("content-disposition", "attachment; filename=" + "User_" + userId + "_Certificate.pdf");
	        responseHeaders.add("Content-Type",type);
	        return new ResponseEntity(out, responseHeaders,HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping("/viewCertificate/{certificateParam}")
	public ResponseEntity viewCertificate(@PathVariable String certificateParam)
	{		
		try {
			JSONParser parser = new JSONParser();
			String[] param = certificateParam.split("S");
			//Long moduleId =  Long.parseLong(String.valueOf(json.get("moduleId")));
			//Long userId =  Long.parseLong(String.valueOf(json.get("userId")));
			//InputStream certificate = certificateService.downloadCertificate(moduleId, userId);
			String type = null;
			byte[]out = null;

			File certificatePDF = new File(certificateLocation.toString() + "/" + param[0] + "/User_" + param[1] + "_Certificate.pdf");
			InputStream inputStream = new FileInputStream(certificateLocation.toString() + "/" + param[0] + "/User_" + param[1] + "_Certificate.pdf");
			type=certificatePDF.toURL().openConnection().guessContentTypeFromName("MCDCertificate.pdf");

	        
			out=org.apache.commons.io.IOUtils.toByteArray(inputStream);

	        HttpHeaders responseHeaders = new HttpHeaders();
	        responseHeaders.add("content-disposition", "inline; filename=" + "MCDCertificate.pdf");
	        responseHeaders.add("Content-Type",type);
	        return new ResponseEntity(out, responseHeaders,HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}

