package dev.ultimatchamp.bettergrass.model;

import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import dev.ultimatchamp.bettergrass.util.SpriteCalculator;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

import java.util.Optional;
import java.util.function.Supplier;

//? if >1.21.3 {
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.minecraft.client.render.model.WrapperBakedModel;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class BetterGrassifyBakedModel extends WrapperBakedModel implements FabricBakedModel {
    public BetterGrassifyBakedModel(BakedModel baseModel) {
        super(baseModel);
    }
//?} else {
/*import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;

public class BetterGrassifyBakedModel extends ForwardingBakedModel {
    public BetterGrassifyBakedModel(BakedModel baseModel) {
        this.wrapped = baseModel;
    }
*///?}

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    //? if >1.21.3 {
    public void emitBlockQuads(QuadEmitter emitter, BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, Predicate<@Nullable Direction> cullTest) {
        emitter.pushTransform(quad -> {
    //?} else {
    /*public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        context.pushTransform(quad -> {
    *///?}
            if (quad.nominalFace().getAxis() == Direction.Axis.Y || !isFullQuad(quad)) {
                return true;
            }

            if (!BetterGrassifyConfig.load().betterGrassMode.equals(BetterGrassifyConfig.BetterGrassMode.OFF))
                betterGrassify(quad, blockView, state, pos, randomSupplier, BetterGrassifyConfig.load().betterGrassMode.equals(BetterGrassifyConfig.BetterGrassMode.FANCY));

            return true;
        });

        //? if >1.21.3 {
        super.emitBlockQuads(emitter, blockView, state, pos, randomSupplier, cullTest);
        emitter.popTransform();
        //?} else {
        /*super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
        context.popTransform();
        *///?}
    }

    public void betterGrassify(MutableQuadView quad, BlockRenderView world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, boolean isFancy) {
        // Fix dirt paths connection, only if on a dirt block
        if (state.isOf(Blocks.DIRT) && isBelowNonFullBlock(world, pos, quad.nominalFace())) {
            dirtSpriteBake(quad, world, pos, randomSupplier);
            return;
        }

        if (isFancy) {
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
            } else if (canHaveSnowLayer(world, pos.up()) && isNeighbourSnow(world, pos.up())) {
                if (!BetterGrassifyConfig.load().snowy) return; // Better Snow Mode check, as block has ghost snow
                spriteBake(quad, snowNeighbour(world, pos.up()).getDefaultState(), randomSupplier);
                return;
            }

            spriteBake(quad, world.getBlockState(pos), randomSupplier);
        }
    }

    public void betterSnowyGrass(MutableQuadView quad, BlockRenderView world, BlockPos pos, Supplier<Random> randomSupplier) {
        Direction face = quad.nominalFace();
        BlockPos adjacentPos = pos.offset(face);

        if (isSnowy(world, pos) && canHaveSnowLayer(world, adjacentPos) && isNeighbourSnow(world, adjacentPos)) { // Self: Snowy Grass | Below: Ghost Snow
            spriteBake(quad, world.getBlockState(pos.up()), randomSupplier);
        } else if (canHaveSnowLayer(world, pos.up()) && isNeighbourSnow(world, pos.up()) && isSnowy(world, adjacentPos.down())) { // Self: Ghost Snow | Below: Snowy Grass
            spriteBake(quad, world.getBlockState(adjacentPos), randomSupplier);
        } else if (canHaveSnowLayer(world, pos.up()) && isNeighbourSnow(world, pos.up()) && canHaveSnowLayer(world, adjacentPos) && isNeighbourSnow(world, adjacentPos)) { // Self: Ghost Snow | Below: Ghost Snow
            if (!BetterGrassifyConfig.load().snowy) return; // Better Snow Mode check, as both self and below have ghost snow
            spriteBake(quad, snowNeighbour(world, pos.up()).getDefaultState(), randomSupplier);
        }
    }

    private static boolean isFullQuad(MutableQuadView quad) {
        if (!BetterGrassifyConfig.load().resourcePackCompatibilityMode) return true;

        float tolerance = 1 / 16f; // 1 pixel
        for (int i = 0; i < 4; i++) {
            float y = quad.y(i);
            if (Math.abs(y - Math.round(y)) > tolerance) return false;
        }

        return true;
    }

    private static boolean canFullyConnect(BlockRenderView world, BlockState self, BlockPos selfPos, Direction direction) {
        return canConnect(world, self, selfPos, selfPos.offset(direction).down());
    }

    private static boolean canConnect(BlockRenderView world, BlockState self, BlockPos selfPos, BlockPos adjacentPos) {
        BlockState adjacent = world.getBlockState(adjacentPos);
        BlockPos upPos = adjacentPos.up();
        BlockState up = world.getBlockState(upPos);

        return canConnect(self, adjacent) && (up.isAir() || isSnowy(world, selfPos) || !up.isSideSolidFullSquare(world, upPos, Direction.DOWN));
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

    public static Block snowNeighbour(BlockRenderView world, BlockPos selfPos) {
        for (String id : BetterGrassifyConfig.load().snowLayers) {
            Identifier identifier = Identifier.tryParse(id);

            //? if >1.21.1 {
            Optional<Block> layer = Registries.BLOCK.getOptionalValue(identifier);
            //?} else {
            /*Optional<Block> layer = Registries.BLOCK.getOrEmpty(identifier);
            *///?}

            if (layer.isEmpty()) continue;

            BlockPos[] directions = { selfPos.north(), selfPos.south(), selfPos.east(), selfPos.west() };
            boolean[] isSnow = new boolean[4];

            for (int i = 0; i < 4; i++) {
                BlockState state = world.getBlockState(directions[i]);
                isSnow[i] = state.isOf(layer.get()) || (id.equals("snow") && (state.isOf(Blocks.SNOW_BLOCK) || state.isOf(Blocks.POWDER_SNOW)));
            }

            boolean layerCheck =
                    switch (BetterGrassifyConfig.load().betterSnowMode) {
                        case OPTIFINE -> isSnow[0] || isSnow[1] || isSnow[2] || isSnow[3];
                        case LAMBDA -> ((isSnow[0] || isSnow[1]) && (isSnow[2] || isSnow[3])) || (isSnow[0] && isSnow[1]) || (isSnow[2] && isSnow[3]);
                        default -> false;
                    };

            if (layerCheck) return layer.get();
        }

        return null;
    }

    public static boolean isNeighbourSnow(BlockRenderView world, BlockPos selfPos) {
        return snowNeighbour(world, selfPos) == Blocks.SNOW;
    }

    public static boolean canHaveSnowLayer(BlockRenderView world, BlockPos selfPos) {
        if (BetterGrassifyConfig.load().betterSnowMode == BetterGrassifyConfig.BetterSnowMode.OFF) return false;

        BlockState self = world.getBlockState(selfPos);

        if (self.isAir() ||
            self.isOf(Blocks.WATER) ||
            self.isSideSolidFullSquare(world, selfPos, Direction.DOWN) ||
            !world.getBlockState(selfPos.down()).isOpaqueFullCube(/*? if <1.21.3 {*//*world, selfPos.down()*//*?}*/)
        ) return false;

        boolean isLayer = snowNeighbour(world, selfPos) != null;
        if (!isLayer) return false;

        boolean isExcludedBlock = isBlockExcluded(self);
        boolean isExcludedTag = isTagExcluded(self, isExcludedBlock);

        return !(isExcludedBlock || isExcludedTag);
    }

    private static boolean isBlockExcluded(BlockState self) {
        boolean isExcluded = false;

        if (BetterGrassifyConfig.load().whitelistedTags.isEmpty() && BetterGrassifyConfig.load().whitelistedBlocks.isEmpty()) {
            for (String block : BetterGrassifyConfig.load().excludedBlocks) {
                if (matchesBlock(self, block)) {
                    isExcluded = true;
                    break;
                }
            }
        } else {
            isExcluded = true;

            for (String block : BetterGrassifyConfig.load().whitelistedBlocks) {
                if (matchesBlock(self, block)) {
                    return false;
                }
            }
        }

        return isExcluded;
    }

    private static boolean isTagExcluded(BlockState self, boolean isExcludedBlock) {
        boolean isExcluded = false;

        if (BetterGrassifyConfig.load().whitelistedTags.isEmpty() && BetterGrassifyConfig.load().whitelistedBlocks.isEmpty()) {
            for (String tag : BetterGrassifyConfig.load().excludedTags) {
                if (self.isIn(TagKey.of(RegistryKeys.BLOCK, Identifier.tryParse(tag)))) {
                    isExcluded = true;
                    break;
                }
            }
        } else {
            isExcluded = true;

            for (String tag : BetterGrassifyConfig.load().whitelistedTags) {
                if (self.isIn(TagKey.of(RegistryKeys.BLOCK, Identifier.tryParse(tag)))) {
                    return false;
                }
            }

            if (!isExcludedBlock) isExcluded = false;
        }

        return isExcluded;
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

    private static void dirtSpriteBake(MutableQuadView quad, BlockRenderView world, BlockPos selfPos, Supplier<Random> randomSupplier) {
        BlockPos upPos = selfPos.up();
        BlockState up = world.getBlockState(upPos);

        Sprite sprite = SpriteCalculator.calculateSprite(up, Direction.UP, randomSupplier);
        if (sprite != null) quad.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
    }
}
