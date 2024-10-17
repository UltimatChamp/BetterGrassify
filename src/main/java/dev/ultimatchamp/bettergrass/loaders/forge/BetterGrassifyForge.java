//? if forge {
/*package dev.ultimatchamp.bettergrass.loaders.forge;

import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("bettergrass")
public final class BetterGrassifyForge {
    public static final Logger LOGGER = LoggerFactory.getLogger("bettergrass");

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
            LOGGER.info("[BetterGrassify] Better Grass is disabled.");
        } else {
            LOGGER.info("[BetterGrassify] [{}] Gamers can finally touch grass!?", BetterGrassifyConfig.instance().betterGrassMode.toString());
        }

        if (!FabricLoader.getInstance().isModLoaded("embeddium")) {
            LOGGER.warn("[BetterGrassify] Embeddium is not installed. 'Better Snow' feature has been disabled.");
        }
    }
}
*///?}
