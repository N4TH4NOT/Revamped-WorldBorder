package fr.n4th4not.worldborder;

import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import org.jetbrains.annotations.NotNull;

public interface ILevelProperties {
    @NotNull WorldBorder.Properties parseWorldBorder(@NotNull World world);
    void saveWorldBorder(@NotNull World world);
}
