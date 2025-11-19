package com.cyberrange.api.google;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

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



@Component
@PropertySource("classpath:application.properties")
public class SendMailLib {

    @Value("${spring.mail.username}")
    private String FROM_MAIL_ADDRESS;

    public String getFromMailAddressFromPropertiesFile() throws IOException{
        
        return this.FROM_MAIL_ADDRESS;
    }

}
