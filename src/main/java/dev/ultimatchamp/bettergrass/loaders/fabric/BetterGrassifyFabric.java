//? if fabric {
package dev.ultimatchamp.bettergrass.loaders.fabric;

import dev.ultimatchamp.bettergrass.BetterGrassify;
import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

/*? if >1.21.1 && <1.21.5 {*//*import net.minecraft.client.util.ModelIdentifier;*//*?} */

//? if >1.21.1 {
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;

import java.util.List;
//?}

//? if >1.21.4 {
import dev.ultimatchamp.bettergrass.model.BetterGrassifyUnbakedGroupedBlockStateModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
//?} else if >1.21.3 {
/*import dev.ultimatchamp.bettergrass.model.BetterGrassifyGroupableModel;
*///?} else if >1.21.1 {
/*import dev.ultimatchamp.bettergrass.model.BetterGrassifyUnbakedModel;
*///?}

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

        //? if >1.21.4 {
        ModelLoadingPlugin.register(pluginContext -> pluginContext.modifyBlockModelOnLoad()
        .register(ModelModifier.WRAP_LAST_PHASE, (model, context) -> {
            BlockState state = context.state();
            List<String> blocks = BetterGrassify.getBlocks();

            for (String blockId : blocks) {
                Block block =  Registries.BLOCK.getOptionalValue(Identifier.tryParse(blockId)).orElse(null);

                if (state.isOf(block)) {
                    if (state.getOrEmpty(Properties.SNOWY).orElse(false)) {
                        if (BetterGrassifyConfig.load().snowy) {
                            return new BetterGrassifyUnbakedGroupedBlockStateModel(model);
                        }
                    } else {
                        return new BetterGrassifyUnbakedGroupedBlockStateModel(model);
                    }
                }
            }

            if (state.isOf(Blocks.DIRT)) {
                if (blocks.contains("minecraft:dirt_path") || blocks.contains("minecraft:farmland")) {
                    return new BetterGrassifyUnbakedGroupedBlockStateModel(model);
                }
            }

            return model;
        }));
        //?} else if >1.21.1 {
        /*ModelLoadingPlugin.register(pluginContext -> pluginContext
        //? if >1.21.3 {
        .modifyBlockModelOnLoad()
        //?} else {
        /^.modifyModelOnLoad()
        ^///?}
        .register(ModelModifier.WRAP_LAST_PHASE, (model, context) -> {
            //? if >1.21.3 {
            ModelIdentifier modelId = context.id();
            //?} else {
            /^ModelIdentifier modelId = context.topLevelId();
            ^///?}

            if (!modelId.getVariant().equals("inventory")) {
                List<String> blocks = BetterGrassify.getBlocks();

                for (String block : blocks) {
                    if (modelId.id().toString().equals(block)) {
                        if (modelId.getVariant().contains("snowy=true")) {
                            if (BetterGrassifyConfig.load().snowy) {
                                //? if >1.21.3 {
                                return new BetterGrassifyGroupableModel(model);
                                //?} else {
                                /^return new BetterGrassifyUnbakedModel(model);
                                ^///?}
                            }
                        } else {
                            //? if >1.21.3 {
                            return new BetterGrassifyGroupableModel(model);
                            //?} else {
                            /^return new BetterGrassifyUnbakedModel(model);
                            ^///?}
                        }
                    }
                }

                if (modelId.id().toString().equals("minecraft:dirt")) {
                    if (blocks.contains("minecraft:dirt_path") || blocks.contains("minecraft:farmland")) {
                        //? if >1.21.3 {
                        return new BetterGrassifyGroupableModel(model);
                        //?} else {
                        /^return new BetterGrassifyUnbakedModel(model);
                        ^///?}
                    }
                }
            }

            return model;
        }));
        *///?}
    }
}
//?}
