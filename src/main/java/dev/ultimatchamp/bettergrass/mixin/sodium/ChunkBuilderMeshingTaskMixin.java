package dev.ultimatchamp.bettergrass.mixin.sodium;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.ultimatchamp.bettergrass.util.BetterSnowUtils;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.ChunkBuildContext;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.ChunkBuildOutput;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderCache;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.tasks.ChunkBuilderMeshingTask;
import net.caffeinemc.mods.sodium.client.util.task.CancellationToken;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChunkBuilderMeshingTask.class, priority = 990)
public class ChunkBuilderMeshingTaskMixin {
    @Inject(method = "execute(Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/ChunkBuildContext;Lnet/caffeinemc/mods/sodium/client/util/task/CancellationToken;)Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/ChunkBuildOutput;",
            at = @At(value = "INVOKE",
                    target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderCache;getBlockModels()Lnet/minecraft/client/renderer/block/BlockModelShaper;"
            )
    )
    private void bettergrass$execute(ChunkBuildContext buildContext,
                                     CancellationToken cancellationToken,
                                     CallbackInfoReturnable<ChunkBuildOutput> cir,
                                     @Local BlockRenderCache cache,
                                     @Local LevelSlice slice,
                                     @Local(ordinal = 0) BlockPos.MutableBlockPos pos,
                                     @Local(ordinal = 1) BlockPos.MutableBlockPos modelOffset,
                                     @Local LocalRef<BlockState> blockState) {
        BlockState layerNeighbour = BetterSnowUtils.getLayerNeighbour(slice, pos.above());

        if (layerNeighbour != null) {
            if (BetterSnowUtils.canHaveGhostLayer(slice, pos.above())) {
                if (layerNeighbour.is(Blocks.SNOW)) {
                    if (blockState.get().getOptionalValue(BlockStateProperties.SNOWY).isPresent()) {
                        blockState.set(blockState.get().setValue(BlockStateProperties.SNOWY, true));
                    }
                }

                BlockStateModel model = cache.getBlockModels().getBlockModel(layerNeighbour);
                cache.getBlockRenderer().renderModel(model, layerNeighbour, pos.above(), modelOffset.above());
            }
        }
    }
}
