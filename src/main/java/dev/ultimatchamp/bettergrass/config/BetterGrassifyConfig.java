package dev.ultimatchamp.bettergrass.config;

import blue.endless.jankson.*;
import blue.endless.jankson.api.SyntaxError;
import com.google.common.collect.Lists;
import dev.ultimatchamp.bettergrass.BetterGrassify;
import dev.ultimatchamp.bettergrass.model.BetterGrassifyBakedModel;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.OptionEnum;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BetterGrassifyConfig {
    @Comment("-> General\nOFF/FAST/FANCY (default: FANCY)")
    public BetterGrassMode betterGrassMode = BetterGrassMode.FANCY;

    public enum BetterGrassMode implements OptionEnum {
        OFF(0, "options.off"),
        FAST(1, "options.graphics.fast"),
        FANCY(2, "options.graphics.fancy");

        private final int id;
        private final String key;

        BetterGrassMode(final int id, final String key) {
            this.id = id;
            this.key = key;
        }

        public int getId() {
            return this.id;
        }

        public @NotNull String getKey() {
            return this.key;
        }
    }

    @Comment("(default: true)")
    public boolean resourcePackCompatibilityMode = true;

    @Comment("Blocks\n(default: true)")
    public boolean grassBlocks = true;

    @Comment("(default: true)")
    public boolean snowy = true;

    @Comment("(default: true)")
    public boolean dirtPaths = true;

    @Comment("(default: true)")
    public boolean farmLands = true;

    @Comment("(default: true)")
    public boolean podzol = true;

    @Comment("(default: true)")
    public boolean mycelium = true;

    @Comment("(default: true)")
    public boolean crimsonNylium = true;

    @Comment("(default: true)")
    public boolean warpedNylium = true;

    public List<String> moreBlocks = Lists.newArrayList(
            "minecraft:sculk_catalyst" // Example
    );

    @Comment("-> Better Snow\nOFF/OPTIFINE/LAMBDA (default: LAMBDA)")
    public BetterSnowMode betterSnowMode = BetterSnowMode.LAMBDA;

    public enum BetterSnowMode implements OptionEnum {
        OFF(0, "options.off"),
        OPTIFINE(1, "bettergrass.betterSnowMode.optifine"),
        LAMBDA(2, "bettergrass.betterSnowMode.lambda");

        private final int id;
        private final String key;

        BetterSnowMode(final int id, final String key) {
            this.id = id;
            this.key = key;
        }

        @Override
        public int getId() {
            return this.id;
        }

        @Override
        public @NotNull String getKey() {
            return this.key;
        }
    }

    public List<String> snowLayers = Lists.newArrayList(
            "snow",
            "moss_carpet"
            /*? if >1.21.1 {*/, "pale_moss_carpet"/*?}*/
            /*? if >1.21.4 {*/, "leaf_litter"/*?}*/
            , "pink_petals"
            /*? if >1.21.4 {*/, "wildflowers"/*?}*/
    );

    public List<String> excludedTags = Lists.newArrayList();

    public List<String> excludedBlocks = Lists.newArrayList(
            "lantern[hanging]",
            "redstone_wall_torch",
            "soul_lantern[hanging]",
            "soul_wall_torch",
            "wall_torch"
    );

    public List<String> whitelistedTags = new ArrayList<>();
    public List<String> whitelistedBlocks = new ArrayList<>();

    private static final Jankson JANKSON = Jankson.builder().build();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("bettergrass.json5");

    private static BetterGrassifyConfig cachedConfig;

    public static BetterGrassifyConfig load() {
        if (cachedConfig != null) {
            return cachedConfig;
        }

        initializeCaches();

        BetterGrassifyConfig config;

        try {
            if (!Files.exists(CONFIG_PATH)) {
                BetterGrassify.LOGGER.info("[BetterGrassify] Config file not found. Creating a new one...");
                save(config = new BetterGrassifyConfig());
            } else {
                String configContent = Files.readString(CONFIG_PATH).trim();

                if (!configContent.startsWith("{") || !configContent.endsWith("}")) {
                    BetterGrassify.LOGGER.warn("[BetterGrassify] Config file is empty or invalid. Creating a new one...");
                    save(config = new BetterGrassifyConfig());
                } else {
                    JsonObject configJson = ensureDefaults(JANKSON.load(configContent));
                    config = JANKSON.fromJson(configJson, BetterGrassifyConfig.class);
                }
            }
        } catch (IOException | SyntaxError e) {
            BetterGrassify.LOGGER.error("[BetterGrassify]", e);
            save(config = new BetterGrassifyConfig());
        }

        return cachedConfig = config;
    }

    public static void initializeCaches() {
        BetterGrassifyBakedModel.BETTER_SNOW_CACHE = new CopyOnWriteArrayList<>();

        BetterGrassifyBakedModel.EXCLUDED_BLOCKS_CACHE = new CopyOnWriteArrayList<>();
        BetterGrassifyBakedModel.EXCLUDED_TAGS_CACHE = new CopyOnWriteArrayList<>();

        BetterGrassifyBakedModel.WHITELISTED_BLOCKS_CACHE = new CopyOnWriteArrayList<>();
        BetterGrassifyBakedModel.WHITELISTED_TAGS_CACHE = new CopyOnWriteArrayList<>();
    }

    public static void save(BetterGrassifyConfig config) {
        try {
            String jsonString = JANKSON.toJson(config).toJson(true, true);
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, jsonString);
            cachedConfig = config;
        } catch (IOException e) {
            BetterGrassify.LOGGER.error("[BetterGrassify]", e);
        }
    }

    private static JsonObject ensureDefaults(JsonObject configJson) {
        boolean modified = false;
        BetterGrassifyConfig defaultConfig = new BetterGrassifyConfig();

        for (Field field : BetterGrassifyConfig.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;

            try {
                String fieldName = field.getName();
                Object defaultValue = field.get(defaultConfig);

                if (!configJson.containsKey(fieldName)) {
                    BetterGrassify.LOGGER.info("[BetterGrassify] Missing config field '{}'. Re-saving as default.", fieldName);
                    configJson.put(fieldName, JANKSON.toJson(defaultValue));
                    modified = true;
                }
            } catch (IllegalAccessException e) {
                BetterGrassify.LOGGER.error("[BetterGrassify] Failed to access field '{}'", field.getName(), e);
            }
        }

        if (modified) {
            BetterGrassifyConfig config = JANKSON.fromJson(configJson, BetterGrassifyConfig.class);
            save(config);
        }

        return configJson;
    }

    public static Screen createConfigScreen(Screen parent) {
        if (FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3")) {
            return BetterGrassifyGui.createConfigScreen(parent);
        } else {
            return new NoYACLWarning(parent);
        }
    }
}
