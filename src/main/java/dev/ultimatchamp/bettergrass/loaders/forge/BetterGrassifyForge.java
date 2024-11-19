//? if forge {
/*package dev.ultimatchamp.bettergrass.loaders.forge;

import dev.ultimatchamp.bettergrass.BetterGrassify;
import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("bettergrass")
public final class BetterGrassifyForge {
    public BetterGrassifyForge() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        BetterGrassifyConfig.load();

        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () ->  new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> BetterGrassifyConfig.createConfigScreen(parent))
        );
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        if (BetterGrassifyConfig.instance().betterGrassMode == BetterGrassifyConfig.BetterGrassMode.OFF) {
            BetterGrassify.LOGGER.info("[BetterGrassify] Better Grass is disabled.");
        } else {
            BetterGrassify.LOGGER.info("[BetterGrassify] [{}] Gamers can finally touch grass!?", BetterGrassifyConfig.instance().betterGrassMode.toString());
        }

        if (!FabricLoader.getInstance().isModLoaded("embeddium")) {
            BetterGrassifyConfig.instance().betterSnowMode = BetterGrassifyConfig.BetterSnowMode.OFF;
            BetterGrassify.LOGGER.warn("[BetterGrassify] Embeddium is not installed. 'Better Snow' feature has been disabled.");
        }

        if (FabricLoader.getInstance().isModLoaded("wilderwild")) {
            BetterGrassifyConfig.instance().snowy = false;
            BetterGrassifyConfig.instance().betterSnowMode = BetterGrassifyConfig.BetterSnowMode.OFF;
            BetterGrassify.LOGGER.warn("[BetterGrassify] WilderWild detected. 'Better Snowy Grass' and 'Better Snow' features have been disabled.");
        }
    }
}
*///?}
