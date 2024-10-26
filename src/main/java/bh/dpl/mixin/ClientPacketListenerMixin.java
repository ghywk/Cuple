package bh.dpl.mixin;

import bh.dpl.Cuple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Kev1nLeft
 */

@Mixin(net.minecraft.client.multiplayer.ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "sendChat", at = @At(value = "HEAD"), cancellable = true)
    public void onChat(String string, CallbackInfo ci) {
        if (Cuple.commandManager.runCommand(string)) {
            ci.cancel();
        }
    }
}
