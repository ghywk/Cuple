package bh.dpl.event.impl;

import bh.dpl.event.api.CancellableEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

/**
 * @author Kev1nLeft
 */

public class AttackEvent extends CancellableEvent {
    public final Player player;
    public final Entity entity;

    public AttackEvent(Player player, Entity entity) {
        this.player = player;
        this.entity = entity;
    }

    public Player getPlayer() {
        return player;
    }

    public Entity getEntity() {
        return entity;
    }
}
