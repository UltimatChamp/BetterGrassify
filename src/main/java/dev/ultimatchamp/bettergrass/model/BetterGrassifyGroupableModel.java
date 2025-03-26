//? if =1.21.4 {
/*package dev.ultimatchamp.bettergrass.model;

import net.fabricmc.fabric.api.client.model.loading.v1.WrapperGroupableModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.GroupableModel;

public class BetterGrassifyGroupableModel extends WrapperGroupableModel implements GroupableModel {
    private final GroupableModel wrapped;

    public BetterGrassifyGroupableModel(GroupableModel wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public BakedModel bake(Baker baker) {
        return new BetterGrassifyBakedModel(this.wrapped.bake(baker));
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
*///?}
