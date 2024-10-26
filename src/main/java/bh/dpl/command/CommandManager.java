package bh.dpl.command;

import bh.dpl.command.impl.Bind;
import bh.dpl.command.impl.Help;
import bh.dpl.command.impl.Toggle;
import bh.dpl.command.impl.Value;

import java.util.*;

/**
 * @author KevinLeft
 * @since 2024 - Aug - 12
 */
public class CommandManager {
    private final Map<String[], Command> commandMap = new HashMap<>();

    public CommandManager(){

    }

    public void load(){
        // Register
        Help help = new Help();
        Toggle toggle = new Toggle();
        Bind bind = new Bind();
        Value value = new Value();
        commandMap.put(help.getKey(), help);
        commandMap.put(toggle.getKey(), toggle);
        commandMap.put(bind.getKey(), bind);
        commandMap.put(value.key, value);

        // Finish
        System.out.println("Command Loaded");
    }

    public boolean runCommand(String message){
        int i = message.charAt(0);

        if ('.' == i) {
            String subString = message.substring(1);
            String[] s = subString.split(" ");
            String key = s[0];
            Command command = getCommand(key);
            if (command != null) {
                List<String> args = new ArrayList<>();
                Collections.addAll(args, s);
                args.remove(0);

                command.runCommand(args.toArray(new String[0]));
            }

            return true;
        }

        return false;
    }

    public Command getCommand(String key){
        for (Map.Entry<String[], Command> commandEntry : commandMap.entrySet()) {
            for (String k : commandEntry.getKey()) {
                if (k.equals(key)) {
                    return commandEntry.getValue();
                }
            }
        }
        return null;
    }
}
