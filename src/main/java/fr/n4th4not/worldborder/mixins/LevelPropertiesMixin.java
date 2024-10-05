package fr.n4th4not.worldborder.mixins;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import fr.n4th4not.worldborder.ILevelProperties;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static fr.n4th4not.worldborder.Main.LOGGER;

@SuppressWarnings("deprecation")
@Mixin(LevelProperties.class)
public abstract class LevelPropertiesMixin
    implements ILevelProperties {

    @Unique private static final String STORAGE_KEY = "WorldsData";
    @Unique private NbtCompound worldborders = new NbtCompound();

    @Shadow public abstract WorldBorder.Properties getWorldBorder();

    @Inject(method = "readProperties", at =  @At("RETURN"))
    private static <T> void parse(Dynamic<T> dynamic, LevelInfo info, LevelProperties.SpecialProperty specialProperty, GeneratorOptions generatorOptions, Lifecycle lifecycle, CallbackInfoReturnable<LevelProperties> cir) {
        LOGGER.debug("LevelPropertiesMixin#parse");
        LevelPropertiesMixin data = (LevelPropertiesMixin) (Object) cir.getReturnValue();
        LOGGER.debug((data.worldborders = (NbtCompound) dynamic.get(STORAGE_KEY).orElseEmptyMap().getValue()).toString());
    }

    @Inject(method = "updateProperties", at = @At("TAIL"))
    private void write(DynamicRegistryManager registryManager, NbtCompound nbt, NbtCompound playerNbt, CallbackInfo ci) {
        LOGGER.debug("LevelPropertiesMixin#write");
        nbt.put(STORAGE_KEY,this.worldborders);
        LOGGER.debug(nbt.toString());
    }

    @Override
    public @NotNull WorldBorder.Properties parseWorldBorder(@NotNull RegistryKey<World> key) {
        return WorldBorder.Properties.fromDynamic(
                new Dynamic<>(NbtOps.INSTANCE, this.worldborders.get(key.getRegistry().toString())),
                getWorldBorder()
        );
    }

    @Override
    public void saveWorldBorder(@NotNull WorldBorder border, @NotNull RegistryKey<World> key) {
        LOGGER.debug("LevelPropertiesMixin#saveWorldBorder");
        NbtCompound nbt = new NbtCompound();
        LOGGER.debug(nbt.toString());
        border.write().writeNbt(nbt);
        this.worldborders.put(key.getRegistry().toString(), nbt);
    }
}
