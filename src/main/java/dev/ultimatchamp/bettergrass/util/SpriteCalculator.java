/*
 This code is based on work by PepperCode1 and is distributed under the terms of the GNU Lesser General Public License (LGPL), Version 3.0.
 A copy of the LGPLv3 is available here: https://www.gnu.org/licenses/lgpl-3.0.txt
*/

package dev.ultimatchamp.bettergrass.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Supplier;

public final class SpriteCalculator {
    private static final BlockModelShaper MODELS = Minecraft.getInstance().getModelManager().getBlockModelShaper();

    public static TextureAtlasSprite calculateSprite(BlockState state, Direction face, Supplier<RandomSource> randomSupplier) {
        BlockStateModel model = MODELS.getBlockModel(state);

        List<BakedQuad> quads = model.collectParts(randomSupplier.get()).getFirst().getQuads(face);
        if (!quads.isEmpty()) {
            return quads.getFirst().sprite();
        }

        quads = model.collectParts(randomSupplier.get()).getFirst().getQuads(null);
        if (!quads.isEmpty()) {
            for (BakedQuad quad : quads) {
                if (quad.direction() == face) return quad.sprite();
            }
        }

        return null;
    }
}
