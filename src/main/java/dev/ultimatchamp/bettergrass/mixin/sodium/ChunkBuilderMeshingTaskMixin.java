package dev.ultimatchamp.bettergrass.mixin.sodium;

import com.llamalad7.mixinextras.sugar.Local;
import dev.ultimatchamp.bettergrass.model.BetterGrassifyBakedModel;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    private void bettergrass$execute(ChunkBuildContext buildContext,
                         CancellationToken cancellationToken,
                         CallbackInfoReturnable<ChunkBuildOutput> cir,
                         @Local BlockRenderCache cache,
                         @Local LevelSlice slice,
                         @Local(ordinal = 0) BlockPos.Mutable pos,
                         @Local(ordinal = 1) BlockPos.Mutable modelOffset,
                         @Local LocalRef<BlockState> blockState) {
        Block snowNeighbour = BetterGrassifyBakedModel.snowNeighbour(slice, pos.up());

        if (snowNeighbour != null) {
            if (BetterGrassifyBakedModel.canHaveSnowLayer(slice, pos.up())) {
                if (snowNeighbour == Blocks.SNOW) {
                    if (blockState.get().getOrEmpty(Properties.SNOWY).isPresent()) {
                        blockState.set(blockState.get().with(Properties.SNOWY, true));
                    }
                }

                BlockState state = snowNeighbour.getDefaultState();
                BakedModel model = cache.getBlockModels().getModel(state);
                cache.getBlockRenderer().renderModel(model, state, pos.up(), modelOffset.up());
            }
        }
    }
}
