package dev.ultimatchamp.bettergrass.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import dev.ultimatchamp.bettergrass.util.BetterSnowUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
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

import java.util.List;
import java.util.Map;

@Mixin(value = SectionCompiler.class)
public abstract class SectionCompilerMixin {
    @Shadow
    @Final
    private BlockRenderDispatcher blockRenderer;

    @Shadow
    protected abstract BufferBuilder getOrBeginLayer(Map<ChunkSectionLayer, BufferBuilder> map, SectionBufferBuilderPack sectionBufferBuilderPack, ChunkSectionLayer chunkSectionLayer);

    @Inject(method = "compile",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/chunk/RenderSectionRegion;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    private void bettergrass$render(SectionPos sectionPos, RenderSectionRegion region, VertexSorting vertexSorting,
                                    SectionBufferBuilderPack sectionBufferBuilderPack,
                                    CallbackInfoReturnable<SectionCompiler.Results> cir,
                                    @Local(ordinal = 2) BlockPos blockPos, @Local PoseStack matrixStack,
                                    @Local Map<ChunkSectionLayer, BufferBuilder> map) {
        BlockState layerNeighbour = BetterSnowUtils.getLayerNeighbour(region, blockPos);

        if (layerNeighbour != null) {
            if (BetterSnowUtils.canHaveGhostLayer(region, blockPos)) {
                RandomSource random = RandomSource.create();
                List<BlockModelPart> list = new ObjectArrayList<>();
                ChunkSectionLayer renderLayer = ItemBlockRenderTypes.getChunkRenderType(layerNeighbour);
                BufferBuilder bufferBuilder = this.getOrBeginLayer(map, sectionBufferBuilderPack, renderLayer);

                random.setSeed(layerNeighbour.getSeed(blockPos));
                this.blockRenderer.getBlockModel(layerNeighbour).collectParts(random, list);

                matrixStack.pushPose();
                matrixStack.translate(
                        (float) SectionPos.sectionRelative(blockPos.getX()),
                        (float) SectionPos.sectionRelative(blockPos.getY()),
                        (float) SectionPos.sectionRelative(blockPos.getZ())
                );
                this.blockRenderer.renderBatched(layerNeighbour, blockPos, region, matrixStack, bufferBuilder, true, list);
                matrixStack.popPose();

                list.clear();
            }
        }
    }

    @ModifyVariable(method = "compile", at = @At("STORE"), ordinal = 0)
    private BlockState bettergrass$setGrassState(BlockState state, @Local(ordinal = 2) BlockPos blockPos,
                                                 @Local(argsOnly = true) RenderSectionRegion region) {
        if (state.getOptionalValue(BlockStateProperties.SNOWY).isEmpty() ||
                !BetterSnowUtils.isLayerNeighbourSnow(region, blockPos.above())) return state;

        if (BetterSnowUtils.canHaveGhostLayer(region, blockPos.above()))
            return state.setValue(BlockStateProperties.SNOWY, true);

        return state;
    }
}
