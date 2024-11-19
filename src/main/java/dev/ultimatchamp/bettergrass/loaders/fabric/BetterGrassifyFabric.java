//? if fabric {
package dev.ultimatchamp.bettergrass.loaders.fabric;

import dev.ultimatchamp.bettergrass.BetterGrassify;
import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class BetterGrassifyFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BetterGrassifyConfig.load();

        if (BetterGrassifyConfig.instance().betterGrassMode == BetterGrassifyConfig.BetterGrassMode.OFF) {
            BetterGrassify.LOGGER.info("[BetterGrassify] Better Grass is disabled.");
        } else {
            BetterGrassify.LOGGER.info("[BetterGrassify] [{}] Gamers can finally touch grass!?", BetterGrassifyConfig.instance().betterGrassMode.toString());
        }

        //? if <1.21 {
        /*if (!FabricLoader.getInstance().isModLoaded("sodium")) {
            BetterGrassifyConfig.instance().betterSnowMode = BetterGrassifyConfig.BetterSnowMode.OFF;
            BetterGrassify.LOGGER.warn("[BetterGrassify] Sodium is not installed. 'Better Snow' feature has been disabled.");
        }
        *///?}

        if (FabricLoader.getInstance().isModLoaded("wilderwild")) {
            BetterGrassifyConfig.instance().snowy = false;
            BetterGrassifyConfig.instance().betterSnowMode = BetterGrassifyConfig.BetterSnowMode.OFF;
            BetterGrassify.LOGGER.warn("[BetterGrassify] WilderWild detected. 'Better Snowy Grass' and 'Better Snow' features have been disabled.");
        }
    }
}
//?}
