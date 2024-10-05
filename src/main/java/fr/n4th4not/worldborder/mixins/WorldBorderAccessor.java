package fr.n4th4not.worldborder.mixins;

import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.WorldBorderListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(WorldBorder.class)
public interface WorldBorderAccessor {
    @Accessor
    List<WorldBorderListener> getListeners();
}
