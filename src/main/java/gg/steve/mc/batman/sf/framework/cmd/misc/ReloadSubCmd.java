package gg.steve.mc.batman.sf.framework.cmd.misc;

import gg.steve.mc.batman.sf.Starfall;
import gg.steve.mc.batman.sf.framework.cmd.SubCommand;
import gg.steve.mc.batman.sf.framework.message.GeneralMessage;
import gg.steve.mc.batman.sf.framework.permission.PermissionNode;
import gg.steve.mc.batman.sf.framework.yml.Files;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ReloadSubCmd extends SubCommand {

    public ReloadSubCmd() {
        super("reload", 1, 1, false, PermissionNode.RELOAD);
        addAlias("r");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Files.reload();
        Bukkit.getPluginManager().disablePlugin(Starfall.getInstance());
        Bukkit.getPluginManager().enablePlugin(Starfall.getInstance());
        GeneralMessage.RELOAD.message(sender);
    }
}
