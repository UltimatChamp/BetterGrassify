package dev.ultimatchamp.bettergrass.config;

public class SodiumOptionsStorage {
    private final BetterGrassifyConfig config;

    public SodiumOptionsStorage() {
        this.config = BetterGrassifyConfig.load();
    }

    public BetterGrassifyConfig getData() {
        return this.config;
    }

    public void save() {
        BetterGrassifyConfig.save(this.config);
        BetterGrassifyConfig.cachedConfig = null;
    }
}
