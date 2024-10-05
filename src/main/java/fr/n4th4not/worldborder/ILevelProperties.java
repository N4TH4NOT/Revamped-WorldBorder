package fr.n4th4not.worldborder;

import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import org.jetbrains.annotations.NotNull;

public interface ILevelProperties {
    @NotNull WorldBorder.Properties parseWorldBorders(@NotNull World world);
    void saveWorldBorders(@NotNull World world);
}
