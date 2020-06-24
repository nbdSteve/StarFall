package gg.steve.mc.batman.sf.drop;

import gg.steve.mc.batman.sf.framework.yml.Files;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

public class RadiusCache {
    private static YamlConfiguration conf;
    private static int radius, centerX, centerZ, spawnHeight;

    public static void loadRadiusData() {
        conf = Files.RADIUS_DATA.get();
        radius = conf.getInt("radius");
        centerX = conf.getInt("center.x");
        centerZ = conf.getInt("center.z");
        spawnHeight = Files.CONFIG.get().getInt("spawned-block.height");
    }

    public static Block getBlockInRadius(World world) {
        int x = Math.min(centerX - radius, centerX + radius) + (int) Math.round(-0.5f + (1 + Math.abs((centerX - radius) - (centerX + radius))) * Math.random());
        int z = Math.min(centerZ - radius, centerZ + radius) + (int) Math.round(-0.5f + (1 + Math.abs((centerZ - radius) - (centerZ + radius))) * Math.random());
        return world.getBlockAt(x, spawnHeight, z);
    }

    public static void setRadius(int radius, Location center) {
        conf.set("radius", radius);
        conf.set("center.x", center.getBlockX());
        conf.set("center.z", center.getBlockZ());
        Files.RADIUS_DATA.save();
        loadRadiusData();
    }
}