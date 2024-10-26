package bh.dpl.module.impl.render;

import bh.dpl.Cuple;
import bh.dpl.event.api.SubscribeEvent;
import bh.dpl.event.impl.Render2DEvent;
import bh.dpl.module.Category;
import bh.dpl.module.Module;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

import java.util.List;

/**
 * @author Kev1nLeft
 */

public class ModuleList extends Module {
    public ModuleList() {
        super("ArrayList", InputConstants.KEY_J, Category.Render);
    }

    @SubscribeEvent
    public void onRender2D(Render2DEvent event) {
        Window window = Minecraft.getInstance().getWindow();
        int guiScaledWidth = window.getGuiScaledWidth();

        List<Module> enableModules = Cuple.moduleManager.getEnableModules();
        Font font = Minecraft.getInstance().font;
        enableModules.sort(((o1, o2) -> font.width(o2.getName() + (o2.getSuffix() == null ? "" : ChatFormatting.DARK_GRAY + " [" + o2.getSuffix() + "]")) - font.width(o1.getName() + (o1.getSuffix() == null ? "" : ChatFormatting.DARK_GRAY + " [" + o1.getSuffix() + "]"))));

        int i = 0;
        int y = 27;
        for (Module enableModule : enableModules) {
            if (enableModule.getSuffix() == null){
                event.guiGraphics.drawString(font, enableModule.getName() + " ", guiScaledWidth - font.width(enableModule.getName() + " ") - 2, y, 0xFFFFFFFF, true);
            } else {
                event.guiGraphics.drawString(font, enableModule.getName() + ChatFormatting.DARK_GRAY + " [" + enableModule.getSuffix() + "] ", guiScaledWidth - font.width(enableModule.getName() + ChatFormatting.DARK_GRAY + " [" + enableModule.getSuffix() + "] ") - 2, y, 0xFFFFFFFF, true);
            }

            y += font.lineHeight + 2;
            i ++;
        }
    }
}
