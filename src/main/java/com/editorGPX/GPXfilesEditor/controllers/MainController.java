package com.editorGPX.GPXfilesEditor.controllers;

import com.editorGPX.GPXfilesEditor.classes.Session;
import com.editorGPX.GPXfilesEditor.services.changeGPXservice;
import com.editorGPX.GPXfilesEditor.services.printGPXservice;
import io.jenetics.jpx.GPX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Controller
public class MainController {
    public final static Logger log = LoggerFactory.getLogger(MainController.class);
    public final static String PATH_TO_DIR = "src/main/resources/GPXfiles/uploaded/";
    private final changeGPXservice changeService = new changeGPXservice();
    private final printGPXservice printService = new printGPXservice();
    private Session lastSession = new Session();

    // http://localhost:8080
    @GetMapping("/")
    public String mainPage() {
        return "main_page";
    }

    @GetMapping("/uploadFile")
    public String uploadToMainPage() {
        return "redirect:/";
    }

    @PostMapping(value = "/uploadFile")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
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
                lastSession = new Session(fileName);
                printService.GPXtoConsole(lastSession.getGpx());
                return "redirect:/edit";
            } catch (Exception e) {
                return "redirect:/";
            }
        } else {
            return "redirect:/";
//            return new ModelAndView("error");
        }
    }

    @GetMapping("/edit")
    public String edit() {
        if (lastSession.getFileName().equals("")) {
            // + add error info
            return "redirect:/";
        }
        return "editor";
    }

    @GetMapping("/editGPX")
    public String editToMainPage() {
        return "redirect:/";
    }

    @PostMapping("/editGPX")
    public String editGPXfile(@RequestParam int tag,
                              @RequestParam(required = false, defaultValue = "-1") int tagLevel1,
                              @RequestParam(required = false, defaultValue = "-1") int tagLevel2,
                              @RequestParam(required = false, defaultValue = "-1") int trk_seg_point,
                              @RequestParam String tagName, @RequestParam String tagData) {

        // добавить валидацию пришедших данных

        String newFileName;
        try {
            GPX gpx = changeService.insertExtensions(lastSession.getGpx(), tag,
                    tagLevel1 - 1, tagLevel2 - 1, trk_seg_point - 1,
                    tagName, tagData);
            newFileName = lastSession.setGpx(gpx); // обновление gpx + запись нового файла
        } catch (Exception e) {
            log.error(e.getMessage());
            return "redirect:/";
        }
        log.info("Файл успешно изменен. Измененный файл: " + newFileName);
        return "redirect:/edit";
    }
}
