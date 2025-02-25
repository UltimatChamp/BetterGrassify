package dev.ultimatchamp.bettergrass.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.VertexSorter;
import dev.ultimatchamp.bettergrass.model.BetterGrassifyBakedModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.BlockBufferAllocatorStorage;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.render.chunk.SectionBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if neoforge {
/*import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;
import java.util.List;
*///?}

@Mixin(value = SectionBuilder.class)
public class SectionBuilderMixin {
    @Shadow
    @Final
    private BlockRenderManager blockRenderManager;

    //? if fabric {
    @Inject(method = "build", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/render/block/BlockRenderManager;renderBlock(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;ZLnet/minecraft/util/math/random/Random;)V"))
    //?} else if neoforge {
    /*@Inject(method = "compile", at = @At(value = "INVOKE",
              target = "Lnet/minecraft/client/render/block/BlockRenderManager;renderBatched(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;ZLnet/minecraft/util/math/random/Random;Lnet/neoforged/neoforge/client/model/data/ModelData;Lnet/minecraft/client/render/RenderLayer;)V"))
    *///?}
    private void bettergrass$render(ChunkSectionPos sectionPos,
                        ChunkRendererRegion renderRegion,
                        VertexSorter vertexSorter,
                        BlockBufferAllocatorStorage allocatorStorage,
                        //? if neoforge {
                        /*List<AddSectionGeometryEvent.AdditionalSectionRenderer> additionalRenderers,
                        *///?}
                        CallbackInfoReturnable<SectionBuilder.RenderData> cir,
                        @Local BlockState blockState,
                        @Local(ordinal = 2) BlockPos blockPos,
                        @Local MatrixStack matrixStack,
                        @Local BufferBuilder bufferBuilder,
                        @Local Random random) {
        Block snowNeighbour = BetterGrassifyBakedModel.snowNeighbour(renderRegion, blockPos.up());

        if (snowNeighbour != null) {
            if (BetterGrassifyBakedModel.canHaveSnowLayer(renderRegion, blockPos.up())) {
                matrixStack.push();
                matrixStack.translate(0, 1, 0);
                blockRenderManager.renderBlock(snowNeighbour.getDefaultState(), blockPos.up(), renderRegion,
                                               matrixStack, bufferBuilder, true, random);
                matrixStack.pop();
            }
        }
    }

    //? if fabric {
    @ModifyVariable(method = "build", at = @At("STORE"), ordinal = 0)
    //?} else if neoforge {
    /*@ModifyVariable(method = "compile", at = @At("STORE"), ordinal = 0)
    *///?}
    private BlockState bettergrass$setGrassState(BlockState state, @Local(ordinal = 2) BlockPos blockPos,
                                                 @Local(argsOnly = true) ChunkRendererRegion renderRegion) {
        if (state.getOrEmpty(Properties.SNOWY).isEmpty()) return state;
        if (BetterGrassifyBakedModel.isNeighbourSnow(renderRegion, blockPos.up())) return state;

        if (BetterGrassifyBakedModel.canHaveSnowLayer(renderRegion, blockPos.up()))
            return state.with(Properties.SNOWY, true);

        return state;
    }
}
