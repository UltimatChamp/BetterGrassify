//? if <1.21.11 {
/*package dev.ultimatchamp.bettergrass.mixin.sodium;

import dev.ultimatchamp.bettergrass.config.SodiumOptionsScreen;
import net.caffeinemc.mods.sodium.client.gui.SodiumOptionsGUI;
import net.caffeinemc.mods.sodium.client.gui.options.*;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = SodiumOptionsGUI.class, remap = false)
public class SodiumOptionsGUIMixin {
    @Shadow
    @Final
    private List<OptionPage> pages;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 3, shift = At.Shift.AFTER))
    private void bettergrass$addOptionsPage(Screen prevScreen, CallbackInfo ci) {
        SodiumOptionsScreen.addOptionsPage(this.pages);
    }
}
*///?}
