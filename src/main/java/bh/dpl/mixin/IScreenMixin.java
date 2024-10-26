package bh.dpl.mixin;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

/**
 * @author Kev1nLeft
 */

@Mixin(Screen.class)
public interface IScreenMixin {
    @Accessor("renderables")
    List<Renderable> getRenderables();
}
