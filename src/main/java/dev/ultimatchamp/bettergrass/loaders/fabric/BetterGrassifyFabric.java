//? if fabric {
package dev.ultimatchamp.bettergrass.loaders.fabric;

import dev.ultimatchamp.bettergrass.BetterGrassify;
import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

//? if >1.21.3 {
import dev.ultimatchamp.bettergrass.model.BetterGrassifyGroupableModel;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
//?}

public class BetterGrassifyFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        var config = BetterGrassifyConfig.load();

        if (config.betterGrassMode == BetterGrassifyConfig.BetterGrassMode.OFF) {
            BetterGrassify.LOGGER.info("[BetterGrassify] Better Grass is disabled.");
        } else {
            BetterGrassify.LOGGER.info("[BetterGrassify] [{}] Gamers can finally touch grass!?", config.betterGrassMode.toString());
        }

        //? if <1.21 {
        /*if (!FabricLoader.getInstance().isModLoaded("sodium")) {
            config.betterSnowMode = BetterGrassifyConfig.BetterSnowMode.OFF;
            BetterGrassify.LOGGER.warn("[BetterGrassify] Sodium is not installed. 'Better Snow' feature has been disabled.");
        }
        *///?}

        if (FabricLoader.getInstance().isModLoaded("wilderwild")) {
            config.snowy = false;
            config.betterSnowMode = BetterGrassifyConfig.BetterSnowMode.OFF;
            BetterGrassify.LOGGER.warn("[BetterGrassify] WilderWild detected. 'Better Snowy Grass' and 'Better Snow' features have been disabled.");
        }

        //? if >1.21.3 {
        ModelLoadingPlugin.register(pluginContext -> pluginContext.modifyBlockModelOnLoad().register(ModelModifier.WRAP_LAST_PHASE, (model, context) -> {
            var modelId = context.id();

            if (!modelId.getVariant().equals("inventory")) {
                var blocks = BetterGrassify.getBlocks();

                for (String block : blocks) {
                    if (modelId.id().toString().equals(block)) {
                        if (modelId.getVariant().contains("snowy=true")) {
                            if (BetterGrassifyConfig.load().snowy) {
                                return new BetterGrassifyGroupableModel(model);
                            }
                        } else {
                            return new BetterGrassifyGroupableModel(model);
                        }
                    }
                }

                if (blocks.contains("minecraft:dirt_path") || blocks.contains("minecraft:farmland")) {
                    if (modelId.id().toString().equals("minecraft:dirt")) {
                        return new BetterGrassifyGroupableModel(model);
                    }
                }
            }

            return model;
        }));
        //?}
    }
}
//?}
