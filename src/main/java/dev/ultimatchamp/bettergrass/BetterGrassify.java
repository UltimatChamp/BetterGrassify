package dev.ultimatchamp.bettergrass;

import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BetterGrassify {
    public static final String MOD_ID = "bettergrass";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static List<String> getBlocks() {
        BetterGrassifyConfig config = BetterGrassifyConfig.load();

        List<String> blocks = new ArrayList<>();

        if (config.grassBlocks) blocks.add("minecraft:grass_block");
        if (config.dirtPaths) blocks.add("minecraft:dirt_path");
        if (config.farmLands) blocks.add("minecraft:farmland");
        if (config.podzol) blocks.add("minecraft:podzol");
        if (config.mycelium) blocks.add("minecraft:mycelium");
        if (config.crimsonNylium) blocks.add("minecraft:crimson_nylium");
        if (config.warpedNylium) blocks.add("minecraft:warped_nylium");

        blocks.addAll(config.moreBlocks);

        return blocks;
    }
}
