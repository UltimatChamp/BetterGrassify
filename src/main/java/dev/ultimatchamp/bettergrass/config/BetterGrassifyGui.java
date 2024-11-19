//? if !forge {
package dev.ultimatchamp.bettergrass.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.gui.controllers.cycling.EnumController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BetterGrassifyGui {
    public static Screen createConfigScreen(Screen parent) {
        return YetAnotherConfigLib.create(BetterGrassifyConfig.handler(), (defaults, config, builder) -> builder
                        .title(Text.translatable("bettergrass.title"))
                        .category(ConfigCategory.createBuilder()
                                .name(Text.translatable("stat.generalButton"))
                                .option(Option.<BetterGrassifyConfig.BetterGrassMode>createBuilder()
                                        .name(Text.translatable("bettergrass.betterGrassMode"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("bettergrass.betterGrassMode.desc"))
                                                .webpImage(Identifier.of("bettergrass", "textures/images/bettergrassmode.webp"))
                                                .build())
                                        .binding(
                                                defaults.betterGrassMode,
                                                () -> config.betterGrassMode,
                                                (value) -> config.betterGrassMode = value
                                        )
                                        .customController(opt -> new EnumController<>(opt, BetterGrassifyConfig.BetterGrassMode.class))
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(Text.translatable("soundCategory.block"))
                                        .option(Option.<Boolean>createBuilder()
                                                .name(Text.translatable("block.minecraft.grass_block"))
                                                .description(OptionDescription.createBuilder()
                                                        .text(Text.translatable("bettergrass.grassBlocks.desc"))
                                                        .build())
                                                .binding(
                                                        defaults.grassBlocks,
                                                        () -> config.grassBlocks,
                                                        (value) -> config.grassBlocks = value
                                                )
                                                .controller(TickBoxControllerBuilder::create)
                                                .build())
                                        .option(Option.<Boolean>createBuilder()
                                                .name(Text.translatable("block.minecraft.snow"))
                                                .description(OptionDescription.createBuilder()
                                                        .text(Text.translatable("bettergrass.snowy.desc"))
                                                        .build())
                                                .binding(
                                                        defaults.snowy,
                                                        () -> config.snowy,
                                                        (value) -> config.snowy = value
                                                )
                                                .available(!FabricLoader.getInstance().isModLoaded("wilderwild"))
                                                .controller(TickBoxControllerBuilder::create)
                                                .build())
                                        .option(Option.<Boolean>createBuilder()
                                                .name(Text.translatable("block.minecraft.dirt_path"))
                                                .description(OptionDescription.createBuilder()
                                                        .text(Text.translatable("bettergrass.dirtPaths.desc"))
                                                        .build())
                                                .binding(
                                                        defaults.dirtPaths,
                                                        () -> config.dirtPaths,
                                                        (value) -> config.dirtPaths = value
                                                )
                                                .controller(TickBoxControllerBuilder::create)
                                                .build())
                                        .option(Option.<Boolean>createBuilder()
                                                .name(Text.translatable("block.minecraft.farmland"))
                                                .description(OptionDescription.createBuilder()
                                                        .text(Text.translatable("bettergrass.farmLands.desc"))
                                                        .build())
                                                .binding(
                                                        defaults.farmLands,
                                                        () -> config.farmLands,
                                                        (value) -> config.farmLands = value
                                                )
                                                .controller(TickBoxControllerBuilder::create)
                                                .build())
                                        .option(Option.<Boolean>createBuilder()
                                                .name(Text.translatable("block.minecraft.podzol"))
                                                .description(OptionDescription.createBuilder()
                                                        .text(Text.translatable("bettergrass.podzol.desc"))
                                                        .build())
                                                .binding(
                                                        defaults.podzol,
                                                        () -> config.podzol,
                                                        (value) -> config.podzol = value
                                                )
                                                .controller(TickBoxControllerBuilder::create)
                                                .build())
                                        .option(Option.<Boolean>createBuilder()
                                                .name(Text.translatable("block.minecraft.mycelium"))
                                                .description(OptionDescription.createBuilder()
                                                        .text(Text.translatable("bettergrass.mycelium.desc"))
                                                        .build())
                                                .binding(
                                                        defaults.mycelium,
                                                        () -> config.mycelium,
                                                        (value) -> config.mycelium = value
                                                )
                                                .controller(TickBoxControllerBuilder::create)
                                                .build())
                                        .option(Option.<Boolean>createBuilder()
                                                .name(Text.translatable("block.minecraft.crimson_nylium"))
                                                .description(OptionDescription.createBuilder()
                                                        .text(Text.translatable("bettergrass.crimsonNylium.desc"))
                                                        .build())
                                                .binding(
                                                        defaults.crimsonNylium,
                                                        () -> config.crimsonNylium,
                                                        (value) -> config.crimsonNylium = value
                                                )
                                                .controller(TickBoxControllerBuilder::create)
                                                .build())
                                        .option(Option.<Boolean>createBuilder()
                                                .name(Text.translatable("block.minecraft.warped_nylium"))
                                                .description(OptionDescription.createBuilder()
                                                        .text(Text.translatable("bettergrass.warpedNylium.desc"))
                                                        .build())
                                                .binding(
                                                        defaults.warpedNylium,
                                                        () -> config.warpedNylium,
                                                        (value) -> config.warpedNylium = value
                                                )
                                                .controller(TickBoxControllerBuilder::create)
                                                .build())
                                        .build())
                                .group(ListOption.<String>createBuilder()
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("bettergrass.moreBlocks.desc"))
                                                .build())
                                        .binding(
                                                defaults.moreBlocks,
                                                () -> config.moreBlocks,
                                                val -> config.moreBlocks = val
                                        )
                                        .controller(StringControllerBuilder::create)
                                        .initial("minecraft:")
                                        .insertEntriesAtEnd(true)
                                        .build())
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(Text.translatable("bettergrass.betterSnow"))
                                .option(Option.<BetterGrassifyConfig.BetterSnowMode>createBuilder()
                                        .name(Text.translatable("bettergrass.betterSnowMode"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("bettergrass.betterSnowMode.desc"))
                                                .build())
                                        .binding(
                                                defaults.betterSnowMode,
                                                () -> config.betterSnowMode,
                                                (value) -> config.betterSnowMode = value
                                        )
                                        //? if >1.20.6 {
                                        .available(!FabricLoader.getInstance().isModLoaded("wilderwild"))
                                        //?} else {
                                        /*.available(!(!FabricLoader.getInstance().isModLoaded("sodium") || FabricLoader.getInstance().isModLoaded("wilderwild")))
                                        *///?}
                                        .customController(opt -> new EnumController<>(opt, BetterGrassifyConfig.BetterSnowMode.class))
                                        .build())
                                .group(ListOption.<String>createBuilder()
                                        .name(Text.translatable("bettergrass.snowLayers"))
                                        .binding(
                                                defaults.snowLayers,
                                                () -> config.snowLayers,
                                                val -> config.snowLayers = val
                                        )
                                        .controller(StringControllerBuilder::create)
                                        .initial("")
                                        .insertEntriesAtEnd(true)
                                        .build())
                                .group(ListOption.<String>createBuilder()
                                        .name(Text.translatable("bettergrass.excludedTags"))
                                        .binding(
                                                defaults.excludedTags,
                                                () -> config.excludedTags,
                                                val -> config.excludedTags = val
                                        )
                                        .available(config.whitelistedTags.isEmpty())
                                        .controller(StringControllerBuilder::create)
                                        .initial("")
                                        .insertEntriesAtEnd(true)
                                        .build())
                                .group(ListOption.<String>createBuilder()
                                        .name(Text.translatable("bettergrass.excludedBlocks"))
                                        .binding(
                                                defaults.excludedBlocks,
                                                () -> config.excludedBlocks,
                                                val -> config.excludedBlocks = val
                                        )
                                        .available(config.whitelistedBlocks.isEmpty())
                                        .controller(StringControllerBuilder::create)
                                        .initial("")
                                        .insertEntriesAtEnd(true)
                                        .build())
                                .build())
                        .save(BetterGrassifyConfig.handler()::save)
                )
                .generateScreen(parent);
    }
}
//?}
