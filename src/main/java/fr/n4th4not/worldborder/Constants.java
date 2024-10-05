package fr.n4th4not.worldborder;

import net.minecraft.world.World;

public class Constants {
    public static final String MOD_ID = "rev_wb";

    public static String getIdentifier(World world) {
        return world.getRegistryKey().getValue().toString();
    }
}
