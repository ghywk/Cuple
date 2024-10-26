package bh.dpl.command.impl;

import bh.dpl.Cuple;
import bh.dpl.command.Command;
import bh.dpl.module.Module;
import bh.dpl.utils.options.ChatUtils;
import bh.dpl.value.impl.ModeValue;
import bh.dpl.value.impl.NumberValue;

import java.util.Objects;

/**
 * @author KevinLeft
 * @since 2024 - Aug - 25
 */
public class Value extends Command {
    public Value() {
        super(new String[]{"v","val","value"});
    }

    @Override
    public void runCommand(String[] args){
        if (args.length == 3) {
            final Module module = Cuple.moduleManager.getModuleByName(args[0]);

            if (module == null){
                ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " " + args[0] + "Not Found.");
            }

            bh.dpl.value.Value value = Objects.requireNonNull(module).getValue(args[1]);

            if (value == null){
                ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " " + args[1] + " Not Found.");
            }

            if(value != null && value.value instanceof Double) {
                final double newValue = Double.parseDouble(args[2]);

                NumberValue valueN = (NumberValue) value;

                if (newValue > valueN.getMax() || newValue < valueN.getMin()) {
                    if (newValue > valueN.getMax()) {
                        ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " Sorry, Value Is Bigger Than The MaxValue! Now Set to MaxValue.");
                        valueN.value = valueN.getMax();
                    } else if (newValue < valueN.getMin()) {
                        ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " Sorry, Value Is Smaller Than The MinValue! Now Set to MinValue.");
                        valueN.value = valueN.getMin();
                    }
                } else {
                    value.value = newValue;
                    ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " " + args[1] + " Was Set To " + newValue + ".");
                }
            }
            else if (value != null && value.value instanceof Boolean){
                final boolean newValue = Boolean.parseBoolean(args[2]);
                value.value = newValue;
                ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " " + args[1] + " Was Set To " + newValue + ".");
            }
            else if (value != null && value instanceof ModeValue && value.value instanceof String && ((ModeValue) value).isValid(args[2])){
                final String newValue = args[2];
                ((ModeValue<?>) value).setMode(newValue);
                ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " " + args[1] + " Was Set To " + newValue + ".");
            }
            else if (value != null && value instanceof ModeValue && value.value instanceof String && !((ModeValue) value).isValid(args[2])){
                ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " Sorry, But " + args[1] + " Could Not Find!");
            }
        } else {
            ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " Usage : .val <module> <value_name> <new_value>.");
        }
    }
}
