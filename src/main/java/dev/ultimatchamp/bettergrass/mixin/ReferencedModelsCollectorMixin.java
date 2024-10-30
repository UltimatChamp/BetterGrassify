//? if >1.21.1 {
package dev.ultimatchamp.bettergrass.mixin;

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
            for (String path : BetterGrassifyConfig.instance().moreBlocks) {
                if (modelId.toString().startsWith(path) && !modelId.toString().contains("snowy=true")) {
                    return new BetterGrassifyUnbakedModel(model);
                } else if (modelId.toString().startsWith(path) && modelId.toString().contains("snowy=true") && BetterGrassifyConfig.instance().snowy) {
                    return new BetterGrassifyUnbakedModel(model);
                }
            }

            if (BetterGrassifyConfig.instance().grassBlocks) {
                if (modelId.toString().startsWith("minecraft:grass_block") && !modelId.toString().contains("snowy=true")) {
                    return new BetterGrassifyUnbakedModel(model);
                } else if (modelId.toString().startsWith("minecraft:grass_block") && modelId.toString().contains("snowy=true") && BetterGrassifyConfig.instance().snowy) {
                    return new BetterGrassifyUnbakedModel(model);
                }
            }

            if (BetterGrassifyConfig.instance().dirtPaths) {
                if (modelId.toString().startsWith("minecraft:dirt_path")) {
                    return new BetterGrassifyUnbakedModel(model);
                }
            }

            if (BetterGrassifyConfig.instance().farmLands) {
                if (modelId.toString().startsWith("minecraft:farmland")) {
                    return new BetterGrassifyUnbakedModel(model);
                }
            }

            if (BetterGrassifyConfig.instance().podzol) {
                if (modelId.toString().startsWith("minecraft:podzol") && !modelId.toString().contains("snowy=true")) {
                    return new BetterGrassifyUnbakedModel(model);
                } else if (modelId.toString().startsWith("minecraft:podzol") && modelId.toString().contains("snowy=true") && BetterGrassifyConfig.instance().snowy) {
                    return new BetterGrassifyUnbakedModel(model);
                }
            }

            if (BetterGrassifyConfig.instance().mycelium) {
                if (modelId.toString().startsWith("minecraft:mycelium") && !modelId.toString().contains("snowy=true")) {
                    return new BetterGrassifyUnbakedModel(model);
                } else if (modelId.toString().startsWith("minecraft:mycelium") && modelId.toString().contains("snowy=true") && BetterGrassifyConfig.instance().snowy) {
                    return new BetterGrassifyUnbakedModel(model);
                }
            }

            if (BetterGrassifyConfig.instance().crimsonNylium) {
                if (modelId.toString().startsWith("minecraft:crimson_nylium")) {
                    return new BetterGrassifyUnbakedModel(model);
                }
            }

            if (BetterGrassifyConfig.instance().warpedNylium) {
                if (modelId.toString().startsWith("minecraft:warped_nylium")) {
                    return new BetterGrassifyUnbakedModel(model);
                }
            }
        }
        return model;
    }
}
//?}
