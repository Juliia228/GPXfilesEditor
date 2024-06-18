package com.editorGPX.GPXfilesEditor.services;

import com.editorGPX.GPXfilesEditor.models.Session;
import io.jenetics.jpx.GPX;
import lombok.Data;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
@Data
public class MainService {
    private Session lastSession = new Session();

    public MainService() throws IOException {
    }

    public String getFullPathToFile() {
        return lastSession.getFullPathToFile();
    }

    public GPX getGPX() {
        return lastSession.getGpx();
    }

    public void setFilename(String name) {
        lastSession.setFileName(name);
        lastSession.setFullPathToFile();
    }

    public void setGPX(GPX gpx) throws IOException {
        lastSession.setGpx(gpx);
    }
}
