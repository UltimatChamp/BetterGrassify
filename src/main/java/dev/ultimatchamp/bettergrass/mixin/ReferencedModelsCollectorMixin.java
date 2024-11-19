//? if >1.21.1 {
package dev.ultimatchamp.bettergrass.mixin;

import dev.ultimatchamp.bettergrass.BetterGrassify;
import dev.ultimatchamp.bettergrass.config.BetterGrassifyConfig;
import dev.ultimatchamp.bettergrass.model.BetterGrassifyUnbakedModel;
import net.minecraft.client.render.model.ReferencedModelsCollector;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ReferencedModelsCollector.class)
public class ReferencedModelsCollectorMixin {
    @ModifyVariable(method = "addTopLevelModel", at = @At("HEAD"), argsOnly = true)
    private UnbakedModel bettergrass$onAddTopLevelModel(UnbakedModel model, ModelIdentifier modelId) {
        if (!modelId.getVariant().equals("inventory")) {
            var blocks = BetterGrassify.getBlocks();

            for (String block : blocks) {
                if (modelId.toString().startsWith(block)) {
                    if (modelId.toString().contains("snowy=true")) {
                        if (BetterGrassifyConfig.instance().snowy) {
                            return new BetterGrassifyUnbakedModel(model);
                        }
                    } else {
                        return new BetterGrassifyUnbakedModel(model);
                    }
                }
            }
        }

        return model;
    }
}
//?}
