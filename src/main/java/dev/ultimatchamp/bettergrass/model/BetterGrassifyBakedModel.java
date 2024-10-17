package dev.ultimatchamp.bettergrass.model;

import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import dev.ultimatchamp.bettergrass.util.SpriteCalculator;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
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

import java.util.function.Supplier;

//? if neo || <1.21 {
/*import net.fabricmc.loader.api.FabricLoader;
*///?}

@SuppressWarnings("ConstantConditions")
public class BetterGrassifyBakedModel extends ForwardingBakedModel {
    public BetterGrassifyBakedModel(BakedModel baseModel) {
        this.wrapped = baseModel;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        context.pushTransform(quad -> {
            if (BetterGrassifyConfig.instance().betterGrassMode.equals(BetterGrassifyConfig.BetterGrassMode.OFF)) {
                return true;
            } else if (BetterGrassifyConfig.instance().betterGrassMode.equals(BetterGrassifyConfig.BetterGrassMode.FAST)) {
                if (quad.nominalFace().getAxis() != Direction.Axis.Y) {
                    if (isSnowy(blockView, pos)) {
                        spriteBake(quad, blockView.getBlockState(pos.up()), randomSupplier);
                        return true;
                    } else if (canHaveSnowLayer(blockView, pos.up())) {
                        spriteBake(quad, Blocks.SNOW.getDefaultState(), randomSupplier);
                        return true;
                    }
                    spriteBake(quad, blockView.getBlockState(pos), randomSupplier);
                    return true;
                }
                return true;
            } else if (BetterGrassifyConfig.instance().betterGrassMode.equals(BetterGrassifyConfig.BetterGrassMode.FANCY)) {
                if (quad.nominalFace().getAxis() != Direction.Axis.Y) {
                    Direction face = quad.nominalFace();

                    if (canFullyConnect(blockView, state, pos, face)) {
                        if (isSnowy(blockView, pos)) {
                            spriteBake(quad, blockView.getBlockState(pos.up()), randomSupplier);
                            return true;
                        }
                        spriteBake(quad, state, randomSupplier);
                        return true;
                    } else if (isSnowy(blockView, pos) && canHaveSnowLayer(blockView, pos.offset(face))) {
                        spriteBake(quad, blockView.getBlockState(pos.up()), randomSupplier);
                        return true;
                    } else if (canHaveSnowLayer(blockView, pos.up()) && isSnowy(blockView, pos.offset(face).down())) {
                        spriteBake(quad, blockView.getBlockState(pos.offset(face)), randomSupplier);
                        return true;
                    } else if (canHaveSnowLayer(blockView, pos.up()) && canHaveSnowLayer(blockView, pos.offset(face))) {
                        spriteBake(quad, Blocks.SNOW.getDefaultState(), randomSupplier);
                        return true;
                    }
                }
                return true;
            }
            return true;
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

    public static boolean hasSnowNeighbour(BlockRenderView world, BlockPos selfPos) {
        var isNorth = world.getBlockState(selfPos.north()).isOf(Blocks.SNOW) || world.getBlockState(selfPos.north()).isOf(Blocks.SNOW_BLOCK);
        var isSouth = world.getBlockState(selfPos.south()).isOf(Blocks.SNOW) || world.getBlockState(selfPos.south()).isOf(Blocks.SNOW_BLOCK);
        var isEast = world.getBlockState(selfPos.east()).isOf(Blocks.SNOW) || world.getBlockState(selfPos.east()).isOf(Blocks.SNOW_BLOCK);
        var isWest = world.getBlockState(selfPos.west()).isOf(Blocks.SNOW) || world.getBlockState(selfPos.west()).isOf(Blocks.SNOW_BLOCK);

        if (BetterGrassifyConfig.instance().betterSnowMode == BetterGrassifyConfig.BetterSnowMode.OPTIFINE) {
            return isNorth || isSouth || isEast || isWest;
        } else if (BetterGrassifyConfig.instance().betterSnowMode == BetterGrassifyConfig.BetterSnowMode.LAMBDA) {
            return ((isNorth || isSouth) && (isEast || isWest)) || (isNorth && isSouth) || (isEast && isWest);
        }
        return false;
    }

    public static boolean canHaveSnowLayer(BlockRenderView world, BlockPos selfPos) {
        //? if neo {
        /*if (!FabricLoader.getInstance().isModLoaded("sodium")) {
            return false;
        }
        *///?}

        //? if <1.21 {
        /*if (!(FabricLoader.getInstance().isModLoaded("sodium") || FabricLoader.getInstance().isModLoaded("embeddium"))) {
            return false;
        }
        *///?}

        if (BetterGrassifyConfig.instance().betterSnowMode == BetterGrassifyConfig.BetterSnowMode.OFF) {
            return false;
        }

        var self = world.getBlockState(selfPos);
        var bottom = world.getBlockState(selfPos.down());

        var isBottomSnowy = false;
        if (bottom.getOrEmpty(Properties.SNOWY).isPresent()) {
            isBottomSnowy =
                    bottom == bottom.with(Properties.SNOWY, false);
        }

        var snowDetectionMode = hasSnowNeighbour(world, selfPos);

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
            /*var blockCheck = Registries.BLOCK.getOptionalValue(identifier);
            *///?} else {
            var blockCheck = Registries.BLOCK.getOrEmpty(identifier);
            //?}

            if (blockCheck.isPresent()) {
                if (self.getBlock().equals(blockCheck.get())) {
                    isExcludedBlock = true;
                }
            }
        }

        return snowDetectionMode && !self.isAir() && !self.isOf(Blocks.WATER) && !(isExcludedTag || isExcludedBlock) && isBottomSnowy && !self.isSideSolidFullSquare(world, selfPos, Direction.DOWN);
    }

    @SuppressWarnings({"deprecation", "UnusedReturnValue"})
    private static boolean spriteBake(MutableQuadView quad, BlockState state, Supplier<Random> randomSupplier) {
        var sprite = SpriteCalculator.calculateSprite(state, Direction.UP, randomSupplier);

        if (sprite != null) {
            quad.spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV);
        }
        return sprite != null;
    }
}
