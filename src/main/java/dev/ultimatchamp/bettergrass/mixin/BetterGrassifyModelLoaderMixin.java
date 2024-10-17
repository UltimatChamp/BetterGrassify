//? if <1.21.2 {
package dev.ultimatchamp.bettergrass.mixin;

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
/*import net.minecraft.util.Identifier;
import java.util.Set;
*///?}

@Mixin(ModelLoader.class)
public class BetterGrassifyModelLoaderMixin {
    @Shadow
    @Final
    //? if >1.20.6 {
    private Map<ModelIdentifier, UnbakedModel> modelsToBake;
    //?} else {
    /*private Map<Identifier, UnbakedModel> unbakedModels;

    @Shadow
    @Final
    private Set<Identifier> modelsToLoad;
    *///?}

    //? if >1.20.6 {
    @Inject(method = "addModelToBake", at = @At("HEAD"), cancellable = true)
    private void onAddModelToBake(ModelIdentifier id, UnbakedModel unbakedModel, CallbackInfo ci) {
    //?} else {
    /*@Inject(method = "putModel", at = @At("HEAD"), cancellable = true)
    private void onPutModel(Identifier id, UnbakedModel unbakedModel, CallbackInfo ci) {
    *///?}
        if (id instanceof ModelIdentifier modelId) {
            if (!modelId.getVariant().equals("inventory")) {
                for (String path : BetterGrassifyConfig.instance().moreBlocks) {
                    if (BetterGrassifyConfig.instance().snowy) {
                        if (modelId.toString().startsWith(path) && modelId.toString().contains("snowy=true")) {
                            var newModel = new BetterGrassifyUnbakedModel(unbakedModel);
                            //? if >1.20.6 {
                            this.modelsToBake.put(id, newModel);
                            //?} else {
                            /*this.unbakedModels.put(id, newModel);
                            this.modelsToLoad.addAll(newModel.getModelDependencies());
                            *///?}
                            ci.cancel();
                        }
                    } else if (modelId.toString().startsWith(path) && !modelId.toString().contains("snowy=true")) {
                        var newModel = new BetterGrassifyUnbakedModel(unbakedModel);
                        //? if >1.20.6 {
                        this.modelsToBake.put(id, newModel);
                        //?} else {
                        /*this.unbakedModels.put(id, newModel);
                        this.modelsToLoad.addAll(newModel.getModelDependencies());
                        *///?}
                        ci.cancel();
                    }
                }

                if (BetterGrassifyConfig.instance().grassBlocks) {
                    if (modelId.toString().startsWith("minecraft:grass_block") && !modelId.toString().contains("snowy=true")) {
                        var newModel = new BetterGrassifyUnbakedModel(unbakedModel);
                        //? if >1.20.6 {
                        this.modelsToBake.put(id, newModel);
                        //?} else {
                        /*this.unbakedModels.put(id, newModel);
                        this.modelsToLoad.addAll(newModel.getModelDependencies());
                        *///?}
                        ci.cancel();
                    } else if (modelId.toString().startsWith("minecraft:grass_block") && modelId.toString().contains("snowy=true") && BetterGrassifyConfig.instance().snowy) {
                        var newModel = new BetterGrassifyUnbakedModel(unbakedModel);
                        //? if >1.20.6 {
                        this.modelsToBake.put(id, newModel);
                        //?} else {
                        /*this.unbakedModels.put(id, newModel);
                        this.modelsToLoad.addAll(newModel.getModelDependencies());
                        *///?}
                        ci.cancel();
                    }
                }

                if (BetterGrassifyConfig.instance().dirtPaths) {
                    if (modelId.toString().startsWith("minecraft:dirt_path")) {
                        var newModel = new BetterGrassifyUnbakedModel(unbakedModel);
                        //? if >1.20.6 {
                        this.modelsToBake.put(id, newModel);
                        //?} else {
                        /*this.unbakedModels.put(id, newModel);
                        this.modelsToLoad.addAll(newModel.getModelDependencies());
                        *///?}
                        ci.cancel();
                    }
                }

                if (BetterGrassifyConfig.instance().farmLands) {
                    if (modelId.toString().startsWith("minecraft:farmland")) {
                        var newModel = new BetterGrassifyUnbakedModel(unbakedModel);
                        //? if >1.20.6 {
                        this.modelsToBake.put(id, newModel);
                        //?} else {
                        /*this.unbakedModels.put(id, newModel);
                        this.modelsToLoad.addAll(newModel.getModelDependencies());
                        *///?}
                        ci.cancel();
                    }
                }

                if (BetterGrassifyConfig.instance().podzol) {
                    if (modelId.toString().startsWith("minecraft:podzol") && !modelId.toString().contains("snowy=true")) {
                        var newModel = new BetterGrassifyUnbakedModel(unbakedModel);
                        //? if >1.20.6 {
                        this.modelsToBake.put(id, newModel);
                        //?} else {
                        /*this.unbakedModels.put(id, newModel);
                        this.modelsToLoad.addAll(newModel.getModelDependencies());
                        *///?}
                        ci.cancel();
                    } else if (modelId.toString().startsWith("minecraft:podzol") && modelId.toString().contains("snowy=true") && BetterGrassifyConfig.instance().snowy) {
                        var newModel = new BetterGrassifyUnbakedModel(unbakedModel);
                        //? if >1.20.6 {
                        this.modelsToBake.put(id, newModel);
                        //?} else {
                        /*this.unbakedModels.put(id, newModel);
                        this.modelsToLoad.addAll(newModel.getModelDependencies());
                        *///?}
                        ci.cancel();
                    }
                }

                if (BetterGrassifyConfig.instance().mycelium) {
                    if (modelId.toString().startsWith("minecraft:mycelium") && !modelId.toString().contains("snowy=true")) {
                        var newModel = new BetterGrassifyUnbakedModel(unbakedModel);
                        //? if >1.20.6 {
                        this.modelsToBake.put(id, newModel);
                        //?} else {
                        /*this.unbakedModels.put(id, newModel);
                        this.modelsToLoad.addAll(newModel.getModelDependencies());
                        *///?}
                        ci.cancel();
                    } else if (modelId.toString().startsWith("minecraft:mycelium") && modelId.toString().contains("snowy=true") && BetterGrassifyConfig.instance().snowy) {
                        var newModel = new BetterGrassifyUnbakedModel(unbakedModel);
                        //? if >1.20.6 {
                        this.modelsToBake.put(id, newModel);
                        //?} else {
                        /*this.unbakedModels.put(id, newModel);
                        this.modelsToLoad.addAll(newModel.getModelDependencies());
                        *///?}
                        ci.cancel();
                    }
                }

                if (BetterGrassifyConfig.instance().crimsonNylium) {
                    if (modelId.toString().startsWith("minecraft:crimson_nylium")) {
                        var newModel = new BetterGrassifyUnbakedModel(unbakedModel);
                        //? if >1.20.6 {
                        this.modelsToBake.put(id, newModel);
                        //?} else {
                        /*this.unbakedModels.put(id, newModel);
                        this.modelsToLoad.addAll(newModel.getModelDependencies());
                        *///?}
                        ci.cancel();
                    }
                }

                if (BetterGrassifyConfig.instance().warpedNylium) {
                    if (modelId.toString().startsWith("minecraft:warped_nylium")) {
                        var newModel = new BetterGrassifyUnbakedModel(unbakedModel);
                        //? if >1.20.6 {
                        this.modelsToBake.put(id, newModel);
                        //?} else {
                        /*this.unbakedModels.put(id, newModel);
                        this.modelsToLoad.addAll(newModel.getModelDependencies());
                        *///?}
                        ci.cancel();
                    }
                }
            }
        }
    }
}
//?}
