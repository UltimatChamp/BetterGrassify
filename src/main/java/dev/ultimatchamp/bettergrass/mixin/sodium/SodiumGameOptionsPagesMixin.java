package dev.ultimatchamp.bettergrass.mixin.sodium;

import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import dev.ultimatchamp.bettergrass.config.SodiumOptionsStorage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

import net.caffeinemc.mods.sodium.client.gui.SodiumGameOptionPages;
import net.caffeinemc.mods.sodium.client.gui.options.*;
import net.caffeinemc.mods.sodium.client.gui.options.control.CyclingControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;

@Mixin(value = SodiumGameOptionPages.class, remap = false)
public class SodiumGameOptionsPagesMixin {
    @Inject(method = "quality", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup;createBuilder()Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder;", ordinal = 1, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT, remap = false)
    private static void bettergrass$quality(CallbackInfoReturnable<OptionPage> cir, List<OptionGroup> groups) {
        BetterGrassifyConfig config = BetterGrassifyConfig.load();

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(BetterGrassifyConfig.BetterGrassMode.class, SodiumOptionsStorage.INSTANCE)
                        .setName(Text.translatable("bettergrass.betterGrassMode"))
                        .setTooltip(Text.translatable("bettergrass.betterGrassMode.desc"))
                        .setControl((opt) -> new CyclingControl<>(opt, BetterGrassifyConfig.BetterGrassMode.class, new Text[]{
                                Text.translatable("options.off"),
                                Text.translatable("options.graphics.fast"),
                                Text.translatable("options.graphics.fancy")
                        }))
                        .setBinding((options, value) -> config.betterGrassMode = value,
                                (options) -> config.betterGrassMode)
                        .setImpact(OptionImpact.VARIES)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, SodiumOptionsStorage.INSTANCE)
                        .setName(Text.translatable("bettergrass.resourcePackCompatibilityMode"))
                        .setTooltip(Text.translatable("bettergrass.resourcePackCompatibilityMode.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> config.resourcePackCompatibilityMode = value,
                                (options) -> config.resourcePackCompatibilityMode)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, SodiumOptionsStorage.INSTANCE)
                        .setName(Text.translatable("block.minecraft.grass_block"))
                        .setTooltip(Text.translatable("bettergrass.grassBlocks.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> config.grassBlocks = value,
                                (options) -> config.grassBlocks)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, SodiumOptionsStorage.INSTANCE)
                        .setName(Text.translatable("block.minecraft.snow"))
                        .setTooltip(Text.translatable("bettergrass.snowy.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> config.snowy = value,
                                (options) -> config.snowy)
                        .setEnabled(() -> !FabricLoader.getInstance().isModLoaded("wilderwild"))
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, SodiumOptionsStorage.INSTANCE)
                        .setName(Text.translatable("block.minecraft.dirt_path"))
                        .setTooltip(Text.translatable("bettergrass.dirtPaths.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> config.dirtPaths = value,
                                (options) -> config.dirtPaths)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, SodiumOptionsStorage.INSTANCE)
                        .setName(Text.translatable("block.minecraft.farmland"))
                        .setTooltip(Text.translatable("bettergrass.farmLands.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> config.farmLands = value,
                                (options) -> config.farmLands)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, SodiumOptionsStorage.INSTANCE)
                        .setName(Text.translatable("block.minecraft.podzol"))
                        .setTooltip(Text.translatable("bettergrass.podzol.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> config.podzol = value,
                                (options) -> config.podzol)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, SodiumOptionsStorage.INSTANCE)
                        .setName(Text.translatable("block.minecraft.mycelium"))
                        .setTooltip(Text.translatable("bettergrass.mycelium.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> config.mycelium = value,
                                (options) -> config.mycelium)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, SodiumOptionsStorage.INSTANCE)
                        .setName(Text.translatable("block.minecraft.crimson_nylium"))
                        .setTooltip(Text.translatable("bettergrass.crimsonNylium.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> config.crimsonNylium = value,
                                (options) -> config.crimsonNylium)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, SodiumOptionsStorage.INSTANCE)
                        .setName(Text.translatable("block.minecraft.warped_nylium"))
                        .setTooltip(Text.translatable("bettergrass.warpedNylium.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> config.warpedNylium = value,
                                (options) -> config.warpedNylium)
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
                        .setBinding((options, value) -> config.betterSnowMode = value,
                                (options) -> config.betterSnowMode)
                        .setEnabled(() -> !FabricLoader.getInstance().isModLoaded("wilderwild"))
                        .setImpact(OptionImpact.VARIES)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .build()
        );
    }
}
