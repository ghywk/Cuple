package bh.dpl.mixin;

import bh.dpl.Cuple;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Kev1nLeft
 */

@Mixin(value = Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "close", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/telemetry/ClientTelemetryManager;close()V"))
    public void onClose(CallbackInfo ci) {
        Cuple.INSTANCE.onShut();
    }
}
