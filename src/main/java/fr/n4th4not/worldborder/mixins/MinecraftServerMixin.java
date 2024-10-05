package fr.n4th4not.worldborder.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import fr.n4th4not.worldborder.ILevelProperties;
import fr.n4th4not.worldborder.LocalBorderListener;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.UnmodifiableLevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static fr.n4th4not.worldborder.Main.LOGGER;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow public abstract Iterable<ServerWorld> getWorlds();

    @Inject(method = "createWorlds", at = @At("TAIL"))
    public void createLevels(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci, @Local ServerWorldProperties srvProp) {
        LOGGER.debug("MinecraftServerMixin#createLevels");
        for (ServerWorld level : getWorlds()) {
            RegistryKey<World> key = level.getRegistryKey();
            LOGGER.debug(key.toString());

            WorldBorder border = level.getWorldBorder();
            purge(level, border);

            if (level.getLevelProperties() instanceof UnmodifiableLevelProperties prop) {
                WorldBorder.Properties settings = ((ILevelProperties) srvProp).parseWorldBorder(key);
                prop.setWorldBorder(settings);
                border.load(settings);
            }
        }
    }

    @Unique
    private void purge(ServerWorld level, WorldBorder border) {
        if (((WorldBorderAccessor) border).getListeners().removeIf(listener -> listener.getClass().getPackageName().startsWith("net.minecraft."))) {
            LOGGER.debug("MinecraftServerMixin: Vanilla listeners removed from {}", level.getRegistryKey());
        }
        border.addListener(new LocalBorderListener(level));
    }
}
