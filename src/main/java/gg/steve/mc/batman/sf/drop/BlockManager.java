package gg.steve.mc.batman.sf.drop;

import gg.steve.mc.batman.sf.Starfall;
import gg.steve.mc.batman.sf.framework.yml.Files;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.*;

public class BlockManager {
    private static Map<UUID, StarfallBlock> activeStarfalls;
    private static boolean waitingForMain = false;

    public static void initialise() {
        activeStarfalls = new HashMap<>();
        for (String entry : Files.BLOCK_DATA.get().getKeys(false)) {
            UUID starfallId = UUID.fromString(entry);
            activeStarfalls.put(starfallId, new StarfallBlock(starfallId));
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Starfall.getInstance(), () -> {
            if (activeStarfalls.isEmpty()) return;
            List<UUID> removed = new ArrayList<>();
            for (UUID starfallId : activeStarfalls.keySet()) {
                if (activeStarfalls.get(starfallId).getRemaining() <= 0) {
                    if (starfallId.equals(UUID.fromString(Files.CONFIG.get().getString("persistent-uuid")))) {
                        if (activeStarfalls.get(starfallId).getCooldown() <= 0) {
                            if (waitingForMain) continue;
                            waitingForMain = true;
                            SpawnBlockUtil.isSpawned(Bukkit.getWorld(Files.CONFIG.get().getString("default-world")));
                        } else {
                            activeStarfalls.get(starfallId).decrementCooldown();
                        }
                    } else {
                        removed.add(starfallId);
                        activeStarfalls.get(starfallId).getBlock().setType(Material.AIR);
                        activeStarfalls.get(starfallId).purge();
                    }
                } else {
                    activeStarfalls.get(starfallId).decrementRemaining();
                }
            }
            for (UUID starfallId : removed) {
                activeStarfalls.remove(starfallId);
            }
        }, 0L, 20L);
    }

    public static void shutdown() {
        for (UUID starfallId : activeStarfalls.keySet()) {
            activeStarfalls.get(starfallId).saveToFile();
        }
        activeStarfalls.clear();
    }

    public static void addStarfall(Block block) {
        if (waitingForMain) {
            waitingForMain = false;
            UUID persistent = UUID.fromString(Files.CONFIG.get().getString("persistent-uuid"));
            activeStarfalls.put(persistent, new StarfallBlock(persistent, block));
        } else {
            UUID starfallId = UUID.randomUUID();
            activeStarfalls.put(starfallId, new StarfallBlock(starfallId, block));
        }
    }

    public static StarfallBlock getStarfall(UUID starfallId) {
        return activeStarfalls.get(starfallId);
    }

    public static boolean isStarfallBlock(Block block) {
        if (activeStarfalls.isEmpty()) return false;
        for (StarfallBlock blocks : activeStarfalls.values()) {
            if (blocks.getBlock().equals(block)) return true;
        }
        return false;
    }

    public static StarfallBlock getStarfallFromBlock(Block block) {
        if (activeStarfalls.isEmpty()) return null;
        for (StarfallBlock blocks : activeStarfalls.values()) {
            if (blocks.getBlock().equals(block)) return blocks;
        }
        return null;
    }
}
