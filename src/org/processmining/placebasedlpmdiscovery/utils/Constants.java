package org.processmining.placebasedlpmdiscovery.utils;

import java.awt.*;

public class Constants {

    public static class Visualization {

        public static class Colors {
            public static final Color DialogBackgroundColor = new Color(150, 150, 150);

            public static final Color TransparentColor = new Color(0f, 0f, 0f, 0f);
        }
    }

    public static class Grouping {

        public static class Config {
            public static final String CLUSTERING_ALG = "clustering_alg";
            public static final String TITLE = "title";

            public static final String DISTANCE_METHOD = "distance_method";

            public static final String DISTANCE_CONFIG = "distance_config";
        }
    }
}
