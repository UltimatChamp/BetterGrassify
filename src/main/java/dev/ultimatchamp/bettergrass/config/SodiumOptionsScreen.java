package dev.ultimatchamp.bettergrass.config;

import com.google.common.collect.ImmutableList;
import dev.ultimatchamp.bettergrass.BetterGrassify;
import dev.ultimatchamp.bettergrass.compat.WilderWildCompat;
import net.caffeinemc.mods.sodium.client.gui.options.*;
import net.caffeinemc.mods.sodium.client.gui.options.control.CyclingControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class SodiumOptionsScreen {
    public static void addOptionsPage(List<OptionPage> pages) {
        SodiumOptionsStorage store = new SodiumOptionsStorage();
        List<OptionGroup> groups = new ArrayList<>();

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(BetterGrassifyConfig.BetterGrassMode.class, store)
                        .setName(Component.translatable("bettergrass.general.betterGrassMode"))
                        .setTooltip(Component.translatable("bettergrass.general.betterGrassMode.desc"))
                        .setControl((opt) ->
                                new CyclingControl<>(opt, BetterGrassifyConfig.BetterGrassMode.class, new Component[]{
                                        Component.translatable("options.off"),
                                        Component.translatable("options.graphics.fast"),
                                        Component.translatable("options.graphics.fancy")
                                }))
                        .setBinding((options, value) -> options.general.betterGrassMode = value,
                                (options) -> options.general.betterGrassMode)
                        .setImpact(OptionImpact.VARIES)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, store)
                        .setName(Component.translatable("bettergrass.general.resourcePackCompatibilityMode"))
                        .setTooltip(Component.translatable("bettergrass.general.resourcePackCompatibilityMode.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.general.resourcePackCompatibilityMode = value,
                                (options) -> options.general.resourcePackCompatibilityMode)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .build()
        );

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, store)
                        .setName(Component.translatable("block.minecraft.grass_block"))
                        .setTooltip(Component.translatable("bettergrass.general.blocks.grassBlocks.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.general.blocks.grassBlocks = value,
                                (options) -> options.general.blocks.grassBlocks)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, store)
                        .setName(Component.translatable("bettergrass.general.blocks.snowy"))
                        .setTooltip(Component.translatable("bettergrass.general.blocks.snowy.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.general.blocks.snowy = value,
                                (options) -> options.general.blocks.snowy)
                        .setEnabled(() -> !FabricLoader.getInstance().isModLoaded("wilderwild") || !WilderWildCompat.isSnowloggingOn())
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, store)
                        .setName(Component.translatable("block.minecraft.dirt_path"))
                        .setTooltip(Component.translatable("bettergrass.general.blocks.dirtPaths.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.general.blocks.dirtPaths = value,
                                (options) -> options.general.blocks.dirtPaths)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, store)
                        .setName(Component.translatable("block.minecraft.farmland"))
                        .setTooltip(Component.translatable("bettergrass.general.blocks.farmLands.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.general.blocks.farmLands = value,
                                (options) -> options.general.blocks.farmLands)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, store)
                        .setName(Component.translatable("block.minecraft.podzol"))
                        .setTooltip(Component.translatable("bettergrass.general.blocks.podzol.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.general.blocks.podzol = value,
                                (options) -> options.general.blocks.podzol)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, store)
                        .setName(Component.translatable("block.minecraft.mycelium"))
                        .setTooltip(Component.translatable("bettergrass.general.blocks.mycelium.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.general.blocks.mycelium = value,
                                (options) -> options.general.blocks.mycelium)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, store)
                        .setName(Component.translatable("block.minecraft.crimson_nylium"))
                        .setTooltip(Component.translatable("bettergrass.general.blocks.crimsonNylium.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.general.blocks.crimsonNylium = value,
                                (options) -> options.general.blocks.crimsonNylium)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                ).add(OptionImpl.createBuilder(boolean.class, store)
                        .setName(Component.translatable("block.minecraft.warped_nylium"))
                        .setTooltip(Component.translatable("bettergrass.general.blocks.warpedNylium.desc"))
                        .setControl(TickBoxControl::new)
                        .setBinding((options, value) -> options.general.blocks.warpedNylium = value,
                                (options) -> options.general.blocks.warpedNylium)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD)
                        .build()
                )
                .build()
        );

        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(BetterGrassifyConfig.BetterSnowMode.class, store)
                        .setName(Component.translatable("bettergrass.betterSnow.betterSnowMode"))
                        .setTooltip(Component.translatable("bettergrass.betterSnow.betterSnowMode.desc"))
                        .setControl((opt) ->
                                new CyclingControl<>(opt, BetterGrassifyConfig.BetterSnowMode.class, new Component[]{
                                        Component.translatable("options.off"),
                                        Component.translatable("bettergrass.betterSnow.betterSnowMode.optifine"),
                                        Component.translatable("bettergrass.betterSnow.betterSnowMode.lambda")
                                }))
                        .setBinding((options, value) -> options.betterSnow.betterSnowMode = value,
                                (options) -> options.betterSnow.betterSnowMode)
                        .setEnabled(() -> !FabricLoader.getInstance().isModLoaded("wilderwild") || !WilderWildCompat.isSnowloggingOn())
                        .setImpact(OptionImpact.VARIES)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .build()
        );

        pages.add(new OptionPage(Component.literal(BetterGrassify.MOD_NAME), ImmutableList.copyOf(groups)));
    }
}
