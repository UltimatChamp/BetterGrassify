package dev.ultimatchamp.bettergrass.model;

import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import dev.ultimatchamp.bettergrass.util.SpriteCalculator;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.function.Supplier;

public class BetterGrassifyBakedModel extends ForwardingBakedModel {
    public BetterGrassifyBakedModel(BakedModel baseModel) {
        this.wrapped = baseModel;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        context.pushTransform(quad ->
                switch (BetterGrassifyConfig.instance().betterGrassMode) {
                    case OFF:
                        yield true;
                    case FAST:
                        if (quad.nominalFace().getAxis() != Direction.Axis.Y) {
                            if (isSnowy(blockView, pos)) {
                                spriteBake(quad, blockView.getBlockState(pos.up()), randomSupplier);
                                yield true;
                            } else if (canHaveSnowLayer(blockView, pos.up()) && isNeighbourSnow(blockView, pos.up())) {
                                spriteBake(quad, snowNeighbour(blockView, pos.up()).getDefaultState(), randomSupplier);
                                yield true;
                            }
                            spriteBake(quad, blockView.getBlockState(pos), randomSupplier);
                        }
                        yield true;
                    case FANCY:
                        if (quad.nominalFace().getAxis() != Direction.Axis.Y) {
                            Direction face = quad.nominalFace();

                            if (canFullyConnect(blockView, state, pos, face)) {
                                if (isSnowy(blockView, pos)) {
                                    spriteBake(quad, blockView.getBlockState(pos.up()), randomSupplier);
                                    yield true;
                                }
                                spriteBake(quad, state, randomSupplier);
                            } else if (isSnowy(blockView, pos) && canHaveSnowLayer(blockView, pos.offset(face)) && isNeighbourSnow(blockView, pos.offset(face))) {
                                spriteBake(quad, blockView.getBlockState(pos.up()), randomSupplier);
                            } else if (canHaveSnowLayer(blockView, pos.up()) && isNeighbourSnow(blockView, pos.up()) && isSnowy(blockView, pos.offset(face).down())) {
                                spriteBake(quad, blockView.getBlockState(pos.offset(face)), randomSupplier);
                            } else if (canHaveSnowLayer(blockView, pos.up()) && isNeighbourSnow(blockView, pos.up()) && canHaveSnowLayer(blockView, pos.offset(face)) && canHaveSnowLayer(blockView, pos.offset(face))) {
                                spriteBake(quad, snowNeighbour(blockView, pos.up()).getDefaultState(), randomSupplier);
                            }
                        }
                        yield true;
                });
        super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
        context.popTransform();
    }

    private static boolean canFullyConnect(BlockRenderView world, BlockState self, BlockPos selfPos, Direction direction) {
        return canConnect(world, self, selfPos, selfPos.offset(direction).down());
    }

    private static boolean canConnect(BlockRenderView world, BlockState self, BlockPos selfPos, BlockPos adjacentPos) {
        var adjacent = world.getBlockState(adjacentPos);
        var upPos = adjacentPos.up();
        var up = world.getBlockState(upPos);

        return canConnect(self, adjacent) && (up.isAir() || isSnowy(world, selfPos) || !up.isSideSolidFullSquare(world, upPos, Direction.DOWN));
    }

    private static boolean canConnect(BlockState self, BlockState adjacent) {
        return self == adjacent;
    }

    private static boolean isSnowy(BlockRenderView world, BlockPos selfPos) {
        var self = world.getBlockState(selfPos);

        var snowyCheck = false;
        if (self.getOrEmpty(Properties.SNOWY).isPresent()) {
            snowyCheck =
                    self == self.with(Properties.SNOWY, true);
        }

        return snowyCheck;
    }

    public static Block snowNeighbour(BlockRenderView world, BlockPos selfPos) {
        for (String id : BetterGrassifyConfig.instance().snowLayers) {
            Identifier identifier = Identifier.tryParse(id);

            //? if >1.21.1 {
            var layer = Registries.BLOCK.getOptionalValue(identifier);
            //?} else {
            /*var layer = Registries.BLOCK.getOrEmpty(identifier);
            *///?}

            if (layer.isEmpty()) {
                continue;
            }

            List<BlockPos> directions = List.of(selfPos.north(), selfPos.south(), selfPos.east(), selfPos.west());
            var isSnow = new boolean[4];

            for (BlockPos direction : directions) {
                isSnow[directions.indexOf(direction)] = world.getBlockState(direction).isOf(layer.get()) || (id.equals("snow") && world.getBlockState(direction).isOf(Blocks.SNOW_BLOCK));
            }

            var layerCheck = false;
            switch (BetterGrassifyConfig.instance().betterSnowMode) {
                case OPTIFINE ->
                        layerCheck = isSnow[0] || isSnow[1] || isSnow[2] || isSnow[3];
                case LAMBDA ->
                        layerCheck = ((isSnow[0] || isSnow[1]) && (isSnow[2] || isSnow[3])) || (isSnow[0] && isSnow[1]) || (isSnow[2] && isSnow[3]);
                default -> {
                    continue;
                }
            }

            if (layerCheck) {
                return layer.get();
            }
        }
        return null;
    }

    public static boolean isNeighbourSnow(BlockRenderView world, BlockPos selfPos) {
        return snowNeighbour(world, selfPos) == Blocks.SNOW;
    }

    public static boolean canHaveSnowLayer(BlockRenderView world, BlockPos selfPos) {
        if (BetterGrassifyConfig.instance().betterSnowMode == BetterGrassifyConfig.BetterSnowMode.OFF) {
            return false;
        }

        var self = world.getBlockState(selfPos);

        var isLayer =
                snowNeighbour(world, selfPos) != null;

        var isExcludedTag = false;
        for (String tag : BetterGrassifyConfig.instance().excludedTags) {
            Identifier identifier = Identifier.tryParse(tag);

            var tagKey = TagKey.of(RegistryKeys.BLOCK, identifier);

            if (self.isIn(tagKey)) {
                isExcludedTag = true;
            }
        }

        var isExcludedBlock = false;
        for (String block : BetterGrassifyConfig.instance().excludedBlocks) {
            Identifier identifier = Identifier.tryParse(block);

            //? if >1.21.1 {
            var blockCheck = Registries.BLOCK.getOptionalValue(identifier);
            //?} else {
            /*var blockCheck = Registries.BLOCK.getOrEmpty(identifier);
            *///?}

            if (blockCheck.isPresent()) {
                if (self.getBlock().equals(blockCheck.get())) {
                    isExcludedBlock = true;
                }
            }
        }

        return isLayer && !self.isAir() && !self.isOf(Blocks.WATER) && !(isExcludedTag || isExcludedBlock) && !self.isSideSolidFullSquare(world, selfPos, Direction.DOWN);
    }

    private static void spriteBake(MutableQuadView quad, BlockState state, Supplier<Random> randomSupplier) {
        var sprite = SpriteCalculator.calculateSprite(state, Direction.UP, randomSupplier);

        if (sprite != null) {
            quad.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
        }
    }
}
