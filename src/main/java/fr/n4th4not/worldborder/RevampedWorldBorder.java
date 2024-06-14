package fr.n4th4not.worldborder;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
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

        if (level.dimension() != Level.OVERWORLD)
            ((IPrimaryLevelData) level.getServer().getWorldData().overworldData()).setWorldBorder(level.getWorldBorder(), level.dimension());
    }
}
