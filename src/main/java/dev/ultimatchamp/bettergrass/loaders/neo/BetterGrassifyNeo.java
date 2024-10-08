//? if neo {
/*package dev.ultimatchamp.bettergrass;

import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("bettergrass")
public final class BetterGrassifyNeo {
    public static final Logger LOGGER = LoggerFactory.getLogger("bettergrass");

    public BetterGrassifyNeo(ModContainer modContainer, IEventBus modBus) {
        modBus.addListener(this::onClientSetup);
        BetterGrassifyConfig.load();

        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (client, parent) -> BetterGrassifyConfig.createConfigScreen(parent));
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        if (BetterGrassifyConfig.instance().betterGrassMode == BetterGrassifyConfig.BetterGrassMode.OFF) {
            LOGGER.info("[BetterGrassify] Better Grass is disabled.");
        } else {
            LOGGER.info("[BetterGrassify] [{}] Gamers can finally touch grass!?", BetterGrassifyConfig.instance().betterGrassMode.toString());
        }

        if (!FabricLoader.getInstance().isModLoaded("sodium")) {
            LOGGER.warn("[BetterGrassify] Sodium is not installed. 'Better Snow' feature has been disabled.");
        }
    }
}
*///?}
