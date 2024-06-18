package com.editorGPX.GPXfilesEditor.controllers;

import com.editorGPX.GPXfilesEditor.models.GPXCoordinates;
import io.jenetics.jpx.GPX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
@RequestMapping("/")
public class GPXController extends BaseController {
    public final Logger log = LoggerFactory.getLogger(GPXController.class);
    public GPXController() throws IOException {
    }

    @GetMapping("/getTextGPX")
    public @ResponseBody String getTextGPX() throws IOException {
        return Files.readString(Path.of(service.getFullPathToFile()));
    }

    @GetMapping("/getCoordinates")
    public @ResponseBody GPXCoordinates getGPXCoordinates() {
        return new GPXCoordinates(service.getGPX());
    }

    @PostMapping("/newGPX")
    public String newGPX(@RequestParam String creator) throws IOException {
//        GPX newGPX = GPX.builder(creator).build();

//        if (creator.equals("")) {
//            lastSession.setGpx(GPX.builder().build());
//        } else {
        service.setGPX(GPX.builder(creator.equals("") ? "https://github.com/Juliia228/GPXfilesEditor" : creator).build());
//        }

//        // need to change
//        File[] dirFiles = new File(PATH_TO_DIR).listFiles();
//        String fileName = "file";
//        if (dirFiles == null || dirFiles.length == 0) {
//            fileName += 100;
//        } else {
//            fileName += dirFiles.length;
//            int i = 1;
//            while (new File(PATH_TO_DIR + fileName + ".gpx").exists()) {
//                fileName = "file" + (100 + dirFiles.length + i++);
//            }
//        }
//        lastSession = new Session(fileName);
//        lastSession.setGpx(newGPX);
//        // to change

        return "redirect:/map";
    }

    @PostMapping("/updateGPX")
    public @ResponseBody ResponseEntity<?> updateGPX(@RequestParam String fullTextFile) throws IOException {
        try {
            service.setGPX(GPX.Reader.DEFAULT.fromString(fullTextFile));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

//    @PostMapping("/updateGPX")
//    public @ResponseBody String saveGPX(StringBuffer textGPX) {
//        try {
//            // textGPX -> file -> GPX.read(file)
//            Path
//            GPX newGPX = GPX.
//        }
//    }

    //    @GetMapping("/edit")
//    public String edit() {
//        if (lastSession.getFileName().equals("")) {
//            // + add error info
//            return "redirect:/";
//        }
//        return "editor";
//    }
//
//    @GetMapping("/editGPX")
//    public String editToMainPage() {
//        return "redirect:/";
//    }
//
//    @PostMapping("/editGPX")
//    public String editGPXfile(@RequestParam int tag,
//                              @RequestParam(required = false, defaultValue = "-1") int tagLevel1,
//                              @RequestParam(required = false, defaultValue = "-1") int tagLevel2,
//                              @RequestParam(required = false, defaultValue = "-1") int trk_seg_point,
//                              @RequestParam String tagName, @RequestParam String tagData) {
//
//        // добавить валидацию пришедших данных
//
//        String newFileName;
//        try {
//            GPX gpx = changeService.insertExtensions(lastSession.getGpx(), tag,
//                    tagLevel1 - 1, tagLevel2 - 1, trk_seg_point - 1,
//                    tagName, tagData);
//            newFileName = lastSession.setGpx(gpx); // обновление gpx + запись нового файла
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return "redirect:/";
//        }
//        log.info("Файл успешно изменен. Измененный файл: " + newFileName);
//        return "redirect:/edit";
//    }
}
