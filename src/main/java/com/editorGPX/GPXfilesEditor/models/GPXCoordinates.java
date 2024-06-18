package com.editorGPX.GPXfilesEditor.models;

import io.jenetics.jpx.*;
import lombok.Data;
import java.util.List;

@Data
public class GPXCoordinates {
    private double min_latitude = 90.0;
    private double min_longitude = 180.0;
    private double max_latitude = -90.0;
    private double max_longitude = -180.0;
    private double[][] wayPoints;
    private double[][][] routes;
    private double[][][][] tracks;

    public GPXCoordinates(GPX gpx) {
        wayPoints = !gpx.getWayPoints().isEmpty() ? listToWaypointCoordinates(gpx.getWayPoints()) : null;
        routes = !gpx.getRoutes().isEmpty() ? listToRouteCoordinates(gpx.getRoutes()) : null;
        tracks = !gpx.getTracks().isEmpty() ? listToTrackCoordinates(gpx.getTracks()) : null;
    }

    private void setMinMaxValues(double latitude, double longitude) {
        min_latitude = Math.min(latitude, min_latitude);
        min_longitude = Math.min(longitude, min_longitude);
        max_latitude = Math.max(latitude, max_latitude);
        max_longitude = Math.max(longitude, max_longitude);
    }

    private double[][] listToWaypointCoordinates(List<WayPoint> waypoints) {
        double[][] result = new double[waypoints.size()][];
        for (int i = 0; i < waypoints.size(); i++) {
            double latitude = waypoints.get(i).getLatitude().doubleValue();
            double longitude = waypoints.get(i).getLongitude().doubleValue();
            result[i] = new double[]{latitude, longitude};
            setMinMaxValues(latitude, longitude);
        }
        return result;
    }

    private double[][][] listToRouteCoordinates(List<Route> routes) {
        double[][][] result = new double[routes.size()][][];
        for (int i = 0; i < routes.size(); i++) {
            result[i] = listToWaypointCoordinates(routes.get(i).getPoints());
        }
        return result;
    }

    private double[][][][] listToTrackCoordinates(List<Track> tracks) {
        double[][][][] result = new double[tracks.size()][][][];
        // double[tracks][segments][points][lat lon]
        for (int i = 0; i < tracks.size(); i++) {
            Track track = tracks.get(i);
            result[i] = new double[track.getSegments().size()][][];
            for (int j = 0; j < track.getSegments().size(); j++) {
                result[i][j] = listToWaypointCoordinates(track.getSegments().get(j).getPoints());
            }
        }
        return result;
    }
}
