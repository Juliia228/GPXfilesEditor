package com.editorGPX.GPXfilesEditor.classes;

import com.editorGPX.GPXfilesEditor.controllers.MainController;
import io.jenetics.jpx.GPX;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Path;

@Data
public class Session {
    private String fileName;
    private String fullPath;
    private String pathChangedFile;
    private GPX gpx;

    public Session() {
        fileName = "";
        fullPath = "";
        pathChangedFile = "";
        gpx = GPX.builder().build();
    }

    public Session(String fileName) throws IOException {
        this.fileName = fileName;
        pathChangedFile = "";
        try {
            this.fullPath = MainController.PATH_TO_DIR + fileName;
            gpx = GPX.read(Path.of(this.fullPath));
            MainController.log.info("Файл успешно загружен");
        } catch (IOException e) {
            this.fullPath = "";
            gpx = GPX.builder().build();
            throw new IOException(e.getMessage());
        }
    }

    public void setGpx(GPX gpx) throws IOException {
        this.gpx = gpx;
        if (pathChangedFile.equals("")) {
            pathChangedFile = MainController.PATH_TO_DIR + "changed_" + fileName;
        }
        GPX.write(gpx, Path.of(pathChangedFile));
    }
}
