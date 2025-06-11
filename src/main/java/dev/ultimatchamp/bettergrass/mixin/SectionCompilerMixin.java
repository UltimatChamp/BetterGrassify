package dev.ultimatchamp.bettergrass.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import dev.ultimatchamp.bettergrass.model.BetterGrassifyBakedModel;
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

/*?if <1.21.6 {*//*import net.minecraft.client.renderer.chunk.RenderChunkRegion;*//*?}*/

//? if >1.21.5 {
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
//?} else if >1.21.4 {
import net.minecraft.client.renderer.RenderType;
//?}

//? if >1.21.4 {
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.block.model.BlockModelPart;

import java.util.List;
import java.util.Map;
//?}

//? if neoforge {
/*import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;
import java.util.List;
*///?}

@Mixin(value = SectionCompiler.class)
public abstract class SectionCompilerMixin {
    @Shadow
    @Final
    private BlockRenderDispatcher blockRenderer;

    //? if >1.21.5 {
    @Shadow
    protected abstract BufferBuilder getOrBeginLayer(Map<ChunkSectionLayer, BufferBuilder> map, SectionBufferBuilderPack sectionBufferBuilderPack, ChunkSectionLayer chunkSectionLayer);
    //?} else if >1.21.4 {
    /*@Shadow
    protected abstract BufferBuilder getOrBeginLayer(Map<RenderType, BufferBuilder> bufferLayers, SectionBufferBuilderPack sectionBufferBuilderPack, RenderType renderType);
    *///?}

    //? if fabric {
    @Inject(method = "compile", at = @At(value = "INVOKE",
    //?} else {
    //@Inject(method = "compile(Lnet/minecraft/core/SectionPos;Lnet/minecraft/client/renderer/chunk/RenderChunkRegion;Lcom/mojang/blaze3d/vertex/VertexSorting;Lnet/minecraft/client/renderer/SectionBufferBuilderPack;Ljava/util/List;)Lnet/minecraft/client/renderer/chunk/SectionCompiler$Results;", at = @At(value = "INVOKE",
    //?}
            //? if >1.21.5 {
            target = "Lnet/minecraft/client/renderer/chunk/RenderSectionRegion;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;")
            //?} else if >1.21.4 {
            //target = "Lnet/minecraft/client/renderer/chunk/RenderChunkRegion;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;")
            //?} else if 1.21.1 && neoforge {
            //target = "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;renderBatched(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/BlockAndTintGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;Lnet/neoforged/neoforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V")
            //?} else {
            //target = "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;renderBatched(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/BlockAndTintGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;)V")
            //?}
    )
    private void bettergrass$render(SectionPos sectionPos, /*? if >1.21.5 {*/RenderSectionRegion/*?} else {*//*RenderChunkRegion*//*?}*/ region, VertexSorting vertexSorting,
                                    SectionBufferBuilderPack sectionBufferBuilderPack,
                                    //? if neoforge {
                                    //List<AddSectionGeometryEvent.AdditionalSectionRenderer> additionalRenderers,
                                    //?}
                                    CallbackInfoReturnable<SectionCompiler.Results> cir,
                                    @Local(ordinal = 2) BlockPos blockPos, @Local PoseStack matrixStack,
                                    //? if >1.21.4 {
                                    @Local Map</*? if >1.21.5 {*/ChunkSectionLayer/*?} else {*//*RenderType*//*?}*/, BufferBuilder> map, @Local List<BlockModelPart> list,
                                    //?} else {
                                    //@Local BufferBuilder bufferBuilder,
                                    //?}
                                    @Local RandomSource random) {
        BlockState layerNeighbour = BetterGrassifyBakedModel.getLayerNeighbour(region, blockPos);

        if (layerNeighbour != null) {
            if (BetterGrassifyBakedModel.canHaveGhostLayer(region, blockPos)) {
                matrixStack.pushPose();
                //? if >1.21.4 {
                var renderLayer = ItemBlockRenderTypes.getChunkRenderType(layerNeighbour);
                BufferBuilder bufferBuilder = this.getOrBeginLayer(map, sectionBufferBuilderPack, renderLayer);
                random.setSeed(layerNeighbour.getSeed(blockPos));
                this.blockRenderer.getBlockModel(layerNeighbour).collectParts(random, list);
                matrixStack.translate(
                        (float) SectionPos.sectionRelative(blockPos.getX()),
                        (float) SectionPos.sectionRelative(blockPos.getY()),
                        (float) SectionPos.sectionRelative(blockPos.getZ())
                );
                this.blockRenderer.renderBatched(layerNeighbour, blockPos, region,
                        matrixStack, bufferBuilder, true, list);
                list.clear();
                //?} else {
                /*blockRenderer.renderBatched(layerNeighbour, blockPos, region,
                        matrixStack, bufferBuilder, true, random);
                *///?}
                matrixStack.popPose();
            }
        }
    }

    //? if fabric {
    @ModifyVariable(method = "compile", at = @At("STORE"), ordinal = 0)
    //?} else if neoforge {
    //@ModifyVariable(method = "compile(Lnet/minecraft/core/SectionPos;Lnet/minecraft/client/renderer/chunk/RenderChunkRegion;Lcom/mojang/blaze3d/vertex/VertexSorting;Lnet/minecraft/client/renderer/SectionBufferBuilderPack;Ljava/util/List;)Lnet/minecraft/client/renderer/chunk/SectionCompiler$Results;", at = @At("STORE"), ordinal = 0)
    //?}
    private BlockState bettergrass$setGrassState(BlockState state, @Local(ordinal = 2) BlockPos blockPos,
        @Local(argsOnly = true) /*? if >1.21.5 {*/RenderSectionRegion/*?} else {*//*RenderChunkRegion*//*?}*/ region) {
        if (state.getOptionalValue(BlockStateProperties.SNOWY).isEmpty()) return state;
        if (!BetterGrassifyBakedModel.isLayerNeighbourSnow(region, blockPos.above())) return state;

        if (BetterGrassifyBakedModel.canHaveGhostLayer(region, blockPos.above()))
            return state.setValue(BlockStateProperties.SNOWY, true);

        return state;
    }
}
