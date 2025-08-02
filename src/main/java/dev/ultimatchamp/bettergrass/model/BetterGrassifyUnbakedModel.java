//? if 1.21.1 {
package dev.ultimatchamp.bettergrass.model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public class BetterGrassifyUnbakedModel implements UnbakedModel {
    private final UnbakedModel baseModel;

    public BetterGrassifyUnbakedModel(UnbakedModel unbakedModel) {
        this.baseModel = unbakedModel;
    }

    @Override
    public @NotNull Collection<ResourceLocation> getDependencies() {
        return this.baseModel.getDependencies();
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> modelLoader) {
        this.baseModel.resolveParents(modelLoader);
    }

    @Override
    public @NotNull BakedModel bake(ModelBaker loader, Function<Material, TextureAtlasSprite> textureGetter,
                                    ModelState rotationContainer) {
        return new BetterGrassifyBlockStateModel(this.baseModel.bake(loader, textureGetter, rotationContainer));
    }
}
//?}
