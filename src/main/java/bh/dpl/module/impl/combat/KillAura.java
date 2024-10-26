package bh.dpl.module.impl.combat;

import bh.dpl.event.api.SubscribeEvent;
import bh.dpl.event.impl.*;
import bh.dpl.event.type.EventPriority;
import bh.dpl.module.Category;
import bh.dpl.module.Module;
import bh.dpl.utils.options.IteratorUtils;
import bh.dpl.utils.options.MathUtils;
import bh.dpl.utils.options.TimeUtils;
import bh.dpl.value.impl.BoolValue;
import bh.dpl.value.impl.ModeValue;
import bh.dpl.value.impl.NumberValue;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

/**
 * @author Kev1nLeft
 */

public class KillAura extends Module {
    public NumberValue range = new NumberValue("Range", 3, 3, 6);
    public NumberValue scanRange = new NumberValue("ScanRange", 5, 3, 8);

    public NumberValue maxCPS = new NumberValue("MaxCPS", 15 , 1 , 20);
    public NumberValue minCPS = new NumberValue("MinCPS", 5 , 1 , 20);

    public BoolValue ab = new BoolValue("AutoBlock", false);
    public ModeValue<abMode> abModeMode = new ModeValue<>("AutoBlockMode", abMode.values(), abMode.UseItem, () -> ab.getValue());

    public BoolValue strafeFix = new BoolValue("MovementFix", true);

    @Nullable
    public LivingEntity target;
    private final TimeUtils attackTimer = new TimeUtils();
    private final TimeUtils abTimer = new TimeUtils();

    private boolean needUpdate;
    private float[] angle;

    int yawEX = 0,pitchEX = 0;

    public KillAura() {
        super("KillAura", -1, Category.Combat);
        Collections.addAll(valueList, range, scanRange, maxCPS, minCPS, strafeFix, ab, abModeMode);
    }

    @SubscribeEvent
    public void onMotion(MotionEvent event){
        if (!this.isEnable()){
            return;
        }

        yawEX = MathUtils.getRandomInRange(-5, 5);
        pitchEX = MathUtils.getRandomInRange(-5, 5);

        if (!strafeFix.value){
            if (angle != null){
                event.setYaw(angle[0] + yawEX);
                event.setPitch(angle[1] + pitchEX);
                angle = null;
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event){
        if (mc.player == null || mc.level == null || mc.gameMode == null) return;

        if (minCPS.value > maxCPS.value){
            minCPS.value = maxCPS.value;
        }

        if (!IteratorUtils.isEmpty(mc.level.entitiesForRendering())) {
            for (Entity entity : mc.level.entitiesForRendering()) {
                if (entity instanceof LivingEntity && !(entity instanceof ArmorStand) && getDistanceToEntity(entity, mc.player) < scanRange.value.floatValue() && entity.getId() != mc.player.getId()){
                    target = (LivingEntity) entity;
                    break;
                }
            }
        }

        if (target != null && getDistanceToEntity(target, mc.player) > scanRange.value) {
            target = null;
        }

        if (target == null) return;

        double toX = target.getX() - mc.player.getX();
        double toY = target.getY() - mc.player.getY();
        double toZ = target.getZ() - mc.player.getZ();

        double distance = Math.sqrt(toX * toX + toZ * toZ);
        float yaw = (float) (Math.toDegrees(Math.atan2(toZ, toX)) - 90f);
        float pitch = (float) -Math.toDegrees(Math.atan2(toY, distance));

        this.setAngle(angle = new float[]{yaw + yawEX, pitch + pitchEX});

        if (this.attackTimer.hasReached(1000.0 / (MathUtils.getRandomInRange(minCPS.value, maxCPS.value) * 1.5)) && getDistanceToEntity(target, mc.player) < range.value){
            mc.gameMode.attack(mc.player, target);
            mc.player.swing(InteractionHand.MAIN_HAND);
            attackTimer.reset();

            if (ab.getValue()) {
                switch (abModeMode.getValue()) {
                    case UseItem: {
                        mc.gameMode.useItem(mc.player,InteractionHand.MAIN_HAND);
                        break;
                    }
                    case RightClick: {
                        mc.options.keyUse.setDown(true);
                        mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
                        mc.options.keyUse.setDown(false);
                    }
                }
            }
        }

        if (target != null && (target.isDeadOrDying() || target.getHealth() <= 0F) && mc.player != null ){
            target = null;
        }
    }

    @Override
    public void onDisable(){
        target = null;
        if (mc.player != null) {
            angle = new float[]{mc.player.getYRot(),mc.player.getXRot()};
        }
    }

    @Override
    public void onEnable() {
        abTimer.reset();
        attackTimer.reset();
    }

    /**
     * Alan34 Moment,
     * LOL
     */
    @SubscribeEvent
    public void onMoveInput(MoveInputEvent event){
        if (!strafeFix.value || mc.player == null){
            return;
        }

        if (target == null) return;

        if (angle == null && !needUpdate) return;

        final float forward = event.getForward();
        final float strafe = event.getStrafe();

        final double yaw = Mth.wrapDegrees(Math.toDegrees(direction(mc.player.getYRot(), forward, strafe)));

        if (forward == 0 && strafe == 0) {
            return;
        }

        float closestForward = 0, closestStrafe = 0, closestDifference = Float.MAX_VALUE;

        for (float predictedForward = -1F; predictedForward <= 1F; predictedForward += 1F) {
            for (float predictedStrafe = -1F; predictedStrafe <= 1F; predictedStrafe += 1F) {
                if (predictedStrafe == 0 && predictedForward == 0) continue;

                final double predictedAngle = Mth.wrapDegrees(Math.toDegrees(direction(angle[0], predictedForward, predictedStrafe)));
                final double difference = Math.abs(yaw - predictedAngle);

                if (difference < closestDifference) {
                    closestDifference = (float) difference;
                    closestForward = predictedForward;
                    closestStrafe = predictedStrafe;
                }
            }
        }

        event.setForward(closestForward);
        event.setStrafe(closestStrafe);
    }

    @SubscribeEvent
    public void onStrafe(StrafeEvent event){
        if (target == null) return;

        if (!strafeFix.value){
            return;
        }

        if (needUpdate){
            event.setYaw(angle[0]);
        }
    }

    @SubscribeEvent (priority = EventPriority.LOW)
    public void onMotionUpdate(MotionEvent event){
        if (target == null) return;

        if (!strafeFix.value){
            return;
        }

        if (needUpdate){
            event.setYaw(angle[0]);
            event.setPitch(angle[1]);
            needUpdate = false;
        }
    }

    @SubscribeEvent
    public void onJump(JumpEvent event){
        if (target == null) return;

        if (!strafeFix.value){
            return;
        }

        if (needUpdate){
            event.setYaw(angle[0]);
        }
    }

    public void setAngle(float[] angle) {
        this.angle = angle;
        this.needUpdate = true;
    }

    public float getDistanceToEntity(Entity target, Entity entityIn) {
        Vec3 eyes = entityIn.getEyePosition(1.0f);
        Vec3 pos = getNearestPointBB(eyes, target.getBoundingBox());
        double xDist = Math.abs(pos.x - eyes.x);
        double yDist = Math.abs(pos.y - eyes.y);
        double zDist = Math.abs(pos.z - eyes.z);
        return (float)Math.sqrt(Math.pow(xDist, 2.0) + Math.pow(yDist, 2.0) + Math.pow(zDist, 2.0));
    }

    public Vec3 getNearestPointBB(final Vec3 eye, final AABB box) {
        final double[] origin = { eye.x, eye.y, eye.z };
        final double[] destMins = { box.minX, box.minY, box.minZ };
        final double[] destMaxs = { box.maxX, box.maxY, box.maxZ };
        for (int i = 0; i < 3; ++i) {
            if (origin[i] > destMaxs[i]) {
                origin[i] = destMaxs[i];
            }
            else if (origin[i] < destMins[i]) {
                origin[i] = destMins[i];
            }
        }
        return new Vec3(origin[0], origin[1], origin[2]);
    }

    public double direction(float rotationYaw, final double moveForward, final double moveStrafing) {
        if (moveForward < 0F) rotationYaw += 180F;

        float forward = 1F;

        if (moveForward < 0F) forward = -0.5F;
        else if (moveForward > 0F) forward = 0.5F;

        if (moveStrafing > 0F) rotationYaw -= 90F * forward;
        if (moveStrafing < 0F) rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    public enum abMode {
        RightClick,
        UseItem
    }
}
