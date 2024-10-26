package bh.dpl.file.config.impl;

import bh.dpl.Cuple;
import bh.dpl.module.Module;
import bh.dpl.file.config.Config;
import bh.dpl.file.config.ConfigManager;
import bh.dpl.value.Value;
import bh.dpl.value.impl.BoolValue;
import bh.dpl.value.impl.ModeValue;
import bh.dpl.value.impl.NumberValue;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class ModuleConfig extends Config {
    public ModuleConfig() {
        super("module", "Module.json");
    }

    @Override
    public void loadConfig() {
        if (!configFile.exists()) return;
        try {
            JsonObject config = ConfigManager.gson.fromJson(new FileReader(configFile), JsonObject.class);
            for (Module module : Cuple.moduleManager.getModules()) {
                if (!config.has(module.getName())) continue;
                JsonObject moduleObject = config.get(module.getName()).getAsJsonObject();

                if (moduleObject.has("enabled") && module.isEnable() != moduleObject.get("enabled").getAsBoolean()) module.toggle();
                if (moduleObject.has("key")) module.setKey(moduleObject.get("key").getAsInt());

                if (!moduleObject.has("value")) continue;
                JsonObject valueObject = moduleObject.get("value").getAsJsonObject();

                for (Value<?> value : module.getValueList()) {
                    if (!valueObject.has(value.getName())) continue;
                    JsonElement valueElement = valueObject.get(value.getName().toString());

                    if (value instanceof BoolValue bv) {
                        bv.setValue(valueElement.getAsBoolean());
                    } else if (value instanceof NumberValue nv) {
                        nv.setValue(valueElement.getAsDouble());
                    } else if (value instanceof ModeValue mv) {
                        mv.setMode(valueElement.getAsString());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Cuple.LOGGER.error("Failed to load {} config.", name);
        }
    }

    @Override
    public void saveConfig() {
        JsonObject config = new JsonObject();

        for (Module module : Cuple.moduleManager.getModules()) {
            JsonObject moduleObject = new JsonObject();
            moduleObject.addProperty("enabled", module.isEnable());
            moduleObject.addProperty("key", module.getKey());

            JsonObject valueObject = new JsonObject();

            for (Value<?> value : module.getValueList()) {
                if (value instanceof BoolValue bv) {
                    valueObject.addProperty(bv.getName(), bv.value);
                } else if (value instanceof NumberValue nv) {
                    valueObject.addProperty(nv.getName(),nv.value);
                } else if (value instanceof ModeValue mv) {
                    valueObject.addProperty(mv.getName(), mv.value.toString());
                }
            }
            moduleObject.add("value", valueObject);
            config.add(module.getName(), moduleObject);
        }

        try {
            PrintWriter pw = new PrintWriter(configFile);
            pw.write(ConfigManager.gson.toJson(config));
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            Cuple.LOGGER.error("Failed to write {} config.", name);
        }
    }
}
