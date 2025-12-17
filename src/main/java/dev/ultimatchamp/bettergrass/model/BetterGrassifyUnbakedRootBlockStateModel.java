//? if >1.21.1 {
package dev.ultimatchamp.bettergrass.model;

import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperUnbakedGroupedBlockStateModel;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BetterGrassifyUnbakedRootBlockStateModel extends WrapperUnbakedGroupedBlockStateModel implements BlockStateModel.UnbakedRoot {
    private final BlockStateModel.UnbakedRoot wrapped;

    public BetterGrassifyUnbakedRootBlockStateModel(BlockStateModel.UnbakedRoot wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public @NotNull BlockStateModel bake(@NotNull BlockState blockState, @NotNull ModelBaker modelBaker) {
        return new BetterGrassifyBlockStateModel(this.wrapped.bake(blockState, modelBaker));
    }

    @Override
    public @NotNull Object visualEqualityGroup(@NotNull BlockState state) {
        return this.wrapped.visualEqualityGroup(state);
    }

    @Override
    public void resolveDependencies(@NotNull Resolver resolver) {
        this.wrapped.resolveDependencies(resolver);
    }
}
//?}
