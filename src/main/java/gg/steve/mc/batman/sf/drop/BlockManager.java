package gg.steve.mc.batman.sf.drop;

import gg.steve.mc.batman.sf.Starfall;
import gg.steve.mc.batman.sf.framework.message.GeneralMessage;
import gg.steve.mc.batman.sf.framework.utils.TimeUtil;
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
                            if (Bukkit.getOnlinePlayers().size() < Files.CONFIG.get().getInt("min-player-limit")) {
                                activeStarfalls.get(starfallId).resetCooldown();
                                GeneralMessage.TOO_FEW_PLAYERS.doBroadcast();
                            } else {
                                if (waitingForMain) continue;
                                waitingForMain = true;
                                SpawnBlockUtil.isSpawned(Bukkit.getWorld(Files.CONFIG.get().getString("default-world")));
                            }
                        } else {
                            if (activeStarfalls.get(starfallId).getCooldown() == Files.CONFIG.get().getInt("event-cooldown")) {
                                GeneralMessage.STARFALL_END.doBroadcast();
                                activeStarfalls.get(starfallId).getBlock().setType(Material.AIR);
                            }
                            switch (activeStarfalls.get(starfallId).getCooldown()) {
                                case 3600:
                                case 1800:
                                case 60:
                                case 30:
                                case 10:
                                    TimeUtil time = new TimeUtil(activeStarfalls.get(starfallId).getCooldown());
                                    GeneralMessage.STARFALL_TIMER.doBroadcast(time.getHours(), time.getMinutes(), time.getSeconds());
                                    break;
                            }
                            activeStarfalls.get(starfallId).decrementCooldown();
                        }
                    } else {
                        GeneralMessage.STARFALL_END.doBroadcast();
                        activeStarfalls.get(starfallId).purge();
                        removed.add(starfallId);
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
            activeStarfalls.get(starfallId).getHologram().delete();
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

    public static void removeStarfall(Block block) {
        if (!isStarfallBlock(block)) return;
        StarfallBlock starfall = getStarfallFromBlock(block);
        if (starfall.getStarfallId().equals(UUID.fromString(Files.CONFIG.get().getString("persistent-uuid")))) {
            starfall.getHologram().delete();
            starfall.setRemaining(0);
            starfall.getBlock().setType(Material.AIR);
        } else {
            activeStarfalls.remove(starfall.getStarfallId());
            starfall.purge();
        }
    }

    public static void cancel() {
        StarfallBlock starfall = activeStarfalls.get(UUID.fromString(Files.CONFIG.get().getString("persistent-uuid")));
        if (starfall.getRemaining() <= 0) return;
        starfall.getHologram().delete();
        starfall.setRemaining(0);
        starfall.getBlock().setType(Material.AIR);
    }

    public static StarfallBlock getStarfallFromBlock(Block block) {
        if (activeStarfalls.isEmpty()) return null;
        for (StarfallBlock blocks : activeStarfalls.values()) {
            if (blocks.getBlock().equals(block)) return blocks;
        }
        return null;
    }

    public static StarfallBlock getMainStarfall() {
        return activeStarfalls.get(UUID.fromString(Files.CONFIG.get().getString("persistent-uuid")));
    }
}
