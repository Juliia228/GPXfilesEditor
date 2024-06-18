package com.editorGPX.GPXfilesEditor.controllers;

import com.editorGPX.GPXfilesEditor.models.Session;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
@RequestMapping("/")
public class FileController extends BaseController {

    public FileController() throws IOException {
    }

    @PostMapping("/uploadFile")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                File[] dirFiles = new File(PATH_TO_DIR).listFiles();
                String fileName = "file";
                if (dirFiles == null || dirFiles.length == 0) {
                    fileName += 100;
                } else {
                    fileName += dirFiles.length;
                    int i = 1;
                    while (new File(PATH_TO_DIR + fileName + ".gpx").exists()) {
                        fileName = "file" + (100 + dirFiles.length + i++);
                    }
                }
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(PATH_TO_DIR + fileName + ".gpx"));
                byte[] bytes = file.getBytes();
                stream.write(bytes);
                stream.close();
                service.setLastSession(new Session(fileName));
//                printService.GPXtoConsole(lastSession.getGpx());
                return "redirect:/map";
            } else {
                return "redirect:/";
            }
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile() throws IOException {
        byte[] gpxFile = Files.readAllBytes(Paths.get(service.getFullPathToFile()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setContentDispositionFormData("filename", "YourFile.gpx");
        return new ResponseEntity<>(gpxFile, headers, HttpStatus.OK);
    }
}
