package gg.steve.mc.batman.sf.core;

import gg.steve.mc.batman.sf.drop.SpawnBlockUtil;
import gg.steve.mc.batman.sf.framework.nbt.NBTItem;
import gg.steve.mc.batman.sf.framework.utils.ItemBuilderUtil;
import gg.steve.mc.batman.sf.framework.yml.Files;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class StarfallItem implements Listener {
    private static ItemStack item;

    public static void loadItem(YamlConfiguration config) {
        ConfigurationSection section = config.getConfigurationSection("item");
        ItemBuilderUtil builder = ItemBuilderUtil.getBuilderForMaterial(section.getString("material"), section.getString("data"));
        builder.addName(section.getString("name"));
        builder.addLore(section.getStringList("lore"));
        builder.addEnchantments(section.getStringList("enchantments"));
        builder.addItemFlags(section.getStringList("item-flags"));
        builder.addItemNBT(config);
        item = builder.getItem();
    }

    public static ItemStack getItem() {
        return item;
    }

    public static boolean giveItem(Player player, int amount) {
        if (player.getInventory().firstEmpty() == -1) return false;
        for (int i = 0; i < amount; i++) {
            player.getInventory().addItem(item);
        }
        return true;
    }

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        if (player.getItemInHand().getType() == Material.AIR) return;
        NBTItem item = new NBTItem(player.getItemInHand());
        if (!item.getBoolean("starfall.item")) return;
        Block block = event.getClickedBlock();
        if (Files.CONFIG.get().getStringList("blocked-worlds").contains(block.getWorld().getName())) {
            player.sendMessage("World blocked.");
            return;
        }
        event.setCancelled(true);
        if (player.getItemInHand().getAmount() > 1) {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        } else {
            player.setItemInHand(new ItemStack(Material.AIR));
        }
        player.updateInventory();
        SpawnBlockUtil.isSpawned(block.getWorld());
    }
}
