package com.library.database_system.controller;

import com.library.database_system.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping( path = "api/v1/images")
public class ImageController {

    public ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // admin
    @PostMapping(path = "admin/upload")
    public ResponseEntity uploadImage(@RequestBody MultipartFile file){
        return this.imageService.uploadToLocalFileSystem(file);
    }

    // white list
    @GetMapping(
            path = "all/getImage/{imageName:.+}",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE}
    )

    public @ResponseBody byte[] getImageWithMediaType(@PathVariable(name = "imageName" ) String fileName) throws IOException {
        return this.imageService.getImageWithMediaType(fileName);
    }

    // admin
    @DeleteMapping(path = "admin/{imageName:.+}")
    public void removeImg(@PathVariable(name = "imageName")  String fileName){
        this.imageService.removeImage(fileName);
    }
}