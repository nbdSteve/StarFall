package gg.steve.mc.batman.sf.framework.cmd.misc;

import gg.steve.mc.batman.sf.drop.SpawnBlockUtil;
import gg.steve.mc.batman.sf.framework.cmd.SubCommand;
import gg.steve.mc.batman.sf.framework.permission.PermissionNode;
import gg.steve.mc.batman.sf.framework.yml.Files;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartSubCmd extends SubCommand {

    public StartSubCmd() {
        super("start", 1, 1, false, PermissionNode.START);
        addAlias("s");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        // /starfall start
        World world;
        if (!(sender instanceof Player)) {
            world = Bukkit.getWorld(Files.CONFIG.get().getString("default-world"));
        } else {
            world = getPlayer(sender).getWorld();
            if (Files.CONFIG.get().getStringList("blocked-worlds").contains(world.getName())) {
                getPlayer(sender).sendMessage("World blocked.");
                return;
            }
        }
        SpawnBlockUtil.isSpawned(world);
    }
}
