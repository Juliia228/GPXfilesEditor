package com.editorGPX.GPXfilesEditor.controllers;

import com.editorGPX.GPXfilesEditor.models.GPXCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
public class MainController extends BaseController {
    public final static Logger log = LoggerFactory.getLogger(MainController.class);
    public MainController() throws IOException {
    }

    // http://localhost:8080
    @GetMapping("/")
    public String mainPage() {
        return "main_page";
    }

    @GetMapping("/map")
    public String showGPXmap(Model model) throws IOException {
//        if (lastSession.getGpx().equals(GPX.builder().build())) {
//            // + add error info
//            return "redirect:/";
//        }
        model.addAttribute("gpxCoordinates", new GPXCoordinates(service.getGPX()));
        model.addAttribute("gpx", Files.readString(Path.of(service.getFullPathToFile())));
        return "mapView";
    }
}
