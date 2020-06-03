package gg.steve.mc.batman.sf.framework.cmd.misc;

import gg.steve.mc.batman.sf.drop.RadiusCache;
import gg.steve.mc.batman.sf.framework.cmd.SubCommand;
import gg.steve.mc.batman.sf.framework.message.DebugMessage;
import gg.steve.mc.batman.sf.framework.permission.PermissionNode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

public class RadiusSubCmd extends SubCommand {

    public RadiusSubCmd() {
        super("radius", 2, 2, true, PermissionNode.RADIUS);
        addAlias("rad");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        // /starfall radius 100
        try {
            Integer.parseInt(args[1]);
        } catch (Exception e) {
            DebugMessage.INVALID_RADIUS.message(sender);
            return;
        }
        Location loc = getPlayer(sender).getLocation();
        RadiusCache.setRadius(Integer.parseInt(args[1]), loc);
        DebugMessage.RADIUS_SET.message(sender, String.valueOf(loc.getBlockX()), String.valueOf(loc.getBlockZ()), args[1]);
    }
}
