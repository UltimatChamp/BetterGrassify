package dev.ultimatchamp.bettergrass.util;

import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BetterSnowPredicateUtils {
    public static boolean isBlockWhitelisted(BlockState self) {
        return BetterGrassifyCacheUtils.WHITELISTED_BLOCKS_CACHE.stream()
                .anyMatch(e -> matchesBlock(self, e.first(), e.second()));
    }

    public static boolean isTagWhitelisted(BlockState self) {
        return BetterGrassifyCacheUtils.WHITELISTED_TAGS_CACHE.stream().anyMatch(self::is);
    }

    public static boolean isBlockExcluded(BlockState self) {
        return BetterGrassifyCacheUtils.EXCLUDED_BLOCKS_CACHE.stream()
                .anyMatch(e -> matchesBlock(self, e.first(), e.second()));
    }

    public static boolean isTagExcluded(BlockState self) {
        return BetterGrassifyCacheUtils.EXCLUDED_TAGS_CACHE.stream().anyMatch(self::is);
    }

    public static boolean matchesBlock(BlockState self, Block block, String blockName) {
        if (!self.getBlock().equals(block)) return false;
        if (!blockName.contains("[")) return true;

        String attribute = blockName.substring(blockName.indexOf("[") + 1, blockName.indexOf("]")).replaceAll(" ", "");
        if (attribute.isEmpty()) return true;

        if (!attribute.contains("=")) {
            if (attribute.startsWith("!"))
                attribute = attribute.substring(1) + "=false";
            else
                attribute += "=true";
        }

        String attr = attribute;
        return self.getValues().entrySet().stream()
                .anyMatch(p -> (p.getKey().getName() + "=" + p.getValue()).equalsIgnoreCase(attr));
    }

    public static String withoutAttribute(String block) {
        int attributeIndex = block.indexOf("[");
        return attributeIndex > 0 ? block.substring(0, attributeIndex) : block;
    }

    public static boolean isWhitelistOn() {
        return !BetterGrassifyConfig.load().betterSnow.whitelistedBlocks.isEmpty() || !BetterGrassifyConfig.load().betterSnow.whitelistedTags.isEmpty();
    }
}
