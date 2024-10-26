package bh.dpl.module;

import bh.dpl.Cuple;
import bh.dpl.value.Value;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KevinLeft
 */
public class Module {
    protected Minecraft mc = Minecraft.getInstance();

    public final String name;
    public boolean enable;
    public int key;
    public Category category;
    public String suffix = null;
    public List<Value<?>> valueList = new ArrayList<>();

    public Module(String name, int key, Category category) {
        this.name = name;
        this.key = key;
        this.category = category;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        if(enable){
            Cuple.eventBus.register(this);
            onEnable();
        } else {
            Cuple.eventBus.unregister(this);
            onDisable();
        }
    }

    public void toggle(){
        this.enable = !this.enable;
        if(enable){
            Cuple.eventBus.register(this);
            onEnable();
        } else {
            Cuple.eventBus.unregister(this);
            onDisable();
        }
    }

    //Overrides

    public void onEnable(){

    }

    public void onDisable(){

    }

    public Value getValue(String name) {
        for (Value<?> value : valueList) {
            if (value.getName().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }

    public boolean isEnable() {
        return enable;
    }

    public Category getCategory() {
        return category;
    }

    public List<Value<?>> getValueList() {
        return valueList;
    }

    public int getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
