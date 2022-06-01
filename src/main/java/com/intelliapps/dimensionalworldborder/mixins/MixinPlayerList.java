package com.intelliapps.dimensionalworldborder.mixins;

import com.mojang.logging.LogUtils;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.border.WorldBorder;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class MixinPlayerList {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Inject(at=@At("HEAD"), method="sendLevelInfo(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/level/ServerLevel;)V", cancellable = true)
    public void sendLevelInfo(ServerPlayer player, ServerLevel level, CallbackInfo callbackInfo) {
        WorldBorder worldborder = level.getWorldBorder();
        player.connection.send(new ClientboundInitializeBorderPacket(worldborder));
        player.connection.send(new ClientboundSetTimePacket(level.getGameTime(), level.getDayTime(), level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)));
        player.connection.send(new ClientboundSetDefaultSpawnPositionPacket(level.getSharedSpawnPos(), level.getSharedSpawnAngle()));
        if (level.isRaining()) {
            player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.START_RAINING, 0.0F));
            player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, level.getRainLevel(1.0F)));
            player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, level.getThunderLevel(1.0F)));
        }
        callbackInfo.cancel();
    }

    @Inject(at=@At("HEAD"), method="addWorldborderListener(Lnet/minecraft/server/level/ServerLevel;)V", cancellable = true)
    public void addWorldborderListener(ServerLevel level, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    }
}
