package bh.dpl.mixin;

import bh.dpl.Cuple;
import bh.dpl.event.impl.MotionEvent;
import bh.dpl.event.impl.SlowDownEvent;
import bh.dpl.event.impl.UpdateEvent;
import bh.dpl.event.type.EventType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Kev1nLeft
 */

@Mixin(value = LocalPlayer.class, priority = 500)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    public LocalPlayerMixin() {
        super(null, null);
    }

    @Shadow protected abstract void sendIsSprintingIfNeeded();

    @Shadow public abstract boolean isShiftKeyDown();

    @Shadow private boolean wasShiftKeyDown;

    @Shadow @Final public ClientPacketListener connection;

    @Shadow protected abstract boolean isControlledCamera();

    @Shadow private double xLast;

    @Shadow private double yLast1;

    @Shadow private double zLast;

    @Shadow private float yRotLast;

    @Shadow private float xRotLast;

    @Shadow private int positionReminder;

    @Shadow private boolean lastOnGround;

    @Shadow private boolean autoJumpEnabled;

    @Shadow @Final protected Minecraft minecraft;

    @Shadow protected int sprintTriggerTime;

    @Shadow public Input input;

    @Shadow protected abstract void handleConfusionTransitionEffect(boolean bl);

    @Shadow public abstract Portal.Transition getActivePortalLocalTransition();

    @Shadow public abstract boolean isUsingItem();

    @Shadow public abstract boolean isMovingSlowly();

    @Shadow protected abstract boolean hasEnoughImpulseToStartSprinting();

    @Shadow private boolean crouching;

    @Shadow private int autoJumpTime;

    @Shadow protected abstract void moveTowardsClosestSpace(double d, double e);

    @Shadow protected abstract boolean canStartSprinting();

    @Shadow protected abstract boolean hasEnoughFoodToStartSprinting();

    @Shadow private boolean wasFallFlying;

    @Shadow private int waterVisionTime;

    @Shadow @Nullable public abstract PlayerRideableJumping jumpableVehicle();

    @Shadow private int jumpRidingTicks;

    @Shadow private float jumpRidingScale;

    @Shadow public abstract float getJumpRidingScale();

    @Shadow protected abstract void sendRidingJump();

    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void onUpdate(CallbackInfo ci) {
        Cuple.eventBus.post(new UpdateEvent());
    }

    /**
     * @author Kevinleft
     * @reason MotionEvent
     */
    @Overwrite
    private void sendPosition() {
        MotionEvent event = new MotionEvent(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot(), this.onGround(), EventType.PRE);
        Cuple.eventBus.post(event);

        this.sendIsSprintingIfNeeded();
        boolean bl = this.isShiftKeyDown();
        if (bl != this.wasShiftKeyDown) {
            ServerboundPlayerCommandPacket.Action action = bl ? ServerboundPlayerCommandPacket.Action.PRESS_SHIFT_KEY : ServerboundPlayerCommandPacket.Action.RELEASE_SHIFT_KEY;
            this.connection.send(new ServerboundPlayerCommandPacket(this, action));
            this.wasShiftKeyDown = bl;
        }

        if (this.isControlledCamera()) {
            double d = event.getX() - this.xLast;
            double e = event.getY() - this.yLast1;
            double f = event.getZ() - this.zLast;
            double g = (double)(event.getYaw() - this.yRotLast);
            double h = (double)(event.getPitch() - this.xRotLast);
            ++this.positionReminder;
            boolean bl2 = Mth.lengthSquared(d, e, f) > Mth.square(2.0E-4) || this.positionReminder >= 20;
            boolean bl3 = g != 0.0 || h != 0.0;
            if (this.isPassenger()) {
                Vec3 vec3 = this.getDeltaMovement();
                this.connection.send(new ServerboundMovePlayerPacket.PosRot(vec3.x, -999.0, vec3.z, event.getYaw(), event.getPitch(), event.isOnGround()));
                bl2 = false;
            } else if (bl2 && bl3) {
                this.connection.send(new ServerboundMovePlayerPacket.PosRot(event.getX(), event.getY(), event.getZ(), event.getYaw(), event.getPitch(), event.isOnGround()));
            } else if (bl2) {
                this.connection.send(new ServerboundMovePlayerPacket.Pos(event.getX(), event.getY(), event.getZ(), event.isOnGround()));
            } else if (bl3) {
                this.connection.send(new ServerboundMovePlayerPacket.Rot(event.getYaw(), event.getPitch(), event.isOnGround()));
            } else if (this.lastOnGround != this.onGround()) {
                this.connection.send(new ServerboundMovePlayerPacket.StatusOnly(event.isOnGround()));
            }

            if (bl2) {
                this.xLast = event.getX();
                this.yLast1 = event.getY();
                this.zLast = event.getZ();
                this.positionReminder = 0;
            }

            if (bl3) {
                this.yRotLast = event.getYaw();
                this.xRotLast = event.getPitch();
            }

            this.lastOnGround = event.isOnGround();
            this.autoJumpEnabled = (Boolean)this.minecraft.options.autoJump().get();

            MotionEvent motionEventpost = new MotionEvent(this.getYRot(), this.getXRot(), EventType.POST);
            Cuple.eventBus.post(motionEventpost);
        }
    }

    /**
     * @author KevinLeft
     * @reason SlowDownEvent
     */
    @Overwrite
    public void aiStep() {
        if (this.sprintTriggerTime > 0) {
            --this.sprintTriggerTime;
        }

        if (!(this.minecraft.screen instanceof ReceivingLevelScreen)) {
            this.handleConfusionTransitionEffect(this.getActivePortalLocalTransition() == Portal.Transition.CONFUSION);
            this.processPortalCooldown();
        }

        boolean bl = this.input.jumping;
        boolean bl2 = this.input.shiftKeyDown;
        boolean bl3 = this.hasEnoughImpulseToStartSprinting();
        Abilities abilities = this.getAbilities();
        this.crouching = !abilities.flying && !this.isSwimming() && !this.isPassenger() && this.canPlayerFitWithinBlocksAndEntitiesWhen(Pose.CROUCHING) && (this.isShiftKeyDown() || !this.isSleeping() && !this.canPlayerFitWithinBlocksAndEntitiesWhen(Pose.STANDING));
        float f = (float)this.getAttributeValue(Attributes.SNEAKING_SPEED);
        this.input.tick(this.isMovingSlowly(), f);
        this.minecraft.getTutorial().onInput(this.input);

        SlowDownEvent slowDownEvent = new SlowDownEvent(0.2f, 0.2f);
        Cuple.eventBus.post(slowDownEvent);

        if (this.isUsingItem() && !this.isPassenger() && !slowDownEvent.isCancelled()) {
            Input var10000 = this.input;
            var10000.leftImpulse *= 0.2F;
            var10000 = this.input;
            var10000.forwardImpulse *= 0.2F;
            this.sprintTriggerTime = 0;
        }

        boolean bl4 = false;
        if (this.autoJumpTime > 0) {
            --this.autoJumpTime;
            bl4 = true;
            this.input.jumping = true;
        }

        if (!this.noPhysics) {
            this.moveTowardsClosestSpace(this.getX() - (double)this.getBbWidth() * 0.35, this.getZ() + (double)this.getBbWidth() * 0.35);
            this.moveTowardsClosestSpace(this.getX() - (double)this.getBbWidth() * 0.35, this.getZ() - (double)this.getBbWidth() * 0.35);
            this.moveTowardsClosestSpace(this.getX() + (double)this.getBbWidth() * 0.35, this.getZ() - (double)this.getBbWidth() * 0.35);
            this.moveTowardsClosestSpace(this.getX() + (double)this.getBbWidth() * 0.35, this.getZ() + (double)this.getBbWidth() * 0.35);
        }

        if (bl2) {
            this.sprintTriggerTime = 0;
        }

        boolean bl5 = this.canStartSprinting();
        boolean bl6 = this.isPassenger() ? this.getVehicle().onGround() : this.onGround();
        boolean bl7 = !bl2 && !bl3;
        if ((bl6 || this.isUnderWater()) && bl7 && bl5) {
            if (this.sprintTriggerTime <= 0 && !this.minecraft.options.keySprint.isDown()) {
                this.sprintTriggerTime = 7;
            } else {
                this.setSprinting(true);
            }
        }

        if ((!this.isInWater() || this.isUnderWater()) && bl5 && this.minecraft.options.keySprint.isDown()) {
            this.setSprinting(true);
        }

        boolean bl8;
        if (this.isSprinting()) {
            bl8 = !this.input.hasForwardImpulse() || !this.hasEnoughFoodToStartSprinting();
            boolean bl9 = bl8 || this.horizontalCollision && !this.minorHorizontalCollision || this.isInWater() && !this.isUnderWater();
            if (this.isSwimming()) {
                if (!this.onGround() && !this.input.shiftKeyDown && bl8 || !this.isInWater()) {
                    this.setSprinting(false);
                }
            } else if (bl9) {
                this.setSprinting(false);
            }
        }

        bl8 = false;
        if (abilities.mayfly) {
            if (this.minecraft.gameMode.isAlwaysFlying()) {
                if (!abilities.flying) {
                    abilities.flying = true;
                    bl8 = true;
                    this.onUpdateAbilities();
                }
            } else if (!bl && this.input.jumping && !bl4) {
                if (this.jumpTriggerTime == 0) {
                    this.jumpTriggerTime = 7;
                } else if (!this.isSwimming()) {
                    abilities.flying = !abilities.flying;
                    if (abilities.flying && this.onGround()) {
                        this.jumpFromGround();
                    }

                    bl8 = true;
                    this.onUpdateAbilities();
                    this.jumpTriggerTime = 0;
                }
            }
        }

        if (this.input.jumping && !bl8 && !bl && !abilities.flying && !this.isPassenger() && !this.onClimbable()) {
            ItemStack itemStack = this.getItemBySlot(EquipmentSlot.CHEST);
            if (itemStack.is(Items.ELYTRA) && ElytraItem.isFlyEnabled(itemStack) && this.tryToStartFallFlying()) {
                this.connection.send(new ServerboundPlayerCommandPacket(this, ServerboundPlayerCommandPacket.Action.START_FALL_FLYING));
            }
        }

        this.wasFallFlying = this.isFallFlying();
        if (this.isInWater() && this.input.shiftKeyDown && this.isAffectedByFluids()) {
            this.goDownInWater();
        }

        int i;
        if (this.isEyeInFluid(FluidTags.WATER)) {
            i = this.isSpectator() ? 10 : 1;
            this.waterVisionTime = Mth.clamp(this.waterVisionTime + i, 0, 600);
        } else if (this.waterVisionTime > 0) {
            this.isEyeInFluid(FluidTags.WATER);
            this.waterVisionTime = Mth.clamp(this.waterVisionTime - 10, 0, 600);
        }

        if (abilities.flying && this.isControlledCamera()) {
            i = 0;
            if (this.input.shiftKeyDown) {
                --i;
            }

            if (this.input.jumping) {
                ++i;
            }

            if (i != 0) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, (double)((float)i * abilities.getFlyingSpeed() * 3.0F), 0.0));
            }
        }

        PlayerRideableJumping playerRideableJumping = this.jumpableVehicle();
        if (playerRideableJumping != null && playerRideableJumping.getJumpCooldown() == 0) {
            if (this.jumpRidingTicks < 0) {
                ++this.jumpRidingTicks;
                if (this.jumpRidingTicks == 0) {
                    this.jumpRidingScale = 0.0F;
                }
            }

            if (bl && !this.input.jumping) {
                this.jumpRidingTicks = -10;
                playerRideableJumping.onPlayerJump(Mth.floor(this.getJumpRidingScale() * 100.0F));
                this.sendRidingJump();
            } else if (!bl && this.input.jumping) {
                this.jumpRidingTicks = 0;
                this.jumpRidingScale = 0.0F;
            } else if (bl) {
                ++this.jumpRidingTicks;
                if (this.jumpRidingTicks < 10) {
                    this.jumpRidingScale = (float)this.jumpRidingTicks * 0.1F;
                } else {
                    this.jumpRidingScale = 0.8F + 2.0F / (float)(this.jumpRidingTicks - 9) * 0.1F;
                }
            }
        } else {
            this.jumpRidingScale = 0.0F;
        }

        super.aiStep();
        if (this.onGround() && abilities.flying && !this.minecraft.gameMode.isAlwaysFlying()) {
            abilities.flying = false;
            this.onUpdateAbilities();
        }

    }
}
