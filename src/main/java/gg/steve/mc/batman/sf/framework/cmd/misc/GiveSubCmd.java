package gg.steve.mc.batman.sf.framework.cmd.misc;

import gg.steve.mc.batman.sf.core.StarfallItem;
import gg.steve.mc.batman.sf.core.StarfallTool;
import gg.steve.mc.batman.sf.core.StarfallToolManager;
import gg.steve.mc.batman.sf.framework.cmd.SubCommand;
import gg.steve.mc.batman.sf.framework.message.DebugMessage;
import gg.steve.mc.batman.sf.framework.permission.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveSubCmd extends SubCommand {

    public GiveSubCmd() {
        super("give", 3, 4, false, PermissionNode.GIVE);
        addAlias("g");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        // /starfall give player name 4
        Player target;
        try {
            target = Bukkit.getPlayer(args[1]);
        } catch (Exception e) {
            DebugMessage.PLAYER_NOT_ONLINE.message(sender);
            return;
        }
        int amount = 1;
        if (args.length == 4) {
            try {
                amount = Integer.parseInt(args[3]);
                if (amount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                DebugMessage.INVALID_AMOUNT.message(sender);
                return;
            }
        }
        StarfallTool tool;
        try {
            tool = StarfallToolManager.getTool(args[2]);
            if (tool == null) throw new NullPointerException();
        } catch (NullPointerException e) {
            DebugMessage.INVALID_TOOL.message(sender);
            return;
        }
        if (!sender.equals(target)) {
            DebugMessage.GIVE_TOOL_GIVER.message(sender, target.getName(), String.valueOf(amount), args[2]);
        }
        tool.givePlayer(target, amount);
        DebugMessage.GIVE_TOOL_RECEIVER.message(target, String.valueOf(amount), args[2]);
    }
}
