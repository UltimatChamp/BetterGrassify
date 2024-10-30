//? if fabric {
package dev.ultimatchamp.bettergrass.loaders.fabric;

import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterGrassifyFabric implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("bettergrass");

    @Override
    public void onInitializeClient() {
        BetterGrassifyConfig.load();

        if (BetterGrassifyConfig.instance().betterGrassMode == BetterGrassifyConfig.BetterGrassMode.OFF) {
            LOGGER.info("[BetterGrassify] Better Grass is disabled.");
        } else {
            LOGGER.info("[BetterGrassify] [{}] Gamers can finally touch grass!?", BetterGrassifyConfig.instance().betterGrassMode.toString());
        }

        //? if <1.21 {
        /*if (!FabricLoader.getInstance().isModLoaded("sodium")) {
            BetterGrassifyConfig.instance().betterSnowMode = BetterGrassifyConfig.BetterSnowMode.OFF;
            LOGGER.warn("[BetterGrassify] Sodium is not installed. 'Better Snow' feature has been disabled.");
        }
        *///?}

        if (FabricLoader.getInstance().isModLoaded("wilderwild")) {
            BetterGrassifyConfig.instance().snowy = false;
            BetterGrassifyConfig.instance().betterSnowMode = BetterGrassifyConfig.BetterSnowMode.OFF;
            LOGGER.warn("[BetterGrassify] WilderWild detected. 'Better Snowy Grass' and 'Better Snow' features have been disabled.");
        }
    }
}
//?}
