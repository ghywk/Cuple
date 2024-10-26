package bh.dpl.module.impl.movement;

import bh.dpl.event.api.SubscribeEvent;
import bh.dpl.event.impl.MotionEvent;
import bh.dpl.event.impl.SlowDownEvent;
import bh.dpl.event.type.EventType;
import bh.dpl.module.Category;
import bh.dpl.module.Module;
import bh.dpl.value.impl.ModeValue;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;

import java.util.Collections;

/**
 * @author Kev1nLeft
 */

public class NoSlow extends Module {
    public ModeValue<NoSlowMode> noSlowMode = new ModeValue<>("Mode", NoSlowMode.values(), NoSlowMode.Release);

    public NoSlow() {
        super("NoSlow", -1, Category.Movement);
        Collections.addAll(valueList, noSlowMode);
    }

    @SubscribeEvent
    public void onSlowDown(SlowDownEvent event) {
        if (mc.player != null && mc.level != null && mc.player.isUsingItem()) {
            event.cancel();
            if (mc.options.keyUp.isDown() && !mc.player.isInLiquid()) {
                mc.player.setSprinting(true);
            }
        }
    }

    @SubscribeEvent
    public void onMotion(MotionEvent event) {
        this.setSuffix(noSlowMode.getValue().name());

        if (noSlowMode.is(NoSlowMode.Switch.name())) {
            if (mc.player != null && event.getType().equals(EventType.PRE) && mc.player.isUsingItem()) {
                InteractionHand hand = mc.player.getUsedItemHand();

                if (hand.equals(InteractionHand.OFF_HAND)) {
                    int slot = mc.player.getInventory().selected;
                    mc.player.connection.send(new ServerboundSetCarriedItemPacket(slot % 8 + 1));
                    mc.player.connection.send(new ServerboundSetCarriedItemPacket(slot % 7 + 2));
                    mc.player.connection.send(new ServerboundSetCarriedItemPacket(slot));
                } else {
                    mc.player.connection.send(new ServerboundUseItemPacket(InteractionHand.OFF_HAND, 0, mc.player.getYRot(), mc.player.getXRot()));
                }
            }
        } else if (noSlowMode.is(NoSlowMode.Release.name())) {
            if (mc.player != null && event.getType().equals(EventType.PRE) && mc.player.isUsingItem()) {
                mc.player.connection.send(new ServerboundPlayerActionPacket
                        (ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM,
                        mc.player.blockPosition(),
                        Direction.UP)
                );
            }
        }
    }

    public enum NoSlowMode {
        Release,
        Switch
    }
}
