package dev.ultimatchamp.bettergrass.model;

import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import dev.ultimatchamp.bettergrass.util.SpriteCalculator;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.texture.Sprite;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

//? if >1.21.4 {
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.minecraft.client.render.model.BlockStateModel;
//?} else {
/*import net.minecraft.client.render.model.BakedModel;
*///?}

//? if >1.21.3 {
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;
//?}

//? if =1.21.4 {
/*import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.minecraft.client.render.model.WrapperBakedModel;
*///?}

//? if >1.21.4 {
public class BetterGrassifyBakedModel extends WrapperBlockStateModel implements BlockStateModel {
    public BetterGrassifyBakedModel(BlockStateModel wrapped) {
        super(wrapped);
    }
//?} else if >1.21.3 {
/*public class BetterGrassifyBakedModel extends WrapperBakedModel implements FabricBakedModel {
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
    public void emitQuads(QuadEmitter emitter, BlockRenderView blockView, BlockPos pos, BlockState state, Random random,
                          Predicate<@Nullable Direction> cullTest) {
        Supplier<Random> randomSupplier = () -> random;

        emitter.pushTransform(quad -> {
    //?} else if >1.21.3 {
    /*public void emitBlockQuads(QuadEmitter emitter, BlockRenderView blockView, BlockState state, BlockPos pos,
                                 Supplier<Random> randomSupplier, Predicate<@Nullable Direction> cullTest) {
        emitter.pushTransform(quad -> {
    *///?} else {
    /*public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos,
                                 Supplier<Random> randomSupplier, RenderContext context) {
        context.pushTransform(quad -> {
    *///?}
            if (!BetterGrassifyConfig.load().betterGrassMode.equals(BetterGrassifyConfig.BetterGrassMode.OFF)) {
                if (quad.nominalFace().getAxis().isVertical() ||
                    state.hasBlockEntity() ||
                    !isFullQuad(quad)
                ) return true;

                betterGrassify(quad, blockView, state, pos, randomSupplier);
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

    public void betterGrassify(MutableQuadView quad, BlockRenderView world, BlockState state, BlockPos pos,
                               Supplier<Random> randomSupplier) {
        // Fix dirt paths connection, only if on a dirt block
        if (state.isOf(Blocks.DIRT) && isBelowNonFullBlock(world, pos, quad.nominalFace())) {
            dirtSpriteBake(quad, world, pos, randomSupplier);
            return;
        }

        if (BetterGrassifyConfig.load().betterGrassMode.equals(BetterGrassifyConfig.BetterGrassMode.FANCY)) {
            Direction face = quad.nominalFace();

            if (canFullyConnect(world, state, pos, face)) {
                if (isSnowy(world, pos)) {
                    spriteBake(quad, world.getBlockState(pos.up()), randomSupplier);
                    return;
                }

                spriteBake(quad, state, randomSupplier);
            } else {
                betterSnowyGrass(quad, world, pos, randomSupplier);
            }
        } else { // Fast
            if (isSnowy(world, pos)) {
                spriteBake(quad, world.getBlockState(pos.up()), randomSupplier);
                return;
            } else if (canHaveGhostSnowLayer(world, pos.up())) {
                if (!BetterGrassifyConfig.load().snowy) return; // Better Snow Mode check, as block has ghost snow
                spriteBake(quad, getLayerNeighbour(world, pos.up()), randomSupplier);
                return;
            }

            spriteBake(quad, world.getBlockState(pos), randomSupplier);
        }
    }

    public void betterSnowyGrass(MutableQuadView quad, BlockRenderView world, BlockPos pos,
                                 Supplier<Random> randomSupplier) {
        Direction face = quad.nominalFace();
        BlockPos adjacentPos = pos.offset(face);

        if (isSnowy(world, pos) && canHaveGhostSnowLayer(world, adjacentPos)) {
            // Self: Snowy Grass | Below: Ghost Snow
            spriteBake(quad, world.getBlockState(pos.up()), randomSupplier);
        } else if (canHaveGhostSnowLayer(world, pos.up()) && isSnowy(world, adjacentPos.down())) {
            // Self: Ghost Snow | Below: Snowy Grass
            spriteBake(quad, world.getBlockState(adjacentPos), randomSupplier);
        } else if (canHaveGhostSnowLayer(world, pos.up()) && canHaveGhostSnowLayer(world, adjacentPos)) {
            // Self: Ghost Snow | Below: Ghost Snow
            if (!BetterGrassifyConfig.load().snowy) return; // Better Snow Mode check, as both self and below have ghost snow
            spriteBake(quad, getLayerNeighbour(world, pos.up()), randomSupplier);
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

    private static boolean canFullyConnect(BlockRenderView world, BlockState self, BlockPos selfPos,
                                           Direction direction) {
        return canConnect(world, self, selfPos, selfPos.offset(direction).down());
    }

    private static boolean canConnect(BlockRenderView world, BlockState self, BlockPos selfPos, BlockPos adjacentPos) {
        BlockState adjacent = world.getBlockState(adjacentPos);
        BlockPos upPos = adjacentPos.up();
        BlockState up = world.getBlockState(upPos);

        return canConnect(self, adjacent) &&
               (up.isAir() || isSnowy(world, selfPos) ||
               !up.isSideSolidFullSquare(world, upPos, Direction.DOWN));
    }

    private static boolean canConnect(BlockState self, BlockState adjacent) {
        return self == adjacent;
    }

    private static boolean isBelowNonFullBlock(BlockRenderView world, BlockPos selfPos, Direction direction) {
        BlockPos upPos = selfPos.up();
        BlockState up = world.getBlockState(upPos);

        if (!(up.getBlock().equals(Blocks.DIRT_PATH) || up.getBlock().equals(Blocks.FARMLAND))) return false;

        if (up.getBlock().equals(Blocks.DIRT_PATH) && !BetterGrassifyConfig.load().dirtPaths) return false;
        if (up.getBlock().equals(Blocks.FARMLAND) && !BetterGrassifyConfig.load().farmLands) return false;

        return canFullyConnect(world, up, upPos, direction);
    }

    private static boolean isSnowy(BlockRenderView world, BlockPos selfPos) {
        BlockState self = world.getBlockState(selfPos);
        return self.getOrEmpty(Properties.SNOWY).orElse(false) && !world.getBlockState(selfPos.up()).isAir();
    }

    public static BlockState getLayerNeighbour(BlockRenderView world, BlockPos selfPos) {
        if (BETTER_SNOW_CACHE.isEmpty()) {
            for (String id : BetterGrassifyConfig.load().snowLayers) {
                Identifier identifier = Identifier.tryParse(id);

                //? if >1.21.1 {
                Optional<Block> layer = Registries.BLOCK.getOptionalValue(identifier);
                //?} else {
                /*Optional<Block> layer = Registries.BLOCK.getOrEmpty(identifier);
                *///?}

                if (layer.isEmpty()) continue;

                BETTER_SNOW_CACHE.add(layer.get());
            }
        }

        for (Block layer : BETTER_SNOW_CACHE) {
            BlockPos[] directions = { selfPos.north(), selfPos.south(), selfPos.east(), selfPos.west() };
            boolean[] isLayer = new boolean[4];

            for (int i = 0; i < 4; i++) {
                BlockState state = world.getBlockState(directions[i]);
                isLayer[i] = state.isOf(layer) || (layer.getDefaultState().isOf(Blocks.SNOW) &&
                            (state.isOf(Blocks.SNOW_BLOCK) || state.isOf(Blocks.POWDER_SNOW)));
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

                if (layer.getDefaultState().isOf(Blocks.SNOW) &&
                   (state.isOf(Blocks.SNOW_BLOCK) || state.isOf(Blocks.POWDER_SNOW)))
                  return layer.getDefaultState();
                //? if >1.21.4 {
                if (layer.getDefaultState().isOf(Blocks.LEAF_LITTER))
                  return layer.getDefaultState().with(Properties.SEGMENT_AMOUNT, 4);
                //?}
                if (state.isOf(layer)) return state;
            }
        }

        return null;
    }

    public static boolean isLayerNeighbourSnow(BlockRenderView world, BlockPos selfPos) {
        BlockState layerNeighbour = getLayerNeighbour(world, selfPos);
        return layerNeighbour != null && (layerNeighbour.isOf(Blocks.SNOW) || layerNeighbour.isOf(Blocks.SNOW_BLOCK) ||
               layerNeighbour.isOf(Blocks.POWDER_SNOW));
    }

    public static boolean canHaveGhostSnowLayer(BlockRenderView world, BlockPos selfPos) {
        return canHaveGhostLayer(world, selfPos) && isLayerNeighbourSnow(world, selfPos);
    }

    public static boolean canHaveGhostLayer(BlockRenderView world, BlockPos selfPos) {
        if (BetterGrassifyConfig.load().betterSnowMode == BetterGrassifyConfig.BetterSnowMode.OFF) return false;

        BlockState self = world.getBlockState(selfPos);

        VoxelShape outlineShape = self.getOutlineShape(world, selfPos);
        VoxelShape bottomFace = outlineShape.getFace(Direction.DOWN);
        double height = outlineShape.getMax(Direction.Axis.Y) - outlineShape.getMin(Direction.Axis.Y);
        if (self.isAir() ||
            self.isOf(Blocks.WATER) ||
            self.hasBlockEntity() ||
            !(bottomFace.getMin(Direction.Axis.X) > 0F &&
              bottomFace.getMax(Direction.Axis.X) < 1F &&
              bottomFace.getMin(Direction.Axis.Z) > 0F &&
              bottomFace.getMax(Direction.Axis.Z) < 1F
            ) ||
            height <= 0.125F
        ) return false;

        boolean isLayer = getLayerNeighbour(world, selfPos) != null;
        if (!isLayer) return false;

        boolean isExcludedBlock = isBlockExcluded(self);
        boolean isExcludedTag = isTagExcluded(self, isExcludedBlock);

        return !(isExcludedBlock || isExcludedTag);
    }

    private static boolean isBlockExcluded(BlockState self) {
        if (EXCLUDED_BLOCKS_CACHE.isEmpty() && BetterGrassifyConfig.load().whitelistedBlocks.isEmpty() &&
            BetterGrassifyConfig.load().whitelistedTags.isEmpty()) {
            for (String block : BetterGrassifyConfig.load().excludedBlocks) {
                Identifier identifier = Identifier.tryParse(withoutAttribute(block));

                //? if >1.21.1 {
                Optional<Block> excludedBlock = Registries.BLOCK.getOptionalValue(identifier);
                //?} else {
                /*Optional<Block> excludedBlock = Registries.BLOCK.getOrEmpty(identifier);
                *///?}

                if (excludedBlock.isEmpty()) continue;

                EXCLUDED_BLOCKS_CACHE.add(Map.entry(excludedBlock.get(), block));
            }
        } else if (WHITELISTED_BLOCKS_CACHE.isEmpty()) {
            for (String block : BetterGrassifyConfig.load().whitelistedBlocks) {
                Identifier identifier = Identifier.tryParse(withoutAttribute(block));

                //? if >1.21.1 {
                Optional<Block> whitelistedBlock = Registries.BLOCK.getOptionalValue(identifier);
                //?} else {
                /*Optional<Block> whitelistedBlock = Registries.BLOCK.getOrEmpty(identifier);
                *///?}

                if (whitelistedBlock.isEmpty()) continue;

                WHITELISTED_BLOCKS_CACHE.add(Map.entry(whitelistedBlock.get(), block));
            }
        }

        if (BetterGrassifyConfig.load().whitelistedTags.isEmpty() &&
            BetterGrassifyConfig.load().whitelistedBlocks.isEmpty()) {
            for (Map.Entry<Block, String> entry : EXCLUDED_BLOCKS_CACHE) {
                if (matchesBlock(self, entry.getValue())) {
                    return true;
                }
            }
        } else {
            for (Map.Entry<Block, String> entry : WHITELISTED_BLOCKS_CACHE) {
                if (matchesBlock(self, entry.getValue())) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    private static String withoutAttribute(String block) {
        boolean hasAttribute = block.contains("[");
        return hasAttribute ? block.substring(0, block.indexOf("[")) : block;
    }

    private static boolean isTagExcluded(BlockState self, boolean isExcludedBlock) {
        if (EXCLUDED_TAGS_CACHE.isEmpty() && BetterGrassifyConfig.load().whitelistedBlocks.isEmpty() &&
            BetterGrassifyConfig.load().whitelistedTags.isEmpty()) {
            for (String tag : BetterGrassifyConfig.load().excludedTags) {
                Identifier identifier = Identifier.tryParse(tag);
                EXCLUDED_TAGS_CACHE.add(TagKey.of(RegistryKeys.BLOCK, identifier));
            }
        } else if (WHITELISTED_TAGS_CACHE.isEmpty()) {
            for (String tag : BetterGrassifyConfig.load().whitelistedTags) {
                Identifier identifier = Identifier.tryParse(tag);
                WHITELISTED_TAGS_CACHE.add(TagKey.of(RegistryKeys.BLOCK, identifier));
            }
        }

        if (BetterGrassifyConfig.load().whitelistedTags.isEmpty() &&
            BetterGrassifyConfig.load().whitelistedBlocks.isEmpty()) {
            for (TagKey<Block> tag : EXCLUDED_TAGS_CACHE) {
                if (self.isIn(tag)) {
                    return true;
                }
            }
        } else {
            for (TagKey<Block> tag : WHITELISTED_TAGS_CACHE) {
                if (self.isIn(tag)) {
                    return false;
                }
            }

            if (!isExcludedBlock) return false;
        }

        return false;
    }

    private static boolean matchesBlock(BlockState self, String block) {
        boolean hasAttribute = block.contains("[");
        String blockName = hasAttribute ? block.substring(0, block.indexOf("[")) : block;
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

        //? if >1.21.1 {
        var blockCheck = Registries.BLOCK.getOptionalValue(Identifier.tryParse(blockName));
        //?} else {
        /*var blockCheck = Registries.BLOCK.getOrEmpty(Identifier.tryParse(blockName));
        *///?}

        if (blockCheck.isEmpty()) return false;

        if (self.getBlock().equals(blockCheck.get())) {
            return !hasAttribute || self.toString().contains(attribute + "=" + attributeEnabled);
        }

        return false;
    }

    private static void spriteBake(MutableQuadView quad, BlockState state, Supplier<Random> randomSupplier) {
        Sprite sprite = SpriteCalculator.calculateSprite(state, Direction.UP, randomSupplier);
        if (sprite != null) quad.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
    }

    private static void dirtSpriteBake(MutableQuadView quad, BlockRenderView world, BlockPos selfPos,
                                       Supplier<Random> randomSupplier) {
        BlockPos upPos = selfPos.up();
        BlockState up = world.getBlockState(upPos);

        Sprite sprite = SpriteCalculator.calculateSprite(up, Direction.UP, randomSupplier);
        if (sprite != null) quad.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
    }
}
