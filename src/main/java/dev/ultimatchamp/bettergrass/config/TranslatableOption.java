package dev.ultimatchamp.bettergrass.config;

import net.minecraft.network.chat.Component;

public interface TranslatableOption {
    String getKey();

    default Component getLocalizedName() {
        return Component.translatable(getKey());
    }
}
