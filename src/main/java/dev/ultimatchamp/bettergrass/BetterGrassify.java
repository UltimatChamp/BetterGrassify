package dev.ultimatchamp.bettergrass;

import dev.ultimatchamp.bettergrass.compat.WilderWildCompat;
import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirtPathBlock;
import net.minecraft.world.level.block.FarmBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

//? if >1.21.1 {
import dev.ultimatchamp.bettergrass.model.BetterGrassifyUnbakedRootBlockStateModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//?} else {
/*import dev.ultimatchamp.bettergrass.model.BetterGrassifyUnbakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
*///?}

public class BetterGrassify implements ClientModInitializer {
    public static final String MOD_NAME = "BetterGrassify";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitializeClient() {
        BetterGrassifyConfig config = BetterGrassifyConfig.load();

        if (config.general.betterGrassMode.equals(BetterGrassifyConfig.BetterGrassMode.OFF))
            BetterGrassify.LOGGER.info("[{}] Better Grass is disabled! ;(", MOD_NAME);
        else
            BetterGrassify.LOGGER.info("[{}] [{}] Gamers can finally touch grass!?", MOD_NAME, config.general.betterGrassMode);

        if (FabricLoader.getInstance().isModLoaded("wilderwild") && WilderWildCompat.isSnowloggingOn()) {
            config.general.blocks.snowy = false;
            config.betterSnow.betterSnowMode = BetterGrassifyConfig.BetterSnowMode.OFF;
            BetterGrassify.LOGGER.warn("[{}] 'WilderWild' detected. 'Better Snowy Grass' and 'Better Snow' features have been disabled.", MOD_NAME);
        }

        ModelLoadingPlugin.register(pluginContext -> pluginContext./*? if >1.21.1 {*/modifyBlockModelOnLoad()/*?} else {*//*modifyModelOnLoad()*//*?}*/.register(ModelModifier.WRAP_LAST_PHASE, (model, context) -> {
            //? if >1.21.1 {
            BlockState state = context.state();
            //?} else {
            /*ModelResourceLocation modelId = context.topLevelId();
            if (modelId == null || modelId.getVariant().equals("inventory")) return model;
            Block self = BuiltInRegistries.BLOCK.getOptional(modelId.id()).orElse(null);
            if (self == null) return model;
            *///?}
            List<String> blocks = BetterGrassify.getBlocks();

            for (String blockId : blocks) {
                Block block = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.tryParse(blockId)).orElse(null);

                //? if >1.21.1 {
                if (state.is(block)) {
                    if (state.getOptionalValue(BlockStateProperties.SNOWY).orElse(false)) {
                //?} else {
                /*if (self.equals(block)) {
                    if (modelId.getVariant().contains("snowy=true")) {
                        *///?}
                        if (BetterGrassifyConfig.load().general.blocks.snowy) {
                            //? if >1.21.1 {
                            return new BetterGrassifyUnbakedRootBlockStateModel(model);
                            //?} else {
                            /*return new BetterGrassifyUnbakedModel(model);
                            *///?}
                        }
                    } else {
                        //? if >1.21.1 {
                        return new BetterGrassifyUnbakedRootBlockStateModel(model);
                        //?} else {
                        /*return new BetterGrassifyUnbakedModel(model);
                        *///?}
                    }
                //? if >1.21.1 {
                } else if (state.is(Blocks.DIRT)) {
                //?} else {
                /*} else if (self.equals(Blocks.DIRT)) {
                *///?}
                    if (block instanceof DirtPathBlock || block instanceof FarmBlock) {
                        //? if >1.21.1 {
                        return new BetterGrassifyUnbakedRootBlockStateModel(model);
                        //?} else {
                        /*return new BetterGrassifyUnbakedModel(model);
                        *///?}
                    }
                }
            }

            return model;
        }));
    }

    public static List<String> getBlocks() {
        BetterGrassifyConfig.GeneralConfig.BlocksConfig config = BetterGrassifyConfig.load().general.blocks;
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
