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
        service.setGPX(GPX.builder(creator.equals("") ? "https://github.com/Juliia228/GPXfilesEditor" : creator).build());
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
}
