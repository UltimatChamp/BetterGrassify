package dev.ultimatchamp.bettergrass.config;

/*? if <1.21.11 {*//*import net.caffeinemc.mods.sodium.client.gui.options.storage.OptionStorage;*//*?}*/

public class SodiumOptionsStorage
    /*? if <1.21.11 {*//*implements OptionStorage<BetterGrassifyConfig>*//*?}*/ {
    private final BetterGrassifyConfig config;

    public SodiumOptionsStorage() {
        this.config = BetterGrassifyConfig.load();
    }

    /*? if <1.21.11 {*//*@Override*//*?}*/
    public BetterGrassifyConfig getData() {
        return this.config;
    }

    /*? if <1.21.11 {*//*@Override*//*?}*/
    public void save() {
        BetterGrassifyConfig.save(this.config);
        BetterGrassifyConfig.cachedConfig = null;
    }
}
