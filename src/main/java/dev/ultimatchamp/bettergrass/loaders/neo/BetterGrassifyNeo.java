//? if neo {
/*package dev.ultimatchamp.bettergrass.loaders.neo;

import dev.ultimatchamp.bettergrass.BetterGrassify;
import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod("bettergrass")
public final class BetterGrassifyNeo {
    public BetterGrassifyNeo(ModContainer modContainer, IEventBus modBus) {
        modBus.addListener(this::onClientSetup);
        BetterGrassifyConfig.load();

        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (client, parent) -> BetterGrassifyConfig.createConfigScreen(parent));
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        if (BetterGrassifyConfig.instance().betterGrassMode == BetterGrassifyConfig.BetterGrassMode.OFF) {
            BetterGrassify.LOGGER.info("[BetterGrassify] Better Grass is disabled.");
        } else {
            BetterGrassify.LOGGER.info("[BetterGrassify] [{}] Gamers can finally touch grass!?", BetterGrassifyConfig.instance().betterGrassMode.toString());
        }

        if (FabricLoader.getInstance().isModLoaded("wilderwild")) {
            BetterGrassifyConfig.instance().snowy = false;
            BetterGrassifyConfig.instance().betterSnowMode = BetterGrassifyConfig.BetterSnowMode.OFF;
            BetterGrassify.LOGGER.warn("[BetterGrassify] WilderWild detected. 'Better Snowy Grass' and 'Better Snow' features have been disabled.");
        }
    }
}
*///?}
