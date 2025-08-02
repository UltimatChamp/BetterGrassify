package dev.ultimatchamp.bettergrass.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import dev.ultimatchamp.bettergrass.util.BetterSnowUtils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

//? if >1.21.1 {
/*import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;

import java.util.List;
*///?} else {
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
//?}

@Mixin(value = SectionCompiler.class)
public abstract class SectionCompilerMixin {
    @Shadow
    @Final
    private BlockRenderDispatcher blockRenderer;

    @Shadow
    protected abstract BufferBuilder getOrBeginLayer(
            Map</*? if >1.21.1 {*//*ChunkSectionLayer*//*?} else {*/RenderType/*?}*/,
                    BufferBuilder> map, SectionBufferBuilderPack sectionBufferBuilderPack,
            /*? if >1.21.1 {*//*ChunkSectionLayer*//*?} else {*/RenderType/*?}*/ chunkSectionLayer);

    @Inject(method = "compile",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/chunk/" +
                            /*? if >1.21.1 {*//*"RenderSectionRegion"*//*?} else {*/"RenderChunkRegion"/*?}*/ +
                            ";getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    private void bettergrass$render(SectionPos sectionPos,
                                    /*? if >1.21.1 {*//*RenderSectionRegion*//*?} else {*/RenderChunkRegion/*?}*/ region,
                                    VertexSorting vertexSorting,
                                    SectionBufferBuilderPack sectionBufferBuilderPack,
                                    CallbackInfoReturnable<SectionCompiler.Results> cir,
                                    @Local(ordinal = 2) BlockPos blockPos, @Local PoseStack matrixStack,
                                    @Local Map</*? if >1.21.1 {*//*ChunkSectionLayer*//*?} else {*/RenderType/*?}*/,
                                    BufferBuilder> map) {
        BlockState layerNeighbour = BetterSnowUtils.getLayerNeighbour(region, blockPos);

        if (layerNeighbour != null) {
            if (BetterSnowUtils.canHaveGhostLayer(region, blockPos)) {
                RandomSource random = RandomSource.create();
                /*? if >1.21.1 {*//*List<BlockModelPart> list = new ObjectArrayList<>();*//*?}*/
                var renderLayer = ItemBlockRenderTypes.getChunkRenderType(layerNeighbour);
                BufferBuilder bufferBuilder = this.getOrBeginLayer(map, sectionBufferBuilderPack, renderLayer);

                random.setSeed(layerNeighbour.getSeed(blockPos));
                /*? if >1.21.1 {*//*this.blockRenderer.getBlockModel(layerNeighbour).collectParts(random, list);*//*?}*/

                matrixStack.pushPose();
                matrixStack.translate(
                        (float) SectionPos.sectionRelative(blockPos.getX()),
                        (float) SectionPos.sectionRelative(blockPos.getY()),
                        (float) SectionPos.sectionRelative(blockPos.getZ())
                );
                this.blockRenderer.renderBatched(layerNeighbour, blockPos, region, matrixStack, bufferBuilder, true, /*? if >1.21.1 {*//*list*//*?} else {*/random/*?}*/);
                matrixStack.popPose();

                /*? if >1.21.1 {*//*list.clear();*//*?}*/
            }
        }
    }

    @ModifyVariable(method = "compile", at = @At("STORE"), ordinal = 0)
    private BlockState bettergrass$setGrassState(BlockState state, @Local(ordinal = 2) BlockPos blockPos, @Local(argsOnly = true)
                                                 /*? if >1.21.1 {*//*RenderSectionRegion*//*?} else {*/RenderChunkRegion/*?}*/ region) {
        if (state.getOptionalValue(BlockStateProperties.SNOWY).isEmpty() ||
                !BetterSnowUtils.isLayerNeighbourSnow(region, blockPos.above())) return state;

        if (BetterSnowUtils.canHaveGhostLayer(region, blockPos.above()))
            return state.setValue(BlockStateProperties.SNOWY, true);

        return state;
    }
}
