package com.editorGPX.GPXfilesEditor.controllers;

import io.jenetics.jpx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/track")
public class TrackController extends BaseController {
    public final Logger log = LoggerFactory.getLogger(TrackController.class);
    public TrackController() throws IOException {
    }

    @GetMapping("/info")
    public @ResponseBody Track getTrackInfo(@RequestParam int trkID) {
        return service.getGPX().getTracks().get(trkID);
    }

    @PostMapping("/update")
    public @ResponseBody ResponseEntity<?> updateTrack(@RequestParam int id, @RequestParam List<TrackSegment> trackSegments,
                                                       @RequestParam(required = false) String name, @RequestParam(required = false) String comment,
                                                       @RequestParam(required = false) String description, @RequestParam(required = false) String source,
                                                       @RequestParam(required = false) String link, @RequestParam(required = false) Integer number,
                                                       @RequestParam(required = false) String type, @RequestParam(required = false) String extensions) {
        try {
            Document ext;
            if (extensions != null && !extensions.equals("")) {
                DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader("<extensions>" + extensions + "</extensions>"));
                ext = db.parse(is);
            } else {
                ext = null;
            }

            Track updatedTRK = Track.of(!name.equals("") ? name : null, !comment.equals("") ? comment : null,
                    !description.equals("") ? description : null, !source.equals("") ? source : null,
                    link != null && !link.equals("") ? List.of(Link.of(link)) : null,
                    UInt.of(number), !type.equals("") ? type : null, ext, trackSegments);

            int size = service.getGPX().getTracks().size();
            List<Track> updatedTRKs = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                updatedTRKs.add(i, i != id ? service.getGPX().getTracks().get(i) : updatedTRK);
            }
            service.setGPX(service.getGPX().toBuilder().tracks(updatedTRKs).build());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/del")
    public @ResponseBody ResponseEntity<?> deleteTrack(@RequestParam int id) {
        try {
            int size = service.getGPX().getTracks().size();
            int index = 0;
            List<Track> updatedTRKs = new ArrayList<>(size - 1);
            for (int i = 0; i < size; i++) {
                if (i != id) {
                    updatedTRKs.add(index++, service.getGPX().getTracks().get(i));
                }
            }
            service.setGPX(service.getGPX().toBuilder().tracks(updatedTRKs).build());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/segment/info")
    public @ResponseBody TrackSegment getTrackSegmentInfo(@RequestParam int trkID, @RequestParam int segID) {
        return service.getGPX().getTracks().get(trkID).getSegments().get(segID);
    }

    @PostMapping("/segment/update")
    public @ResponseBody ResponseEntity<?> updateTrackSegment(@RequestParam int trkID,
                                                              @RequestParam int segID,
                                                              @RequestParam List<WayPoint> wayPoints,
                                                              @RequestParam(required = false) String extensions) {
        try {
            Document ext;
            if (extensions != null && !extensions.equals("")) {
                DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader("<extensions>" + extensions + "</extensions>"));
                ext = db.parse(is);
            } else {
                ext = null;
            }
            TrackSegment updatedTrkSeg = TrackSegment.of(wayPoints, ext);
            service.getGPX().getTracks().get(trkID).getSegments().set(segID, updatedTrkSeg);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
