//? if >1.21.3 {
package dev.ultimatchamp.bettergrass.model;

import net.fabricmc.fabric.api.client.model.loading.v1.WrapperGroupableModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.GroupableModel;

public class BetterGrassifyGroupableModel extends WrapperGroupableModel implements GroupableModel {
    private final GroupableModel baseModel;

    public BetterGrassifyGroupableModel(GroupableModel groupableModel) {
        this.baseModel = groupableModel;
    }

    @Override
    public Object getEqualityGroup(BlockState state) {
        return baseModel.getEqualityGroup(state);
    }

    @Override
    public void resolve(Resolver resolver) {
        baseModel.resolve(resolver);
    }

    @Override
    public BakedModel bake(Baker baker) {
        return new BetterGrassifyBakedModel(baseModel.bake(baker));
    }
}
//?}
