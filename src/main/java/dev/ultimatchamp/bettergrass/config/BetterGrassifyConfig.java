package dev.ultimatchamp.bettergrass.config;

import com.google.common.collect.Lists;
import dev.isxander.yacl3.api.NameableEnum;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class BetterGrassifyConfig {
    private static final ConfigClassHandler<BetterGrassifyConfig> GSON = ConfigClassHandler.createBuilder(BetterGrassifyConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("bettergrass.json5"))
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry(comment = "General\nOFF/FAST/FANCY (default: FANCY)")
    public BetterGrassMode betterGrassMode = BetterGrassMode.FANCY;

    public enum BetterGrassMode implements NameableEnum {
        OFF("options.off"),
        FAST("options.graphics.fast"),
        FANCY("options.graphics.fancy");

        private final String displayName;

        BetterGrassMode(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public Text getDisplayName() {
            return Text.translatable(displayName);
        }
    }

    @SerialEntry(comment = "(default: true)")
    public boolean snowy = true;

    @SerialEntry(comment = "\nConnected Blocks\n(default: true)")
    public boolean grassBlocks = true;

    @SerialEntry(comment = "(default: false)")
    public boolean dirtPaths = false;

    @SerialEntry(comment = "(default: false)")
    public boolean farmLands = false;

    @SerialEntry(comment = "(default: true)")
    public boolean podzol = true;

    @SerialEntry(comment = "(default: true)")
    public boolean mycelium = true;

    @SerialEntry(comment = "(default: true)")
    public boolean crimsonNylium = true;

    @SerialEntry(comment = "(default: true)")
    public boolean warpedNylium = true;

    @SerialEntry
    public List<String> moreBlocks = Lists.newArrayList();

    @SerialEntry(comment = "\nBetter Snow\nOFF/OPTIFINE/LAMBDA (default: OPTIFINE)")
    public BetterSnowMode betterSnowMode = BetterSnowMode.OPTIFINE;

    public enum BetterSnowMode implements NameableEnum {
        OFF("options.off"),
        OPTIFINE("bettergrass.betterSnowMode.optifine"),
        LAMBDA("bettergrass.betterSnowMode.lambda");

        private final String displayName;

        BetterSnowMode(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public Text getDisplayName() {
            return Text.translatable(displayName);
        }
    }

    @SerialEntry
    public List<String> excludedTags = Lists.newArrayList(
            "leaves",
            "buttons",
            "rails",
            "wooden_pressure_plates"
    );

    @SerialEntry
    public List<String> excludedBlocks = Lists.newArrayList(
            "heavy_weighted_pressure_plate",
            "light_weighted_pressure_plate",
            "polished_blackstone_pressure_plate",
            "stone_pressure_plate"
    );

    public static ConfigClassHandler<BetterGrassifyConfig> handler() {
        return GSON;
    }

    public static void load() {
        GSON.load();
    }

    public static void save() {
        GSON.save();
    }

    public static BetterGrassifyConfig instance() {
        return GSON.instance();
    }

    public static Screen createConfigScreen(Screen parent) {
        //? if !forge {
        return BetterGrassifyGui.createConfigScreen(parent);
        //?} else {
        /*return new NoConfigScreenWarning(parent);
        *///?}
    }
}
