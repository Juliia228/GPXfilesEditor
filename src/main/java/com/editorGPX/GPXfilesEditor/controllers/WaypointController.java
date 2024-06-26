package com.editorGPX.GPXfilesEditor.controllers;

import io.jenetics.jpx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/waypoint")
public class WaypointController extends BaseController {
    public final Logger log = LoggerFactory.getLogger(WaypointController.class);
    public WaypointController() throws IOException {
    }

    @GetMapping("/info")
    public @ResponseBody WayPoint getWaypointInfo(@RequestParam int wpID) {
        return service.getGPX().getWayPoints().get(wpID);
    }

    @GetMapping("/new")
    public @ResponseBody WayPoint getNewWaypoint(@RequestParam double latitude, @RequestParam double longitude) throws IOException {
        WayPoint newWP = WayPoint.of(latitude, longitude);
        service.setGPX(service.getGPX().toBuilder().addWayPoint(newWP).build());
        return newWP;
    }

    @PostMapping("/update")
    public @ResponseBody ResponseEntity<?> updateWaypoint(@RequestParam int id, @RequestParam double latitude, @RequestParam double longitude,
                                                          @RequestParam(required = false) Double elevation, @RequestParam(required = false) CharSequence time,
                                                          @RequestParam(required = false) Double magneticVariation, @RequestParam(required = false) Double geoidHeight,
                                                          @RequestParam(required = false) String name, @RequestParam(required = false) String comment,
                                                          @RequestParam(required = false) String description, @RequestParam(required = false) String source,
                                                          @RequestParam(required = false) String link, @RequestParam(required = false) String symbol,
                                                          @RequestParam(required = false) String type, @RequestParam(required = false) String fix,
                                                          @RequestParam(required = false) Integer sat, @RequestParam(required = false) Double hdop,
                                                          @RequestParam(required = false) Double vdop, @RequestParam(required = false) Double pdop,
                                                          @RequestParam(required = false) Long ageOfGPSData, @RequestParam(required = false) Integer dgpsid,
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

            WayPoint updatedWP = WayPoint.of(Latitude.ofDegrees(latitude), Longitude.ofDegrees(longitude),
                    elevation != null ? Length.of(elevation, Length.Unit.METER) : null, null,
                    time != null && !time.equals("") ? Instant.parse(time) : null,
                    magneticVariation != null ? Degrees.ofDegrees(magneticVariation) : null,
                    geoidHeight != null ? Length.of(geoidHeight, Length.Unit.METER) : null,
                    !name.equals("") ? name : null, !comment.equals("") ? comment : null,
                    !description.equals("") ? description : null, !source.equals("") ? source : null,
                    link != null && !link.equals("") ? List.of(Link.of(link)) : null,
                    !symbol.equals("") ? symbol : null, !type.equals("") ? type : null,
                    fix != null && !fix.equals("") ? Fix.valueOf(fix) : null,
                    sat != null ? UInt.of(sat) : null, hdop, vdop, pdop,
                    ageOfGPSData != null ? Duration.ofSeconds(ageOfGPSData) : null, null,
                    dgpsid != null ? Degrees.ofDegrees(dgpsid) : null, ext);

            int size = service.getGPX().getWayPoints().size();
            List<WayPoint> updatedWPs = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                updatedWPs.add(i, i != id ? service.getGPX().getWayPoints().get(i) : updatedWP);
            }
            service.setGPX(service.getGPX().toBuilder().wayPoints(updatedWPs).build());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/del")
    public @ResponseBody ResponseEntity<?> deleteWaypoint(@RequestParam int id) {
        try {
            int size = service.getGPX().getWayPoints().size();
            int index = 0;
            List<WayPoint> updatedWPs = new ArrayList<>(size - 1);
            for (int i = 0; i < size; i++) {
                if (i != id) {
                    updatedWPs.add(index++, service.getGPX().getWayPoints().get(i));
                }
            }
            service.setGPX(service.getGPX().toBuilder().wayPoints(updatedWPs).build());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
