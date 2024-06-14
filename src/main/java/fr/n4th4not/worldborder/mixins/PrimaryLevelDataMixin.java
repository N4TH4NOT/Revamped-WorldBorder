package fr.n4th4not.worldborder.mixins;

import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import fr.n4th4not.worldborder.IPrimaryLevelData;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelVersion;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static fr.n4th4not.worldborder.RevampedWorldBorder.LOGGER;

@Mixin(PrimaryLevelData.class)
public abstract class PrimaryLevelDataMixin
    implements IPrimaryLevelData {

    @Unique
    private static final String STORAGE_KEY = "WorldsData";

    @Unique
    private CompoundTag worldborders = new CompoundTag();

    @Inject(method = "parse", at =  @At("RETURN"))
    private static void parse(Dynamic<Tag> tag, DataFixer fixer, int playerDataVersion, CompoundTag loadedPlayerTag,
            LevelSettings settings, LevelVersion ver, WorldGenSettings genSettings, Lifecycle cycle,
            CallbackInfoReturnable<PrimaryLevelData> cir) {
        LOGGER.debug("PrimaryLevelDataMixin#parse");
        PrimaryLevelDataMixin data = (PrimaryLevelDataMixin) (Object) cir.getReturnValue();
        LOGGER.debug((data.worldborders = (CompoundTag) tag.get(STORAGE_KEY).orElseEmptyMap().getValue()).toString());
        //TODO: Handle when the level.dat doesn't exists
    }

    @Inject(method = "setTagData", at = @At("TAIL"))
    private void setTagData(RegistryAccess access, CompoundTag root, CompoundTag playerNbt, CallbackInfo ci) {
        LOGGER.debug("PrimaryLevelDataMixin#setTagData");
        root.put(STORAGE_KEY,this.worldborders);
        LOGGER.debug(root.toString());
    }

    @Override
    public @NotNull CompoundTag getWorldBorders() {
        return worldborders;
    }

    @Override
    public void setWorldBorder(@NotNull WorldBorder border, @NotNull ResourceKey<Level> key) {
        LOGGER.debug("PrimaryLevelDataMixin#setWorldBorders");
        CompoundTag nbt = new CompoundTag();
        LOGGER.debug(nbt.toString());
        border.createSettings().write(nbt);
        this.worldborders.put(key.location().toString(), nbt);
    }
}
