package fr.n4th4not.worldborder;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetBorderCenterPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderLerpSizePacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDelayPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDistancePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.border.WorldBorder;
import org.jetbrains.annotations.NotNull;

public class LocalBorderListener
    implements BorderChangeListener {

    private final ServerLevel level;

    public LocalBorderListener(ServerLevel parent) {
        this.level = parent;
    }
    public void onBorderSizeSet(@NotNull WorldBorder border, double size) {
        broadcast(new ClientboundSetBorderSizePacket(border));
    }

    public void onBorderSizeLerping(@NotNull WorldBorder border, double oldSize, double size, long time) {
        broadcast(new ClientboundSetBorderLerpSizePacket(border));
    }

    public void onBorderCenterSet(@NotNull WorldBorder border, double x, double z) {
        broadcast(new ClientboundSetBorderCenterPacket(border));
    }

    public void onBorderSetWarningTime(@NotNull WorldBorder border, int time) {
        broadcast(new ClientboundSetBorderWarningDelayPacket(border));
    }

    public void onBorderSetWarningBlocks(@NotNull WorldBorder border, int blocks) {
        broadcast(new ClientboundSetBorderWarningDistancePacket(border));
    }

    public void onBorderSetDamagePerBlock(@NotNull WorldBorder border, double damage) {
    }

    public void onBorderSetDamageSafeZOne(@NotNull WorldBorder border, double distance) {
    }

    private void broadcast(Packet<?> packet) {
        if (this.level.players().isEmpty()) return;

        RevampedWorldBorder.LOGGER.debug("Broadcast change in {} to : {}",
                this.level.dimension().location(),
                String.join(", ", this.level.players().stream().map(player -> player.getGameProfile().getName()).toArray(CharSequence[]::new))
        );
        this.level.players().forEach(player -> player.connection.send(packet));
    }

}
