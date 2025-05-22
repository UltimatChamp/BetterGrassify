package dev.ultimatchamp.bettergrass.model;

import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import dev.ultimatchamp.bettergrass.util.SpriteCalculator;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

//? if >1.21.4 {
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.minecraft.client.renderer.block.model.BlockStateModel;
//?} else {
//import net.minecraft.client.resources.model.BakedModel;
//?}

//? if >1.21.3 {
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;
//?}

//? if =1.21.4 {
/*import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.minecraft.client.resources.model.DelegateBakedModel;
*///?}

//? if >1.21.4 {
public class BetterGrassifyBakedModel extends WrapperBlockStateModel implements BlockStateModel {
    public BetterGrassifyBakedModel(BlockStateModel wrapped) {
        super(wrapped);
    }
//?} else if >1.21.3 {
/*public class BetterGrassifyBakedModel extends DelegateBakedModel implements FabricBakedModel {
    public BetterGrassifyBakedModel(BakedModel wrapped) {
        super(wrapped);
    }
*///?} else {
/*import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;

public class BetterGrassifyBakedModel extends ForwardingBakedModel {
    public BetterGrassifyBakedModel(BakedModel baseModel) {
        this.wrapped = baseModel;
    }
*///?}

    public static List<Block> BETTER_SNOW_CACHE = new CopyOnWriteArrayList<>();

    public static List<Map.Entry<Block, String>> EXCLUDED_BLOCKS_CACHE = new CopyOnWriteArrayList<>();
    public static List<TagKey<Block>> EXCLUDED_TAGS_CACHE = new CopyOnWriteArrayList<>();

    public static List<Map.Entry<Block, String>> WHITELISTED_BLOCKS_CACHE = new CopyOnWriteArrayList<>();
    public static List<TagKey<Block>> WHITELISTED_TAGS_CACHE = new CopyOnWriteArrayList<>();

    //? if <1.21.5 {
    /*@Override
    public boolean isVanillaAdapter() {
        return false;
    }
    *///?}

    @Override
    //? if >1.21.4 {
    public void emitQuads(QuadEmitter emitter, BlockAndTintGetter blockView, BlockPos pos, BlockState state,
                          RandomSource random, Predicate<@Nullable Direction> cullTest) {
            Supplier<RandomSource> randomSupplier = () -> random;

        emitter.pushTransform(quad -> {
    //?} else if >1.21.3 {
    /*public void emitBlockQuads(QuadEmitter emitter, BlockAndTintGetter blockView, BlockState state, BlockPos pos,
                                 Supplier<RandomSource> randomSupplier, Predicate<@Nullable Direction> cullTest) {
        emitter.pushTransform(quad -> {
    *///?} else {
    /*public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos,
                                 Supplier<RandomSource> randomSupplier, RenderContext context) {
        context.pushTransform(quad -> {
    *///?}
            if (!BetterGrassifyConfig.load().betterGrassMode.equals(BetterGrassifyConfig.BetterGrassMode.OFF)) {
                Direction face = quad.nominalFace();
                if (face == null ||
                    face.getAxis().isVertical() ||
                    state.hasBlockEntity() ||
                    !(isFullQuad(quad) || state.getBlock() instanceof SlabBlock)
                ) return true;

                betterGrassify(quad, blockView, state, pos, face, randomSupplier);
            }

            return true;
        });

        //? if >1.21.4 {
        super.emitQuads(emitter, blockView, pos, state, random, cullTest);
        emitter.popTransform();
        //?} else if >1.21.3 {
        /*super.emitBlockQuads(emitter, blockView, state, pos, randomSupplier, cullTest);
        emitter.popTransform();
        *///?} else {
        /*super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
        context.popTransform();
        *///?}
    }

    public void betterGrassify(MutableQuadView quad, BlockAndTintGetter world, BlockState state, BlockPos pos,
                               Direction face, Supplier<RandomSource> randomSupplier) {
        // Fix dirt paths connection, only if on a dirt block
        if (state.is(Blocks.DIRT) && isBelowNonFullBlock(world, pos, face)) {
            dirtSpriteBake(quad, world, pos, randomSupplier);
            return;
        }

        if (BetterGrassifyConfig.load().betterGrassMode.equals(BetterGrassifyConfig.BetterGrassMode.FANCY)) {
            if (canFullyConnect(world, state, pos, face)) {
                if (isSnowy(world, pos)) {
                    spriteBake(quad, world.getBlockState(pos.above()), randomSupplier);
                    return;
                }

                spriteBake(quad, state, randomSupplier);
            } else {
                betterSnowyGrass(quad, world, pos, face, randomSupplier);
            }
        } else { // Fast
            if (isSnowy(world, pos)) {
                spriteBake(quad, world.getBlockState(pos.above()), randomSupplier);
                return;
            } else if (canHaveGhostSnowLayer(world, pos.above())) {
                if (!BetterGrassifyConfig.load().snowy) return; // Better Snow Mode check, as block has ghost snow
                spriteBake(quad, getLayerNeighbour(world, pos.above()), randomSupplier);
                return;
            }

            spriteBake(quad, world.getBlockState(pos), randomSupplier);
        }
    }

    public void betterSnowyGrass(MutableQuadView quad, BlockAndTintGetter world, BlockPos pos, Direction face,
                                 Supplier<RandomSource> randomSupplier) {
        BlockPos adjacentPos = pos.relative(face);

        if (isSnowy(world, pos) && canHaveGhostSnowLayer(world, adjacentPos)) {
            // Self: Snowy Grass | Below: Ghost Snow
            spriteBake(quad, world.getBlockState(pos.above()), randomSupplier);
        } else if (canHaveGhostSnowLayer(world, pos.above()) && isSnowy(world, adjacentPos.below())) {
            // Self: Ghost Snow | Below: Snowy Grass
            spriteBake(quad, world.getBlockState(adjacentPos), randomSupplier);
        } else if (canHaveGhostSnowLayer(world, pos.above()) && canHaveGhostSnowLayer(world, adjacentPos)) {
            // Self: Ghost Snow | Below: Ghost Snow
            if (!BetterGrassifyConfig.load().snowy) return; // Better Snow Mode check, as both self and below have ghost snow
            spriteBake(quad, getLayerNeighbour(world, pos.above()), randomSupplier);
        }
    }

    private static boolean isFullQuad(MutableQuadView quad) {
        if (!BetterGrassifyConfig.load().resourcePackCompatibilityMode) return true;

        float tolerance = 1 / 16f; // 1 pixel
        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;

        for (int i = 0; i < 4; i++) {
            float y = quad.y(i);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }

        return minY <= tolerance && maxY >= (1 - tolerance);
    }

    private static boolean canFullyConnect(BlockAndTintGetter world, BlockState self, BlockPos selfPos,
                                           Direction direction) {
        return canConnect(world, self, selfPos, selfPos.relative(direction).below());
    }

    private static boolean canConnect(BlockAndTintGetter world, BlockState self, BlockPos selfPos, BlockPos adjacentPos) {
        BlockState adjacent = world.getBlockState(adjacentPos);
        BlockPos upPos = adjacentPos.above();
        BlockState up = world.getBlockState(upPos);

        return canConnect(self, adjacent) &&
               (up.isAir() || isSnowy(world, selfPos) ||
               !up.isFaceSturdy(world, upPos, Direction.DOWN));
    }

    private static boolean canConnect(BlockState self, BlockState adjacent) {
        return self == adjacent;
    }

    private static boolean isBelowNonFullBlock(BlockAndTintGetter world, BlockPos selfPos, Direction direction) {
        BlockPos upPos = selfPos.above();
        BlockState up = world.getBlockState(upPos);

        if (!(up.getBlock().equals(Blocks.DIRT_PATH) || up.getBlock().equals(Blocks.FARMLAND))) return false;

        if (up.getBlock().equals(Blocks.DIRT_PATH) && !BetterGrassifyConfig.load().dirtPaths) return false;
        if (up.getBlock().equals(Blocks.FARMLAND) && !BetterGrassifyConfig.load().farmLands) return false;

        return canFullyConnect(world, up, upPos, direction);
    }

    private static boolean isSnowy(BlockAndTintGetter world, BlockPos selfPos) {
        BlockState self = world.getBlockState(selfPos);
        return self.getOptionalValue(BlockStateProperties.SNOWY).orElse(false) && !world.getBlockState(selfPos.above()).isAir();
    }

    public static BlockState getLayerNeighbour(BlockAndTintGetter world, BlockPos selfPos) {
        if (BETTER_SNOW_CACHE.isEmpty()) {
            for (String id : BetterGrassifyConfig.load().snowLayers) {
                ResourceLocation identifier = ResourceLocation.tryParse(id);
                Optional<Block> layer = BuiltInRegistries.BLOCK.getOptional(identifier);

                if (layer.isEmpty()) continue;

                BETTER_SNOW_CACHE.add(layer.get());
            }
        }

        for (Block layer : BETTER_SNOW_CACHE) {
            BlockPos[] directions = { selfPos.north(), selfPos.south(), selfPos.east(), selfPos.west() };
            boolean[] isLayer = new boolean[4];

            for (int i = 0; i < 4; i++) {
                BlockState state = world.getBlockState(directions[i]);
                isLayer[i] = state.is(layer) || (layer.defaultBlockState().is(Blocks.SNOW) &&
                            (state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.POWDER_SNOW)));
            }

            boolean layerCheck =
                    switch (BetterGrassifyConfig.load().betterSnowMode) {
                        case OPTIFINE -> isLayer[0] || isLayer[1] || isLayer[2] || isLayer[3]; // Any 1 neighbouring layer
                        case LAMBDA -> ((isLayer[0] || isLayer[1]) && (isLayer[2] || isLayer[3])) ||
                                       (isLayer[0] && isLayer[1]) || (isLayer[2] && isLayer[3]); // Any 2 neighbouring layers
                        default -> false;
                    };

            if (!layerCheck) continue;

            for (BlockPos direction : directions) {
                BlockState state = world.getBlockState(direction);

                if (layer.defaultBlockState().is(Blocks.SNOW) &&
                   (state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.POWDER_SNOW)))
                  return layer.defaultBlockState();
                else if (layer.defaultBlockState().is(Blocks.PINK_PETALS))
                    return layer.defaultBlockState().setValue(BlockStateProperties.FLOWER_AMOUNT, 4);
                //? if >1.21.4 {
                else if (layer.defaultBlockState().is(Blocks.LEAF_LITTER))
                  return layer.defaultBlockState().setValue(BlockStateProperties.SEGMENT_AMOUNT, 4);
                else if (layer.defaultBlockState().is(Blocks.WILDFLOWERS))
                    return layer.defaultBlockState().setValue(BlockStateProperties.FLOWER_AMOUNT, 4);
                //?}
                else if (state.is(layer)) return state;
            }
        }

        return null;
    }

    public static boolean isLayerNeighbourSnow(BlockAndTintGetter world, BlockPos selfPos) {
        BlockState layerNeighbour = getLayerNeighbour(world, selfPos);
        return layerNeighbour != null && (layerNeighbour.is(Blocks.SNOW) || layerNeighbour.is(Blocks.SNOW_BLOCK) ||
               layerNeighbour.is(Blocks.POWDER_SNOW));
    }

    public static boolean canHaveGhostSnowLayer(BlockAndTintGetter world, BlockPos selfPos) {
        return canHaveGhostLayer(world, selfPos) && isLayerNeighbourSnow(world, selfPos);
    }

    public static boolean canHaveGhostLayer(BlockAndTintGetter world, BlockPos selfPos) {
        if (BetterGrassifyConfig.load().betterSnowMode == BetterGrassifyConfig.BetterSnowMode.OFF) return false;

        BlockState self = world.getBlockState(selfPos);

        VoxelShape outlineShape = self.getShape(world, selfPos);
        VoxelShape bottomFace = outlineShape.getFaceShape(Direction.DOWN);
        double height = outlineShape.max(Direction.Axis.Y) - outlineShape.min(Direction.Axis.Y);
        if (self.isAir() ||
            self.is(Blocks.WATER) ||
            self.hasBlockEntity() ||
            !(bottomFace.min(Direction.Axis.X) > 0F &&
              bottomFace.max(Direction.Axis.X) < 1F &&
              bottomFace.min(Direction.Axis.Z) > 0F &&
              bottomFace.max(Direction.Axis.Z) < 1F
            ) ||
            height <= 0.125F
        ) return false;

        boolean isLayer = getLayerNeighbour(world, selfPos) != null;
        if (!isLayer) return false;

        boolean isWhitelistedBlock = isBlockWhitelisted(self);
        boolean isWhitelistedTag = isTagWhitelisted(self);

        if (isWhitelistOn()) return isWhitelistedBlock || isWhitelistedTag;

        boolean isExcludedBlock = isBlockExcluded(self);
        boolean isExcludedTag = isTagExcluded(self);

        return !isExcludedBlock && !isExcludedTag;
    }

    private static boolean isBlockWhitelisted(BlockState self) {
        if (WHITELISTED_BLOCKS_CACHE.isEmpty()) {
            for (String block : BetterGrassifyConfig.load().whitelistedBlocks) {
                ResourceLocation identifier = ResourceLocation.tryParse(withoutAttribute(block));
                Optional<Block> whitelistedBlock = BuiltInRegistries.BLOCK.getOptional(identifier);

                if (whitelistedBlock.isEmpty()) continue;

                WHITELISTED_BLOCKS_CACHE.add(Map.entry(whitelistedBlock.get(), block));
            }
        }

        for (Map.Entry<Block, String> entry : WHITELISTED_BLOCKS_CACHE) {
            if (matchesBlock(self, entry.getValue())) {
                return true;
            }
        }

        return false;
    }

    private static boolean isTagWhitelisted(BlockState self) {
        if (WHITELISTED_TAGS_CACHE.isEmpty()) {
            for (String tag : BetterGrassifyConfig.load().whitelistedTags) {
                ResourceLocation identifier = ResourceLocation.tryParse(tag);
                WHITELISTED_TAGS_CACHE.add(TagKey.create(Registries.BLOCK, identifier));
            }
        }

        for (TagKey<Block> tag : WHITELISTED_TAGS_CACHE) {
            if (self.is(tag)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isBlockExcluded(BlockState self) {
        if (EXCLUDED_BLOCKS_CACHE.isEmpty()) {
            for (String block : BetterGrassifyConfig.load().excludedBlocks) {
                ResourceLocation identifier = ResourceLocation.tryParse(withoutAttribute(block));
                Optional<Block> excludedBlock = BuiltInRegistries.BLOCK.getOptional(identifier);

                if (excludedBlock.isEmpty()) continue;

                EXCLUDED_BLOCKS_CACHE.add(Map.entry(excludedBlock.get(), block));
            }
        }

        for (Map.Entry<Block, String> entry : EXCLUDED_BLOCKS_CACHE) {
            if (matchesBlock(self, entry.getValue())) {
                return true;
            }
        }

        return false;
    }

    private static boolean isTagExcluded(BlockState self) {
        if (EXCLUDED_TAGS_CACHE.isEmpty()) {
            for (String tag : BetterGrassifyConfig.load().excludedTags) {
                ResourceLocation identifier = ResourceLocation.tryParse(tag);
                EXCLUDED_TAGS_CACHE.add(TagKey.create(Registries.BLOCK, identifier));
            }
        }

        for (TagKey<Block> tag : EXCLUDED_TAGS_CACHE) {
            if (self.is(tag)) {
                return true;
            }
        }

        return false;
    }

    private static boolean matchesBlock(BlockState self, String block) {
        boolean hasAttribute = block.contains("[");
        String blockName = withoutAttribute(block);
        String attribute = "";
        boolean attributeEnabled = true;

        if (hasAttribute) {
            if (block.contains("=")) {
                attribute = block.substring(block.indexOf("[") + 1, block.indexOf("="));
            } else {
                attribute = block.substring(block.indexOf("[") + 1, block.indexOf("]"));
            }
            attributeEnabled = !block.contains("=false");
        }

        var blockCheck = BuiltInRegistries.BLOCK.getOptional(ResourceLocation.tryParse(blockName));

        if (blockCheck.isEmpty()) return false;

        if (self.getBlock().equals(blockCheck.get())) {
            return !hasAttribute || self.toString().contains(attribute + "=" + attributeEnabled);
        }

        return false;
    }

    private static String withoutAttribute(String block) {
        boolean hasAttribute = block.contains("[");
        return hasAttribute ? block.substring(0, block.indexOf("[")) : block;
    }

    private static boolean isWhitelistOn() {
        return !BetterGrassifyConfig.load().whitelistedBlocks.isEmpty() || !BetterGrassifyConfig.load().whitelistedTags.isEmpty();
    }

    private static void spriteBake(MutableQuadView quad, BlockState state, Supplier<RandomSource> randomSupplier) {
        TextureAtlasSprite sprite = SpriteCalculator.calculateSprite(state, Direction.UP, randomSupplier);
        if (sprite != null) quad.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
    }

    private static void dirtSpriteBake(MutableQuadView quad, BlockAndTintGetter world, BlockPos selfPos,
                                       Supplier<RandomSource> randomSupplier) {
        BlockPos upPos = selfPos.above();
        BlockState up = world.getBlockState(upPos);

        TextureAtlasSprite sprite = SpriteCalculator.calculateSprite(up, Direction.UP, randomSupplier);
        if (sprite != null) quad.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
    }
}
