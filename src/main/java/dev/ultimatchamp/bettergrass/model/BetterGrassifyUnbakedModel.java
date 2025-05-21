//? if <1.21.4 {
/*package dev.ultimatchamp.bettergrass.model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

//? if <1.21.3 {
/^import net.minecraft.resources.ResourceLocation;
import java.util.Collection;
^///?}

public class BetterGrassifyUnbakedModel implements UnbakedModel {
    private final UnbakedModel baseModel;

    public BetterGrassifyUnbakedModel(UnbakedModel unbakedModel) {
        this.baseModel = unbakedModel;
    }

    //? if >1.21.1 {
    @Override
    public void resolveDependencies(Resolver resolver) {
        this.baseModel.resolveDependencies(resolver);
    }
    //?} else {
    /^@Override
    public @NotNull Collection<ResourceLocation> getDependencies() {
        return this.baseModel.getDependencies();
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> modelLoader) {
        this.baseModel.resolveParents(modelLoader);
    }
    ^///?}

    @Override
    public @NotNull BakedModel bake(ModelBaker loader, Function<Material, TextureAtlasSprite> textureGetter,
                                    ModelState rotationContainer) {
        return new BetterGrassifyBakedModel(this.baseModel.bake(loader, textureGetter, rotationContainer));
    }
}
*///?}
