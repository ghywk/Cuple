package bh.dpl.file.config;

import bh.dpl.Cuple;
import bh.dpl.file.config.impl.ModuleConfig;
import bh.dpl.utils.exception.MemberNotFoundException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    protected static final Minecraft mc = Minecraft.getInstance();
    public static final File rootDir = new File(mc.gameDirectory, Cuple.Information.Name.getDisplayInfo());
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final List<Config> configList;

    public ConfigManager() {
        configList = new ArrayList<>();
        if (!rootDir.exists()) rootDir.mkdir();
    }

    public Config getConfig(String name) {
        for (Config config : configList) {
            if (config.name.equals(name)) return config;
        }

        throw new MemberNotFoundException("Config not found: " + name);
    }

    public Config getConfig(Class<? extends Config> klass) {
        for (Config config : configList) {
            if (config.getClass() == klass) return config;
        }

        throw new MemberNotFoundException("Config not found: " + klass.getName());
    }

    public void registerConfigs() {
        configList.add(new ModuleConfig());
    }

    public void loadConfigs() {
        configList.forEach(Config::loadConfig);
    }

    public void saveConfigs() {
        configList.forEach(it -> {
            if (!it.configFile.exists()) {
                try {
                    it.configFile.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        configList.forEach(Config::saveConfig);
    }
}
