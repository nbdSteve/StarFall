package gg.steve.mc.batman.sf.papi;

import gg.steve.mc.batman.sf.drop.BlockManager;
import gg.steve.mc.batman.sf.framework.utils.TimeUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class StarfallExpansion extends PlaceholderExpansion {
    private JavaPlugin instance;

    public StarfallExpansion(JavaPlugin instance) {
        this.instance = instance;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return instance.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "starfall";
    }

    @Override
    public String getVersion() {
        return instance.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        TimeUtil cooldown = new TimeUtil(BlockManager.getMainStarfall().getCooldown());
        TimeUtil remaining = new TimeUtil(BlockManager.getMainStarfall().getRemaining());
        if (identifier.equalsIgnoreCase("cooldown_hours")) {
            return cooldown.getHours();
        }
        if (identifier.equalsIgnoreCase("cooldown_minutes")) {
            return cooldown.getMinutes();
        }
        if (identifier.equalsIgnoreCase("cooldown_seconds")) {
            return cooldown.getSeconds();
        }
        if (identifier.equalsIgnoreCase("remaining_hours")) {
            return remaining.getHours();
        }
        if (identifier.equalsIgnoreCase("remaining_minutes")) {
            return remaining.getMinutes();
        }
        if (identifier.equalsIgnoreCase("remaining_seconds")) {
            return remaining.getSeconds();
        }
        return "Wrong placeholder";
    }
}
