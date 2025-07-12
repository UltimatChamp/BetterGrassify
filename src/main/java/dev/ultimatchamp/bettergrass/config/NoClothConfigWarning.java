package dev.ultimatchamp.bettergrass.config;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class NoClothConfigWarning extends Screen {
    private final Screen parent;

    public NoClothConfigWarning(Screen parent) {
        super(Component.empty());
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        if (this.minecraft == null) return;

        Button btn = Button.builder(Component.translatable("dataPack.validation.back"),
                        button -> this.minecraft.setScreen(parent))
                .bounds(this.width / 2 - 100, this.height / 2 + 50, 200, 20)
                .build();
        this.addRenderableWidget(btn);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        Component warning = Component.translatable("bettergrass.labels.noClothConfig");
        guiGraphics.drawWordWrap(this.font, warning, this.width / 2 - 100, this.height / 2 - 50, 200, 0xFFFFFFFF, true);
    }
}
