package com.editorGPX.GPXfilesEditor.models;

import com.editorGPX.GPXfilesEditor.controllers.MainController;
import io.jenetics.jpx.GPX;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import static com.editorGPX.GPXfilesEditor.controllers.BaseController.PATH_TO_DIR;

@Data
public class Session {
    private String fileName; // название созданного файла
    private String fullPathToFile; // полный путь до файла
    private GPX gpx;

    public Session() throws IOException {
        fileName = createFilename();
        setFullPathToFile();
        gpx = GPX.builder().build();
    }

    public Session(MultipartFile file) throws IOException {
        fileName = createFilename();
        setFullPathToFile();

        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fullPathToFile));
        byte[] bytes = file.getBytes();
        stream.write(bytes);
        stream.close();

        try {
            gpx = GPX.read(Path.of(fullPathToFile));
            MainController.log.info("Файл успешно загружен");
        } catch (IOException e) {
            gpx = GPX.builder().build();
            throw new IOException(e.getMessage());
        }
    }

    private String createFilename() {
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
        return fileName;
    }

    public void setGpx(GPX gpx) throws IOException {
        this.gpx = gpx;
        GPX.write(gpx, Path.of(fullPathToFile));
    }

    public void setFullPathToFile() {
            fullPathToFile = PATH_TO_DIR + fileName + ".gpx";
    }
}