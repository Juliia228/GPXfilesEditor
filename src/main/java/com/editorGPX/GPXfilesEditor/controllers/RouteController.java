package com.editorGPX.GPXfilesEditor.controllers;

import io.jenetics.jpx.Route;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("/")
public class RouteController extends BaseController {
    public RouteController() throws IOException {
    }

    @GetMapping("/routeInfo")
    public @ResponseBody Route getRouteInfo(@RequestParam int rtID) {
        return service.getGPX().getRoutes().get(rtID);
    }

}
