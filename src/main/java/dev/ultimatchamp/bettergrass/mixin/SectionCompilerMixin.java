package dev.ultimatchamp.bettergrass.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexSorting;
import dev.ultimatchamp.bettergrass.util.BetterSnowUtils;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if >1.21.11 {
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.block.BlockQuadOutput;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
//?} else {
/*import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.util.RandomSource;

import java.util.Map;
*///?}

//? if <26.1 && >1.21.1 {
/*import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

import java.util.List;
*///?}

//? if >1.21.1 {
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
//?} else {
/*import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
*///?}

@Mixin(value = SectionCompiler.class)
public abstract class SectionCompilerMixin {
    //? if >1.21.11 {
    @Shadow
    @Final
    private BlockStateModelSet blockModelSet;

    @Shadow
    @Final
    private boolean cutoutLeaves;
    //?} else {
    /*@Shadow
    @Final
    private BlockRenderDispatcher blockRenderer;

    @Shadow
    protected abstract BufferBuilder getOrBeginLayer(
            Map</^? if >1.21.1 {^/ChunkSectionLayer/^?} else {^//^RenderType^//^?}^/,
        BufferBuilder> map, SectionBufferBuilderPack sectionBufferBuilderPack,
            /^? if >1.21.1 {^/ChunkSectionLayer/^?} else {^//^RenderType^//^?}^/ chunkSectionLayer);
    *///?}

    @Inject(method = "compile",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/chunk/" +
                            /*? if >1.21.1 {*/"RenderSectionRegion"/*?} else {*//*"RenderChunkRegion"*//*?}*/ +
                            ";getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    private void bettergrass$render(SectionPos sectionPos,
                                    /*? if >1.21.1 {*/RenderSectionRegion/*?} else {*//*RenderChunkRegion*//*?}*/ region,
                                    VertexSorting vertexSorting,
                                    SectionBufferBuilderPack builders,
                                    CallbackInfoReturnable<SectionCompiler.Results> cir,
                                    //? if >1.21.11 {
                                    @Local(name = "blockRenderer") ModelBlockRenderer blockRenderer,
                                    @Local(name = "quadOutput") BlockQuadOutput quadOutput,
                                    @Local(name = "opaqueQuadOutput") BlockQuadOutput opaqueQuadOutput,
                                    @Local(name = "pos") BlockPos pos
                                    //?} else {
                                    /*@Local(ordinal = 2) BlockPos pos, @Local PoseStack matrixStack,
                                    @Local Map</^? if >1.21.1 {^/ChunkSectionLayer/^?} else {^//^RenderType^//^?}^/,
                                    BufferBuilder> map
                                    *///?}
                                    ) {
        BlockState layerNeighbour = BetterSnowUtils.getLayerNeighbour(region, pos);

        if (layerNeighbour != null) {
            if (BetterSnowUtils.canHaveGhostLayer(region, pos)) {
                //? if >1.21.11 {
                blockRenderer.tesselateBlock(
                        ModelBlockRenderer.forceOpaque(this.cutoutLeaves, layerNeighbour) ? opaqueQuadOutput : quadOutput,
                        (float) SectionPos.sectionRelative(pos.getX()),
                        (float) SectionPos.sectionRelative(pos.getY()),
                        (float) SectionPos.sectionRelative(pos.getZ()),
                        region,
                        pos,
                        layerNeighbour,
                        this.blockModelSet.get(layerNeighbour),
                        layerNeighbour.getSeed(pos)
                );
                //?} else {
                /*RandomSource random = RandomSource.create();
                /^? if >1.21.1 {^/List<BlockModelPart> list = new ObjectArrayList<>();/^?}^/
                var renderLayer = ItemBlockRenderTypes.getChunkRenderType(layerNeighbour);
                BufferBuilder bufferBuilder = this.getOrBeginLayer(map, builders, renderLayer);

                random.setSeed(layerNeighbour.getSeed(pos));
                /^? if >1.21.1 {^/this.blockRenderer.getBlockModel(layerNeighbour).collectParts(random, list);/^?}^/

                matrixStack.pushPose();
                matrixStack.translate(
                        (float) SectionPos.sectionRelative(pos.getX()),
                        (float) SectionPos.sectionRelative(pos.getY()),
                        (float) SectionPos.sectionRelative(pos.getZ())
                );
                this.blockRenderer.renderBatched(layerNeighbour, pos, region, matrixStack, bufferBuilder, true, /^? if >1.21.1 {^/list/^?} else {^//^random^//^?}^/);
                    matrixStack.popPose();

                /^? if >1.21.1 {^/list.clear();/^?}^/
                *///?}
            }
        }
    }

    @ModifyVariable(method = "compile", at = @At("STORE"), ordinal = 0)
    private BlockState bettergrass$setGrassState(BlockState blockState, @Local(ordinal = 2) BlockPos pos, @Local(argsOnly = true)
                                                 /*? if >1.21.1 {*/RenderSectionRegion/*?} else {*//*RenderChunkRegion*//*?}*/ region) {
        if (blockState.getOptionalValue(BlockStateProperties.SNOWY).isEmpty() ||
                !BetterSnowUtils.isLayerNeighbourSnow(region, pos.above())) return blockState;

        if (BetterSnowUtils.canHaveGhostLayer(region, pos.above()))
            return blockState.setValue(BlockStateProperties.SNOWY, true);

        return blockState;
    }
}
