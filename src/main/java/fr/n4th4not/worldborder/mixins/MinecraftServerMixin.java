package fr.n4th4not.worldborder.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import fr.n4th4not.worldborder.Constants;
import fr.n4th4not.worldborder.ILevelProperties;
import fr.n4th4not.worldborder.LocalBorderListener;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static fr.n4th4not.worldborder.RevWbMod.LOGGER;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow public abstract Iterable<ServerWorld> getWorlds();

    @Inject(method = "createWorlds", at = @At("TAIL"))
    public void createLevels(WorldGenerationProgressListener genListener, CallbackInfo ci, @Local ServerWorldProperties srvProp) {
        LOGGER.debug("MinecraftServerMixin#createLevels");
        for (ServerWorld world : getWorlds()) {
            LOGGER.debug(Constants.getIdentifier(world));

            WorldBorder wb = world.getWorldBorder();
            purge(world, wb);

            if (world.getLevelProperties() instanceof UnmodifiableLevelProperties prop) {
                WorldBorder.Properties wbProp = ((ILevelProperties) srvProp).parseWorldBorder(world);
                prop.setWorldBorder(wbProp);
                wb.load(wbProp);
            }
        }
    }

    @Inject(method = "save", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getWorldBorder()Lnet/minecraft/world/border/WorldBorder;"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void save(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir, boolean bl, ServerWorld overworld,
                  ServerWorldProperties prop) {
        LOGGER.debug("MinecraftServerMixin#save");
        ILevelProperties iprop = (ILevelProperties) prop;
        for (ServerWorld world : getWorlds()) {
            if (!world.getRegistryKey().getValue().equals(World.OVERWORLD.getValue()))
                iprop.saveWorldBorder(world);
        }
    }

    @Unique
    private void purge(ServerWorld world, WorldBorder wb) {
        if (((WorldBorderAccessor) wb).getListeners().removeIf(listener -> listener.getClass().getPackageName().startsWith("net.minecraft."))) {
            LOGGER.debug("MinecraftServerMixin: Vanilla listeners was removed from {}", Constants.getIdentifier(world));
        }
        wb.addListener(new LocalBorderListener(world));
    }
}
