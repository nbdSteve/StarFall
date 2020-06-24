package gg.steve.mc.batman.sf.framework.cmd.misc;

import gg.steve.mc.batman.sf.drop.BlockManager;
import gg.steve.mc.batman.sf.drop.SpawnBlockUtil;
import gg.steve.mc.batman.sf.framework.cmd.SubCommand;
import gg.steve.mc.batman.sf.framework.permission.PermissionNode;
import org.bukkit.command.CommandSender;

public class EndSubCmd extends SubCommand {

    public EndSubCmd() {
        super("end", 1, 1, false, PermissionNode.START);
        addAlias("e");
        addAlias("cancel");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        // /starfall end
        BlockManager.cancel();
    }
}
