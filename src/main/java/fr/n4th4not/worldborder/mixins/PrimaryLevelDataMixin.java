package fr.n4th4not.worldborder.mixins;

import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import fr.n4th4not.worldborder.RevampedWorldBorder;
import fr.n4th4not.worldborder.IPrimaryLevelData;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.LevelSettings;
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
    private CompoundTag worldborders = new CompoundTag();

    @Inject(method = "parse", at =  @At("RETURN"))
    private static void parse(Dynamic<Tag> tag, DataFixer fixer, int playerDataVersion, CompoundTag loadedPlayerTag,
            LevelSettings settings, LevelVersion ver, WorldGenSettings genSettings, Lifecycle cycle,
            CallbackInfoReturnable<PrimaryLevelData> cir) {
        LOGGER.debug("PrimaryLevelDataMixin#parse");
        PrimaryLevelDataMixin data = (PrimaryLevelDataMixin) (Object) cir.getReturnValue();
        LOGGER.debug((data.worldborders = (CompoundTag) tag.get("WorldsData").orElseEmptyMap().getValue()).toString());
        //TODO: Handle when the level.dat doesn't exists
    }

    @Inject(method = "setTagData", at = @At("HEAD"))
    private void setTagData(RegistryAccess access, CompoundTag root, CompoundTag playerNbt, CallbackInfo ci) {
        root.put("WorldBorders",this.worldborders);
    }

    @Override
    public @NotNull CompoundTag getWorldBorders() {
        return worldborders;
    }

    @Override
    public void setWorldBorders(@NotNull CompoundTag nbt) {
        LOGGER.debug("PrimaryLevelDataMixin#setWorldBorders");
        this.worldborders.merge(nbt);
        LOGGER.debug(this.worldborders.toString());
    }
}
