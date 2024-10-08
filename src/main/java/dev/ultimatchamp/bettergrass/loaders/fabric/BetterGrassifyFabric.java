//? if fabric {
package dev.ultimatchamp.bettergrass.loaders.fabric;

import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? if <1.21 {
/*import net.fabricmc.loader.api.FabricLoader;
*///?}

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
            LOGGER.warn("[BetterGrassify] Sodium is not installed. 'Better Snow' feature has been disabled.");
        }
        *///?}
    }
}
//?}
