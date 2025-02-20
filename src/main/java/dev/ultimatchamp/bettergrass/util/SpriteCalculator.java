/*
 This code is based on work by PepperCode1 and is distributed under the terms of the GNU Lesser General Public License (LGPL), Version 3.0.
 A copy of the LGPLv3 is available here: https://www.gnu.org/licenses/lgpl-3.0.txt
*/

package dev.ultimatchamp.bettergrass.util;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.function.Supplier;

public final class SpriteCalculator {
    private static final BlockModels MODELS = MinecraftClient.getInstance().getBakedModelManager().getBlockModels();

    public static Sprite calculateSprite(BlockState state, Direction face, Supplier<Random> randomSupplier) {
        BakedModel model = MODELS.getModel(state);

        List<BakedQuad> quads = model.getQuads(state, face, randomSupplier.get());
        if (!quads.isEmpty()) {
            return quads.get(0).getSprite();
        }

        quads = model.getQuads(state, null, randomSupplier.get());
        if (!quads.isEmpty()) {
            for (BakedQuad quad : quads) {
                if (quad.getFace() == face) return quad.getSprite();
            }
        }

        return model.getParticleSprite();
    }
}
