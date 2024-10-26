package bh.dpl.module.impl.render;

import bh.dpl.Cuple;
import bh.dpl.event.api.SubscribeEvent;
import bh.dpl.event.impl.Render2DEvent;
import bh.dpl.module.Category;
import bh.dpl.module.Module;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;

import java.awt.*;

/**
 * @author Kev1nLeft
 */

public class WaterMark extends Module {
    public WaterMark() {
        super("WaterMark", -1, Category.Render);
    }

    @SubscribeEvent
    public void onRender2D(Render2DEvent event) {
        String text = ChatFormatting.RED + "C" + ChatFormatting.WHITE + "uple [" + mc.fpsString.substring(0, 2) + "FPS] [" + getPing(mc.player) + "MS]";
        int width = mc.font.width(text);


        event.guiGraphics.fill(3, 3, 9 + width, 7 + mc.font.lineHeight, mc.options.getBackgroundColor(Integer.MIN_VALUE));
        event.guiGraphics.drawString(mc.font, text ,8, 5, Color.WHITE.getRGB(), true);
        event.guiGraphics.fill(3, 3, 5, 7 + mc.font.lineHeight, Color.WHITE.getRGB());
    }

    public int getPing(LocalPlayer player) {
        if (player == null)
            return 0;

        PlayerInfo playerInfo = player.connection.getPlayerInfo(player.getUUID());

        if (playerInfo == null) {
            return 0;
        } else {
            return playerInfo.getLatency();
        }
    }
}
