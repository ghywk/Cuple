package bh.dpl.mixin;

import bh.dpl.Cuple;
import bh.dpl.event.impl.AttackEvent;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Kev1nLeft
 */

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void onAttack(Player player, Entity entity, CallbackInfo ci) {
        AttackEvent attackEvent = new AttackEvent(player, entity);
        Cuple.eventBus.post(attackEvent);
        if (attackEvent.isCancelled()) ci.cancel();
    }
}
