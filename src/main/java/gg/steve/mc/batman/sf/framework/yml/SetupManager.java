package gg.steve.mc.batman.sf.framework.yml;

import gg.steve.mc.batman.sf.cmd.StarfallCmd;
import gg.steve.mc.batman.sf.drop.BlockManager;
import gg.steve.mc.batman.sf.drop.RadiusCache;
import gg.steve.mc.batman.sf.drop.SpawnBlockUtil;
import gg.steve.mc.batman.sf.framework.yml.utils.FileManagerUtil;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Class that handles setting up the plugin on start
 */
public class SetupManager {
    private static FileManagerUtil fileManager;

    private SetupManager() throws IllegalAccessException {
        throw new IllegalAccessException("Manager class cannot be instantiated.");
    }

    /**
     * Loads the files into the file manager
     */
    public static void setupFiles(FileManagerUtil fm) {
        fileManager = fm;
        Files.CONFIG.load(fm);
        Files.PERMISSIONS.load(fm);
        Files.DEBUG.load(fm);
        Files.MESSAGES.load(fm);
        Files.RADIUS_DATA.load(fm);
        Files.BLOCK_DATA.load(fm);
    }

    public static void registerCommands(JavaPlugin instance) {
        instance.getCommand("starfall").setExecutor(new StarfallCmd());
    }

    /**
     * Register all of the events for the plugin
     *
     * @param instance Plugin, the main plugin instance
     */
    public static void registerEvents(JavaPlugin instance) {
        PluginManager pm = instance.getServer().getPluginManager();
        pm.registerEvents(new SpawnBlockUtil(), instance);
    }

    public static void registerEvent(JavaPlugin instance, Listener listener) {
        instance.getServer().getPluginManager().registerEvents(listener, instance);
    }

    public static void loadPluginCache() {
        RadiusCache.loadRadiusData();
        BlockManager.initialise();
    }

    public static void shutdownPluginCache() {
        BlockManager.shutdown();
    }

    public static FileManagerUtil getFileManager() {
        return fileManager;
    }
}
