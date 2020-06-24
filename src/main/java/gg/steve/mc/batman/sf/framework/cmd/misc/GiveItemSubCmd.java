package gg.steve.mc.batman.sf.framework.cmd.misc;

import gg.steve.mc.batman.sf.core.StarfallItem;
import gg.steve.mc.batman.sf.framework.cmd.SubCommand;
import gg.steve.mc.batman.sf.framework.message.DebugMessage;
import gg.steve.mc.batman.sf.framework.permission.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveItemSubCmd extends SubCommand {

    public GiveItemSubCmd() {
        super("giveitem", 2, 3, false, PermissionNode.GIVE);
        addAlias("givei");
        addAlias("gi");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        // /starfall giveitem player amount
        int amount = 1;
        if (args.length == 3) {
            try {
                amount = Integer.parseInt(args[2]);
                if (amount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                DebugMessage.INVALID_AMOUNT.message(sender);
                return;
            }
        }
        Player target;
        try {
            target = Bukkit.getPlayer(args[1]);
        } catch (Exception e) {
            DebugMessage.PLAYER_NOT_ONLINE.message(sender);
            return;
        }
        if (!sender.equals(target)) {
            DebugMessage.GIVE_TOOL_GIVER.message(sender, target.getName(), String.valueOf(amount));
        }
        StarfallItem.giveItem(target, amount);
        DebugMessage.GIVE_ITEM_RECEIVER.message(target, args[2]);
    }
}
