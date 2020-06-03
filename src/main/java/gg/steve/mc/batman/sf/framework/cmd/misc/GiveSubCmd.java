package gg.steve.mc.batman.sf.framework.cmd.misc;

import gg.steve.mc.batman.sf.framework.cmd.SubCommand;
import gg.steve.mc.batman.sf.framework.permission.PermissionNode;
import org.bukkit.command.CommandSender;

public class GiveSubCmd extends SubCommand {

    public GiveSubCmd() {
        super("give", 3, 4, false, PermissionNode.GIVE);
        addAlias("g");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        // /starfall give player name 4
    }
}
