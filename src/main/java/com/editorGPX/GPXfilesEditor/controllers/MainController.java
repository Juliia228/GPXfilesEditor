package com.editorGPX.GPXfilesEditor.controllers;

import com.editorGPX.GPXfilesEditor.classes.Session;
import com.editorGPX.GPXfilesEditor.models.GPXCoordinates;
import com.editorGPX.GPXfilesEditor.services.changeGPXservice;
import com.editorGPX.GPXfilesEditor.services.printGPXservice;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Route;
import io.jenetics.jpx.WayPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

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

    // http://localhost:8080/map
    @GetMapping("/map")
    public String showGPXmap(Model model) throws IOException {
        if (lastSession.getFileName().equals("")) {
            // + add error info
            return "redirect:/";
        }
        model.addAttribute("gpxCoordinates", new GPXCoordinates(lastSession.getGpx()));
        model.addAttribute("gpx", Files.readString(Path.of(MainController.PATH_TO_DIR + lastSession.getFileName() + ".gpx")));
        return "mapView";
    }

    @GetMapping("/waypointInfo")
    public @ResponseBody WayPoint getWaypointInfo(@RequestParam int wpID) {
        return lastSession.getGpx().getWayPoints().get(wpID);
    }

    @GetMapping("/routeInfo")
    public @ResponseBody Route getRouteInfo(@RequestParam int rtID) {
        return lastSession.getGpx().getRoutes().get(rtID);
    }

    @PostMapping("/updateGPX")
    public String updateGPX(Model model) {
        //
        return "error";
    }

    @GetMapping("/uploadFile")
    public String uploadToMainPage() {
        return "redirect:/";
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
                lastSession = new Session(fileName);
                printService.GPXtoConsole(lastSession.getGpx());
                //return "redirect:/edit";
                return "redirect:/map";
            } else {
                return "redirect:/";
            }
        } catch (Exception e) {
            return "redirect:/";
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
