package bh.dpl.module.impl.movement;

import bh.dpl.event.api.SubscribeEvent;
import bh.dpl.event.impl.UpdateEvent;
import bh.dpl.module.Category;
import bh.dpl.module.Module;
import com.mojang.blaze3d.platform.InputConstants;

/**
 * @author Kev1nLeft
 */
 
public class Sprint extends Module {
    public Sprint() {
        super("Sprint", InputConstants.KEY_LCONTROL, Category.Movement);
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        mc.options.keySprint.setDown(true);
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.setSprinting(false);
            mc.options.keySprint.setDown(false);
        }
    }
}
