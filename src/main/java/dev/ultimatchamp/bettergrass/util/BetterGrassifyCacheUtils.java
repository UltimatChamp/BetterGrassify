package dev.ultimatchamp.bettergrass.util;

import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BetterGrassifyCacheUtils {
    public static List<Block> BETTER_SNOW_CACHE = new CopyOnWriteArrayList<>();

    public static List<Pair<Block, String>> EXCLUDED_BLOCKS_CACHE = new CopyOnWriteArrayList<>();
    public static List<TagKey<Block>> EXCLUDED_TAGS_CACHE = new CopyOnWriteArrayList<>();

    public static List<Pair<Block, String>> WHITELISTED_BLOCKS_CACHE = new CopyOnWriteArrayList<>();
    public static List<TagKey<Block>> WHITELISTED_TAGS_CACHE = new CopyOnWriteArrayList<>();

    public static void reloadCaches() {
        BETTER_SNOW_CACHE.clear();

        WHITELISTED_BLOCKS_CACHE.clear();
        WHITELISTED_TAGS_CACHE.clear();

        EXCLUDED_BLOCKS_CACHE.clear();
        EXCLUDED_TAGS_CACHE.clear();
    }

    public static void initCaches() {
        if (!BETTER_SNOW_CACHE.isEmpty()) return;

        BetterGrassifyConfig config = BetterGrassifyConfig.load();

        for (String identifier : config.betterSnow.snowLayers) {
            ResourceLocation id = ResourceLocation.tryParse(identifier);
            BuiltInRegistries.BLOCK.getOptional(id).ifPresent(BETTER_SNOW_CACHE::add);
        }

        for (String block : config.betterSnow.whitelistedBlocks) {
            ResourceLocation id = ResourceLocation.parse(BetterSnowPredicateUtils.withoutAttribute(block));
            BuiltInRegistries.BLOCK.getOptional(id).ifPresent(b -> WHITELISTED_BLOCKS_CACHE.add(Pair.of(b, block)));
        }

        for (String tag : config.betterSnow.whitelistedTags) {
            ResourceLocation id = ResourceLocation.parse(tag);
            WHITELISTED_TAGS_CACHE.add(TagKey.create(Registries.BLOCK, id));
        }

        for (String block : config.betterSnow.excludedBlocks) {
            ResourceLocation id = ResourceLocation.parse(BetterSnowPredicateUtils.withoutAttribute(block));
            BuiltInRegistries.BLOCK.getOptional(id).ifPresent(b -> EXCLUDED_BLOCKS_CACHE.add(Pair.of(b, block)));
        }

        for (String tag : config.betterSnow.excludedTags) {
            ResourceLocation id = ResourceLocation.parse(tag);
            EXCLUDED_TAGS_CACHE.add(TagKey.create(Registries.BLOCK, id));
        }
    }
}
