package gg.steve.mc.batman.sf.core;

import gg.steve.mc.batman.sf.framework.yml.Files;

import java.util.HashMap;
import java.util.Map;

public class StarfallToolManager {
    private static Map<String, StarfallTool> tools;

    public static void loadTools() {
        tools = new HashMap<>();
        for (String toolId : Files.PICKAXES.get().getKeys(false)) {
            tools.put(toolId, new StarfallTool(toolId));
        }
    }

    public static void shutdown() {
        if (tools != null && !tools.isEmpty()) tools.clear();
    }

    public static StarfallTool getTool(String toolId) {
        return tools.get(toolId);
    }
}
