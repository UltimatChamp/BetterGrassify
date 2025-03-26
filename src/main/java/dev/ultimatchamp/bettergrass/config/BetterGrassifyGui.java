package dev.ultimatchamp.bettergrass.config;

import com.google.common.collect.Lists;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.gui.controllers.cycling.EnumController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BetterGrassifyGui {
    public static Screen createConfigScreen(Screen parent) {
        BetterGrassifyConfig config = BetterGrassifyConfig.load();

        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("bettergrass.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("stat.generalButton"))
                        .option(Option.<BetterGrassifyConfig.BetterGrassMode>createBuilder()
                                .name(Text.translatable("bettergrass.betterGrassMode"))
                                .description(OptionDescription.createBuilder()
                                        .text(Text.translatable("bettergrass.betterGrassMode.desc"))
                                        .webpImage(Identifier.of("bettergrass",
                                                   "textures/images/bettergrassmode.webp"))
                                        .build())
                                .binding(
                                        BetterGrassifyConfig.BetterGrassMode.FANCY,
                                        () -> config.betterGrassMode,
                                        (value) -> config.betterGrassMode = value
                                )
                                .customController(opt ->
                                        new EnumController<>(opt, BetterGrassifyConfig.BetterGrassMode.class))
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("bettergrass.resourcePackCompatibilityMode"))
                                .description(OptionDescription.createBuilder()
                                        .text(Text.translatable("bettergrass.resourcePackCompatibilityMode.desc"))
                                        .build())
                                .binding(
                                        true,
                                        () -> config.resourcePackCompatibilityMode,
                                        (value) -> config.resourcePackCompatibilityMode = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("soundCategory.block"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("block.minecraft.grass_block"))
                                        .description(OptionDescription.createBuilder()
                                                .text(Text.translatable("bettergrass.grassBlocks.desc"))
                                                .build())
                                        .binding(
                                                true,
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
                                                true,
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
                                                true,
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
                                                true,
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
                                                true,
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
                                                true,
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
                                                true,
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
                                                true,
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
                                        Lists.newArrayList("minecraft:sculk_catalyst"),
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
                                        BetterGrassifyConfig.BetterSnowMode.LAMBDA,
                                        () -> config.betterSnowMode,
                                        (value) -> config.betterSnowMode = value
                                )
                                .available(/*? if >1.21.4 {*/FabricLoader.getInstance().isModLoaded("sodium") || /*?} */!FabricLoader.getInstance().isModLoaded("wilderwild"))
                                .customController(opt ->
                                        new EnumController<>(opt, BetterGrassifyConfig.BetterSnowMode.class))
                                .build())
                        .group(ListOption.<String>createBuilder()
                                .name(Text.translatable("bettergrass.snowLayers"))
                                .binding(
                                        Lists.newArrayList(
                                                "snow",
                                                "moss_carpet"
                                                /*? if >1.21.1 {*/, "pale_moss_carpet"/*?} */
                                                /*? if >1.21.4 {*/, "leaf_litter"/*?} */
                                        ),
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
                                        Lists.newArrayList(),
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
                                        Lists.newArrayList(
                                                "lantern[hanging]",
                                                "redstone_wall_torch",
                                                "soul_lantern[hanging]",
                                                "soul_wall_torch",
                                                "wall_torch"
                                        ),
                                        () -> config.excludedBlocks,
                                        val -> config.excludedBlocks = val
                                )
                                .available(config.whitelistedBlocks.isEmpty())
                                .controller(StringControllerBuilder::create)
                                .initial("")
                                .insertEntriesAtEnd(true)
                                .build())
                        .build())
                .save(() -> {
                            BetterGrassifyConfig.save(config);

                            MinecraftClient world = MinecraftClient.getInstance();
                            if (world != null) {
                                world.reloadResources();
                            }
                        }
                )
                .build()
                .generateScreen(parent);
    }
}
