package dev.ultimatchamp.bettergrass.mixin.sodium;

import com.llamalad7.mixinextras.sugar.Local;
import dev.ultimatchamp.bettergrass.model.BetterGrassifyBakedModel;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if >1.20.6 {
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.BlockState;

import net.caffeinemc.mods.sodium.client.render.chunk.compile.ChunkBuildContext;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.ChunkBuildOutput;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderCache;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.tasks.ChunkBuilderMeshingTask;
import net.caffeinemc.mods.sodium.client.util.task.CancellationToken;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;

@Mixin(value = ChunkBuilderMeshingTask.class, priority = 990)
public class ChunkBuilderMeshingTaskMixin {
    @Inject(method = "execute(Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/ChunkBuildContext;Lnet/caffeinemc/mods/sodium/client/util/task/CancellationToken;)Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/ChunkBuildOutput;",
            at = @At(value = "INVOKE",
                    target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderCache;getBlockModels()Lnet/minecraft/client/render/block/BlockModels;"
            )
    )
    private void execute(ChunkBuildContext buildContext,
                         CancellationToken cancellationToken,
                         CallbackInfoReturnable<ChunkBuildOutput> cir,
                         @Local BlockRenderCache cache,
                         @Local LevelSlice slice,
                         @Local(ordinal = 0) BlockPos.Mutable pos,
                         @Local(ordinal = 1) BlockPos.Mutable modelOffset,
                         @Local LocalRef<BlockState> blockState) {
        var hasSnowNeighbour = BetterGrassifyBakedModel.hasSnowNeighbour(slice, pos.up());

        if (hasSnowNeighbour) {
            if (BetterGrassifyBakedModel.canHaveSnowLayer(slice, pos.up())) {
                blockState.set(blockState.get().with(Properties.SNOWY, true));

                var snow = Blocks.SNOW.getDefaultState();
                var model = cache.getBlockModels().getModel(snow);
                cache.getBlockRenderer().renderModel(model, snow, pos.up(), modelOffset.up());
            }
        }
    }
//?} else {
/*import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildOutput;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderCache;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.tasks.ChunkBuilderMeshingTask;
import me.jellysquid.mods.sodium.client.util.task.CancellationToken;
import me.jellysquid.mods.sodium.client.world.WorldSlice;

@Mixin (value = ChunkBuilderMeshingTask.class, priority = 990)
public class ChunkBuilderMeshingTaskMixin {
    @Inject(method = "execute(Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationToken;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildOutput;",
            at = @At(value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer;renderModel(Lme/jellysquid/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderContext;Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildBuffers;)V",
                    remap = false
            ),
            remap = false
    )
    private void execute(ChunkBuildContext buildContext, CancellationToken cancellationToken, CallbackInfoReturnable<ChunkBuildOutput> cir, @Local BlockRenderContext ctx, @Local ChunkBuildBuffers buffers, @Local BlockRenderCache cache, @Local WorldSlice slice, @Local(ordinal = 1) BlockPos.Mutable modelOffset) {
        var hasSnowNeighbour = BetterGrassifyBakedModel.hasSnowNeighbour(slice, ctx.pos().up());

        if (hasSnowNeighbour) {
            if (BetterGrassifyBakedModel.canHaveSnowLayer(slice, ctx.pos().up())) {
                var newState = ctx.state().with(Properties.SNOWY, true);
                var newModel = cache.getBlockModels().getModel(newState);
                //? if fabric {
                ctx.update(ctx.pos(), modelOffset, newState, newModel, ctx.seed());
                //?} else {
                /^ctx.update(ctx.pos(), modelOffset, newState, newModel, ctx.seed(), ctx.modelData(), ctx.renderLayer());
                ^///?}

                var context = new BlockRenderContext(slice);
                var snow = Blocks.SNOW.getDefaultState();
                var model = cache.getBlockModels().getModel(snow);
                //? if fabric {
                context.update(ctx.pos().up(), modelOffset.up(), snow, model, ctx.seed());
                //?} else {
                /^context.update(ctx.pos().up(), modelOffset.up(), snow, model, ctx.seed(), ctx.modelData(), ctx.renderLayer());
                ^///?}
                cache.getBlockRenderer().renderModel(context, buffers);
            }
        }
    }
*///?}
}
