package bh.dpl.mixin;

import bh.dpl.Cuple;
import bh.dpl.event.impl.KeyInputEvent;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Kev1nLeft
 */

@Mixin(KeyboardHandler.class)
public class KeyBoardHandlerMixin {
    @Inject(method = "keyPress", at = @At("HEAD"))
    public void onPress(long p_90894_, int p_90895_, int p_90896_, int p_90897_, int p_90898_, CallbackInfo ci) {
        if (p_90897_ == GLFW.GLFW_PRESS && p_90895_ != GLFW.GLFW_KEY_UNKNOWN && !(Minecraft.getInstance().screen instanceof ChatScreen) && !Minecraft.getInstance().isPaused()) {
            Cuple.eventBus.post(new KeyInputEvent(p_90895_));
            Cuple.moduleManager.onKey(p_90895_);
        }
    }
}
