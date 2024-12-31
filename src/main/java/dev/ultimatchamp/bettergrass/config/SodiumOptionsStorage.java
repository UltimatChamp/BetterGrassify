package dev.ultimatchamp.bettergrass.config;

//? if >1.20.6 {
import net.caffeinemc.mods.sodium.client.gui.options.storage.OptionStorage;
//?} else {
/*import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
*///?}

public class SodiumOptionsStorage implements OptionStorage<Object> {
    public static final OptionStorage<?> INSTANCE = new SodiumOptionsStorage();

    @Override
    public Object getData() {
        return new Object();
    }

    @Override
    public void save() {
        var config = BetterGrassifyConfig.load();
        BetterGrassifyConfig.save(config);
    }
}
