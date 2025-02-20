package dev.ultimatchamp.bettergrass.config;

import net.caffeinemc.mods.sodium.client.gui.options.storage.OptionStorage;

public class SodiumOptionsStorage implements OptionStorage<Object> {
    public static final OptionStorage<?> INSTANCE = new SodiumOptionsStorage();

    @Override
    public Object getData() {
        return new Object();
    }

    @Override
    public void save() {
        BetterGrassifyConfig.save(BetterGrassifyConfig.load());
    }
}
