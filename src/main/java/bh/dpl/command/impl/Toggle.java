package bh.dpl.command.impl;

import bh.dpl.Cuple;
import bh.dpl.command.Command;
import bh.dpl.module.Module;
import bh.dpl.utils.options.ChatUtils;

/**
 * @author KevinLeft
 * @since 2024 - Aug - 12
 */
public class Toggle extends Command {
    public Toggle() {
        super(new String[]{"t","toggle"});
    }

    @Override
    public void runCommand(String[] args) {
        if (args.length == 1) {
            Module moduleByName = Cuple.moduleManager.getModuleByName(args[0]);
            if (moduleByName != null) {
                moduleByName.setEnable(!moduleByName.isEnable());
                String stringText = moduleByName.isEnable() ? " Enabled" : " Disabled";
                ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " " + moduleByName.getName() + stringText);
            } else {
                ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " Module : " + args[0] + " Not Found.");
            }
        } else {
            ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " Usage : .t <module>.");
        }
    }
}
