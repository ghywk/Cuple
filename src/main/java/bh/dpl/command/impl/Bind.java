package bh.dpl.command.impl;


import bh.dpl.Cuple;
import bh.dpl.command.Command;
import bh.dpl.utils.options.ChatUtils;
import bh.dpl.utils.options.KeyMap;
import org.lwjgl.glfw.GLFW;

/**
 * @author KevinLeft
 * @since 2024 - Aug - 13
 */
public class Bind extends Command {
    public Bind() {
        super(new String[]{"b","bind"});
    }

    @Override
    public void runCommand(String[] args) {
        if (args.length == 2) {
            if (Cuple.moduleManager.getModuleByName(args[0]) != null) {
                char userInput = ' ';
                int keyCode = 0;

                if (args[1].equalsIgnoreCase("lshift") || args[1].equalsIgnoreCase("rshift") || args[1].equalsIgnoreCase("lctrl") || args[1].equalsIgnoreCase("rctrl")){
                    if (args[1].equalsIgnoreCase("lshift")){
                        Cuple.moduleManager.getModuleByName(args[0]).setKey(GLFW.GLFW_KEY_LEFT_SHIFT);
                        ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " " + Cuple.moduleManager.getModuleByName(args[0]).getName() + " Has Been Bound To " + args[1] + ".");
                    } else if (args[1].equalsIgnoreCase("rshift")){
                        Cuple.moduleManager.getModuleByName(args[0]).setKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
                        ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " " + Cuple.moduleManager.getModuleByName(args[0]).getName() + " Has Been Bound To " + args[1] + ".");
                    } else if (args[1].equalsIgnoreCase("lctrl")){
                        Cuple.moduleManager.getModuleByName(args[0]).setKey(GLFW.GLFW_KEY_LEFT_CONTROL);
                        ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " " + Cuple.moduleManager.getModuleByName(args[0]).getName() + " Has Been Bound To " + args[1] + ".");
                    } else if (args[1].equalsIgnoreCase("rctrl")){
                        Cuple.moduleManager.getModuleByName(args[0]).setKey(GLFW.GLFW_KEY_RIGHT_CONTROL);
                        ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " " + Cuple.moduleManager.getModuleByName(args[0]).getName() + " Has Been Bound To " + args[1] + ".");
                    }
                } else {
                    userInput = args[1].toLowerCase().charAt(0);
                    keyCode = KeyMap.keyMap.getOrDefault(userInput, -1);
                    if (keyCode != -1){
                        Cuple.moduleManager.getModuleByName(args[0]).setKey(keyCode);
                    } else {
                        ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " Key " + userInput + "Not Found?");
                    }
                    ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " " + Cuple.moduleManager.getModuleByName(args[0]).getName() + " Has Been Bound To " + args[1] + ".");
                }
            } else {
                ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " " + args[0] + " Not Found.");
            }
        } else {
            ChatUtils.sendMessageClientSide(Cuple.Information.PreFix.getDisplayInfo() + " Usage : .bind <module> <key>.");
        }

    }
}
