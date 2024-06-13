package fr.n4th4not.worldborder;

import com.mojang.serialization.Dynamic;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.WorldWorkerManager;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static fr.n4th4not.worldborder.RevampedWorldBorder.MOD_ID;

@Mod(MOD_ID)
public class RevampedWorldBorder {

    public static final String MOD_ID = "rev_wb";
    public static final Logger LOGGER = LoggerFactory.getLogger(RevampedWorldBorder.class);

    public RevampedWorldBorder() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStart(ServerAboutToStartEvent event) {
        MinecraftForge.EVENT_BUS.addListener(this::onLevelCreation);
        MinecraftForge.EVENT_BUS.addListener(this::onLevelSaving);
        MinecraftForge.EVENT_BUS.addListener(this::onTeleportation);
    }

    public void onLevelCreation(LevelEvent.Load event) {
        LOGGER.debug("RevampedWorldBorder#onLevelCreation");
        //Prevent overworld to control all others level wb
        ServerLevel level = (ServerLevel) event.getLevel();
        LOGGER.debug(level.dimension().location().toString());
        WorldBorder border = level.getWorldBorder();

        if (level.dimension() != Level.OVERWORLD) {
            WorldBorder.Settings settings = WorldBorder.Settings.read(
                    new Dynamic<>(NbtOps.INSTANCE, ((IPrimaryLevelData) level.getServer().getWorldData().overworldData()).getWorldBorders().get(level.dimension().location().toString())),
                    /*((PrimaryLevelData) level.getLevelData()).overworldData().getWorldBorder()*/ WorldBorder.DEFAULT_SETTINGS
            );

            purge(level,border);
            if (level.getLevelData() instanceof DerivedLevelData data) {
                data.setWorldBorder(settings);
                border.applySettings(settings);
            }
            //TODO:
            // - Overworld world border is reference for other levels when no data but not handle as intended
            // - World borders are not loaded from the right key
        }
    }

    public void onLevelSaving(LevelEvent.Save event) {
        LOGGER.debug("RevampedWorldBorder#onLevelSaving");
        ServerLevel level = (ServerLevel) event.getLevel();
        LOGGER.debug(level.dimension().location().toString());
        if (level.dimension() == Level.OVERWORLD) return;

        if (level.getLevelData() instanceof DerivedLevelData data) {
            CompoundTag container = new CompoundTag();
            CompoundTag nbt = new CompoundTag();
            container.put(level.dimension().location().toString(),nbt);
            data.getWorldBorder().write(nbt);
            LOGGER.debug("SAVED DATA:");
            LOGGER.debug(container.toString());
            ((IPrimaryLevelData) level.getServer().getWorldData().overworldData()).setWorldBorders(container);
        }

    }

    public void onTeleportation(PlayerEvent.PlayerChangedDimensionEvent event) {
        LOGGER.debug("RevampedWorldBorder#onTeleportation");
        if (event.getEntity() instanceof ServerPlayer player && !event.getFrom().equals(event.getTo())) {
            ((IWorldBorder) player.getServer().getLevel(event.getTo()).getWorldBorder()).update(player);
        }
    }

    public static void purge(ServerLevel level, WorldBorder border) {
        border.listeners.removeIf(listener -> listener.getClass().getPackageName().startsWith("net.minecraft."));
        border.listeners.add(new LocalBorderListener(level));
    }
}
