package com.editorGPX.GPXfilesEditor.services;

import io.jenetics.jpx.*;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class GPXservice {
    private final static int TYPE_GPX = 1;
    private final static int TYPE_METADATA = 2;
    private final static int TYPE_WAYPOINT = 3;
    private final static int TYPE_ROUTE = 4;
    private final static int TYPE_ROUTE_POINT = 5;
    private final static int TYPE_TRACK = 6;
    private final static int TYPE_TRACK_SEGMENT = 7;
    private final static int TYPE_TRACK_SEG_POINT = 8;

    public void GPXtoConsole(GPX gpx) throws Exception {
        if (gpx == null) {
            throw new Exception("Wrong GPX file");
        }
        boolean hasNext = true;
        int level = 0;
        System.out.println("\n Your GPX file:");
        System.out.println("Version: " + gpx.getVersion());
        System.out.println("Creator: " + gpx.getCreator());
        if (gpx.getMetadata().isPresent()) {
            System.out.println("Metadata: ");
            printMetadata(gpx.getMetadata().get(), countStrLineLevel(++level));
        }
        while (hasNext) {
            hasNext = false;
            if (gpx.getWayPoints() != null) {
                level++;
            }
        }
        if (gpx.getExtensions().isPresent()) {
            System.out.println("Extensions: " + gpx.getExtensions().get());
        }
    }

    private String countStrLineLevel(int level) {
        String lineLevel = "";
        while (level != 0) {
            lineLevel += " ";
            level--;
        }
        return lineLevel;
    }

    private void printMetadata(Metadata metadata, String lineLevel) {
        printIfExist(lineLevel + "Name: ", metadata.getName().isPresent() ? metadata.getName().get() : false);
        printIfExist(lineLevel + "Description: ", metadata.getDescription().isPresent() ? metadata.getDescription().get() : false);
        printIfExist(lineLevel + "Author: ", metadata.getAuthor().isPresent() ? metadata.getAuthor().get() : false);
        printIfExist(lineLevel + "Copyright: ", metadata.getCopyright().isPresent() ? metadata.getCopyright().get() : false);

        // протестить как выводится links и сделать для них вывод
        printIfExist(lineLevel + "Links: ", !metadata.getLinks().isEmpty() ? metadata.getLinks() : false);
        // сделать вывод links

        printIfExist(lineLevel + "Time: ", metadata.getTime().isPresent() ? metadata.getTime().get() : false);
        printIfExist(lineLevel + "Keywords: ", metadata.getKeywords().isPresent() ? metadata.getKeywords().get() : false);
        printIfExist(lineLevel + "Bounds: ", metadata.getBounds().isPresent() ? metadata.getBounds().get() : false);
        if (metadata.getExtensions().isPresent()) {
            printExtensions(metadata.getExtensions().get(), lineLevel);
        }
    }

    private void printExtensions(Document extensions, String lineLevel) {
        System.out.println(lineLevel + "Extensions:");

    }

    //    private void printByTag(GPX gpx, Metadata metadata, String tag, int level) {
//        String levelLine = "";
//        while (level != 0) {
//            levelLine += " ";
//            level--;
//        }
//        System.out.println(levelLine + tag + ":");
//        switch (tag) {
//            case "Metadata":
//                printIfExist(metadata.getName().isPresent(), levelLine + "Name: ", metadata.getName());
//                printIfExist(metadata.getDescription().isPresent(), levelLine + "Description: ", metadata.getDescription());
//                printIfExist(metadata.getAuthor().isPresent(), levelLine + "Author: ", metadata.getAuthor());
//                printIfExist(metadata.getCopyright().isPresent(), levelLine + "Copyright: ", metadata.getCopyright());
//
//                printIfExist(!metadata.getLinks().isEmpty(), levelLine + "Links: ", metadata.getLinks());
//
//                printIfExist(metadata.getTime().isPresent(), levelLine + "Time: ", metadata.getTime());
//                printIfExist(metadata.getKeywords().isPresent(), levelLine + "Keywords: ", metadata.getKeywords());
//                printIfExist(metadata.getBounds().isPresent(), levelLine + "Bounds: ", metadata.getBounds());
//
//                printIfExist(metadata.getName().isPresent(), levelLine + "Name: ", metadata.getName());
//                break;
//            case "Extensions":
//                //
//                break;
//            case "":
//                //
//                break;
//            default:
//                System.out.println("Тег не может быть выведен");
//        }
//    }
    private void printIfExist(String tag, Object printedObject) {
        if (!printedObject.equals(false)) {
            System.out.println(tag + printedObject);
        }
    }

    public GPX insertExtensions(GPX gpx, int type, int globalIndex, int index, int trk_seg_wp_index) throws Exception {
        // для примера!!!!!!!!!!!!!!!!!!!
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element extElement = doc.createElement("extensions");
        Element requestElement = doc.createElement("ExampleExtensions");
        requestElement.appendChild(doc.createTextNode("It is content"));
        requestElement.setAttribute("id", "12374748");
        extElement.appendChild(requestElement);
        doc.appendChild(extElement);
        ///////////////////////////////////////////////////////////////////////////

        switch (type) {
            case TYPE_GPX -> gpx = gpx.toBuilder()
                    .extensions(doc)
                    .build();

            case TYPE_METADATA -> {
                Metadata md = gpx.getMetadata().isPresent() ? gpx.toBuilder().metadata().get() : null;
                if (md != null) {
                    gpx = gpx.toBuilder()
                            .metadata(md
                                    .toBuilder()
                                    .extensions(doc).build())
                            .build();
                } else {
                    throw new Exception("There is no metadata");
                }
            }

            case TYPE_WAYPOINT -> {
                if (gpx.getWayPoints().size() >= 1 && gpx.getWayPoints().size() > index) {
                    WayPoint wayPoint = gpx.getWayPoints().get(index);
                    gpx = gpx.toBuilder()
                            .wayPointFilter()
                            .map(wp -> {
                                if (wp.equals(wayPoint)) {
                                    wp = wp.toBuilder()
                                            .extensions(doc)
                                            .build();
                                }
                                return wp;
                            })
                            .build()
                            .build();
                }
            }

            case TYPE_ROUTE -> {
                if (gpx.getRoutes().size() >= 1 && gpx.getRoutes().size() > globalIndex) {
                    AtomicBoolean done = new AtomicBoolean(false);
                    Route rte = gpx.getRoutes().get(globalIndex);
                    try {
                        return gpx.toBuilder()
                                .routeFilter()
                                .map(route -> {
                                    if (route.equals(rte) && !done.get()) {
                                        route = route.toBuilder()
                                                .extensions(doc)
                                                .build();
                                        done.set(true);
                                    }
                                    return route;
                                })
                                .build()
                                .build();
                    } catch (Exception e) {
                        // сделать свои ошибки //////////////////////////////////////////////////
                        throw new Exception(e.getMessage());
                    }
                } else {
                    throw new Exception("Не нашлось нужного route");
                }
            }

            case TYPE_ROUTE_POINT -> {
                if (gpx.getRoutes().size() >= 1 && gpx.getRoutes().size() > globalIndex) {
                    AtomicBoolean done = new AtomicBoolean(false);
                    Route rte = gpx.getRoutes().get(globalIndex);
                    try {
                        if (rte.getPoints().size() >= 1 && rte.getPoints().size() > index) {
                            WayPoint wayPoint = rte.getPoints().get(index);
                            return gpx.toBuilder()
                                    .routeFilter()
                                    .map(route -> {
                                        if (route.equals(rte) && !done.get()) {
                                            route = route.toBuilder()
                                                    .map(wp -> {
                                                        if (wp.equals(wayPoint) && !done.get()) {
                                                            wp = wp.toBuilder()
                                                                    .extensions(doc)
                                                                    .build();
                                                            done.set(true);
                                                        }
                                                        return wp;
                                                    })
                                                    .build();
                                        }
                                        return route;
                                    })
                                    .build()
                                    .build();
                        } else {
                            throw new Exception("Не нашлось нужной точки");
                        }
                    } catch (Exception e) {
                        // сделать свои ошибки //////////////////////////////////////////////////
                        throw new Exception(e.getMessage());
                    }
                } else {
                    throw new Exception("Не нашлось нужного route");
                }
            }

            case TYPE_TRACK -> {
                if (gpx.getTracks().size() >= 1 && gpx.getTracks().size() > globalIndex) {
                    Track trk = gpx.getTracks().get(globalIndex);
                    AtomicBoolean done = new AtomicBoolean(false);
                    try {
                        return gpx.toBuilder()
                                .trackFilter()
                                .map(track -> {
                                    if (track.equals(trk) && !done.get()) {
                                        track = track.toBuilder()
                                                .extensions(doc)
                                                .build();
                                        done.set(true);
                                    }
                                    return track;
                                })
                                .build()
                                .build();
                    } catch (Exception e) {
                        // сделать свои ошибки //////////////////////////////////////////////////
                        throw new Exception(e.getMessage());
                    }
                } else {
                    throw new Exception("Не нашлось нужного track");
                }
            }

            case TYPE_TRACK_SEGMENT -> {
                if (gpx.getTracks().size() >= 1 && gpx.getTracks().size() > globalIndex) {
                    Track trk = gpx.getTracks().get(globalIndex);
                    AtomicBoolean done = new AtomicBoolean(false);
                    try {
                        if (trk.getSegments().size() >= 1 && trk.getSegments().size() > index) {
                            TrackSegment trk_seg = trk.getSegments().get(index);
                            return gpx.toBuilder()
                                    .trackFilter()
                                    .map(track -> {
                                        if (track.equals(trk) && !done.get()) {
                                            track = track.toBuilder()
                                                    .map(segment -> {
                                                        if (segment.equals(trk_seg) && !done.get()) {
                                                            segment = segment
                                                                    .toBuilder()
                                                                    .extensions(doc)
                                                                    .build();
                                                            done.set(true);
                                                        }
                                                        return segment;
                                                    })
                                                    .build();
                                        }
                                        return track;
                                    })
                                    .build()
                                    .build();
                        } else {
                            throw new Exception("Не нашлось нужного segment");
                        }
                    } catch (Exception e) {
                        // сделать свои ошибки //////////////////////////////////////////////////
                        throw new Exception(e.getMessage());
                    }
                } else {
                    throw new Exception("Не нашлось нужного track");
                }
            }

            case TYPE_TRACK_SEG_POINT -> {
                try {
                    if (gpx.getTracks().size() >= 1 && gpx.getTracks().size() > globalIndex) {
                        Track trk = gpx.getTracks().get(globalIndex);
                        AtomicBoolean done = new AtomicBoolean(false);
                        if (trk.getSegments().size() >= 1 && trk.getSegments().size() > index) {
                            TrackSegment trk_seg = trk.getSegments().get(index);
                            if (trk_seg.getPoints().size() >= 1 && trk_seg.getPoints().size() > trk_seg_wp_index) {
                                WayPoint pt = trk_seg.getPoints().get(trk_seg_wp_index);
                                return gpx.toBuilder()
                                        .trackFilter()
                                        .map(track -> {
                                            if (track.equals(trk) && !done.get()) {
                                                track = track.toBuilder()
                                                        .map(segment -> {
                                                            if (segment.equals(trk_seg) && !done.get()) {
                                                                segment = segment.toBuilder()
                                                                        .map(point -> {
                                                                            if (point.equals(pt) && !done.get()) {
                                                                                point = point.toBuilder()
                                                                                        .extensions(doc)
                                                                                        .build();
                                                                                done.set(true);
                                                                            }
                                                                            return point;
                                                                        })
                                                                        .build();
                                                            }
                                                            return segment;
                                                        })
                                                        .build();
                                            }
                                            return track;
                                        })
                                        .build()
                                        .build();
                            } else {
                                throw new Exception("Не нашлось нужной точки");
                            }
                        } else {
                            throw new Exception("Не нашлось нужного segment");
                        }
                    } else {
                        throw new Exception("Не нашлось нужного track");
                    }
                } catch (Exception e) {
                    // сделать свои ошибки //////////////////////////////////////////////////
                    throw new Exception(e.getMessage());
                }
            }
        }
        throw new Exception("Невозможно изменить файл таким способом");
    }
}

