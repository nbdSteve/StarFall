package gg.steve.mc.batman.sf.cmd;

import gg.steve.mc.batman.sf.framework.cmd.SubCommandType;
import gg.steve.mc.batman.sf.framework.message.DebugMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StarfallCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!SubCommandType.HELP_CMD.getSub().isValidCommand(sender, args)) return true;
            SubCommandType.HELP_CMD.getSub().onCommand(sender, args);
            return true;
        }
        for (SubCommandType subCommand : SubCommandType.values()) {
            if (!subCommand.getSub().isExecutor(args[0])) continue;
            if (!subCommand.getSub().isValidCommand(sender, args)) break;
            subCommand.getSub().onCommand(sender, args);
            return true;
        }
        DebugMessage.INVALID_COMMAND.message(sender);
        return true;
    }
}
