package bh.dpl.ui.clickgui;

import bh.dpl.Cuple;
import bh.dpl.mixin.IScreenMixin;
import bh.dpl.module.Category;
import bh.dpl.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * @author Kev1nLeft
 */

public class MainClickGui extends Screen {
    private boolean shouldUpdate = false;
    private Category renderingCategory = Category.Combat;
    private Module shouldRenderModule = null;

    private int wh = Minecraft.getInstance().getWindow().getGuiScaledHeight();

    private int CX = 5;

    private int MX = 5;
    private int MY = 5;

    public MainClickGui() {
        super(Component.literal("MainClickGui"));
    }

    public void tick() {
        if (shouldUpdate) {
            CX = 5;
            MX = 5;
            MY = 5;

            this.clearWidgets();

            this.onDrawC();
            this.onDrawM();
            shouldUpdate = false;
        }

        if (shouldRenderModule != null) {
            if (minecraft != null) {
                minecraft.execute(() -> { minecraft.setScreen(null); minecraft.setScreen(new ModuleClickGui(shouldRenderModule)); });
                shouldRenderModule = null;
            }
        }

        super.tick();
    }

    public void removed() {
        Cuple.configManager.saveConfigs();
        CX = 5;
        MX = 5;
        MY = 5;
        super.removed();
    }

    public void init() {
        this.onDrawC();
        this.onDrawM();
        super.init();
    }

    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseClicked(double x, double y, int but) {
        if (but == 1) {
            IScreenMixin iScreenMixin = (IScreenMixin) minecraft.screen;

            for (Renderable widget : iScreenMixin.getRenderables()) {
                if (widget instanceof Button) {
                    Button button = (Button) widget;

                    for (Category category : Category.values()) {
                        if (! button.getMessage().getString().equalsIgnoreCase(category.name())) {
                            if (x > button.getX() && x < button.getX() + 98 && y > button.getY() && y < button.getY() + 20) {
                                System.out.println(button.getMessage().getString());
                                shouldRenderModule = Cuple.moduleManager.getModuleByName(button.getMessage().getString());
                            }
                        }
                    }
                }
            }
        }
        return super.mouseClicked(x, y, but);
    }

    public void onDrawC() {
        for (Category category : Category.values()) {
            this.addRenderableWidget(Button.builder(Component.literal(category.name()), (buttonC) -> {
                this.renderingCategory = category;
                shouldUpdate = true;
            }).bounds(CX, wh - 25, 49, 20).build());
            CX += 54;
        }
    }

    public void onDrawM() {
        for (Module module : Cuple.moduleManager.getModules()) {
            if (module.getCategory().equals(renderingCategory)) {
                this.addRenderableWidget(Button.builder(Component.literal(module.getName()), (buttonM) -> {
                    module.toggle();
                }).bounds(MX, MY, 98, 20).build());
                MX += 103;
                if (MX > Minecraft.getInstance().getWindow().getGuiScaledWidth()) {
                    MX = 30;
                    MY += 25;
                }
            }
        }
    }
}
