package gg.steve.mc.batman.sf.core;

import gg.steve.mc.batman.sf.framework.utils.ItemBuilderUtil;
import gg.steve.mc.batman.sf.framework.yml.Files;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StarfallTool {
    private String toolId;
    private ConfigurationSection section;
    private int duration;
    private ItemStack item;

    public StarfallTool(String toolId) {
        this.toolId = toolId;
        section = Files.PICKAXES.get().getConfigurationSection(toolId);
        this.duration = section.getInt("duration");
        ItemBuilderUtil builder = ItemBuilderUtil.getBuilderForMaterial(section.getString("material"), section.getString("data"));
        builder.addName(section.getString("name"));
        builder.addLore(section.getStringList("lore"));
        builder.addEnchantments(section.getStringList("enchantments"));
        builder.addItemFlags(section.getStringList("item-flags"));
        builder.addPickaxeNBT(section, toolId);
        item = builder.getItem();
    }

    public int getDuration() {
        return duration;
    }

    public boolean givePlayer(Player player, int amount) {
        if (player.getInventory().firstEmpty() == -1) return false;
        for (int i = 0; i < amount; i++) {
            player.getInventory().addItem(item);
        }
        return true;
    }
}
