package com.editorGPX.GPXfilesEditor.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@RequestMapping("/")
public class TrackController extends BaseController {

    public TrackController() throws IOException {
    }
}
