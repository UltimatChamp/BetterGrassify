//? if >1.21.10 {
package dev.ultimatchamp.bettergrass.config;

import dev.ultimatchamp.bettergrass.BetterGrassify;
import dev.ultimatchamp.bettergrass.compat.WilderWildCompat;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.StorageEventHandler;
import net.caffeinemc.mods.sodium.api.config.option.OptionFlag;
import net.caffeinemc.mods.sodium.api.config.option.OptionImpact;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.client.gui.options.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class SodiumOptionsScreenBuilder implements ConfigEntryPoint {
    private final SodiumOptionsStorage storage = new SodiumOptionsStorage();
    private final StorageEventHandler handler = this.storage::save;

    @Override
    public void registerConfigLate(ConfigBuilder builder) {
        builder.registerOwnModOptions()
                .setName(BetterGrassify.MOD_NAME)
                .setIcon(Identifier.fromNamespaceAndPath("bettergrass", "textures/gui/icon.png"))
                .setColorTheme(builder.createColorTheme().setBaseThemeRGB(0x72e9ae))
                .addPage(builder.createOptionPage()
                        .setName(Component.translatable("stat.generalButton"))
                        .addOptionGroup(builder.createOptionGroup()
                                .addOption(builder.createEnumOption(Identifier.parse("bettergrass:general.bettergrassmode"), BetterGrassifyConfig.BetterGrassMode.class)
                                        .setName(Component.translatable("bettergrass.general.betterGrassMode"))
                                        .setTooltip(Component.translatable("bettergrass.general.betterGrassMode.desc"))
                                        .setStorageHandler(this.handler)
                                        .setBinding((value) -> this.storage.getData().general.betterGrassMode = value, () -> this.storage.getData().general.betterGrassMode)
                                        .setElementNameProvider(TranslatableOption::getLocalizedName)
                                        .setDefaultValue(BetterGrassifyConfig.BetterGrassMode.FANCY)
                                        .setImpact(OptionImpact.VARIES)
                                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD))
                                .addOption(builder.createBooleanOption(Identifier.parse("bettergrass:general.resourcepackcompatibilitymode"))
                                        .setName(Component.translatable("bettergrass.general.resourcePackCompatibilityMode"))
                                        .setTooltip(Component.translatable("bettergrass.general.resourcePackCompatibilityMode.desc"))
                                        .setStorageHandler(this.handler)
                                        .setBinding((value) -> this.storage.getData().general.resourcePackCompatibilityMode = value, () -> this.storage.getData().general.resourcePackCompatibilityMode)
                                        .setDefaultValue(true)
                                        .setImpact(OptionImpact.LOW)
                                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)))
                        .addOptionGroup(builder.createOptionGroup()
                                .setName(Component.translatable("soundCategory.block"))
                                .addOption(builder.createBooleanOption(Identifier.parse("bettergrass:general.blocks.grassblocks"))
                                        .setName(Component.translatable("block.minecraft.grass_block"))
                                        .setTooltip(Component.translatable("bettergrass.general.blocks.grassBlocks.desc"))
                                        .setStorageHandler(this.handler)
                                        .setBinding((value) -> this.storage.getData().general.blocks.grassBlocks = value, () -> this.storage.getData().general.blocks.grassBlocks)
                                        .setDefaultValue(true)
                                        .setImpact(OptionImpact.LOW)
                                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD))
                                .addOption(builder.createBooleanOption(Identifier.parse("bettergrass:general.blocks.snowy"))
                                        .setName(Component.translatable("bettergrass.general.blocks.snowy"))
                                        .setTooltip(Component.translatable("bettergrass.general.blocks.snowy.desc"))
                                        .setStorageHandler(this.handler)
                                        .setBinding((value) -> this.storage.getData().general.blocks.snowy = value, () -> this.storage.getData().general.blocks.snowy)
                                        .setEnabled(!FabricLoader.getInstance().isModLoaded("wilderwild") || !WilderWildCompat.isSnowloggingOn())
                                        .setDefaultValue(true)
                                        .setImpact(OptionImpact.LOW)
                                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD))
                                .addOption(builder.createBooleanOption(Identifier.parse("bettergrass:general.blocks.dirtpaths"))
                                        .setName(Component.translatable("block.minecraft.dirt_path"))
                                        .setTooltip(Component.translatable("bettergrass.general.blocks.dirtPaths.desc"))
                                        .setStorageHandler(this.handler)
                                        .setBinding((value) -> this.storage.getData().general.blocks.dirtPaths = value, () -> this.storage.getData().general.blocks.dirtPaths)
                                        .setDefaultValue(true)
                                        .setImpact(OptionImpact.LOW)
                                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD))
                                .addOption(builder.createBooleanOption(Identifier.parse("bettergrass:general.blocks.farmlands"))
                                        .setName(Component.translatable("block.minecraft.farmland"))
                                        .setTooltip(Component.translatable("bettergrass.general.blocks.farmLands.desc"))
                                        .setStorageHandler(this.handler)
                                        .setBinding((value) -> this.storage.getData().general.blocks.farmLands = value, () -> this.storage.getData().general.blocks.farmLands)
                                        .setDefaultValue(true)
                                        .setImpact(OptionImpact.LOW)
                                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD))
                                .addOption(builder.createBooleanOption(Identifier.parse("bettergrass:general.blocks.podzol"))
                                        .setName(Component.translatable("block.minecraft.podzol"))
                                        .setTooltip(Component.translatable("bettergrass.general.blocks.podzol.desc"))
                                        .setStorageHandler(this.handler)
                                        .setBinding((value) -> this.storage.getData().general.blocks.podzol = value, () -> this.storage.getData().general.blocks.podzol)
                                        .setDefaultValue(true)
                                        .setImpact(OptionImpact.LOW)
                                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD))
                                .addOption(builder.createBooleanOption(Identifier.parse("bettergrass:general.blocks.mycelium"))
                                        .setName(Component.translatable("block.minecraft.mycelium"))
                                        .setTooltip(Component.translatable("bettergrass.general.blocks.mycelium.desc"))
                                        .setStorageHandler(this.handler)
                                        .setBinding((value) -> this.storage.getData().general.blocks.mycelium = value, () -> this.storage.getData().general.blocks.mycelium)
                                        .setDefaultValue(true)
                                        .setImpact(OptionImpact.LOW)
                                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD))
                                .addOption(builder.createBooleanOption(Identifier.parse("bettergrass:general.blocks.crimsonnylium"))
                                        .setName(Component.translatable("block.minecraft.crimson_nylium"))
                                        .setTooltip(Component.translatable("bettergrass.general.blocks.crimsonNylium.desc"))
                                        .setStorageHandler(this.handler)
                                        .setBinding((value) -> this.storage.getData().general.blocks.crimsonNylium = value, () -> this.storage.getData().general.blocks.crimsonNylium)
                                        .setDefaultValue(true)
                                        .setImpact(OptionImpact.LOW)
                                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD))
                                .addOption(builder.createBooleanOption(Identifier.parse("bettergrass:general.blocks.warpednylium"))
                                        .setName(Component.translatable("block.minecraft.warped_nylium"))
                                        .setTooltip(Component.translatable("bettergrass.general.blocks.warpedNylium.desc"))
                                        .setStorageHandler(this.handler)
                                        .setBinding((value) -> this.storage.getData().general.blocks.warpedNylium = value, () -> this.storage.getData().general.blocks.warpedNylium)
                                        .setDefaultValue(true)
                                        .setImpact(OptionImpact.LOW)
                                        .setFlags(OptionFlag.REQUIRES_ASSET_RELOAD))))
                .addPage(builder.createOptionPage()
                        .setName(Component.translatable("bettergrass.betterSnow"))
                        .addOption(builder.createEnumOption(Identifier.parse("bettergrass:bettersnow.bettersnowmode"), BetterGrassifyConfig.BetterSnowMode.class)
                            .setName(Component.translatable("bettergrass.betterSnow.betterSnowMode"))
                            .setTooltip(Component.translatable("bettergrass.betterSnow.betterSnowMode.desc"))
                            .setStorageHandler(this.handler)
                            .setBinding((value) -> this.storage.getData().betterSnow.betterSnowMode = value, () -> this.storage.getData().betterSnow.betterSnowMode)
                            .setElementNameProvider(TranslatableOption::getLocalizedName)
                            .setEnabled(!FabricLoader.getInstance().isModLoaded("wilderwild") || !WilderWildCompat.isSnowloggingOn())
                            .setDefaultValue(BetterGrassifyConfig.BetterSnowMode.LAMBDA)
                            .setImpact(OptionImpact.VARIES)
                            .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD))
                )
                .addPage(builder.createExternalPage()
                        .setName(Component.translatable("sodium.options.pages.advanced"))
                        .setScreenConsumer(parent ->
                                Minecraft.getInstance().setScreenAndShow(BetterGrassifyConfig.createConfigScreen(parent))
                        )
                );
    }
}
//?}
