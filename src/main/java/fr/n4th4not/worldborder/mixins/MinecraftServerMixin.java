package fr.n4th4not.worldborder.mixins;

import fr.n4th4not.worldborder.RevampedWorldBorder;
import net.minecraft.core.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {


    @Inject(method = "createLevels", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void createLevels(ChunkProgressListener lister, CallbackInfo ci, ServerLevelData serverleveldata, WorldGenSettings worldgensettings,
                             boolean flag, long i, long j, List<CustomSpawner> list, Registry<LevelStem> registry, LevelStem levelstem,
                             ServerLevel serverlevel, DimensionDataStorage dimensiondatastorage, WorldBorder worldborder) {
        RevampedWorldBorder.purge(serverlevel,worldborder);
    }
}
