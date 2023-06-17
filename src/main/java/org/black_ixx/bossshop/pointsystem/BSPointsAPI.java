package org.black_ixx.bossshop.pointsystem;

import java.util.LinkedHashMap;
import java.util.Map;

public class BSPointsAPI {
    private static final Map<String, BSPointsPlugin> interfaces = new LinkedHashMap<>();

    /**
     * Register a points plugin
     * @param points the points to register
     */
    public static void register(BSPointsPlugin points) {
        interfaces.put(points.getName(), points);
    }

    /**
     * Get a points plugin object
     * @param name name of points plugin
     * @return points plugin
     */
    public static BSPointsPlugin get(String name) {
        if (name != null) {
            BSPointsPlugin p = interfaces.get(name);
            if (p != null) {
                return p;
            }
            for (BSPointsPlugin api : interfaces.values()) {
                if (api.getName().equalsIgnoreCase(name)) {
                    return api;
                }
            }
            for (BSPointsPlugin api : interfaces.values()) {
                for (String nickname : api.getNicknames()) {
                    if (nickname.equalsIgnoreCase(name)) {
                        return api;
                    }
                }
            }
        }
        return null;
    }
}
