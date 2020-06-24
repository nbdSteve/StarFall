package gg.steve.mc.batman.sf.core;

import gg.steve.mc.batman.sf.Starfall;
import gg.steve.mc.batman.sf.drop.BlockManager;
import gg.steve.mc.batman.sf.framework.utils.CommandUtil;
import gg.steve.mc.batman.sf.framework.yml.Files;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PlayerStarfallMiner {
    private Player player;
    private Block block;
    private StarfallTool tool;
    private int current;
    private boolean active;

    public PlayerStarfallMiner(Player player, Block block, StarfallTool tool) {
        this.player = player;
        this.block = block;
        this.tool = tool;
        this.current = 0;
        this.active = true;
    }

    public void increment() {
        current++;
        if (current >= tool.getDuration()) {
            CommandUtil.execute(Files.CONFIG.get().getStringList("loot.commands"), player);
            BlockManager.removeStarfall(block);
            this.active = false;
        }
    }

    public void reset(Block block, StarfallTool tool) {
        this.block = block;
        this.tool = tool;
        this.active = true;
        this.current = 0;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public StarfallTool getTool() {
        return tool;
    }

    public void setTool(StarfallTool tool) {
        this.tool = tool;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
