package com.weirdduke.examples.routeguide;

import com.google.protobuf.util.JsonFormat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RouteGuideUtil {

    public static URL getDefaultFeaturesFile() {
        return RouteGuideServer.class.getResource("route_guide_db.json");
    }

    public static List<Feature> parseFeatures(URL file) throws IOException {
        try (InputStream input = file.openStream()) {
            try (Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
                FeatureDatabase.Builder database = FeatureDatabase.newBuilder();
                JsonFormat.parser().merge(reader, database);
                return database.getFeatureList();
            }
        }
    }
 }
