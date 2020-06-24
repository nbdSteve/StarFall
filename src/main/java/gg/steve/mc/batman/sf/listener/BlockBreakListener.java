package gg.steve.mc.batman.sf.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import gg.steve.mc.batman.sf.Starfall;
import gg.steve.mc.batman.sf.core.PlayerStarfallMiner;
import gg.steve.mc.batman.sf.core.StarfallTool;
import gg.steve.mc.batman.sf.core.StarfallToolManager;
import gg.steve.mc.batman.sf.drop.BlockManager;
import gg.steve.mc.batman.sf.framework.nbt.NBTItem;
import gg.steve.mc.batman.sf.framework.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlockBreakListener implements Listener {
    private static Map<UUID, PlayerStarfallMiner> playersMining;

    public BlockBreakListener() {
        playersMining = new HashMap<>();
        interceptPackets();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Starfall.getInstance(), () -> {
            for (PlayerStarfallMiner miners : playersMining.values()) {
                if (!miners.isActive()) continue;
                miners.increment();
            }
        }, 0L, 1L);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockBreak(BlockBreakEvent event) {
        if (BlockManager.isStarfallBlock(event.getBlock())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void damage(BlockDamageEvent event) {
        if (BlockManager.isStarfallBlock(event.getBlock())) event.setCancelled(true);
    }

    private void interceptPackets() {
        Starfall.getProtocolManager().addPacketListener(new PacketAdapter(Starfall.getInstance(), ListenerPriority.HIGHEST, PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                EnumWrappers.PlayerDigType digType = packet.getPlayerDigTypes().getValues().get(0);
                Block block = event.getPlayer().getTargetBlock(null, 10);
                if (BlockManager.isStarfallBlock(block)) {
                    if (getToolFromPlayer(event.getPlayer()) == null) return;
                    if (digType == EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK) {
                        if (!playersMining.containsKey(event.getPlayer().getUniqueId())) return;
                        playersMining.get(event.getPlayer().getUniqueId()).setActive(false);
                    } else if (digType == EnumWrappers.PlayerDigType.START_DESTROY_BLOCK) {
                        if (playersMining.containsKey(event.getPlayer().getUniqueId()) && playersMining.get(event.getPlayer().getUniqueId()).isActive()) return;
                        addStarfallMiner(event.getPlayer(), block, getToolFromPlayer(event.getPlayer()));
                    }
                }
            }
        });
    }

    private static void addStarfallMiner(Player player, Block block, StarfallTool tool) {
        if (playersMining.containsKey(player.getUniqueId())) {
            playersMining.get(player.getUniqueId()).reset(block, tool);
        } else {
            playersMining.put(player.getUniqueId(), new PlayerStarfallMiner(player, block, tool));
        }
    }

    private static StarfallTool getToolFromPlayer(Player player) {
        if (player.getItemInHand().getType() == Material.AIR) return null;
        NBTItem item = new NBTItem(player.getItemInHand());
        if (item.getString("starfall.id").equalsIgnoreCase("")) return null;
        return StarfallToolManager.getTool(item.getString("starfall.id"));
    }
}
