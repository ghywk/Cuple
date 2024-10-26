package bh.dpl.module;

import bh.dpl.module.impl.combat.KillAura;
import bh.dpl.module.impl.combat.SuperKnockBack;
import bh.dpl.module.impl.movement.NoSlow;
import bh.dpl.module.impl.movement.Sprint;
import bh.dpl.module.impl.render.ClickGui;
import bh.dpl.module.impl.render.ModuleList;
import bh.dpl.module.impl.render.WaterMark;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : KevinLeft
 * The time is 2024 - Aug - 06
 */

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public Module getModuleByName(String name){
        for (Module module : modules) {
            if (name.equalsIgnoreCase(module.name)) {
                return module;
            }
        }
        return null;
    }

    public List<Module> getEnableModules(){
        return modules.stream().filter(Module::isEnable).collect(Collectors.toList());
    }

    public void onKey(int key){
        for (Module module : getModules()) {
            if (module.getKey() == key) {
                module.setEnable(!module.isEnable());
            }
        }
    }

    public void load(){
        modules.add(new ModuleList());
        modules.add(new Sprint());
        modules.add(new WaterMark());
        modules.add(new KillAura());
        modules.add(new ClickGui());
        modules.add(new SuperKnockBack());
        modules.add(new NoSlow());

        System.out.println("Module Loaded");
    }

    public List<Module> getModules() {
        return modules;
    }
}
