//? if >1.21.4 {
package dev.ultimatchamp.bettergrass.model;

import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperUnbakedGroupedBlockStateModel;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BetterGrassifyUnbakedGroupedBlockStateModel extends WrapperUnbakedGroupedBlockStateModel implements BlockStateModel.UnbakedRoot {
    private final BlockStateModel.UnbakedRoot wrapped;

    public BetterGrassifyUnbakedGroupedBlockStateModel(BlockStateModel.UnbakedRoot wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public @NotNull BlockStateModel bake(BlockState state, ModelBaker baker) {
        return new BetterGrassifyBakedModel(this.wrapped.bake(state, baker));
    }

    @Override
    public @NotNull Object visualEqualityGroup(BlockState state) {
        return this.wrapped.visualEqualityGroup(state);
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        this.wrapped.resolveDependencies(resolver);
    }
}
//?}
