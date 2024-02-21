package com.editorGPX.GPXfilesEditor.services;

import io.jenetics.jpx.*;
import org.w3c.dom.Document;

public class printGPXservice {
    private int stringLevel = 0;

    public void GPXtoConsole(GPX gpx) throws Exception {
        if (gpx == null) {
            throw new Exception("Wrong GPX file");
        }
        System.out.println("\n Your GPX file:");
        System.out.println("Version: " + gpx.getVersion());
        System.out.println("Creator: " + gpx.getCreator());
        printByTag(gpx.getMetadata().isPresent() ? gpx.getMetadata().get() : null);
            if (!gpx.getWayPoints().isEmpty()) {
                for (int i = 0; i < gpx.getWayPoints().size(); i++) {
                    System.out.println(countStrLineLevel(stringLevel) + (i + 1) + ") Waypoint:");
                    printByTag(gpx.getWayPoints().get(i));
                }
            }
            if (!gpx.getRoutes().isEmpty()) {
                for (int i = 0; i < gpx.getRoutes().size(); i++) {
                    printByTag(gpx.getRoutes().get(i));
                }
            }
            if (!gpx.getTracks().isEmpty()) {
                for (int i = 0; i < gpx.getTracks().size(); i++) {
                    printByTag(gpx.getTracks().get(i));
                }
            }
//        }
        if (gpx.getExtensions().isPresent()) {
            System.out.println("Extensions: " + gpx.getExtensions().get());
        }
    }

    // переделать
    // добавлять порядковые номера, в других функциях потом сбрасывать stringLevel
    private String countStrLineLevel(int level) {
        String lineLevel = "";
        while (level != 0) {
            lineLevel += " ";
            level--;
        }
        return lineLevel;
    }

    private void printByTag(Metadata md) {
        if (md != null) {
            String lineLevel = countStrLineLevel(stringLevel);
            System.out.println(lineLevel + "Metadata:");
            lineLevel = countStrLineLevel(stringLevel + 1);
            System.out.print((md.getName().isPresent() ? lineLevel + "Name: " + md.getName().get() + "\n" : "") +
                    (md.getDescription().isPresent() ? lineLevel + "Description: " + md.getDescription().get() + "\n" : "") +
                    (md.getAuthor().isPresent() ? lineLevel + "Author: " + md.getAuthor().get() + "\n" : "") +
                    (md.getCopyright().isPresent() ? lineLevel + "Copyright: " + md.getCopyright().get() + "\n" : "") +
                    (!md.getLinks().isEmpty() ? lineLevel + "Links: " + md.getLinks() + "\n" : "") +
                    (md.getTime().isPresent() ? lineLevel + "Time: " + md.getTime().get() + "\n" : "") +
                    (md.getKeywords().isPresent() ? lineLevel + "Keywords: " + md.getKeywords().get() + "\n" : "") +
                    (md.getBounds().isPresent() ? lineLevel + "Bounds: " + md.getBounds().get() + "\n" : ""));
//        printIfExist(lineLevel + "Name: ", metadata.getName().isPresent() ? metadata.getName().get() : false);
//        printIfExist(lineLevel + "Description: ", metadata.getDescription().isPresent() ? metadata.getDescription().get() : false);
//        printIfExist(lineLevel + "Author: ", metadata.getAuthor().isPresent() ? metadata.getAuthor().get() : false);
//        printIfExist(lineLevel + "Copyright: ", metadata.getCopyright().isPresent() ? metadata.getCopyright().get() : false);
//
//        // протестить как выводится links и сделать для них вывод
//        printIfExist(lineLevel + "Links: ", !metadata.getLinks().isEmpty() ? metadata.getLinks() : false);
//        // сделать вывод links
//
//        printIfExist(lineLevel + "Time: ", metadata.getTime().isPresent() ? metadata.getTime().get() : false);
//        printIfExist(lineLevel + "Keywords: ", metadata.getKeywords().isPresent() ? metadata.getKeywords().get() : false);
//        printIfExist(lineLevel + "Bounds: ", metadata.getBounds().isPresent() ? metadata.getBounds().get() : false);
            printByTag(md.getExtensions().isPresent() ? md.getExtensions().get() : null);
        }
    }

    private void printByTag(WayPoint wp) {
        stringLevel++;
        String lineLevel = countStrLineLevel(stringLevel);
        System.out.println(lineLevel + "Latitude: " + wp.getLatitude() + "\n" +
                lineLevel + "Longitude: " + wp.getLongitude());
        System.out.print(
                (wp.getElevation().isPresent() ? lineLevel + "Elevation: " + wp.getElevation().get() + "\n" : "") +
                (wp.getTime().isPresent() ? lineLevel + "Time: " + wp.getTime().get() + "\n" : "") +

                // проверить
                (wp.getMagneticVariation().isPresent() ? lineLevel + "Magnetic Variation" + wp.getMagneticVariation().get() + "\n" : "") +

                (wp.getGeoidHeight().isPresent() ? lineLevel + "Geoid Height: " + wp.getGeoidHeight().get() + "\n" : "") +
                (wp.getName().isPresent() ? lineLevel + "Name: " + wp.getName().get() + "\n" : "") +
                (wp.getComment().isPresent() ? lineLevel + "Comment: " + wp.getComment().get() + "\n" : "") +
                (wp.getDescription().isPresent() ? lineLevel + "Description: " + wp.getDescription().get() + "\n" : "") +
                (wp.getSource().isPresent() ? lineLevel + "Src: " + wp.getSource().get() + "\n" : "") +

                (!wp.getLinks().isEmpty() ? lineLevel + "Links: " + wp.getLinks() + "\n" : "") + // проверить

                (wp.getSymbol().isPresent() ? lineLevel + "Symbol: " + wp.getSymbol().get() + "\n" : "") +
                (wp.getType().isPresent() ? lineLevel + "Type: " + wp.getType().get() + "\n" : "") +
                (wp.getSat().isPresent() ? lineLevel + "Sat: " + wp.getSat().get() + "\n" : "") +
                (wp.getHdop().isPresent() ? lineLevel + "Hdop: " + wp.getHdop().get() + "\n" : "") +
                (wp.getVdop().isPresent() ? lineLevel + "Vdop: " + wp.getVdop().get() + "\n" : "") +
                (wp.getPdop().isPresent() ? lineLevel + "Pdop: " + wp.getPdop().get() + "\n" : "") +
                (wp.getAgeOfGPSData().isPresent() ? lineLevel + "Age of GPS data: " + wp.getAgeOfGPSData().get() + "\n" : "") +
                (wp.getDGPSID().isPresent() ? lineLevel + "DGPSID: " + wp.getDGPSID().get() + "\n" : ""));
        printByTag(wp.getExtensions().isPresent() ? wp.getExtensions().get() : null);
        stringLevel--;
    }

    private void printByTag(Route rt) {
        //

        printByTag(rt.getExtensions().isPresent() ? rt.getExtensions().get() : null);
    }

    private void printByTag(Track trk) {
        //

        printByTag(trk.getExtensions().isPresent() ? trk.getExtensions().get() : null);
    }

    private void printByTag(TrackSegment seg) {
        //

        printByTag(seg.getExtensions().isPresent() ? seg.getExtensions().get() : null);
    }

    private void printByTag(Document extensions) {
        if (extensions != null) {
            stringLevel++;
            String lineLevel = countStrLineLevel(stringLevel);
            System.out.println(lineLevel + "Extensions: ");
            lineLevel = countStrLineLevel(stringLevel + 1);

//            System.out.println();

            stringLevel--;
        }
    }
}
