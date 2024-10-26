package bh.dpl.ui.clickgui;

import bh.dpl.Cuple;
import bh.dpl.module.Module;
import bh.dpl.value.Value;
import bh.dpl.value.impl.BoolValue;
import bh.dpl.value.impl.ModeValue;
import bh.dpl.value.impl.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author Kev1nLeft
 */

public class ModuleClickGui extends Screen {
    public Module module;

    private int VX = 5;
    private int VY = 5;

    private boolean fresh = false;

    public ModuleClickGui(Module module) {
        super(Component.literal("ModuleClickGui"));
        this.module = module;
    }

    public void init() {
        onDrawV(module);
        super.init();
    }

    public void tick() {
        if (fresh) {
            this.clearWidgets();
            VX = 5;
            VY = 5;
            onDrawV(module);
            fresh = false;
        }
        super.tick();
    }

    public void removed() {
        Cuple.configManager.saveConfigs();
        VX = 5;
        VY = 5;
        super.removed();
    }

    public boolean isPauseScreen() {
        return false;
    }

    public void onDrawV(Module module) {
        for (Value<?> value : module.getValueList()) {
            if (!value.isAvailable()) continue;

            if (value instanceof BoolValue boolValue) {
                this.addRenderableWidget(Button.builder(Component.literal(boolValue.getName() + " : " + boolValue.getValue()), (button) -> {
                    boolValue.setValue(!boolValue.getValue());
                    button.setMessage(Component.literal(boolValue.getName() + " : " + boolValue.getValue()));
                    fresh = true;
                }).bounds(VX, VY, 98, 20).build());
                VX += 103;
                if (VX >= Minecraft.getInstance().getWindow().getGuiScaledWidth()) {
                    VX = 5;
                    VY += 25;
                }
            } else if (value instanceof ModeValue<?> modeValue) {
                this.addRenderableWidget(Button.builder(Component.literal(modeValue.getName() + " : " + modeValue.getValue().name()), (button) -> {
                    modeValue.next();
                    button.setMessage(Component.literal(modeValue.getName() + " : " + modeValue.getValue().name()));
                    fresh = true;
                }).bounds(VX, VY, 98, 20).build());
                VX += 103;
                if (VX >= Minecraft.getInstance().getWindow().getGuiScaledWidth()) {
                    VX = 5;
                    VY += 25;
                }
            } else if (value instanceof NumberValue numberValue) {
                this.addRenderableWidget(new IntSliderWidget(VX, VY, 98, 20, numberValue.getConfigValue(), numberValue));
                VX += 103;
                if (VX >= Minecraft.getInstance().getWindow().getGuiScaledWidth()) {
                    VX = 5;
                    VY += 25;
                }
            }
        }
    }

    private static class IntSliderWidget extends AbstractSliderButton {
        private final NumberValue numberValue;
        public IntSliderWidget(int x, int y, int width, int height, double initialValue, NumberValue value) {
            super(x, y, width, height, Component.literal(value.getName() + " : " + initialValue), (initialValue - value.getMin()) / (value.getMax() - value.getMin()));
            this.numberValue = value;
        }
        @Override
        protected void updateMessage() {
            this.setMessage(Component.literal(numberValue.getName() + " : " + new DecimalFormat("#.0").format(this.numberValue.value)));
        }
        @Override
        protected void applyValue() {
            numberValue.value = new BigDecimal(numberValue.getMin() + (this.value) * (numberValue.getMax() - numberValue.getMin())).setScale(1, RoundingMode.HALF_UP).doubleValue();
            Cuple.configManager.saveConfigs();
        }
    }
}
