package dev.ultimatchamp.bettergrass;

import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BetterGrassify {
    public static final String MOD_ID = "bettergrass";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static List<String> getBlocks() {
        BetterGrassifyConfig config = BetterGrassifyConfig.load();

        List<String> blocks = Stream.of(
                Map.entry(config.grassBlocks, "minecraft:grass_block"),
                Map.entry(config.dirtPaths, "minecraft:dirt_path"),
                Map.entry(config.farmLands, "minecraft:farmland"),
                Map.entry(config.podzol, "minecraft:podzol"),
                Map.entry(config.mycelium, "minecraft:mycelium"),
                Map.entry(config.crimsonNylium, "minecraft:crimson_nylium"),
                Map.entry(config.warpedNylium, "minecraft:warped_nylium")
        ).filter(Map.Entry::getKey)
        .map(Map.Entry::getValue)
        .collect(Collectors.toList());

        blocks.addAll(config.moreBlocks);

        return blocks;
    }
}
