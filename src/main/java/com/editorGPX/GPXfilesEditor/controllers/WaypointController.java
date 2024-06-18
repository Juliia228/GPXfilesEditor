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
                                                          @RequestParam String name, @RequestParam(required = false) String comment,
                                                          @RequestParam(required = false) String description, @RequestParam(required = false) String source,
                                                          @RequestParam(required = false) Link link, @RequestParam(required = false) String symbol,
                                                          @RequestParam(required = false) String type, @RequestParam(required = false) String fix,
                                                          @RequestParam(required = false) Integer sat, @RequestParam(required = false) Double hdop,
                                                          @RequestParam(required = false) Double vdop, @RequestParam(required = false) Double pdop,
                                                          @RequestParam(required = false) Long ageOfGPSData, @RequestParam(required = false) Double dgpsid,
                                                          @RequestParam(required = false) String extensions) {
        try {
            Document ext = null;
            if (extensions != null) {
                DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(extensions));
                ext = db.parse(is);
            }

            WayPoint previousWP = service.getGPX().getWayPoints().get(id);
            WayPoint updatedWP = WayPoint.of(Latitude.ofDegrees(latitude), Longitude.ofDegrees(longitude),
                    elevation != null ? Length.of(elevation, Length.Unit.METER) : null, null, time != null ? Instant.parse(time) : null,
                    magneticVariation != null ? Degrees.ofDegrees(magneticVariation) : null,
                    geoidHeight != null ? Length.of(geoidHeight, Length.Unit.METER) : null, name, comment, description, source, link != null ? List.of(link) : null, symbol, type,
                    fix != null ? Fix.valueOf(fix) : null, sat != null ? UInt.of(sat) : null, hdop, vdop, pdop,
                    ageOfGPSData != null ? Duration.ofSeconds(ageOfGPSData) : null, null,
                    dgpsid != null ? Degrees.ofDegrees(dgpsid) : null, ext);
//            service.getGPX().toBuilder().wayPoints().set(id, updatedWP);
            service.setGPX(service.getGPX().toBuilder()
                    .wayPointFilter()
                    .map(wp -> {
                        if (wp.equals(previousWP)) {
                            wp = updatedWP;
                        }
                        return wp;
                    })
                    .build()
                    .build());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
