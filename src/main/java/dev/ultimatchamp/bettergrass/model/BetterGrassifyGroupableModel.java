//? if 1.21.4 {
/*package dev.ultimatchamp.bettergrass.model;

import net.fabricmc.fabric.api.client.model.loading.v1.WrapperGroupableModel;
import net.minecraft.client.renderer.block.model.UnbakedBlockStateModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BetterGrassifyGroupableModel extends WrapperGroupableModel implements UnbakedBlockStateModel {
    private final UnbakedBlockStateModel wrapped;

    public BetterGrassifyGroupableModel(UnbakedBlockStateModel wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public @NotNull BakedModel bake(ModelBaker baker) {
        return new BetterGrassifyBakedModel(this.wrapped.bake(baker));
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
*///?}
