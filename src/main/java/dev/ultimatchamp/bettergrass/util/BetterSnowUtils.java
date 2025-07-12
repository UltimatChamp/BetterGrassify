package dev.ultimatchamp.bettergrass.util;

import dev.ultimatchamp.bettergrass.compat.WilderWildCompat;
import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BetterSnowUtils {
    public static BlockState getLayerNeighbour(BlockAndTintGetter world, BlockPos selfPos) {
        BetterGrassifyCacheUtils.initCaches();

        for (Block layer : BetterGrassifyCacheUtils.BETTER_SNOW_CACHE) {
            BlockPos[] directions = {selfPos.north(), selfPos.south(), selfPos.east(), selfPos.west()};
            boolean[] isLayer = new boolean[4];

            for (int i = 0; i < 4; i++) {
                BlockState state = world.getBlockState(directions[i]);
                isLayer[i] = state.is(layer) ||
                        (layer.equals(Blocks.SNOW) &&
                                (state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.POWDER_SNOW)));
            }

            boolean layerCheck = switch (BetterGrassifyConfig.load().betterSnow.betterSnowMode) {
                case OPTIFINE -> isLayer[0] || isLayer[1] || isLayer[2] || isLayer[3];
                case LAMBDA -> ((isLayer[0] || isLayer[1]) && (isLayer[2] || isLayer[3])) ||
                        (isLayer[0] && isLayer[1]) ||
                        (isLayer[2] && isLayer[3]);
                default -> false;
            };

            if (!layerCheck) continue;

            for (BlockPos direction : directions) {
                BlockState state = world.getBlockState(direction);

                if (layer instanceof SnowLayerBlock && (state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.POWDER_SNOW)))
                    return layer.defaultBlockState();
                else if (state.is(layer)) {
                    if (state.getOptionalValue(BlockStateProperties.SEGMENT_AMOUNT).isPresent())
                        return state.setValue(BlockStateProperties.SEGMENT_AMOUNT, 4);
                    else if (state.getOptionalValue(BlockStateProperties.FLOWER_AMOUNT).isPresent())
                        return state.setValue(BlockStateProperties.FLOWER_AMOUNT, 4);
                    else return state;
                }
            }
        }

        return null;
    }

    public static boolean isLayerNeighbourSnow(BlockAndTintGetter world, BlockPos selfPos) {
        BlockState layerNeighbour = getLayerNeighbour(world, selfPos);
        return layerNeighbour != null &&
                (layerNeighbour.is(Blocks.SNOW) || layerNeighbour.is(Blocks.SNOW_BLOCK) || layerNeighbour.is(Blocks.POWDER_SNOW));
    }

    public static boolean canHaveGhostSnowLayer(BlockAndTintGetter world, BlockPos selfPos) {
        return canHaveGhostLayer(world, selfPos) && isLayerNeighbourSnow(world, selfPos);
    }

    public static boolean canHaveGhostLayer(BlockAndTintGetter world, BlockPos selfPos) {
        if (BetterGrassifyConfig.load().betterSnow.betterSnowMode == BetterGrassifyConfig.BetterSnowMode.OFF)
            return false;
        if (FabricLoader.getInstance().isModLoaded("wilderwild") && WilderWildCompat.isSnowloggingOn()) return false;

        BlockState self = world.getBlockState(selfPos);

        VoxelShape outlineShape = self.getShape(world, selfPos);
        VoxelShape bottomFace = outlineShape.getFaceShape(Direction.DOWN);
        double height = outlineShape.max(Direction.Axis.Y) - outlineShape.min(Direction.Axis.Y);

        boolean isWhitelistedBlock = BetterSnowPredicateUtils.isBlockWhitelisted(self);
        if (!isWhitelistedBlock) {
            if (self.isAir() || self.is(Blocks.WATER) || self.hasBlockEntity() ||
                    !(bottomFace.min(Direction.Axis.X) > 0F &&
                            bottomFace.max(Direction.Axis.X) < 1F &&
                            bottomFace.min(Direction.Axis.Z) > 0F &&
                            bottomFace.max(Direction.Axis.Z) < 1F
                    ) ||
                    height <= 0.125F
            ) return false;
        }

        boolean isLayer = getLayerNeighbour(world, selfPos) != null;
        if (!isLayer || !world.getBlockState(selfPos.below()).isFaceSturdy(world, selfPos.below(), Direction.UP))
            return false;

        boolean isWhitelistedTag = BetterSnowPredicateUtils.isTagWhitelisted(self);

        if (BetterSnowPredicateUtils.isWhitelistOn()) return isWhitelistedBlock || isWhitelistedTag;

        boolean isExcludedBlock = BetterSnowPredicateUtils.isBlockExcluded(self);
        boolean isExcludedTag = BetterSnowPredicateUtils.isTagExcluded(self);

        return !isExcludedBlock && !isExcludedTag;
    }
}
