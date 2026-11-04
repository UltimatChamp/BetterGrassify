package dev.ultimatchamp.bettergrass.util;

import dev.ultimatchamp.bettergrass.compat.WilderWildCompat;
import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.VoxelShape;

//? if >1.21.11 {
import net.minecraft.client.renderer.block.BlockAndTintGetter;
//?} else {
/*import net.minecraft.world.level.BlockAndTintGetter;
*///?}

public class BetterSnowUtils {
    // getLayerNeighbour runs for every rendered block of every chunk section being built, on the chunk builder
    // threads, so it reuses one position per thread instead of allocating new ones.
    private static final ThreadLocal<BlockPos.MutableBlockPos> NEIGHBOUR_POS =
            ThreadLocal.withInitial(BlockPos.MutableBlockPos::new);

    public static BlockState getLayerNeighbour(BlockAndTintGetter world, BlockPos selfPos) {
        BetterGrassifyCacheUtils.initCaches();

        int minLayerNeighbours = switch (BetterGrassifyConfig.load().betterSnow.betterSnowMode) {
            case OPTIFINE -> 1;
            case LAMBDA -> 2;
            default -> 0;
        };

        if (minLayerNeighbours == 0 || BetterGrassifyCacheUtils.BETTER_SNOW_CACHE.isEmpty()) return null;

        // The four neighbours are read once and checked against every layer.
        BlockPos.MutableBlockPos neighbourPos = NEIGHBOUR_POS.get();
        BlockState north = world.getBlockState(neighbourPos.setWithOffset(selfPos, Direction.NORTH));
        BlockState south = world.getBlockState(neighbourPos.setWithOffset(selfPos, Direction.SOUTH));
        BlockState east = world.getBlockState(neighbourPos.setWithOffset(selfPos, Direction.EAST));
        BlockState west = world.getBlockState(neighbourPos.setWithOffset(selfPos, Direction.WEST));

        for (Block layer : BetterGrassifyCacheUtils.BETTER_SNOW_CACHE) {
            int layerNeighbours = (isLayer(north, layer) ? 1 : 0) + (isLayer(south, layer) ? 1 : 0) +
                    (isLayer(east, layer) ? 1 : 0) + (isLayer(west, layer) ? 1 : 0);

            if (layerNeighbours < minLayerNeighbours) continue;

            BlockState layerState = getLayerState(north, layer);
            if (layerState == null) layerState = getLayerState(south, layer);
            if (layerState == null) layerState = getLayerState(east, layer);
            if (layerState == null) layerState = getLayerState(west, layer);
            if (layerState != null) return layerState;
        }

        return null;
    }

    private static boolean isLayer(BlockState state, Block layer) {
        return state.is(layer) ||
                (layer.equals(Blocks.SNOW) &&
                        (state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.POWDER_SNOW)));
    }

    private static BlockState getLayerState(BlockState state, Block layer) {
        if (layer instanceof SnowLayerBlock && (state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.POWDER_SNOW)))
            return layer.defaultBlockState();
        else if (state.is(layer)) {
            if (state.getOptionalValue(BlockStateProperties.FLOWER_AMOUNT).isPresent())
                return state.setValue(BlockStateProperties.FLOWER_AMOUNT, 4);
            //? if >1.21.1 {
            else if (state.getOptionalValue(BlockStateProperties.SEGMENT_AMOUNT).isPresent())
                return state.setValue(BlockStateProperties.SEGMENT_AMOUNT, 4);
            //?}
            else return state;
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
