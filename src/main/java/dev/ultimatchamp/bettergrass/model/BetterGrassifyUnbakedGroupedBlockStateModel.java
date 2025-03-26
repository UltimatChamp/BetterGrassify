//? if >1.21.4 {
package dev.ultimatchamp.bettergrass.model;

import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperUnbakedGroupedBlockStateModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.BlockStateModel;

public class BetterGrassifyUnbakedGroupedBlockStateModel extends WrapperUnbakedGroupedBlockStateModel implements BlockStateModel.UnbakedGrouped {
    private final BlockStateModel.UnbakedGrouped wrapped;

    public BetterGrassifyUnbakedGroupedBlockStateModel(BlockStateModel.UnbakedGrouped wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public BlockStateModel bake(BlockState state, Baker baker) {
        return new BetterGrassifyBakedModel(this.wrapped.bake(state, baker));
    }

    @Override
    public Object getEqualityGroup(BlockState state) {
        return this.wrapped.getEqualityGroup(state);
    }

    @Override
    public void resolve(Resolver resolver) {
        this.wrapped.resolve(resolver);
    }
}
//?}
