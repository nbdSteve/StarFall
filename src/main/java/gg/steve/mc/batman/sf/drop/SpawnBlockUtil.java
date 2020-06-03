package gg.steve.mc.batman.sf.drop;

import gg.steve.mc.batman.sf.framework.message.GeneralMessage;
import gg.steve.mc.batman.sf.framework.utils.LogUtil;
import gg.steve.mc.batman.sf.framework.yml.Files;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpawnBlockUtil implements Listener {
    private static List<UUID> fallingBlocks = new ArrayList<>();

    public static boolean isSpawned(World world) {
        Material material;
        try {
            material = Material.valueOf(Files.CONFIG.get().getString("spawned-block.material").toUpperCase());
        } catch (Exception e) {
            LogUtil.warning("The material for the Starfall block does not exist, setting it to default type: STONE.");
            material = Material.STONE;
        }
        int data = Files.CONFIG.get().getInt("spawned-block.data");
        Block block = RadiusCache.getBlockInRadius(world);
        FallingBlock falling = world.spawnFallingBlock(block.getLocation(), material, (byte) data);
        GeneralMessage.STARFALL_SPAWN.doBroadcast(String.valueOf(block.getX()), String.valueOf(block.getZ()));
        fallingBlocks.add(falling.getUniqueId());
        return true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void form(EntityChangeBlockEvent event) {
        if (!fallingBlocks.contains(event.getEntity().getUniqueId())) return;
        fallingBlocks.remove(event.getEntity().getUniqueId());
        BlockManager.addStarfall(event.getBlock());
    }
}
