package bh.dpl.mixin;

import bh.dpl.Cuple;
import bh.dpl.event.impl.JumpEvent;
import com.google.common.annotations.VisibleForTesting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author Kev1nLeft
 */

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow protected abstract float getJumpPower();

    @Shadow public abstract float getJumpBoostPower();

    public LivingEntityMixin() {
        super(null, null);
    }

    /**
     * @author KevinLeft
     * @reason JumpEvent
     */
    @VisibleForTesting
    @Overwrite
    public void jumpFromGround() {
        float angle = this.getYRot();

        if (Minecraft.getInstance().player != null && this.getId() == Minecraft.getInstance().player.getId()) {
            JumpEvent eventJump = new JumpEvent(angle);
            Cuple.eventBus.post(eventJump);
            angle = eventJump.getYaw();
        }

        double d0 = (double)this.getJumpPower() + this.getJumpBoostPower();
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x, d0, vec3.z);
        if (this.isSprinting()) {
            float f = angle * ((float)Math.PI / 180F);
            this.setDeltaMovement(this.getDeltaMovement().add((double)(-Mth.sin(f) * 0.2F), 0.0D, (double)(Mth.cos(f) * 0.2F)));
        }

        this.hasImpulse = true;
    }
}
