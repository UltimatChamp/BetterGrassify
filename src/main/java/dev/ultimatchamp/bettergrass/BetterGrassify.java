package dev.ultimatchamp.bettergrass;

import dev.ultimatchamp.bettergrass.compat.WilderWildCompat;
import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import dev.ultimatchamp.bettergrass.model.BetterGrassifyUnbakedRootBlockStateModel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirtPathBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BetterGrassify implements ClientModInitializer {
    public static final String MOD_NAME = "BetterGrassify";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitializeClient() {
        BetterGrassifyConfig config = BetterGrassifyConfig.load();

        if (config.general.betterGrassMode.equals(BetterGrassifyConfig.BetterGrassMode.OFF))
            BetterGrassify.LOGGER.info("[{}] Better Grass is disabled! ;(", MOD_NAME);
        else
            BetterGrassify.LOGGER.info("[{}] [{}] Gamers can finally touch grass!?", MOD_NAME, config.general.betterGrassMode);

        if (FabricLoader.getInstance().isModLoaded("wilderwild") && WilderWildCompat.isSnowloggingOn()) {
            config.general.blocks.snowy = false;
            config.betterSnow.betterSnowMode = BetterGrassifyConfig.BetterSnowMode.OFF;
            BetterGrassify.LOGGER.warn("[{}] 'WilderWild' detected. 'Better Snowy Grass' and 'Better Snow' features have been disabled.", MOD_NAME);
        }

        ModelLoadingPlugin.register(pluginContext -> pluginContext.modifyBlockModelOnLoad().register(ModelModifier.WRAP_LAST_PHASE, (model, context) -> {
            BlockState state = context.state();
            List<String> blocks = BetterGrassify.getBlocks();

            for (String blockId : blocks) {
                Block block = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.tryParse(blockId)).orElse(null);

                if (state.is(block)) {
                    if (state.getOptionalValue(BlockStateProperties.SNOWY).orElse(false)) {
                        if (BetterGrassifyConfig.load().general.blocks.snowy) {
                            return new BetterGrassifyUnbakedRootBlockStateModel(model);
                        }
                    } else {
                        return new BetterGrassifyUnbakedRootBlockStateModel(model);
                    }
                } else if (state.is(Blocks.DIRT)) {
                    if (block instanceof DirtPathBlock || block instanceof FarmBlock) {
                        return new BetterGrassifyUnbakedRootBlockStateModel(model);
                    }
                }
            }

            return model;
        }));
    }

    public static List<String> getBlocks() {
        BetterGrassifyConfig.GeneralConfig.BlocksConfig config = BetterGrassifyConfig.load().general.blocks;
        List<String> blocks = new ArrayList<>();

        if (config.grassBlocks) blocks.add("minecraft:grass_block");
        if (config.dirtPaths) blocks.add("minecraft:dirt_path");
        if (config.farmLands) blocks.add("minecraft:farmland");
        if (config.podzol) blocks.add("minecraft:podzol");
        if (config.mycelium) blocks.add("minecraft:mycelium");
        if (config.crimsonNylium) blocks.add("minecraft:crimson_nylium");
        if (config.warpedNylium) blocks.add("minecraft:warped_nylium");

        blocks.addAll(config.moreBlocks);

        return blocks;
    }
}
