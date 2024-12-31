//? if !forge {
package dev.ultimatchamp.bettergrass.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class NoYACLWarning extends Screen {
    private final Screen parent;
    public NoYACLWarning(Screen parent) {
        super(Text.empty());
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        var btn = ButtonWidget.builder(Text.translatable("dataPack.validation.back"), button -> this.client.setScreen(parent)).dimensions(this.width / 2 - 100, this.height / 2 + 50, 200, 20).build();
        this.addDrawableChild(btn);
    }

    //? if <1.20.2 {
    /*@Override
    public void renderBackground(DrawContext context) {
        this.renderBackgroundTexture(context);
    }
    *///?}

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context/*? if >1.20.1 {*/, mouseX, mouseY, delta/*?}*/);
        super.render(context, mouseX, mouseY, delta);

        Text warning = Text.translatable("bettergrass.noyacl.warn");
        //? if >1.21.3 {
        context.drawWrappedText(this.textRenderer, warning, this.width / 2 - 100, this.height / 2 - 50, 200, 0xFFFFFFFF, true);
        //?} else {
        /*context.drawTextWrapped(this.textRenderer, warning, this.width / 2 - 100, this.height / 2 - 50, 200, 0xFFFFFFFF);
        *///?}
    }
}
//?}
