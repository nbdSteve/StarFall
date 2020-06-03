package gg.steve.mc.batman.sf.framework.yml;

import gg.steve.mc.batman.sf.framework.yml.utils.FileManagerUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public enum Files {
    // generic
    CONFIG("starfall.yml"),
    // permissions
    PERMISSIONS("permissions.yml"),
    // data
    RADIUS_DATA("data" + File.separator + "radius.yml"),
    BLOCK_DATA("data" + File.separator + "blocks.yml"),
    // lang
    DEBUG("lang" + File.separator + "debug.yml"),
    MESSAGES("lang" + File.separator + "messages.yml");

    private final String path;

    Files(String path) {
        this.path = path;
    }

    public void load(FileManagerUtil fileManager) {
        fileManager.add(name(), this.path);
    }

    public YamlConfiguration get() {
        return FileManagerUtil.get(name());
    }

    public void save() {
        FileManagerUtil.save(name());
    }

    public static void reload() {
        FileManagerUtil.reload();
    }
}
