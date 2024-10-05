package fr.n4th4not.worldborder.mixins;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.WorldBorderCommand;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldBorderCommand.class)
public abstract class WorldBorderCommandMixin {
    @Redirect(method = "executeBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getOverworld()Lnet/minecraft/server/world/ServerWorld;"))
    private static ServerWorld executeBuffer(MinecraftServer srv, ServerCommandSource src) {
        return src.getWorld();
    }

    @Redirect(method = "executeDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getOverworld()Lnet/minecraft/server/world/ServerWorld;"))
    private static ServerWorld executeDamage(MinecraftServer srv, ServerCommandSource src) {
        return src.getWorld();
    }

    @Redirect(method = "executeWarningTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getOverworld()Lnet/minecraft/server/world/ServerWorld;"))
    private static ServerWorld executeWarningTime(MinecraftServer srv, ServerCommandSource src) {
        return src.getWorld();
    }

    @Redirect(method = "executeWarningDistance", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getOverworld()Lnet/minecraft/server/world/ServerWorld;"))
    private static ServerWorld executeWarningDistance(MinecraftServer srv, ServerCommandSource src) {
        return src.getWorld();
    }

    @Redirect(method = "executeGet", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getOverworld()Lnet/minecraft/server/world/ServerWorld;"))
    private static ServerWorld executeGet(MinecraftServer srv, ServerCommandSource src) {
        return src.getWorld();
    }

    @Redirect(method = "executeCenter", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getOverworld()Lnet/minecraft/server/world/ServerWorld;"))
    private static ServerWorld executeCenter(MinecraftServer srv, ServerCommandSource src) {
        return src.getWorld();
    }

    @Redirect(method = "executeSet", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getOverworld()Lnet/minecraft/server/world/ServerWorld;"))
    private static ServerWorld executeSet(MinecraftServer srv, ServerCommandSource src) {
        return src.getWorld();
    }
}
