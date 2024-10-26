package bh.dpl.command.impl;

import bh.dpl.command.Command;
import bh.dpl.utils.options.ChatUtils;

/**
 * @author KevinLeft
 * @since 2024 - Aug - 12
 */
public class Help extends Command {
    public Help() {
        super(new String[]{"h","help"});
    }

    @Override
    public void runCommand(String[] args) {
        ChatUtils.sendMessageClientSide("toggle, bind, help");
    }
}
