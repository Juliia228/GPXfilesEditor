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
@RequestMapping("/route")
public class RouteController extends BaseController {
    public final Logger log = LoggerFactory.getLogger(RouteController.class);
    public RouteController() throws IOException {
    }

    @GetMapping("/info")
    public @ResponseBody Route getRouteInfo(@RequestParam int rtID) {
        return service.getGPX().getRoutes().get(rtID);
    }

    @PostMapping("/update")
    public @ResponseBody ResponseEntity<?> updateRoute(@RequestParam int id, @RequestParam List<WayPoint> wayPoints,
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

            Route updatedRT = Route.of(!name.equals("") ? name : null, !comment.equals("") ? comment : null,
                    !description.equals("") ? description : null, !source.equals("") ? source : null,
                    link != null && !link.equals("") ? List.of(Link.of(link)) : null,
                    UInt.of(number), !type.equals("") ? type : null, ext, wayPoints);

            int size = service.getGPX().getRoutes().size();
            List<Route> updatedRTs = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                updatedRTs.add(i, i != id ? service.getGPX().getRoutes().get(i) : updatedRT);
            }
            service.setGPX(service.getGPX().toBuilder().routes(updatedRTs).build());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/del")
    public @ResponseBody ResponseEntity<?> deleteRoute(@RequestParam int id) {
        try {
            int size = service.getGPX().getRoutes().size();
            int index = 0;
            List<Route> updatedRTs = new ArrayList<>(size - 1);
            for (int i = 0; i < size; i++) {
                if (i != id) {
                    updatedRTs.add(index++, service.getGPX().getRoutes().get(i));
                }
            }
            service.setGPX(service.getGPX().toBuilder().routes(updatedRTs).build());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
