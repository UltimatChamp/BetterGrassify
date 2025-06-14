package dev.ultimatchamp.bettergrass.compat;

import net.frozenblock.wilderwild.config.WWBlockConfig;

public class WilderWildCompat {
    public static boolean isSnowloggingOn() {
        return WWBlockConfig.canSnowlog();
    }
}
