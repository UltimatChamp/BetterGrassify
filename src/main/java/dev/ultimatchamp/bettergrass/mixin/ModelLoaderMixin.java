//? if 1.21.1 {
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

import java.util.List;
import java.util.Map;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
    @Shadow
    @Final
    private Map<ModelIdentifier, UnbakedModel> modelsToBake;

    @Inject(method = "addModelToBake", at = @At("HEAD"), cancellable = true)
    private void bettergrass$onAddModelToBake(ModelIdentifier id, UnbakedModel unbakedModel, CallbackInfo ci) {
        if (id instanceof ModelIdentifier modelId) {
            if (!modelId.getVariant().equals("inventory")) {
                List<String> blocks = BetterGrassify.getBlocks();

                for (String block : blocks) {
                    if (modelId.id().toString().equals(block)) {
                        if (modelId.getVariant().contains("snowy=true")) {
                            if (BetterGrassifyConfig.load().snowy) {
                                BetterGrassifyUnbakedModel newModel = new BetterGrassifyUnbakedModel(unbakedModel);
                                this.modelsToBake.put(id, newModel);
                                ci.cancel();
                            }
                        } else {
                            BetterGrassifyUnbakedModel newModel = new BetterGrassifyUnbakedModel(unbakedModel);
                            this.modelsToBake.put(id, newModel);
                            ci.cancel();
                        }
                    }
                }

                if (modelId.id().toString().equals("minecraft:dirt")) {
                    if (blocks.contains("minecraft:dirt_path") || blocks.contains("minecraft:farmland")) {
                        BetterGrassifyUnbakedModel newModel = new BetterGrassifyUnbakedModel(unbakedModel);
                        this.modelsToBake.put(id, newModel);
                        ci.cancel();
                    }
                }
            }
        }
    }
}
*///?}
