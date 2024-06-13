package fr.n4th4not.worldborder;

import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static fr.n4th4not.worldborder.DimensionalWorldBorder.MOD_ID;

@Mod(MOD_ID)
public class DimensionalWorldBorder {

    public static final String MOD_ID = "rev_wb";
    public static final Logger LOGGER = LoggerFactory.getLogger(DimensionalWorldBorder.class);

    public DimensionalWorldBorder() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStart(ServerAboutToStartEvent event) {
        MinecraftForge.EVENT_BUS.addListener(this::onLevelCreation);
        MinecraftForge.EVENT_BUS.addListener(this::onLevelSaving);
    }

    public void onLevelCreation(LevelEvent.Load event) {
        //Prevent overworld to control all others level wb
        ServerLevel level = (ServerLevel) event.getLevel();
        WorldBorder wb = level.getWorldBorder();
        wb.listeners.removeIf(listener -> listener.getClass().getPackageName().startsWith("net.minecraft."));
        wb.listeners.add(new LocalBorderListener(level));

        if (level.dimension() != Level.OVERWORLD) {
            WorldBorder.Settings settings = WorldBorder.Settings.read(
                    new Dynamic<>(NbtOps.INSTANCE, ((IPrimaryLevelData) level.getServer().getWorldData().overworldData()).getWorldBorders().get(level.dimension().location().toString())),
                    WorldBorder.DEFAULT_SETTINGS
            );

            if (level.getLevelData() instanceof DerivedLevelData data) {
                data.setWorldBorder(settings);
                wb.applySettings(settings);
            }
        }
    }

    public void onLevelSaving(LevelEvent.Save event) {
        ServerLevel level = (ServerLevel) event.getLevel();
        if (level.dimension() == Level.OVERWORLD) return;

        if (level.getLevelData() instanceof DerivedLevelData data) {
            CompoundTag container = new CompoundTag();
            CompoundTag nbt = new CompoundTag();
            container.put(level.dimension().location().toString(),nbt);
            data.getWorldBorder().write(nbt);
            ((IPrimaryLevelData) level.getServer().getWorldData().overworldData()).setWorldBorders(container);
        }

    }
}
