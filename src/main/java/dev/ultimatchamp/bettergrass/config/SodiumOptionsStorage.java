package dev.ultimatchamp.bettergrass.config;

import net.caffeinemc.mods.sodium.client.gui.options.storage.OptionStorage;

public class SodiumOptionsStorage implements OptionStorage<BetterGrassifyConfig> {
    private final BetterGrassifyConfig config;

    public SodiumOptionsStorage() {
        this.config = BetterGrassifyConfig.load();
    }

    @Override
    public BetterGrassifyConfig getData() {
        return this.config;
    }

    @Override
    public void save() {
        BetterGrassifyConfig.save(this.config);
        BetterGrassifyConfig.cachedConfig = null;
    }
}
