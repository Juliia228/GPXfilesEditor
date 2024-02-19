package com.editorGPX.GPXfilesEditor.controllers;

import com.editorGPX.GPXfilesEditor.classes.Session;
import com.editorGPX.GPXfilesEditor.services.GPXservice;
import io.jenetics.jpx.GPX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Controller
public class MainController {
    public final static Logger log = LoggerFactory.getLogger(MainController.class);
    public final static String PATH_TO_DIR = "src/main/resources/GPXfiles/uploaded/";
    //    public final static String LOCAL_PATH_TO_DIR = "src/main/resources/GPXfiles/";
    private final GPXservice service = new GPXservice();
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
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            try {
                File[] dirFiles = new File(PATH_TO_DIR).listFiles();
                String fileName = "file" + (100 + (dirFiles != null ? dirFiles.length : 0)) + ".gpx";
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(PATH_TO_DIR + fileName));
                byte[] bytes = file.getBytes();
                stream.write(bytes);
                stream.close();
                lastSession = new Session(fileName);
                return "redirect:/edit";
            } catch (Exception e) {
                return "redirect:/main_page";
            }
        } else {
            return "redirect:/main_page";
//            return new ModelAndView("error");
        }
    }

    @GetMapping("/edit")
    public String edit() throws Exception {
        if (lastSession.getFullPath().equals("")) {
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
                              @RequestParam(required = false, defaultValue = "-1") int trk_seg_point) throws Exception {
        // добавить валидацию пришедших данных
        try {
            GPX gpx = service.insertExtensions(lastSession.getGpx(), tag, tagLevel1 - 1, tagLevel2 - 1, trk_seg_point - 1);
            lastSession.setGpx(gpx); // обновление gpx + запись нового файла
        } catch (Exception e) {
            log.error(e.getMessage());
            return "redirect:/";
        }
        log.info("Файл успешно изменен. Измененный файл: " + "uploaded/changed_" + lastSession.getFileName());
        return "redirect:/edit";
    }
}
