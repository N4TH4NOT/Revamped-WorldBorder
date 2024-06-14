package fr.n4th4not.worldborder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import org.jetbrains.annotations.NotNull;

public interface IPrimaryLevelData {
    @NotNull CompoundTag getWorldBorders();
    void setWorldBorder(@NotNull WorldBorder border, @NotNull ResourceKey<Level> key);
}
