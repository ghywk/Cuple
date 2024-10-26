package bh.dpl.mixin;

import bh.dpl.Cuple;
import bh.dpl.event.impl.MoveInputEvent;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author Kev1nLeft
 */

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends Input {
    @Shadow @Final private Options options;

    @Shadow
    private static float calculateImpulse(boolean bl, boolean bl2) {
        return 0;
    }

    /**
     * @author KevinLeft
     * @reason MoveInputEvent
     */
    @Overwrite
    public void tick(boolean p_108582_, float f) {
        this.up = this.options.keyUp.isDown();
        this.down = this.options.keyDown.isDown();
        this.left = this.options.keyLeft.isDown();
        this.right = this.options.keyRight.isDown();
        this.forwardImpulse = calculateImpulse(this.up, this.down);
        this.leftImpulse = calculateImpulse(this.left, this.right);

        MoveInputEvent eventMoveInput = new MoveInputEvent(forwardImpulse, leftImpulse);
        Cuple.eventBus.post(eventMoveInput);

        forwardImpulse = eventMoveInput.getForward();
        leftImpulse = eventMoveInput.getStrafe();

        this.jumping = this.options.keyJump.isDown();
        this.shiftKeyDown = this.options.keyShift.isDown();

        if (p_108582_) {
            this.leftImpulse *= f;
            this.forwardImpulse *= f;
        }
    }
}
