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

import com.google.api.client.http.HttpTransport;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.Operation;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;


@Component
@PropertySource("classpath:upload.properties")
public class UploadLib {

    @Value("${fileUploadDirectory}")
    private String UPLOAD_DIR;

    @Value("${htmlfileUploadDirectory}")
    private String HTML_UPLOAD_DIR;
  
    
    public String getImageUploadDirectory() throws IOException{
        
        return this.UPLOAD_DIR;
    }

    public String getHTMLUploadDirectory() throws IOException{
        
        return this.HTML_UPLOAD_DIR;
    }

    public void deleteUploadedFiles(String dir) {
        // TODO Auto-generated method stub
        try{
            String imgDir = this.UPLOAD_DIR + dir;
            String htmlDir = this.HTML_UPLOAD_DIR + dir;
            Path imagePath = Paths.get(imgDir);
            Path htmlPath = Paths.get(htmlDir);
            if (Files.exists(imagePath)) {
                System.out.println(imgDir);
                listFiles(new File(imgDir));
            }
            if (Files.exists(htmlPath)) {
                System.out.println(htmlDir);
                listFiles(new File(htmlDir));
                
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        
    }

    private static void listFiles(File parentDir){
        if(!parentDir.isDirectory()){
          System.out.println("Not a directory.");
          return;
        }
        File[] fileList = parentDir.listFiles();
        // iterate array 
        for(File file : fileList){
          // if it's a directory list files with in sub-directory for deletion
          if(file.isDirectory()){
            System.out.println("Directory- " + file.getName());
            listFiles(file);
          }else{                 
            System.out.println("Deleting File- " + file.getName());
            // if it is a file then delete it
            file.delete();
          }
        }
        // For deleting directories
        System.out.println("Deleting Directory - " + parentDir.getName());
        parentDir.delete();
      }
}
