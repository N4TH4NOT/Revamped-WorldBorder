package fr.n4th4not.worldborder;

import net.minecraft.world.World;

public class Constants {
    public static String getIdentifier(World world) {
        return world.getRegistryKey().getValue().toString();
    }
}
