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
        List<String> blocks = Lists.newArrayList();

        Map<BooleanSupplier, String> defaultBlocks = Map.of(
                () -> BetterGrassifyConfig.instance().grassBlocks, "minecraft:grass_block",
                () -> BetterGrassifyConfig.instance().dirtPaths, "minecraft:dirt_path",
                () -> BetterGrassifyConfig.instance().farmLands, "minecraft:farmland",
                () -> BetterGrassifyConfig.instance().podzol, "minecraft:podzol",
                () -> BetterGrassifyConfig.instance().mycelium, "minecraft:mycelium",
                () -> BetterGrassifyConfig.instance().crimsonNylium, "minecraft:crimson_nylium",
                () -> BetterGrassifyConfig.instance().warpedNylium, "minecraft:warped_nylium"
        );

        defaultBlocks.forEach((isEnabled, block) -> {
            if (isEnabled.getAsBoolean()) {
                blocks.add(block);
            }
        });

        blocks.addAll(BetterGrassifyConfig.instance().moreBlocks);

        return blocks;
    }
}
