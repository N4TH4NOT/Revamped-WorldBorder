package fr.n4th4not.worldborder;

import com.mojang.serialization.Dynamic;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.WorldWorkerManager;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
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
        MinecraftForge.EVENT_BUS.addListener(this::onLevelSaving);
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
}
