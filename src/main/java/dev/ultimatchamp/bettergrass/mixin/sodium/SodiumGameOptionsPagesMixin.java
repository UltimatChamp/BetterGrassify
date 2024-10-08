package dev.ultimatchamp.bettergrass.mixin.sodium;

import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import dev.ultimatchamp.bettergrass.config.SodiumOptionsStorage;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

//? if >1.20.6 {
import net.caffeinemc.mods.sodium.client.gui.SodiumGameOptionPages;
import net.caffeinemc.mods.sodium.client.gui.options.*;
import net.caffeinemc.mods.sodium.client.gui.options.control.CyclingControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;

@Mixin(value = SodiumGameOptionPages.class, remap = false)
public class SodiumGameOptionsPagesMixin {
    @Inject(method = "quality", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup;createBuilder()Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder;", ordinal = 1, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT, remap = false)
//?} else {
/*import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;

@Mixin(value = SodiumGameOptionPages.class, remap = false)
public class SodiumGameOptionsPagesMixin {
    @Inject(method = "quality", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup;createBuilder()Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;", ordinal = 1, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT, remap = false)
*///?}
    private static void betterGrass(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups) {
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(BetterGrassifyConfig.BetterGrassMode.class, SodiumOptionsStorage.INSTANCE)
                    .setName(Text.translatable("bettergrass.betterGrassMode"))
                    .setTooltip(Text.translatable("bettergrass.betterGrassMode.desc"))
                    .setControl((opt) -> new CyclingControl<>(opt, BetterGrassifyConfig.BetterGrassMode.class, new Text[]{
                        Text.translatable("options.off"),
                        Text.translatable("options.graphics.fast"),
                        Text.translatable("options.graphics.fancy")
                    }))
                    .setBinding((options, value) -> BetterGrassifyConfig.instance().betterGrassMode = value,
                        (options) -> BetterGrassifyConfig.instance().betterGrassMode)
                    .setImpact(OptionImpact.VARIES)
                    .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                    .build()
                ).add(OptionImpl.createBuilder(boolean.class, SodiumOptionsStorage.INSTANCE)
                        .setName(Text.translatable("block.minecraft.grass_block"))
                        .setTooltip(Text.translatable("bettergrass.grassBlocks.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> BetterGrassifyConfig.instance().grassBlocks = value,
                                (options) -> BetterGrassifyConfig.instance().grassBlocks)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, SodiumOptionsStorage.INSTANCE)
                        .setName(Text.translatable("block.minecraft.snow"))
                        .setTooltip(Text.translatable("bettergrass.snowy.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> BetterGrassifyConfig.instance().snowy = value,
                                (options) -> BetterGrassifyConfig.instance().snowy)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, SodiumOptionsStorage.INSTANCE)
                                .setName(Text.translatable("block.minecraft.dirt_path"))
                                .setTooltip(Text.translatable("bettergrass.dirtPaths.desc"))
                                .setControl(TickBoxControl::new)
                                .setBinding((options, value) -> BetterGrassifyConfig.instance().dirtPaths = value,
                                        (options) -> BetterGrassifyConfig.instance().dirtPaths)
                                .setImpact(OptionImpact.LOW)
                                .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                                .build()
                ).add(OptionImpl.createBuilder(boolean.class, SodiumOptionsStorage.INSTANCE)
                        .setName(Text.translatable("block.minecraft.farmland"))
                        .setTooltip(Text.translatable("bettergrass.farmLands.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> BetterGrassifyConfig.instance().farmLands = value,
                                (options) -> BetterGrassifyConfig.instance().farmLands)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, SodiumOptionsStorage.INSTANCE)
                        .setName(Text.translatable("block.minecraft.podzol"))
                        .setTooltip(Text.translatable("bettergrass.podzol.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> BetterGrassifyConfig.instance().podzol = value,
                                (options) -> BetterGrassifyConfig.instance().podzol)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, SodiumOptionsStorage.INSTANCE)
                        .setName(Text.translatable("block.minecraft.mycelium"))
                        .setTooltip(Text.translatable("bettergrass.mycelium.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> BetterGrassifyConfig.instance().mycelium = value,
                                (options) -> BetterGrassifyConfig.instance().mycelium)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, SodiumOptionsStorage.INSTANCE)
                        .setName(Text.translatable("block.minecraft.crimson_nylium"))
                        .setTooltip(Text.translatable("bettergrass.crimsonNylium.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> BetterGrassifyConfig.instance().crimsonNylium = value,
                                (options) -> BetterGrassifyConfig.instance().crimsonNylium)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, SodiumOptionsStorage.INSTANCE)
                        .setName(Text.translatable("block.minecraft.warped_nylium"))
                        .setTooltip(Text.translatable("bettergrass.warpedNylium.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> BetterGrassifyConfig.instance().warpedNylium = value,
                                (options) -> BetterGrassifyConfig.instance().warpedNylium)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(BetterGrassifyConfig.BetterSnowMode.class, SodiumOptionsStorage.INSTANCE)
                .setName(Text.translatable("bettergrass.betterSnowMode"))
                .setTooltip(Text.translatable("bettergrass.betterSnowMode.desc"))
                .setControl((opt) -> new CyclingControl<>(opt, BetterGrassifyConfig.BetterSnowMode.class, new Text[]{
                        Text.translatable("options.off"),
                        Text.translatable("bettergrass.betterSnowMode.optifine"),
                        Text.translatable("bettergrass.betterSnowMode.lambda")
                }))
                .setBinding((options, value) -> BetterGrassifyConfig.instance().betterSnowMode = value,
                        (options) -> BetterGrassifyConfig.instance().betterSnowMode)
                .setImpact(OptionImpact.VARIES)
                .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                .build()
                )
                .build()
        );
    }
}
