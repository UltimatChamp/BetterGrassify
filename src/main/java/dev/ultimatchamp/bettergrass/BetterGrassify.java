package dev.ultimatchamp.bettergrass;

import com.google.common.collect.Lists;
import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class BetterGrassify {
    public static final Logger LOGGER = LoggerFactory.getLogger("bettergrass");

    public static List<String> getBlocks() {
        var config = BetterGrassifyConfig.load();
        List<String> blocks = Lists.newArrayList();

        Map<BooleanSupplier, String> defaultBlocks = Map.of(
                () -> config.grassBlocks, "minecraft:grass_block",
                () -> config.dirtPaths, "minecraft:dirt_path",
                () -> config.farmLands, "minecraft:farmland",
                () -> config.podzol, "minecraft:podzol",
                () -> config.mycelium, "minecraft:mycelium",
                () -> config.crimsonNylium, "minecraft:crimson_nylium",
                () -> config.warpedNylium, "minecraft:warped_nylium"
        );

        defaultBlocks.forEach((isEnabled, block) -> {
            if (isEnabled.getAsBoolean()) {
                blocks.add(block);
            }
        });

        blocks.addAll(config.moreBlocks);

        return blocks;
    }
}
