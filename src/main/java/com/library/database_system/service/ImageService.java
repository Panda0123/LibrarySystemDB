package com.library.database_system.service;

import com.library.database_system.DatabaseSystemApplication;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.jni.File;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {

    public String storageDirectoryPath;

    public ImageService() {
        storageDirectoryPath = Paths.get("").toAbsolutePath().toString() + "/cover";
//        try {
//            storageDirectoryPath = new ClassPathResource("static/images/").getFile().getAbsolutePath();
//            storageDirectoryPath = new ClassPathResource("static/images/").getInputStream().getAbsolutePath();
//            String path = new File(DatabaseSystemApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
//            storageDirectoryPath = DatabaseSystemApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
//            storageDirectoryPath = storageDirectoryPath.substring(1, storageDirectoryPath.lastIndexOf("/")) + "/cover";
//            ImageService.class.getResource().getFile()
//            getClass().getClassLoader().getResource("static/images/").getPath();
//                    .getFile().
//                    .getAbsolutePath();

//        } catch (IOException e) {
//            e.printStackTrace();
//
//        } catch (URISyntaxException ex) {
//            ex.printStackTrace();
//        }
    }

    public ResponseEntity uploadToLocalFileSystem(MultipartFile file) {
        /* we will extract the file name (with extension) from the given file to store it in our local machine for now
        and later in virtual machine when we'll deploy the project
         */
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        /* The Path in which we will store our image . we could change it later
        based on the OS of the virtual machine in which we will deploy the project.
        In my case i'm using windows 10 .
         */
        Path storageDirectory = Paths.get(storageDirectoryPath);
        /*
         * we'll do just a simple verification to check if the folder in which we will store our images exists or not
         * */
        if(!Files.exists(storageDirectory)){ // if the folder does not exist
            try {
                Files.createDirectories(storageDirectory); // we create the directory in the given storage directory path
            }catch (Exception e){
                e.printStackTrace();// print the exception
            }
        }

        Path destination = Paths.get(storageDirectory + "\\" + fileName);

        try {
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);// we are Copying all bytes from an input stream to a file
        } catch (IOException e) {
            e.printStackTrace();
        }
        // the response will be the download URL of the image
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/v1/images/getImage/")
                .path(fileName)
                .toUriString();
        // return the download image url as a response entity
        return ResponseEntity.ok(fileDownloadUri);
    }

    public  byte[] getImageWithMediaType(String imageName) throws IOException {
        Path storageDirectory = Paths.get(storageDirectoryPath);
        Path destination = Paths.get(storageDirectory+"\\"+imageName);// retrieve the image by its name
        return IOUtils.toByteArray(destination.toUri());
    }

    public void removeImage(String fileName) {
        java.io.File temp = new java.io.File(storageDirectoryPath + "\\" +  fileName) ;
        System.out.println(storageDirectoryPath.toString() + fileName);
        temp.delete();
    }
}
