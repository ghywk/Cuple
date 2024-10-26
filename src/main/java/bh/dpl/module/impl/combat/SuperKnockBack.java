package bh.dpl.module.impl.combat;

import bh.dpl.event.api.SubscribeEvent;
import bh.dpl.event.impl.MotionEvent;
import bh.dpl.module.Category;
import bh.dpl.module.Module;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;

/**
 * @author Kev1nLeft
 */

public class SuperKnockBack extends Module {
    public SuperKnockBack() {
        super("SuperKnockBack", -1, Category.Combat);
    }

    @SubscribeEvent
    public void onMotionUpdate(MotionEvent event) {
        if (mc.player == null) return;

        mc.player.connection.send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.START_SPRINTING));
        mc.player.connection.send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
        mc.player.connection.send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.START_SPRINTING));
        mc.player.connection.send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
    }
}
