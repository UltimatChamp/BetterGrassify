//? if fabric {
package dev.ultimatchamp.bettergrass.loaders.fabric;

import dev.ultimatchamp.bettergrass.BetterGrassify;
import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

//? if >1.21.1 {
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.minecraft.client.util.ModelIdentifier;
//?}

//? if >1.21.3 {
import dev.ultimatchamp.bettergrass.model.BetterGrassifyGroupableModel;
//?} else if >1.21.1 {
/*import dev.ultimatchamp.bettergrass.model.BetterGrassifyUnbakedModel;
*///?}

import java.util.List;

public class BetterGrassifyFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BetterGrassifyConfig config = BetterGrassifyConfig.load();

        if (config.betterGrassMode == BetterGrassifyConfig.BetterGrassMode.OFF) {
            BetterGrassify.LOGGER.info("[BetterGrassify] Better Grass is disabled.");
        } else {
            BetterGrassify.LOGGER.info("[BetterGrassify] [{}] Gamers can finally touch grass!?",
                                       config.betterGrassMode.toString());
        }

        if (FabricLoader.getInstance().isModLoaded("wilderwild")) {
            config.snowy = false;
            config.betterSnowMode = BetterGrassifyConfig.BetterSnowMode.OFF;
            BetterGrassify.LOGGER.warn("[BetterGrassify] WilderWild detected. " +
                                       "'Better Snowy Grass' and 'Better Snow' features have been disabled.");
        }

        ModelLoadingPlugin.register(pluginContext -> pluginContext
        //? if >1.21.3 {
        .modifyBlockModelOnLoad()
        //?} else if >1.21.1 {
        /*.modifyModelOnLoad()
        *///?}
        .register(ModelModifier.WRAP_LAST_PHASE, (model, context) -> {
            //? if >1.21.3 {
            ModelIdentifier modelId = context.id();
            //?} else if >1.21.1 {
            /*ModelIdentifier modelId = context.topLevelId()
            *///?}

            if (!modelId.getVariant().equals("inventory")) {
                List<String> blocks = BetterGrassify.getBlocks();

                for (String block : blocks) {
                    if (modelId.id().toString().equals(block)) {
                        if (modelId.getVariant().contains("snowy=true")) {
                            if (BetterGrassifyConfig.load().snowy) {
                                //? if >1.21.3 {
                                return new BetterGrassifyGroupableModel(model);
                                //?} else if >1.21.1 {
                                /*return new BetterGrassifyUnbakedModel(model);
                                *///?}
                            }
                        } else {
                            //? if >1.21.3 {
                            return new BetterGrassifyGroupableModel(model);
                            //?} else if >1.21.1 {
                            /*return new BetterGrassifyUnbakedModel(model);
                            *///?}
                        }
                    }
                }

                if (blocks.contains("minecraft:dirt_path") || blocks.contains("minecraft:farmland")) {
                    if (modelId.id().toString().equals("minecraft:dirt")) {
                        //? if >1.21.3 {
                        return new BetterGrassifyGroupableModel(model);
                        //?} else if >1.21.1 {
                        /*return new BetterGrassifyUnbakedModel(model);
                        *///?}
                    }
                }
            }

            return model;
        }));
    }
}
//?}
