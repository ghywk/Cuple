package bh.dpl;

import bh.dpl.command.CommandManager;
import bh.dpl.event.EventBus;
import bh.dpl.file.config.ConfigManager;
import bh.dpl.module.ModuleManager;
import bh.dpl.utils.options.KeyMap;
import net.fabricmc.api.ModInitializer;
import net.minecraft.ChatFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Cuple implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("Cuple Client");
    public static final Cuple INSTANCE = new Cuple();

    public static EventBus eventBus;
    public static ModuleManager moduleManager;
    public static ConfigManager configManager;
    public static CommandManager commandManager;

    @Override
    public void onInitialize() {
        LOGGER.info("Client on loading...");

        // Event REG
        eventBus = new EventBus();

        // Module Manager
        moduleManager = new ModuleManager();

        // CFG
        configManager = new ConfigManager();

        // CMD
        commandManager = new CommandManager();

        // Module
        moduleManager.load();

        // CMD Load
        commandManager.load();

        // Load Config
        configManager.registerConfigs();
        configManager.loadConfigs();

        // Event
        eventBus.register(moduleManager);
        eventBus.register(commandManager);

        // KeyMap
        KeyMap.load();

        LOGGER.info("Client loaded.");
    }

    public void onShut() {
        LOGGER.info("Client on stopping...");

        // Save Config
        configManager.saveConfigs();

        LOGGER.info("Client stopped.");
    }

    public enum Information {
        Name("Cuple"),
        Version("0.1.0"),
        PreFix(ChatFormatting.GREEN + "Client " + ChatFormatting.DARK_GRAY + ">>" + ChatFormatting.WHITE),
        CommandPrefix(".");

        private final String displayInfo;

        Information(String displayInfo) {
            this.displayInfo = displayInfo;
        }

        public String getDisplayInfo() {
            return displayInfo;
        }
    }
}
