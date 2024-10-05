package fr.n4th4not.worldborder;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import org.jetbrains.annotations.NotNull;

public interface ILevelProperties {
    @NotNull WorldBorder.Properties parseWorldBorder(@NotNull RegistryKey<World> key);
    void saveWorldBorder(@NotNull WorldBorder border, @NotNull RegistryKey<World> key);
}
