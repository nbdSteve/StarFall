package gg.steve.mc.batman.sf.drop;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import gg.steve.mc.batman.sf.Starfall;
import gg.steve.mc.batman.sf.framework.utils.ColorUtil;
import gg.steve.mc.batman.sf.framework.yml.Files;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.UUID;

public class StarfallBlock {
    private UUID starfallId;
    private int remaining, cooldown;
    private World world;
    private Block block;
    private Hologram hologram;

    public StarfallBlock(UUID starfallId, Block block) {
        this.starfallId = starfallId;
        this.remaining = Files.CONFIG.get().getInt("event-duration");
        this.world = block.getWorld();
        this.block = block;
        createHologram();
        this.cooldown = Files.CONFIG.get().getInt("event-cooldown");
        saveToFile();
    }

    public StarfallBlock(UUID starfallId) {
        this.starfallId = starfallId;
        this.remaining = Files.BLOCK_DATA.get().getInt(starfallId + ".remaining");
        this.world = Bukkit.getWorld(Files.BLOCK_DATA.get().getString(starfallId + ".world"));
        this.block = this.world.getBlockAt(Files.BLOCK_DATA.get().getInt(starfallId + ".block.x"),
                Files.BLOCK_DATA.get().getInt(starfallId + ".block.y"),
                Files.BLOCK_DATA.get().getInt(starfallId + ".block.z"));
        if (block.getType() != Material.AIR) {
            createHologram();
        }
        if (this.starfallId.equals(UUID.fromString(Files.CONFIG.get().getString("persistent-uuid")))) {
            this.cooldown = Files.BLOCK_DATA.get().getInt(UUID.fromString(Files.CONFIG.get().getString("persistent-uuid")) + ".cooldown");
        }
    }

    public void createHologram() {
        this.hologram = HologramsAPI.createHologram(Starfall.getInstance(), this.block.getLocation().add(0.5, Files.CONFIG.get().getDouble("hologram.mod-y"), 0.5));
        for (String line : Files.CONFIG.get().getStringList("hologram.text")) {
            this.hologram.appendTextLine(ColorUtil.colorize(line));
        }
    }

    public void purge() {
        this.hologram.delete();
        this.block.setType(Material.AIR);
        Files.BLOCK_DATA.get().set(String.valueOf(this.starfallId), null);
        Files.BLOCK_DATA.save();
    }

    public void saveToFile() {
        YamlConfiguration conf = Files.BLOCK_DATA.get();
        conf.set(this.starfallId + ".remaining", this.remaining);
        if (this.starfallId.equals(UUID.fromString(Files.CONFIG.get().getString("persistent-uuid")))) {
            conf.set(this.starfallId + ".cooldown", this.cooldown);
        }
        conf.set(this.starfallId + ".world", this.world.getName());
        conf.set(this.starfallId + ".block.x", this.getX());
        conf.set(this.starfallId + ".block.y", this.getY());
        conf.set(this.starfallId + ".block.z", this.getZ());
        Files.BLOCK_DATA.save();
    }

    public UUID getStarfallId() {
        return starfallId;
    }

    public int getRemaining() {
        return remaining;
    }

    public void decrementRemaining() {
        this.remaining--;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public void resetCooldown() {
        this.cooldown = Files.CONFIG.get().getInt("event-cooldown");
    }

    public int getCooldown() {
        return cooldown;
    }

    public void decrementCooldown() {
        this.cooldown--;
    }

    public World getWorld() {
        return world;
    }

    public Block getBlock() {
        return block;
    }

    public int getX() {
        return block.getX();
    }

    public int getY() {
        return block.getY();
    }

    public int getZ() {
        return block.getZ();
    }

    public Hologram getHologram() {
        return hologram;
    }
}
