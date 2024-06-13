package fr.n4th4not.worldborder.mixins;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.commands.WorldBorderCommand;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldBorderCommand.class)
public abstract class WorldBorderCommandMixin {

    @Redirect(method = "setDamageBuffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;"))
    private static ServerLevel setDamageBuffer(MinecraftServer instance, CommandSourceStack context) {
        return context.getLevel();
    }

    @Redirect(method = "setDamageAmount", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;"))
    private static ServerLevel setDamageAmount(MinecraftServer instance, CommandSourceStack context) {
        return context.getLevel();
    }

    @Redirect(method = "setWarningTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;"))
    private static ServerLevel setWarningTime(MinecraftServer instance, CommandSourceStack context) {
        return context.getLevel();
    }

    @Redirect(method = "setWarningDistance", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;"))
    private static ServerLevel setWarningDistance(MinecraftServer instance, CommandSourceStack context) {
        return context.getLevel();
    }

    @Redirect(method = "getSize", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;"))
    private static ServerLevel getSize(MinecraftServer instance, CommandSourceStack context) {
        return context.getLevel();
    }

    @Redirect(method = "setCenter", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;"))
    private static ServerLevel setCenter(MinecraftServer instance, CommandSourceStack context) {
        return context.getLevel();
    }

    @Redirect(method = "setSize", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;"))
    private static ServerLevel setSize(MinecraftServer instance, CommandSourceStack context) {
        return context.getLevel();
    }
}