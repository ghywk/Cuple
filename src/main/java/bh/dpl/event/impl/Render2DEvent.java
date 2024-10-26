package bh.dpl.event.impl;

import bh.dpl.event.api.Event;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;

/**
 * @author Kev1nLeft
 */
 
public class Render2DEvent extends Event {
    public final GuiGraphics guiGraphics;
    public final DeltaTracker deltaTracker;

    public Render2DEvent(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        this.guiGraphics = guiGraphics;
        this.deltaTracker = deltaTracker;
    }
}
