package com.editorGPX.GPXfilesEditor.classes;

import com.editorGPX.GPXfilesEditor.controllers.MainController;
import io.jenetics.jpx.GPX;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Path;

@Data
public class Session {
    private String fileName; // название исходного созданного файла
    private String pathLastChangedFile; // полный путь до файла с последними изменениями
    private GPX gpx;
    private int changeCount; // количество изменений файла

    public Session() {
        fileName = "";
        pathLastChangedFile = "";
        gpx = GPX.builder().build();
        changeCount = 0;
    }

    public Session(String fileName) throws IOException {
        this.fileName = fileName;
        pathLastChangedFile = "";
        try {
            gpx = GPX.read(Path.of(MainController.PATH_TO_DIR + fileName + ".gpx"));
            MainController.log.info("Файл успешно загружен");
        } catch (IOException e) {
            gpx = GPX.builder().build();
            throw new IOException(e.getMessage());
        }
    }

    public String setGpx(GPX gpx) throws IOException {
        this.gpx = gpx;
        changeCount++;
        pathLastChangedFile = MainController.PATH_TO_DIR + fileName + "_v" + changeCount + ".gpx";
        GPX.write(gpx, Path.of(pathLastChangedFile));
        return fileName + "_v" + changeCount + ".gpx"; // название измененного файла
    }
}
