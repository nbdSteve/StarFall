package gg.steve.mc.batman.sf.framework.cmd;

import gg.steve.mc.batman.sf.framework.cmd.misc.*;

public enum SubCommandType {
    START_CMD(new StartSubCmd()),
    RADIUS_CMD(new RadiusSubCmd()),
    GIVE_CMD(new GiveSubCmd()),
    HELP_CMD(new HelpSubCmd()),
    GIVE_ITEM(new GiveItemSubCmd()),
    END_CMD(new EndSubCmd()),
    RELOAD_CMD(new ReloadSubCmd());

    private SubCommand sub;

    SubCommandType(SubCommand sub) {
        this.sub = sub;
    }

    public SubCommand getSub() {
        return sub;
    }
}
