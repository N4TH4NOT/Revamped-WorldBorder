package fr.n4th4not.worldborder.mixins;

import com.mojang.serialization.Dynamic;
import fr.n4th4not.worldborder.IPrimaryLevelData;
import fr.n4th4not.worldborder.LocalBorderListener;
import fr.n4th4not.worldborder.RevampedWorldBorder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {


    @Shadow public abstract Iterable<ServerLevel> getAllLevels();

    @Inject(method = "createLevels", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void createLevels(ChunkProgressListener lister, CallbackInfo ci, ServerLevelData serverleveldata, WorldGenSettings worldgensettings,
                             boolean flag, long i, long j, List<CustomSpawner> list, Registry<LevelStem> registry, LevelStem levelstem,
                             ServerLevel serverlevel, DimensionDataStorage dimensiondatastorage, WorldBorder worldborder) {
        RevampedWorldBorder.LOGGER.debug("MinecraftServerMixin#createLevels");
        for (ServerLevel level : getAllLevels()) {
            String key = level.dimension().location().toString();
            RevampedWorldBorder.LOGGER.debug(key);

            WorldBorder border = level.getWorldBorder();
            purge(level, border);

            if (level.dimension() != Level.OVERWORLD) {
                WorldBorder.Settings settings = WorldBorder.Settings.read(
                        new Dynamic<>(NbtOps.INSTANCE, ((IPrimaryLevelData) serverleveldata).getWorldBorders().get(key)),
                        serverleveldata.getWorldBorder()
                );
                if (level.getLevelData() instanceof DerivedLevelData data) {
                    data.setWorldBorder(settings);
                    border.applySettings(settings);
                }
            }

        }
    }


    private void purge(ServerLevel level, WorldBorder border) {
        border.listeners.removeIf(listener -> listener.getClass().getPackageName().startsWith("net.minecraft."));
        border.listeners.add(new LocalBorderListener(level));
    }
}
