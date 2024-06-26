package com.editorGPX.GPXfilesEditor.controllers;

import com.editorGPX.GPXfilesEditor.services.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.io.*;

@Controller
public class BaseController {
    @Autowired
    public MainService service;
    public final static String PATH_TO_DIR = "src/main/resources/GPXfiles/uploaded/";
    public BaseController() throws IOException {
    }
}
