package fr.n4th4not.worldborder.mixins;

import fr.n4th4not.worldborder.ILevelProperties;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
    extends World {
    protected ServerWorldMixin(MutableWorldProperties prop, RegistryKey<World> key, DynamicRegistryManager regMan, RegistryEntry<DimensionType> dimEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(prop, key, regMan, dimEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(method = "saveLevel", at = @At("TAIL"))
    public void save(CallbackInfo ci) {
        if (super.getRegistryKey() != World.OVERWORLD && super.getRegistryManager().get(World.OVERWORLD.getRegistryRef()) instanceof World overworld) {
            ((ILevelProperties) overworld.getLevelProperties()).saveWorldBorders(this);
        }
    }
}
