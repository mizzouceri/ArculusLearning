package com.cyberrange.api.controller;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
 

import com.cyberrange.api.bean.Upload;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.cyberrange.api.google.UploadLib;
import com.cyberrange.api.model.Modules;
import com.cyberrange.api.repository.LabStepsRepository;
import com.cyberrange.api.repository.ModuleRepository;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/upload")
public class UploadAPI {

    //private static String UPLOAD_DIR = System.getProperty("user.home") + "/test";

    //@Value("${file_upload_directory}")
    
  
    @Autowired
    UploadLib  uploadLib;

    @Autowired
    LabStepsRepository labStepsRepository;

    @Autowired
    ModuleRepository moduleRepository;

    private static String UPLOAD_DIR;

    @PostMapping("/rest/uploadMultiFiles")
    @ResponseBody
    public ResponseEntity<?> uploadFileMulti(@ModelAttribute Upload form) throws Exception {
 
        
        //System.out.println("Description:" + form.getDescription());
        //String path = UploadAPI.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        //System.out.println(path);
        //System.out.println("==="+uploadLib.getUploadDirectory());
        
        String result = null;
        
        String[] desc = form.getDescription().split("@");
        String fileRootName = desc[0].replaceAll("\\s+", "_").toLowerCase();
        
        Long moduleId = Long.parseLong(desc[1]);
        System.out.println(fileRootName+"  "+moduleId);
        //System.out.println("fileRootName:" + fileRootName);
        Boolean isFileHTML =  false;
        try {
            
            MultipartFile[] files = form.getFiles();
            for(MultipartFile file: files){
                //System.out.println(file.getContentType());
                if(file.getContentType().compareTo("text/html") !=  0){
                    //System.out.println(file.getContentType());
                    isFileHTML = false;
                    break;
                }
                else{
                    isFileHTML = true;
                }
            }
            if(isFileHTML){
                result = this.saveHTMLUploadedFiles(files,fileRootName,moduleId);
                isFileHTML = false;
            }else{
            result = this.saveUploadedFiles(files,fileRootName,moduleId);
            }
        }
        // Here Catch IOException only.
        // Other Exceptions catch by RestGlobalExceptionHandler class.
        catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
 
        return new ResponseEntity<String>("Uploaded to: " + result, HttpStatus.OK);
    }
 
    
    // Save Files
    private String saveUploadedFiles(MultipartFile[] files, String fileRootName,Long moduleId) throws IOException {
 
        // Make sure directory exists!
        String UPLOAD_DIR = uploadLib.getImageUploadDirectory();
        File uploadDir = new File(UPLOAD_DIR + "/" + fileRootName + "/");
        uploadDir.mkdirs();
 
        StringBuilder sb = new StringBuilder();
        String file_name = "name";
        String modifiedFileName = "" ;

        for (MultipartFile file : files) {
 
            if (file.isEmpty()) {
                continue;
            }
            String[] filetypesplit = file.getContentType().split("/");
            String fileExtension = filetypesplit[filetypesplit.length - 1];
            modifiedFileName = fileRootName+"."+fileExtension;

            //String uploadFilePath = UPLOAD_DIR + "/" + file.getOriginalFilename();
            String uploadFilePath = UPLOAD_DIR + "/" + fileRootName + "/" + modifiedFileName;
 
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadFilePath);
            Files.write(path, bytes);
 
            sb.append(uploadFilePath).append(", ");
        }

        /// Write code here
        String filepath = "/MizzouCloudDevOps/img/uploads/" + fileRootName + "/" + modifiedFileName;
        //int re = moduleRepository.setModuleImage(filepath, moduleId);
        Modules module = moduleRepository.getModuleById(moduleId);
        module.setModuleImage(filepath);
        moduleRepository.save(module);
        
        System.out.println("Image Upload Complete");

        return sb.toString();
    }
    private String saveHTMLUploadedFiles(MultipartFile[] files, String fileRootName,Long moduleId) throws IOException {
 
        // Make sure directory exists!
        String UPLOAD_DIR = uploadLib.getHTMLUploadDirectory();
        File uploadDir = new File(UPLOAD_DIR + "/" + fileRootName + "/labs/");
        uploadDir.mkdirs();
 
        StringBuilder sb = new StringBuilder();
        String file_name = "name";
        int count = 0;
        for (MultipartFile file : files) {
 
            if (file.isEmpty()) {
                continue;
            }
            String[] filetypesplit = file.getContentType().split("/");
            String fileExtension = filetypesplit[filetypesplit.length - 1];
            if(count == 0){
                String modifiedFileName = fileRootName+"_lab_getting_start"+"."+fileExtension;
                String uploadFilePath = UPLOAD_DIR + "/" + fileRootName + "/labs/" + modifiedFileName;
                //System.out.println(uploadFilePath);
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadFilePath);
                Files.write(path, bytes);
                count ++;
                sb.append(uploadFilePath).append(", ");
                System.out.println("Getting HTML Upload Complete");
                continue;
            }
            String modifiedFileName = fileRootName+"_lab_step_"+count+"."+fileExtension;

            
            //String uploadFilePath = UPLOAD_DIR + "/" + file.getOriginalFilename();
            String uploadFilePath = UPLOAD_DIR + "/" + fileRootName + "/labs/" + modifiedFileName;
            //System.out.println(uploadFilePath);
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadFilePath);
            Files.write(path, bytes);

            System.out.println("Lab "+count +" HTML Upload Complete");
            // Update the database
            String filePath = "view/modules/" + fileRootName + "/labs/" + modifiedFileName;
            System.out.println(filePath);
            int re = labStepsRepository.setViewName(filePath,moduleId,count+"");


            count ++;
            sb.append(uploadFilePath).append(", ");
        }

        System.out.println("All HTMLs Upload Complete");
        return sb.toString();
    }

    // -------------------------------- END ---------------------------------------//











    @GetMapping("/rest/getAllFiles")
    public List<String> getListFiles() {
        File uploadDir = new File(UPLOAD_DIR);
 
        File[] files = uploadDir.listFiles();
 
        List<String> list = new ArrayList<String>();
        for (File file : files) {
            list.add(file.getName());
        }
        return list;
    }
 
    // @filename: abc.zip,..
    @GetMapping("/rest/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws MalformedURLException {
        File file = new File(UPLOAD_DIR + "/" + filename);
        if (!file.exists()) {
            throw new RuntimeException("File not found");
        }
        Resource resource = new UrlResource(file.toURI());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }
    
}
