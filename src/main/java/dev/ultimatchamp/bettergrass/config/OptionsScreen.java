package dev.ultimatchamp.bettergrass.config;

import com.google.common.collect.Lists;
import dev.ultimatchamp.bettergrass.compat.WilderWildCompat;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.Requirement;
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class OptionsScreen {
    public static Screen createConfigScreen(Screen parent) {
        BetterGrassifyConfig config = BetterGrassifyConfig.load();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("bettergrass.title"));

        builder.setGlobalized(true);
        builder.setGlobalizedExpanded(true);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("stat.generalButton"));

        general.addEntry(entryBuilder
                .startEnumSelector(Component.translatable("bettergrass.general.betterGrassMode"), BetterGrassifyConfig.BetterGrassMode.class, config.general.betterGrassMode)
                .setEnumNameProvider(mode -> ((BetterGrassifyConfig.BetterGrassMode) mode).getCaption())
                .setTooltip(Component.translatable("bettergrass.general.betterGrassMode.desc"))
                .setDefaultValue(BetterGrassifyConfig.BetterGrassMode.FANCY)
                .setSaveConsumer(newValue -> config.general.betterGrassMode = newValue)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Component.translatable("bettergrass.general.resourcePackCompatibilityMode"), config.general.resourcePackCompatibilityMode)
                .setTooltip(Component.translatable("bettergrass.general.resourcePackCompatibilityMode.desc"))
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.general.resourcePackCompatibilityMode = newValue)
                .build());

        general.addEntry(
                entryBuilder.startSubCategory(Component.translatable("soundCategory.block"), List.of(
                                entryBuilder
                                        .startBooleanToggle(Component.translatable("block.minecraft.grass_block"), config.general.blocks.grassBlocks)
                                        .setTooltip(Component.translatable("bettergrass.general.blocks.grassBlocks.desc"))
                                        .setDefaultValue(true)
                                        .setSaveConsumer(newValue -> config.general.blocks.grassBlocks = newValue)
                                        .build(),
                                entryBuilder
                                        .startBooleanToggle(Component.translatable("bettergrass.general.blocks.snowy"), config.general.blocks.snowy)
                                        .setTooltip(Component.translatable("bettergrass.general.blocks.snowy.desc"))
                                        .setDefaultValue(true)
                                        .setRequirement(() -> !FabricLoader.getInstance().isModLoaded("wilderwild") || !WilderWildCompat.isSnowloggingOn())
                                        .setSaveConsumer(newValue -> config.general.blocks.snowy = newValue)
                                        .build(),
                                entryBuilder
                                        .startBooleanToggle(Component.translatable("block.minecraft.dirt_path"), config.general.blocks.dirtPaths)
                                        .setTooltip(Component.translatable("bettergrass.general.blocks.dirtPaths.desc"))
                                        .setDefaultValue(true)
                                        .setSaveConsumer(newValue -> config.general.blocks.dirtPaths = newValue)
                                        .build(),
                                entryBuilder
                                        .startBooleanToggle(Component.translatable("block.minecraft.farmland"), config.general.blocks.farmLands)
                                        .setTooltip(Component.translatable("bettergrass.general.blocks.farmLands.desc"))
                                        .setDefaultValue(true)
                                        .setSaveConsumer(newValue -> config.general.blocks.farmLands = newValue)
                                        .build(),
                                entryBuilder
                                        .startBooleanToggle(Component.translatable("block.minecraft.podzol"), config.general.blocks.podzol)
                                        .setTooltip(Component.translatable("bettergrass.general.blocks.podzol.desc"))
                                        .setDefaultValue(true)
                                        .setSaveConsumer(newValue -> config.general.blocks.podzol = newValue)
                                        .build(),
                                entryBuilder
                                        .startBooleanToggle(Component.translatable("block.minecraft.mycelium"), config.general.blocks.mycelium)
                                        .setTooltip(Component.translatable("bettergrass.general.blocks.mycelium.desc"))
                                        .setDefaultValue(true)
                                        .setSaveConsumer(newValue -> config.general.blocks.mycelium = newValue)
                                        .build(),
                                entryBuilder
                                        .startBooleanToggle(Component.translatable("block.minecraft.crimson_nylium"), config.general.blocks.crimsonNylium)
                                        .setTooltip(Component.translatable("bettergrass.general.blocks.crimsonNylium.desc"))
                                        .setDefaultValue(true)
                                        .setSaveConsumer(newValue -> config.general.blocks.crimsonNylium = newValue)
                                        .build(),
                                entryBuilder
                                        .startBooleanToggle(Component.translatable("block.minecraft.warped_nylium"), config.general.blocks.warpedNylium)
                                        .setTooltip(Component.translatable("bettergrass.general.blocks.warpedNylium.desc"))
                                        .setDefaultValue(true)
                                        .setSaveConsumer(newValue -> config.general.blocks.warpedNylium = newValue)
                                        .build(),
                                entryBuilder
                                        .startStrList(Component.translatable("createWorld.tab.more.title"), config.general.blocks.moreBlocks)
                                        .setDefaultValue(Lists.newArrayList())
                                        .setExpanded(true)
                                        .setSaveConsumer(newValue -> {
                                            config.general.blocks.moreBlocks.clear();
                                            config.general.blocks.moreBlocks.addAll(newValue);
                                        })
                                        .build()
                        ))
                        .setTooltip(Component.translatable("bettergrass.general.blocks.desc"))
                        .setExpanded(true)
                        .build()
        );

        ConfigCategory betterSnow = builder.getOrCreateCategory(Component.translatable("bettergrass.betterSnow"));

        betterSnow.addEntry(entryBuilder
                .startEnumSelector(Component.translatable("bettergrass.betterSnow.betterSnowMode"), BetterGrassifyConfig.BetterSnowMode.class, config.betterSnow.betterSnowMode)
                .setEnumNameProvider(mode -> ((BetterGrassifyConfig.BetterSnowMode) mode).getCaption())
                .setTooltip(Component.translatable("bettergrass.betterSnow.betterSnowMode.desc"))
                .setDefaultValue(BetterGrassifyConfig.BetterSnowMode.LAMBDA)
                .setRequirement(() -> !FabricLoader.getInstance().isModLoaded("wilderwild") || !WilderWildCompat.isSnowloggingOn())
                .setSaveConsumer(newValue -> config.betterSnow.betterSnowMode = newValue)
                .build());

        betterSnow.addEntry(entryBuilder
                .startStrList(Component.translatable("bettergrass.betterSnow.snowLayers"), config.betterSnow.snowLayers)
                .setTooltip(Component.translatable("bettergrass.betterSnow.snowLayers.desc"))
                .setDefaultValue(Lists.newArrayList(
                        "snow",
                        "moss_carpet",
                        "pink_petals"
                        //? if >1.21.1 {
                        , "pale_moss_carpet",
                        "leaf_litter",
                        "wildflowers"
                        //?}
                ))
                .setExpanded(true)
                .setSaveConsumer(newValue -> {
                    config.betterSnow.snowLayers.clear();
                    config.betterSnow.snowLayers.addAll(newValue);
                })
                .build());

        StringListListEntry whitelistedTags = entryBuilder
                .startStrList(Component.translatable("bettergrass.betterSnow.whitelistedTags"), config.betterSnow.whitelistedTags)
                .setTooltip(Component.translatable("bettergrass.betterSnow.whitelistedTags.desc"))
                .setDefaultValue(Lists.newArrayList())
                .setExpanded(true)
                .setSaveConsumer(newValue -> {
                    config.betterSnow.whitelistedTags.clear();
                    config.betterSnow.whitelistedTags.addAll(newValue);
                })
                .build();

        StringListListEntry whitelistedBlocks = entryBuilder
                .startStrList(Component.translatable("bettergrass.betterSnow.whitelistedBlocks"), config.betterSnow.whitelistedBlocks)
                .setTooltip(Component.translatable("bettergrass.betterSnow.whitelistedBlocks.desc"))
                .setDefaultValue(Lists.newArrayList())
                .setExpanded(true)
                .setSaveConsumer(newValue -> {
                    config.betterSnow.whitelistedBlocks.clear();
                    config.betterSnow.whitelistedBlocks.addAll(newValue);
                })
                .build();

        betterSnow.addEntry(whitelistedTags);
        betterSnow.addEntry(whitelistedBlocks);

        var exclusionListModeLabel = entryBuilder
                .startTextDescription(Component.translatable("bettergrass.labels.exclusionListMode"))
                .setDisplayRequirement(Requirement.isValue(whitelistedTags, Lists.newArrayList()))
                .setDisplayRequirement(Requirement.isValue(whitelistedBlocks, Lists.newArrayList()))
                .build();
        betterSnow.addEntry(exclusionListModeLabel);
        betterSnow.addEntry(entryBuilder
                .startTextDescription(Component.translatable("bettergrass.labels.whiteListMode"))
                .setDisplayRequirement(() -> !exclusionListModeLabel.isDisplayed())
                .build());

        betterSnow.addEntry(entryBuilder
                .startStrList(Component.translatable("bettergrass.betterSnow.excludedTags"), config.betterSnow.excludedTags)
                .setTooltip(Component.translatable("bettergrass.betterSnow.excludedTags.desc"))
                .setDefaultValue(Lists.newArrayList())
                .setRequirement(Requirement.isValue(whitelistedTags, Lists.newArrayList()))
                .setRequirement(Requirement.isValue(whitelistedBlocks, Lists.newArrayList()))
                .setExpanded(true)
                .setSaveConsumer(newValue -> {
                    config.betterSnow.excludedTags.clear();
                    config.betterSnow.excludedTags.addAll(newValue);
                })
                .build());

        betterSnow.addEntry(entryBuilder
                .startStrList(Component.translatable("bettergrass.betterSnow.excludedBlocks"), config.betterSnow.excludedBlocks)
                .setTooltip(Component.translatable("bettergrass.betterSnow.excludedBlocks.desc"))
                .setDefaultValue(Lists.newArrayList(
                        "lantern[hanging]",
                        "redstone_wall_torch",
                        "soul_lantern[hanging]",
                        "soul_wall_torch",
                        "wall_torch"
                ))
                .setRequirement(Requirement.isValue(whitelistedTags, Lists.newArrayList()))
                .setRequirement(Requirement.isValue(whitelistedBlocks, Lists.newArrayList()))
                .setExpanded(true)
                .setSaveConsumer(newValue -> {
                    config.betterSnow.excludedBlocks.clear();
                    config.betterSnow.excludedBlocks.addAll(newValue);
                })
                .build());

        builder.setSavingRunnable(() -> {
            BetterGrassifyConfig.save(config);
            BetterGrassifyConfig.cachedConfig = null;
            Minecraft.getInstance().reloadResourcePacks();
        });

        return builder.build();
    }
}
