package gg.steve.mc.batman.sf.framework.utils;

import gg.steve.mc.batman.sf.framework.nbt.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sun.security.krb5.Config;

import java.util.*;

/**
 * Class that handles creating and item
 */
public class ItemBuilderUtil {
    private Material material;
    private Short dataValue;
    private ItemStack item;
    private ItemMeta itemMeta;
    private List<String> lore = new ArrayList<>();
    private Set<ItemFlag> flags = new HashSet<>();
    private List<String> placeholders = new ArrayList<>();
    private NBTItem nbtItem;

    public static ItemBuilderUtil getBuilderForMaterial(String material, String data) {
        if (material.startsWith("hdb")) {
            String[] parts = material.split("-");
            if (Bukkit.getPluginManager().getPlugin("HeadDatabase") != null) {
                try {
//                    return new ItemBuilderUtil(new HeadDatabaseAPI().getItemHead(parts[1]));
                } catch (NullPointerException e) {
                    try {
                        return new ItemBuilderUtil(new ItemStack(Material.valueOf("SKULL_ITEM")));
                    } catch (Exception e1) {
                        return new ItemBuilderUtil(new ItemStack(Material.valueOf("LEGACY_SKULL_ITEM")));
                    }
                }
            } else {
                LogUtil.warning("Tried to create a custom head but the plugin HeadDatabase is not installed, setting to default skull.");
                try {
                    return new ItemBuilderUtil(new ItemStack(Material.valueOf("SKULL_ITEM")));
                } catch (Exception e1) {
                    return new ItemBuilderUtil(new ItemStack(Material.valueOf("LEGACY_SKULL_ITEM")));
                }
            }
        }
        try {
            return new ItemBuilderUtil(material, data);
        } catch (Exception e) {
            return new ItemBuilderUtil("LEGACY_" + material, data);
        }
    }

    public ItemBuilderUtil(ItemStack item) {
        this.item = item;
        this.material = item.getType();
        this.dataValue = item.getDurability();
        this.itemMeta = item.getItemMeta();
        this.lore = item.getItemMeta().getLore();
        this.flags = item.getItemMeta().getItemFlags();
    }

    public ItemBuilderUtil(String material, String dataValue) {
        this.material = Material.getMaterial(material.toUpperCase());
        this.dataValue = Short.parseShort(dataValue);
        this.item = new ItemStack(this.material, 1, this.dataValue);
        this.itemMeta = item.getItemMeta();
    }

    public void addName(String name) {
        itemMeta.setDisplayName(ColorUtil.colorize(name));
        item.setItemMeta(itemMeta);
    }

    public void setLorePlaceholders(String... placeholder) {
        this.placeholders = Arrays.asList(placeholder);
    }

    public void addLore(List<String> lore, String... replacement) {
        if (this.lore == null) this.lore = new ArrayList<>();
        List<String> replacements = Arrays.asList(replacement);
        for (String line : lore) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), replacements.get(i));
            }
            this.lore.add(ColorUtil.colorize(line));
        }
        itemMeta.setLore(this.lore);
        item.setItemMeta(itemMeta);
    }

    public void addEnchantments(List<String> enchants) {
        for (String enchantment : enchants) {
            String[] enchantmentParts = enchantment.split(":");
            itemMeta.addEnchant(Enchantment.getByName(enchantmentParts[0].toUpperCase()),
                    Integer.parseInt(enchantmentParts[1]), true);
        }
        item.setItemMeta(itemMeta);
    }

    public void addItemFlags(List<String> itemFlags) {
        for (String flag : itemFlags) {
            itemMeta.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));
            this.flags.add(ItemFlag.valueOf(flag.toUpperCase()));
        }
        item.setItemMeta(itemMeta);
    }

    public void addGuiNBT(boolean unbreakable) {
        nbtItem = new NBTItem(item);
        if (unbreakable) {
            nbtItem.setBoolean("Unbreakable", true);
        }
        nbtItem.setBoolean("tools+.gui", true);
    }

    public void addItemNBT(YamlConfiguration config) {
        nbtItem = new NBTItem(item);
        if (config.getBoolean("unbreakable")) {
            nbtItem.setBoolean("Unbreakable", true);
        }
        nbtItem.setBoolean("starfall.item", true);
    }

    public void addPickaxeNBT(ConfigurationSection section, String toolId) {
        nbtItem = new NBTItem(item);
        if (section.getBoolean("unbreakable")) {
            nbtItem.setBoolean("Unbreakable", true);
        }
        nbtItem.setBoolean("starfall.pickaxe", true);
        nbtItem.setString("starfall.id", toolId);
    }

    public void setItemMeta(ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
    }

    public Material getMaterial() {
        return material;
    }

    public NBTItem getNbtItem() {
        return nbtItem;
    }

    public ItemStack getItem() {
        if (this.nbtItem != null) {
            return this.nbtItem.getItem();
        }
        return this.item;
    }

    public ItemMeta getItemMeta() {
        return itemMeta;
    }

    public List<String> getLore() {
        return lore;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return itemMeta.getEnchants();
    }

    public Set<ItemFlag> getFlags() {
        return flags;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
        this.itemMeta.setLore(lore);
    }
}