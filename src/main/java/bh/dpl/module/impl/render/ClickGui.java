package bh.dpl.module.impl.render;

import bh.dpl.module.Category;
import bh.dpl.module.Module;
import bh.dpl.ui.clickgui.MainClickGui;
import com.mojang.blaze3d.platform.InputConstants;

/**
 * @author Kev1nLeft
 */

public class ClickGui extends Module {
    public ClickGui() {
        super("ClickGui", InputConstants.KEY_RSHIFT, Category.Render);
    }

    @Override
    public void onEnable() {
        if (mc.player == null) return;

        mc.execute(() -> { mc.setScreen(null); mc.setScreen(new MainClickGui()); });

        this.toggle();
    }
}
