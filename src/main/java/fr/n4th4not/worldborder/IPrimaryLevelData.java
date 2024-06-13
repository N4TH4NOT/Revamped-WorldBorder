package fr.n4th4not.worldborder;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

public interface IPrimaryLevelData {
    @NotNull CompoundTag getWorldBorders();
    void setWorldBorders(@NotNull CompoundTag nbt);
}
