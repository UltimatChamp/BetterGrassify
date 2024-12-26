package dev.ultimatchamp.bettergrass.config;

import blue.endless.jankson.*;
import blue.endless.jankson.api.SyntaxError;
import com.google.common.collect.Lists;
import dev.ultimatchamp.bettergrass.BetterGrassify;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.TranslatableOption;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class BetterGrassifyConfig {
    @Comment("-> General\nOFF/FAST/FANCY (default: FANCY)")
    public BetterGrassMode betterGrassMode = BetterGrassMode.FANCY;

    public enum BetterGrassMode implements TranslatableOption {
        OFF(0, "options.off"),
        FAST(1, "options.graphics.fast"),
        FANCY(2, "options.graphics.fancy");

        private final int id;
        private final String translationKey;

        BetterGrassMode(final int id, final String translationKey) {
            this.id = id;
            this.translationKey = translationKey;
        }

        public int getId() {
            return this.id;
        }

        public String getTranslationKey() {
            return this.translationKey;
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

    @Comment("-> Better Snow\nOFF/OPTIFINE/LAMBDA (default: OPTIFINE)")
    public BetterSnowMode betterSnowMode = BetterSnowMode.OPTIFINE;

    public enum BetterSnowMode implements TranslatableOption {
        OFF(0, "options.off"),
        OPTIFINE(1, "bettergrass.betterSnowMode.optifine"),
        LAMBDA(2, "bettergrass.betterSnowMode.lambda");

        private final int id;
        private final String translationKey;

        BetterSnowMode(final int id, final String translationKey) {
            this.id = id;
            this.translationKey = translationKey;
        }

        @Override
        public int getId() {
            return this.id;
        }

        @Override
        public String getTranslationKey() {
            return this.translationKey;
        }
    }

    public List<String> snowLayers = Lists.newArrayList(
            "snow",
            "moss_carpet"
            //? if >1.21.1 {
            , "pale_moss_carpet"
            //?}
    );

    public List<String> excludedTags = Lists.newArrayList(
            "buttons",
            "doors",
            "fire",
            "leaves",
            "pressure_plates",
            "rails"
    );

    public List<String> excludedBlocks = Lists.newArrayList(
            "lantern[hanging]",
            "redstone_wall_torch",
            "soul_lantern[hanging]",
            "soul_wall_torch",
            "wall_torch"
    );

    public List<String> whitelistedTags = Lists.newArrayList();
    public List<String> whitelistedBlocks = Lists.newArrayList();

    private static final Jankson JANKSON = Jankson.builder().build();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("bettergrass.json5");

    private static BetterGrassifyConfig cachedConfig;

    public static BetterGrassifyConfig load() {
        if (cachedConfig != null) {
            return cachedConfig;
        }

        BetterGrassifyConfig config;

        try {
            if (!Files.exists(CONFIG_PATH)) {
                BetterGrassify.LOGGER.info("[BetterGrassify] No config file found. Creating one...");
                config = new BetterGrassifyConfig();
                save(config);
            } else {
                var configContent = Files.readString(CONFIG_PATH);
                var configJson = JANKSON.load(configContent);
                configJson = ensureDefaults(configJson);
                config = JANKSON.fromJson(configJson, BetterGrassifyConfig.class);
            }
        } catch (IOException | SyntaxError e) {
            BetterGrassify.LOGGER.error("[BetterGrassify]", e);
            config = new BetterGrassifyConfig();
            save(config);
        }

        cachedConfig = config;
        return cachedConfig;
    }

    public static void save(BetterGrassifyConfig config) {
        try {
            var jsonString = JANKSON.toJson(config).toJson(true, true);
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, jsonString);
            cachedConfig = config;
        } catch (IOException e) {
            BetterGrassify.LOGGER.error("[BetterGrassify]", e);
        }
    }

    private static JsonObject ensureDefaults(JsonObject configJson) {
        var defaults = getDefaultConfig();
        return applyDefaults(configJson, defaults);
    }

    private static JsonObject applyDefaults(JsonObject targetJson, JsonObject defaults) {
        var newJson = new JsonObject();
        newJson.putAll(targetJson);

        var modified = new AtomicBoolean(false);
        defaults.forEach((key, value) -> {
            if (!newJson.containsKey(key)) {
                BetterGrassify.LOGGER.info("[BetterGrassify] Missing config field '{}'. Re-saving as default.", key);
                newJson.put(key, value);
                modified.set(true);
            }
        });

        if (modified.get()) {
            var config = JANKSON.fromJson(newJson, BetterGrassifyConfig.class);
            save(config);
        }

        return newJson;
    }

    private static JsonObject getDefaultConfig() {
        Map<String, JsonElement> defaults = new HashMap<>();

        defaults.put("betterGrassMode", new JsonPrimitive(BetterGrassMode.FANCY.name()));
        defaults.put("resourcePackCompatibilityMode", new JsonPrimitive(true));

        defaults.put("grassBlocks", new JsonPrimitive(true));
        defaults.put("snowy", new JsonPrimitive(true));
        defaults.put("dirtPaths", new JsonPrimitive(true));
        defaults.put("farmLands", new JsonPrimitive(true));
        defaults.put("podzol", new JsonPrimitive(true));
        defaults.put("mycelium", new JsonPrimitive(true));
        defaults.put("crimsonNylium", new JsonPrimitive(true));
        defaults.put("warpedNylium", new JsonPrimitive(true));

        defaults.put("moreBlocks", createJsonArray("minecraft:sculk_catalyst"));

        defaults.put("betterSnowMode", new JsonPrimitive(BetterSnowMode.OPTIFINE.name()));

        defaults.put("snowLayers", createJsonArray(
                "snow",
                "moss_carpet"
                //? if >1.21.1 {
                , "pale_moss_carpet"
                //?}
        ));

        defaults.put("excludedTags", createJsonArray("buttons",
                "doors",
                "fire",
                "leaves",
                "pressure_plates",
                "rails"
        ));

        defaults.put("excludedBlocks", createJsonArray(
                "lantern[hanging]",
                "redstone_wall_torch",
                "soul_lantern[hanging]",
                "soul_wall_torch",
                "wall_torch"
        ));

        defaults.put("whitelistedTags", new JsonArray());
        defaults.put("whitelistedBlocks", new JsonArray());

        var jsonObject = new JsonObject();
        jsonObject.putAll(defaults);

        return jsonObject;
    }

    private static JsonArray createJsonArray(String... elements) {
        JsonArray jsonArray = new JsonArray();
        Arrays.stream(elements).map(JsonPrimitive::new).forEach(jsonArray::add);
        return jsonArray;
    }

    public static Screen createConfigScreen(Screen parent) {
        //? if !forge {
        if (FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3")) {
            return BetterGrassifyGui.createConfigScreen(parent);
        } else {
            return new NoYACLWarning(parent);
        }
        //?} else {
        /*return new NoConfigScreenWarning(parent);
        *///?}
    }
}
