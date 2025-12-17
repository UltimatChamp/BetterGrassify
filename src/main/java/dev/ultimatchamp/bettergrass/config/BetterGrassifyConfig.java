package dev.ultimatchamp.bettergrass.config;

import com.google.common.collect.Lists;
import com.google.gson.*;
import dev.ultimatchamp.bettergrass.BetterGrassify;
import dev.ultimatchamp.bettergrass.util.BetterGrassifyCacheUtils;
import it.unimi.dsi.fastutil.Pair;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class BetterGrassifyConfig {
    public GeneralConfig general = new GeneralConfig();
    public BetterSnowConfig betterSnow = new BetterSnowConfig();

    public static class GeneralConfig {
        public BetterGrassMode betterGrassMode = BetterGrassMode.FANCY;
        public boolean resourcePackCompatibilityMode = true;
        public BlocksConfig blocks = new BlocksConfig();

        public static class BlocksConfig {
            public boolean grassBlocks = true;
            public boolean snowy = true;
            public boolean dirtPaths = true;
            public boolean farmLands = true;
            public boolean podzol = true;
            public boolean mycelium = true;
            public boolean crimsonNylium = true;
            public boolean warpedNylium = true;

            public List<String> moreBlocks = Lists.newArrayList();
        }
    }

    public static class BetterSnowConfig {
        public BetterSnowMode betterSnowMode = BetterSnowMode.LAMBDA;
        public List<String> snowLayers = Lists.newArrayList(
                "snow",
                "moss_carpet",
                "pink_petals"
                //? if >1.21.1 {
                , "pale_moss_carpet",
                "leaf_litter",
                "wildflowers"
                //?}
        );

        public List<String> whitelistedTags = Lists.newArrayList();
        public List<String> whitelistedBlocks = Lists.newArrayList();

        public List<String> excludedTags = Lists.newArrayList();
        public List<String> excludedBlocks = Lists.newArrayList(
                "lantern[hanging]",
                "redstone_wall_torch",
                "soul_lantern[hanging]",
                "soul_wall_torch",
                "wall_torch"
        );
    }

    public enum BetterGrassMode implements TranslatableOption {
        OFF("options.off"),
        FAST("options.graphics.fast"),
        FANCY("options.graphics.fancy");

        private final String key;

        BetterGrassMode(final String key) {
            this.key = key;
        }

        public @NotNull String getKey() {
            return this.key;
        }
    }

    public enum BetterSnowMode implements TranslatableOption {
        OFF("options.off"),
        OPTIFINE("bettergrass.betterSnow.betterSnowMode.optifine"),
        LAMBDA("bettergrass.betterSnow.betterSnowMode.lambda");

        private final String key;

        BetterSnowMode(final String key) {
            this.key = key;
        }

        @Override
        public @NotNull String getKey() {
            return this.key;
        }
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("bettergrass.json");

    public static BetterGrassifyConfig cachedConfig;

    public static BetterGrassifyConfig load() {
        if (cachedConfig != null) return cachedConfig;

        BetterGrassifyCacheUtils.reloadCaches();

        BetterGrassifyConfig config;
        try {
            if (Files.notExists(CONFIG_PATH)) {
                BetterGrassify.LOGGER.info("[{}] Config file not found. Creating a new one...", BetterGrassify.MOD_NAME);
                save(config = new BetterGrassifyConfig());
            } else {
                String configContent = Files.readString(CONFIG_PATH).trim();

                if (!configContent.startsWith("{") || !configContent.endsWith("}")) {
                    BetterGrassify.LOGGER.warn("[{}] Config file is empty or invalid. Creating a new one...", BetterGrassify.MOD_NAME);
                    save(config = new BetterGrassifyConfig());
                } else {
                    JsonObject configJson = ensureDefaults(JsonParser.parseString(configContent).getAsJsonObject(),
                            GSON.toJsonTree(new BetterGrassifyConfig()).getAsJsonObject()).first();
                    config = GSON.fromJson(configJson, BetterGrassifyConfig.class);
                }
            }
        } catch (Exception e) {
            BetterGrassify.LOGGER.error("[{}] Failed to read config", BetterGrassify.MOD_NAME, e);
            save(config = new BetterGrassifyConfig());
        }

        return cachedConfig = config;
    }

    public static void save(BetterGrassifyConfig config) {
        try {
            String jsonString = GSON.toJson(config);
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, jsonString);
        } catch (IOException e) {
            BetterGrassify.LOGGER.error("[{}] Failed to write config", BetterGrassify.MOD_NAME, e);
        }
    }

    private static Pair<JsonObject, Boolean> ensureDefaults(JsonObject configJson, JsonObject defaultJson) {
        boolean modified = false;

        for (Map.Entry<String, JsonElement> entry : defaultJson.entrySet()) {
            String key = entry.getKey();
            JsonElement defaultValue = entry.getValue();

            if (!configJson.has(key)) {
                BetterGrassify.LOGGER.warn("[{}] Missing config field '{}'. Re-saving as default.", BetterGrassify.MOD_NAME, key);
                configJson.add(key, defaultValue);
                modified = true;
            } else {
                JsonElement currentValue = configJson.get(key);

                if (defaultValue.isJsonObject() && currentValue.isJsonObject()) {
                    Pair<JsonObject, Boolean> updated = ensureDefaults(currentValue.getAsJsonObject(), defaultValue.getAsJsonObject());
                    if (updated.second()) {
                        configJson.add(key, updated.first());
                        modified = true;
                    }
                }
            }
        }

        if (modified) save(GSON.fromJson(configJson, BetterGrassifyConfig.class));

        return Pair.of(configJson, modified);
    }

    public static Screen createConfigScreen(Screen parent) {
        if (FabricLoader.getInstance().isModLoaded("cloth-config"))
            return OptionsScreen.createConfigScreen(parent);
        else return new NoClothConfigWarning(parent);
    }
}
