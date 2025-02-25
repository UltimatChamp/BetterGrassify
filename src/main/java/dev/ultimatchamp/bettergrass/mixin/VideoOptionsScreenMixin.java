package dev.ultimatchamp.bettergrass.mixin;

import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoOptionsScreen.class)
public abstract class VideoOptionsScreenMixin extends GameOptionsScreen {
    public VideoOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(
            method = "addOptions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/OptionListWidget;addSingleOptionEntry(Lnet/minecraft/client/option/SimpleOption;)V",
                    ordinal = 0
            )
    )
    private void bettergrass$addConfigButton(CallbackInfo ci) {
        this.body.addSingleOptionEntry(new SimpleOption<>("bettergrass.title",
                                       SimpleOption.constantTooltip(Text.empty()), (arg, object) ->
                                       Text.empty(), SimpleOption.BOOLEAN, true, (parent) ->
                                       this.client.setScreen(BetterGrassifyConfig.createConfigScreen(this))));
    }
}
