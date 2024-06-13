package fr.n4th4not.worldborder;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.border.WorldBorder;
import org.jetbrains.annotations.NotNull;

public class LocalBorderListener
    implements BorderChangeListener {

    private final ServerLevel level;

    LocalBorderListener(ServerLevel parent) {
        this.level = parent;
    }
    public void onBorderSizeSet(@NotNull WorldBorder border, double p_11322_) {
        broadcast(new ClientboundSetBorderSizePacket(border));
    }

    public void onBorderSizeLerping(@NotNull WorldBorder border, double p_11329_, double p_11330_, long p_11331_) {
        broadcast(new ClientboundSetBorderLerpSizePacket(border));
    }

    public void onBorderCenterSet(@NotNull WorldBorder border, double p_11325_, double p_11326_) {
        broadcast(new ClientboundSetBorderCenterPacket(border));
    }

    public void onBorderSetWarningTime(@NotNull WorldBorder border, int p_11334_) {
        broadcast(new ClientboundSetBorderWarningDelayPacket(border));
    }

    public void onBorderSetWarningBlocks(@NotNull WorldBorder border, int p_11340_) {
        broadcast(new ClientboundSetBorderWarningDistancePacket(border));
    }

    public void onBorderSetDamagePerBlock(@NotNull WorldBorder border, double p_11337_) {
    }

    public void onBorderSetDamageSafeZOne(@NotNull WorldBorder border, double p_11343_) {
    }

    private void broadcast(Packet<?> packet) {
        this.level.players().forEach(player -> player.connection.send(packet));
    }

}
