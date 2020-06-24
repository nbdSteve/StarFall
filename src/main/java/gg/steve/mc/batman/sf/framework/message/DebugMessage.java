package gg.steve.mc.batman.sf.framework.message;

import gg.steve.mc.batman.sf.framework.utils.ColorUtil;
import gg.steve.mc.batman.sf.framework.yml.Files;
import gg.steve.mc.batman.sf.framework.utils.actionbarapi.ActionBarAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public enum DebugMessage {
    INVALID_COMMAND("invalid-command"),
    INCORRECT_ARGS("incorrect-args"),
    INVALID_RADIUS("invalid-radius"),
    RADIUS_SET("radius-set", "{block-x}", "{block-z}", "{radius}"),
    INVALID_AMOUNT("invalid-amount"),
    PLAYER_NOT_ONLINE("player-not-online"),
    INVALID_TOOL("invalid-tool"),
    GIVE_ITEM_GIVER("give-item-giver", "{player}", "{amount}"),
    GIVE_ITEM_RECEIVER("give-item-receiver", "{amount}"),
    GIVE_TOOL_GIVER("give-tool-giver", "{player}", "{amount}", "{tool-id}"),
    GIVE_TOOL_RECEIVER("give-tool-receiver", "{amount}", "{tool-id}"),
    INSUFFICIENT_PERMISSION("insufficient-permission", "{node}");

    private String path;
    private boolean actionBar;
    private List<String> placeholders;

    DebugMessage(String path, String... placeholders) {
        this.path = path;
        this.placeholders = Arrays.asList(placeholders);
        this.actionBar = Files.MESSAGES.get().getBoolean(this.path + ".action-bar");
    }

    public void message(Player receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        if (this.actionBar) {
            for (String line : Files.DEBUG.get().getStringList(this.path + ".text")) {
                for (int i = 0; i < this.placeholders.size(); i++) {
                    line = line.replace(this.placeholders.get(i), data.get(i));
                }
                ActionBarAPI.sendActionBar(receiver, ColorUtil.colorize(line));
            }
        } else {
            for (String line : Files.DEBUG.get().getStringList(this.path + ".text")) {
                for (int i = 0; i < this.placeholders.size(); i++) {
                    line = line.replace(this.placeholders.get(i), data.get(i));
                }
                receiver.sendMessage(ColorUtil.colorize(line));
            }
        }
    }

    public void message(CommandSender receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        if (this.actionBar && receiver instanceof Player) {
            for (String line : Files.DEBUG.get().getStringList(this.path + ".text")) {
                for (int i = 0; i < this.placeholders.size(); i++) {
                    line = line.replaceAll(this.placeholders.get(i), data.get(i));
                }
                ActionBarAPI.sendActionBar((Player) receiver, ColorUtil.colorize(line));
            }
        } else {
            for (String line : Files.DEBUG.get().getStringList(this.path + ".text")) {
                for (int i = 0; i < this.placeholders.size(); i++) {
                    line = line.replace(this.placeholders.get(i), data.get(i));
                }
                receiver.sendMessage(ColorUtil.colorize(line));
            }
        }
    }
}
