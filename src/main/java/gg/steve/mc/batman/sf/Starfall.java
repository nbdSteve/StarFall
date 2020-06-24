package gg.steve.mc.batman.sf;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import gg.steve.mc.batman.sf.framework.utils.LogUtil;
import gg.steve.mc.batman.sf.framework.SetupManager;
import gg.steve.mc.batman.sf.framework.yml.utils.FileManagerUtil;
import gg.steve.mc.batman.sf.papi.StarfallExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Starfall extends JavaPlugin {
    private static Starfall instance;
    private static ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        LogUtil.setInstance(instance, true);
        protocolManager = ProtocolLibrary.getProtocolManager();
        SetupManager.setupFiles(new FileManagerUtil(instance));
        SetupManager.registerCommands(instance);
        SetupManager.registerEvents(instance);
        SetupManager.loadPluginCache();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            LogUtil.warning("Unable to find the required plugin, PlaceholderAPI, please install it to use this plugin.");
            Bukkit.getPluginManager().disablePlugin(instance);
        } else {
            new StarfallExpansion(instance).register();
            LogUtil.info("PlaceholderAPI plugin found, hooking into it now...");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SetupManager.shutdownPluginCache();
    }

    public static Starfall getInstance() {
        return instance;
    }

    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }
}
