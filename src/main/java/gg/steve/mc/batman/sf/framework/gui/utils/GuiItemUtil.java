package gg.steve.mc.batman.sf.framework.gui.utils;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class GuiItemUtil {

    public static ItemStack createItem(ConfigurationSection section) {
        return new ItemStack(Material.BARRIER);
    }
}
