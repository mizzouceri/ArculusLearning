package com.cyberrange.web.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cyberrange.web.bean.UserSessionVO;
import com.cyberrange.web.utility.RestClient;

@Controller
@RequestMapping("login")
@PropertySource("classpath:github.properties")
public class LoginController {
	
	@Value("${client_id}")
    private String clientId;
	
	@Value("${client_secret}")
	private String clientSecret;
	
	@RequestMapping(value = "logoutUser",method = RequestMethod.GET)
	public String logoutUser( HttpServletRequest request){
		
		HttpSession httpSession = request.getSession();
		httpSession.removeAttribute("UserSessionVO");
		
		return "redirect:/";
		
	}
	
	@RequestMapping(value = "/afterAuthentication/")
	public String afterAuthentication( HttpServletRequest request) {
		
		try {
		
			String code = request.getParameter("code");
			
			HashMap<String,Object> map= new HashMap<String,Object>();
			map.put("client_id",clientId);
			map.put("client_secret",clientSecret);
			map.put("code",code);
			
			String URL = "https://github.com/login/oauth/access_token";
			String access_token_str= RestClient.callRestGITHUBAPI(map, URL, HttpMethod.POST);
			
			String access_token = access_token_str.substring(access_token_str.indexOf("=") + 1,access_token_str.indexOf("&"));
			 
//			HashMap<String,Object> param= new HashMap<String,Object>();
//			param.put("Authorization: token ", access_token);
//			System.out.print(access_token);
			
			URL = "https://api.github.com/user";
			String userDetails= RestClient.callRestGITHUBAPI(access_token, URL, HttpMethod.GET);
			System.out.print(userDetails);
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(userDetails);
			
			JSONObject object = new JSONObject();
			object.put("userName", json.get("login"));
			object.put("githubId", json.get("id"));
			object.put("avatarUrl", json.get("avatar_url"));
			String userMizzouDetails=RestClient.callRestCyberRangeAPI(object, "/login/getUserDetails", HttpMethod.POST);

			JSONObject userJSON = (JSONObject) parser.parse(userMizzouDetails);
			
			System.out.println(userJSON.toString());
			UserSessionVO sessionVO = new UserSessionVO();
			sessionVO.setUserName(String.valueOf(userJSON.get("userName")));
			sessionVO.setUserId(String.valueOf(userJSON.get("userId")));
			sessionVO.setGithubId(String.valueOf(userJSON.get("githubId")));
			sessionVO.setAvatarUrl(String.valueOf(userJSON.get("avatarUrl")));
			sessionVO.setRole(String.valueOf(userJSON.get("role")));
			sessionVO.setApprovalStatus(String.valueOf(userJSON.get("approvalStatus")));
			
			System.out.println("Approval Status "+ sessionVO.getApprovalStatus()+"Role "+sessionVO.getRole());

			HttpSession httpSession = request.getSession();
			httpSession.setAttribute("UserSessionVO", sessionVO);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return "redirect:/";
	
		
	}
}
