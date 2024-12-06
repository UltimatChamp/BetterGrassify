//? if <1.21.4 {
/*package dev.ultimatchamp.bettergrass.model;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

//? if <1.21.2 {
/^import net.minecraft.util.Identifier;
import java.util.Collection;
^///?}

public class BetterGrassifyUnbakedModel implements UnbakedModel {
    private final UnbakedModel baseModel;

    public BetterGrassifyUnbakedModel(UnbakedModel unbakedModel) {
        this.baseModel = unbakedModel;
    }

    //? if >1.21.1 {
    @Override
    public void resolve(Resolver resolver) {
        this.baseModel.resolve(resolver);
    }
    //?} else {
    /^@Override
    public Collection<Identifier> getModelDependencies() {
        return this.baseModel.getModelDependencies();
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {
        this.baseModel.setParents(modelLoader);
    }
    ^///?}

    @Nullable
    @Override
    //? if >1.20.6 {
    public BakedModel bake(Baker loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer) {
        return new BetterGrassifyBakedModel(this.baseModel.bake(loader, textureGetter, rotationContainer));
    }
    //?} else {
    /^public BakedModel bake(Baker loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        return new BetterGrassifyBakedModel(this.baseModel.bake(loader, textureGetter, rotationContainer, modelId));
    }
    ^///?}
}
*///?}
