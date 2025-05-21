//? if fabric {
package dev.ultimatchamp.bettergrass.loaders.fabric;

import dev.ultimatchamp.bettergrass.BetterGrassify;
import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;

//? if >1.21.4 {
import dev.ultimatchamp.bettergrass.model.BetterGrassifyUnbakedGroupedBlockStateModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//?} else if >1.21.3 {
//import dev.ultimatchamp.bettergrass.model.BetterGrassifyGroupableModel;
//?} else {
//import dev.ultimatchamp.bettergrass.model.BetterGrassifyUnbakedModel;
//?}

/*? if <1.21.5 {*//*import net.minecraft.client.resources.model.ModelResourceLocation;*//*?}*/

public class BetterGrassifyFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BetterGrassifyConfig config = BetterGrassifyConfig.load();

        if (config.betterGrassMode == BetterGrassifyConfig.BetterGrassMode.OFF) {
            BetterGrassify.LOGGER.info("[BetterGrassify] Better Grass is disabled! ;(");
        } else {
            BetterGrassify.LOGGER.info("[BetterGrassify] [{}] Gamers can finally touch grass!?",
                                       config.betterGrassMode.toString());
        }

        if (FabricLoader.getInstance().isModLoaded("wilderwild")) {
            config.snowy = false;
            config.betterSnowMode = BetterGrassifyConfig.BetterSnowMode.OFF;
            BetterGrassify.LOGGER.warn("[BetterGrassify] 'WilderWild' detected. " +
                                       "'Better Snowy Grass' and 'Better Snow' features have been disabled.");
        }

        ModelLoadingPlugin.register(pluginContext ->
        //? if >1.21.3 {
        pluginContext.modifyBlockModelOnLoad()
        //?} else {
        //pluginContext.modifyModelOnLoad()
        //?}
        .register(ModelModifier.WRAP_LAST_PHASE, (model, context) -> {
            //? if >1.21.4 {
            BlockState state = context.state();
            //?} else if >1.21.3 {
            //ModelResourceLocation modelId = context.id();
            //?} else {
            //ModelResourceLocation modelId = context.topLevelId();
            //?}

            /*? if <1.21.5 {*//*if (modelId.getVariant().equals("inventory")) return model;*//*?}*/

            List<String> blocks = BetterGrassify.getBlocks();

            for (String blockId : blocks) {
                //? if >1.21.4 {
                Block block = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.tryParse(blockId)).orElse(null);

                if (state.is(block)) {
                    if (state.getOptionalValue(BlockStateProperties.SNOWY).orElse(false)) {
                //?} else {
                /*if (modelId.id().toString().equals(blockId)) {
                    if (modelId.getVariant().contains("snowy=true")) {
                *///?}
                        if (BetterGrassifyConfig.load().snowy) {
                            //? if >1.21.4 {
                            return new BetterGrassifyUnbakedGroupedBlockStateModel(model);
                            //?} else if >1.21.3 {
                            //return new BetterGrassifyGroupableModel(model);
                            //?} else {
                            //return new BetterGrassifyUnbakedModel(model);
                            //?}
                        }
                    } else {
                        //? if >1.21.4 {
                        return new BetterGrassifyUnbakedGroupedBlockStateModel(model);
                        //?} else if >1.21.3 {
                        //return new BetterGrassifyGroupableModel(model);
                        //?} else {
                        //return new BetterGrassifyUnbakedModel(model);
                        //?}
                    }
                }
            }

            //? if >1.21.4 {
            if (state.is(Blocks.DIRT)) {
            //?} else {
            //if (modelId.id().toString().equals("minecraft:dirt")) {
            //?}
                if (blocks.contains("minecraft:dirt_path") || blocks.contains("minecraft:farmland")) {
                    //? if >1.21.4 {
                    return new BetterGrassifyUnbakedGroupedBlockStateModel(model);
                    //?} else if >1.21.3 {
                    //return new BetterGrassifyGroupableModel(model);
                    //?} else {
                    //return new BetterGrassifyUnbakedModel(model);
                    //?}
                }
            }

            return model;
        }));
    }
}
//?}
