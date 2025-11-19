package com.cyberrange.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cyberrange.web.bean.UserSessionVO;
import com.cyberrange.web.utility.JSONtoObject;


@Controller
@SpringBootApplication
public class Application {
	
	
	
    public static void main(String[] args) {
    	System.setProperty("server.contextPath", "/MizzouCloudDevOps");

        SpringApplication.run(Application.class, args);
    }
    
    @RequestMapping("/")
	public ModelAndView welcomePage(HttpServletRequest request) {
      
    	UserSessionVO user = (UserSessionVO) request.getSession().getAttribute("UserSessionVO");

    	ModelAndView mav = null; 
    	if(null != user) {
    		System.out.println("Role is");
			System.out.println(user.getRole());
    		if("STUDENT".equalsIgnoreCase(user.getRole())) {
        		mav = new ModelAndView("index");
    		}else {
        		mav = new ModelAndView("indexInstructor");

    		}
    		 
    		String userSessionVO = JSONtoObject.ObjecttoJson(user);
            mav.addObject("userSessionVO", userSessionVO);
    	}else {
        	mav = new ModelAndView("login");

    	}
    	
    	return mav;
	}

	
	
}

