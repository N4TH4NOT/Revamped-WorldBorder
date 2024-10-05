package fr.n4th4not.worldborder;

import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.WorldBorderCenterChangedS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderInterpolateSizeS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderSizeChangedS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderWarningBlocksChangedS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderWarningTimeChangedS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.WorldBorderListener;

public class LocalBorderListener
    implements WorldBorderListener {

    private final ServerWorld level;

    public LocalBorderListener(ServerWorld parent) {
        this.level = parent;
    }

    @Override
    public void onSizeChange(WorldBorder border, double size) {
        broadcast(new WorldBorderSizeChangedS2CPacket(border));
    }

    @Override
    public void onInterpolateSize(WorldBorder border, double fromSize, double toSize, long time) {
        broadcast(new WorldBorderInterpolateSizeS2CPacket(border));
    }

    @Override
    public void onCenterChanged(WorldBorder border, double centerX, double centerZ) {
        broadcast(new WorldBorderCenterChangedS2CPacket(border));
    }

    @Override
    public void onWarningTimeChanged(WorldBorder border, int warningTime) {
        broadcast(new WorldBorderWarningTimeChangedS2CPacket(border));
    }

    @Override
    public void onWarningBlocksChanged(WorldBorder border, int warningBlockDistance) {
        broadcast(new WorldBorderWarningBlocksChangedS2CPacket(border));
    }

    @Override
    public void onDamagePerBlockChanged(WorldBorder border, double damagePerBlock) {
    }

    @Override
    public void onSafeZoneChanged(WorldBorder border, double safeZoneRadius) {
    }

    private void broadcast(Packet<ClientPlayPacketListener> packet) {
        if (this.level.getPlayers().isEmpty()) return;

        Main.LOGGER.debug("Broadcast packet {} in {} to : {}",
                packet.getClass().getSimpleName(),
                this.level.getDimensionEntry().getIdAsString(),
                String.join(", ", this.level.getPlayers().stream().map(player -> player.getGameProfile().getName()).toArray(CharSequence[]::new))
        );
        this.level.getPlayers().forEach(player -> player.networkHandler.sendPacket(packet));
    }
}
