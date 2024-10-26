package bh.dpl.mixin;

import bh.dpl.Cuple;
import bh.dpl.event.impl.StrafeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author Kev1nLeft
 */

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    private static Vec3 getInputVector(Vec3 vec3, float f, float g) {
        return null;
    }

    @Shadow public abstract void setDeltaMovement(Vec3 vec3);

    @Shadow public abstract Vec3 getDeltaMovement();

    @Shadow public abstract float getYRot();

    @Shadow public abstract int getId();

    /**
     * @author KevinLeft
     * @reason StrafeEvent
     */
    @Overwrite
    public void moveRelative(float p_19921_, Vec3 p_19922_) {
        Vec3 vec3;

        if (Minecraft.getInstance().player != null && this.getId() == Minecraft.getInstance().player.getId()){
            StrafeEvent eventStrafe = new StrafeEvent(this.getYRot());
            Cuple.eventBus.post(eventStrafe);
            vec3 = getInputVector(p_19922_, p_19921_, eventStrafe.getYaw());
        } else {
            vec3 = getInputVector(p_19922_, p_19921_, this.getYRot());
        }
        this.setDeltaMovement(this.getDeltaMovement().add(vec3));
    }
}
