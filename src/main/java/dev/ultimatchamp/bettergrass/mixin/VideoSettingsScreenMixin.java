package dev.ultimatchamp.bettergrass.mixin;

import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoSettingsScreen.class)
public abstract class VideoSettingsScreenMixin extends OptionsSubScreen {
    public VideoSettingsScreenMixin(Screen parent, Options gameOptions, Component title) {
        super(parent, gameOptions, title);
    }

    @Inject(
            method = "addOptions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/OptionsList;addBig(Lnet/minecraft/client/OptionInstance;)V",
                    ordinal = 0
            )
    )
    private void bettergrass$addConfigButton(CallbackInfo ci) {
        if (this.minecraft == null || this.list == null) return;

        this.list.addBig(new OptionInstance<>("bettergrass.title",
                OptionInstance.cachedConstantTooltip(Component.empty()), (arg, object) ->
                Component.empty(), OptionInstance.BOOLEAN_VALUES, true, (parent) ->
                this.minecraft.setScreen(BetterGrassifyConfig.createConfigScreen(this))));
    }
}
