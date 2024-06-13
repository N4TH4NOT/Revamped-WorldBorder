package fr.n4th4not.worldborder.mixins;

import fr.n4th4not.worldborder.IWorldBorder;
import fr.n4th4not.worldborder.RevampedWorldBorder;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldBorder.class)
public abstract class WorldBorderMixin
    implements IWorldBorder {

    @Override
    public void update(ServerPlayer player) {
        WorldBorder border = (WorldBorder) (Object) this;
        RevampedWorldBorder.LOGGER.debug("Listeners: {}", String.join(", ", border.listeners.stream().map(listener -> listener.getClass().getName()).toArray(CharSequence[]::new)));
        player.connection.send(new ClientboundSetBorderSizePacket(border));
        player.connection.send(new ClientboundSetBorderLerpSizePacket(border));
        player.connection.send(new ClientboundSetBorderCenterPacket(border));
        player.connection.send(new ClientboundSetBorderWarningDelayPacket(border));
        player.connection.send(new ClientboundSetBorderWarningDistancePacket(border));
    }
}