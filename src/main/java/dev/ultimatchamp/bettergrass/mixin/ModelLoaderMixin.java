//? if <1.21.2 {
/*package dev.ultimatchamp.bettergrass.mixin;

import dev.ultimatchamp.bettergrass.BetterGrassify;
import dev.ultimatchamp.bettergrass.model.BetterGrassifyUnbakedModel;
import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

//? if <1.21 {
/^import net.minecraft.util.Identifier;
import java.util.Set;
^///?}

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
    @Shadow
    @Final
    //? if >1.20.6 {
    private Map<ModelIdentifier, UnbakedModel> modelsToBake;
    //?} else {
    /^private Map<Identifier, UnbakedModel> unbakedModels;

    @Shadow
    @Final
    private Set<Identifier> modelsToLoad;
    ^///?}

    //? if >1.20.6 {
    @Inject(method = "addModelToBake", at = @At("HEAD"), cancellable = true)
    private void bettergrass$onAddModelToBake(ModelIdentifier id, UnbakedModel unbakedModel, CallbackInfo ci) {
    //?} else {
    /^@Inject(method = "putModel", at = @At("HEAD"), cancellable = true)
    private void bettergrass$onPutModel(Identifier id, UnbakedModel unbakedModel, CallbackInfo ci) {
    ^///?}
        if (id instanceof ModelIdentifier modelId) {
            if (!modelId.getVariant().equals("inventory")) {
                var blocks = BetterGrassify.getBlocks();

                for (String block : blocks) {
                    //? if >1.20.6 {
                    if (modelId.id().toString().equals(block)) {
                    //?} else {
                    /^if (id.toString().equals(block)) {
                    ^///?}
                        if (modelId.getVariant().contains("snowy=true")) {
                            if (BetterGrassifyConfig.load().snowy) {
                                var newModel = new BetterGrassifyUnbakedModel(unbakedModel);
                                //? if >1.20.6 {
                                this.modelsToBake.put(id, newModel);
                                //?} else {
                                /^this.unbakedModels.put(id, newModel);
                                this.modelsToLoad.addAll(newModel.getModelDependencies());
                                ^///?}
                                ci.cancel();
                            }
                        } else {
                            var newModel = new BetterGrassifyUnbakedModel(unbakedModel);
                            //? if >1.20.6 {
                            this.modelsToBake.put(id, newModel);
                            //?} else {
                            /^this.unbakedModels.put(id, newModel);
                            this.modelsToLoad.addAll(newModel.getModelDependencies());
                            ^///?}
                            ci.cancel();
                        }
                    }
                }

                if (blocks.contains("minecraft:dirt_path") || blocks.contains("minecraft:farmland")) {
                    //? if >1.20.6 {
                    if (modelId.id().toString().equals("minecraft:dirt")) {
                    //?} else {
                    /^if ((modelId.getNamespace() + ":" + modelId.getPath()).toString().equals("minecraft:dirt")) {
                    ^///?}
                        var newModel = new BetterGrassifyUnbakedModel(unbakedModel);
                        //? if >1.20.6 {
                        this.modelsToBake.put(id, newModel);
                        //?} else {
                        /^this.unbakedModels.put(id, newModel);
                        this.modelsToLoad.addAll(newModel.getModelDependencies());
                        ^///?}
                        ci.cancel();
                    }
                }
            }
        }
    }
}
*///?}
