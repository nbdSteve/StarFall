package gg.steve.mc.batman.sf;

import gg.steve.mc.batman.sf.framework.utils.LogUtil;
import gg.steve.mc.batman.sf.framework.yml.SetupManager;
import gg.steve.mc.batman.sf.framework.yml.utils.FileManagerUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class Starfall extends JavaPlugin {
    private static Starfall instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        LogUtil.setInstance(instance, true);
        SetupManager.setupFiles(new FileManagerUtil(instance));
        SetupManager.registerCommands(instance);
        SetupManager.registerEvents(instance);
        SetupManager.loadPluginCache();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SetupManager.shutdownPluginCache();
    }

    public static Starfall getInstance() {
        return instance;
    }
}
