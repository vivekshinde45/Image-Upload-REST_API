package com.imageuploader.RestApiImageUpload.Controller;

import com.imageuploader.RestApiImageUpload.Config.AppConstants;
import com.imageuploader.RestApiImageUpload.Payload.FileResponse;
import com.imageuploader.RestApiImageUpload.Services.Implements.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String folderPath;

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> upload(@RequestParam("image")MultipartFile image){
        String imageName = image.getOriginalFilename();
        String fileExtension = imageName.substring(imageName.lastIndexOf("."));

        if(AppConstants.ALLOW_EXTENSIONS.contains(fileExtension)){
            String fileName = null;
            try {
                fileName = this.fileService.uploadImage(folderPath, image);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(
                        new FileResponse(fileName, "Image not uploaded due to Internal Server Error !!"),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(
                    new FileResponse(fileName, "Image uploaded successfully !!"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(
                new FileResponse(null, "Please provide image !!"),
                HttpStatus.OK);
    }

    // serve the image
    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName,
            HttpServletResponse response
    ) throws IOException {
        try {
            InputStream resource = this.fileService.getResource(folderPath, imageName);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            StreamUtils.copy(resource, response.getOutputStream());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
