package dev.ultimatchamp.bettergrass.model;

import dev.ultimatchamp.bettergrass.compat.WilderWildCompat;
import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import dev.ultimatchamp.bettergrass.util.BetterSnowUtils;
import dev.ultimatchamp.bettergrass.util.SpriteCalculator;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.Supplier;

//? if >1.21.1 {
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;
//?} else {
/*import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.resources.model.BakedModel;
*///?}

public class BetterGrassifyBlockStateModel extends
        //? if >1.21.1 {
        WrapperBlockStateModel implements BlockStateModel {
        //?} else {
        /*ForwardingBakedModel implements BakedModel {
        *///?}
    private BetterGrassifyConfig config;

    public BetterGrassifyBlockStateModel(/*? if >1.21.1 {*/BlockStateModel/*?} else {*//*BakedModel*//*?}*/ wrapped) {
        //? if >1.21.1 {
        super(wrapped);
        //?} else {
        /*this.wrapped = wrapped;
        *///?}
        this.config = BetterGrassifyConfig.load();
    }

    //? if 1.21.1 {
    /*@Override
    public boolean isVanillaAdapter() {
        return false;
    }
    *///?}

    @Override
    //? if >1.21.1 {
    public void emitQuads(@NotNull QuadEmitter emitter, @NotNull BlockAndTintGetter blockView, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull RandomSource random, @NotNull Predicate<@Nullable Direction> cullTest) {
    //?} else {
    /*public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext emitter) {
    *///?}
        if (config.general.betterGrassMode == BetterGrassifyConfig.BetterGrassMode.OFF) {
            //? if >1.21.1 {
            super.emitQuads(emitter, blockView, pos, state, random, cullTest);
            //?} else {
            /*super.emitBlockQuads(blockView, state, pos, randomSupplier, emitter);
            *///?}
            return;
        }

        /*? if >1.21.1 {*/Supplier<RandomSource> randomSupplier = () -> random;/*?}*/
        emitter.pushTransform(quad -> {
            this.config = BetterGrassifyConfig.load();

            Direction face = quad.nominalFace();
            if (face == null || face.getAxis().isVertical() || state.hasBlockEntity() || !isFullQuad(quad)) return true;

            if (state.is(Blocks.DIRT)) { // Fix dirt paths connection, only if on a dirt block
                if (isBelowNonFullBlock(blockView, pos, face))
                    spriteBake(quad, blockView.getBlockState(pos.above()), randomSupplier);
            } else betterGrassify(quad, blockView, state, pos, face, randomSupplier);

            return true;
        });

        //? if >1.21.1 {
        super.emitQuads(emitter, blockView, pos, state, random, cullTest);
        //?} else {
        /*super.emitBlockQuads(blockView, state, pos, randomSupplier, emitter);
        *///?}
        emitter.popTransform();
    }

    public void betterGrassify(MutableQuadView quad, BlockAndTintGetter world, BlockState state, BlockPos pos, Direction face, Supplier<RandomSource> randomSupplier) {
        if (this.config.general.betterGrassMode.equals(BetterGrassifyConfig.BetterGrassMode.FANCY)) {
            if (canFullyConnect(world, state, pos, face))
                spriteBake(quad, isSnowy(world, pos) ? world.getBlockState(pos.above()) : state, randomSupplier);
            else betterSnowyGrass(quad, world, pos, face, randomSupplier);
        } else if (this.config.general.betterGrassMode.equals(BetterGrassifyConfig.BetterGrassMode.FAST)) {
            if (isSnowy(world, pos))
                spriteBake(quad, world.getBlockState(pos.above()), randomSupplier);
            else if (BetterSnowUtils.canHaveGhostSnowLayer(world, pos.above()) && this.config.general.blocks.snowy) // Better Snow Mode check, as block has ghost snow
                spriteBake(quad, BetterSnowUtils.getLayerNeighbour(world, pos.above()), randomSupplier);
            else spriteBake(quad, world.getBlockState(pos), randomSupplier);
        }
    }

    public void betterSnowyGrass(MutableQuadView quad, BlockAndTintGetter world, BlockPos pos, Direction face, Supplier<RandomSource> randomSupplier) {
        BlockPos adjacentPos = pos.relative(face);

        if (isSnowy(world, pos) && BetterSnowUtils.canHaveGhostSnowLayer(world, adjacentPos)) {
            // Self: Snowy Grass | Below: Ghost Snow
            spriteBake(quad, world.getBlockState(pos.above()), randomSupplier);
        } else if (BetterSnowUtils.canHaveGhostSnowLayer(world, pos.above()) && isSnowy(world, adjacentPos.below())) {
            // Self: Ghost Snow | Below: Snowy Grass
            spriteBake(quad, world.getBlockState(adjacentPos), randomSupplier);
        } else if (BetterSnowUtils.canHaveGhostSnowLayer(world, pos.above()) && BetterSnowUtils.canHaveGhostSnowLayer(world, adjacentPos)) {
            // Self: Ghost Snow | Below: Ghost Snow
            if (!this.config.general.blocks.snowy)
                return; // Better Snow Mode check, as both self and below have ghost snow
            spriteBake(quad, BetterSnowUtils.getLayerNeighbour(world, pos.above()), randomSupplier);
        }
    }

    private boolean isFullQuad(MutableQuadView quad) {
        if (!this.config.general.resourcePackCompatibilityMode) return true;

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

        return canConnect(self, adjacent) && (up.isAir() || isSnowy(world, selfPos) || !up.isFaceSturdy(world, upPos, Direction.DOWN));
    }

    private static boolean canConnect(BlockState self, BlockState adjacent) {
        return self == adjacent;
    }

    private boolean isBelowNonFullBlock(BlockAndTintGetter world, BlockPos selfPos, Direction direction) {
        BlockPos upPos = selfPos.above();
        BlockState up = world.getBlockState(upPos);

        if (!(up.is(Blocks.DIRT_PATH) || up.is(Blocks.FARMLAND))) return false;

        if (up.is(Blocks.DIRT_PATH) && !this.config.general.blocks.dirtPaths) return false;
        if (up.is(Blocks.FARMLAND) && !this.config.general.blocks.farmLands) return false;

        return canFullyConnect(world, up, upPos, direction);
    }

    private static boolean isSnowy(BlockAndTintGetter world, BlockPos selfPos) {
        if (FabricLoader.getInstance().isModLoaded("wilderwild") && WilderWildCompat.isSnowloggingOn()) return false;

        BlockState self = world.getBlockState(selfPos);
        return self.getOptionalValue(BlockStateProperties.SNOWY).orElse(false) && !world.getBlockState(selfPos.above()).isAir();
    }

    private static void spriteBake(MutableQuadView quad, BlockState state, Supplier<RandomSource> randomSupplier) {
        TextureAtlasSprite sprite = SpriteCalculator.calculateSprite(state, Direction.UP, randomSupplier);
        if (sprite != null) quad.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
    }
}
