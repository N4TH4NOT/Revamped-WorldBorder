package fr.n4th4not.worldborder.mixins;

import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.storage.DerivedLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DerivedLevelData.class)
public abstract class DerivedLevelDataMixin {

    @Unique private WorldBorder.Settings worldBorder;

    @Inject(method = "setWorldBorder", at = @At(value = "HEAD"))
    public void setWorldBorder(WorldBorder.Settings settings, CallbackInfo ci) {
        this.worldBorder = settings;
    }

    @Inject(method = "getWorldBorder", at = @At("RETURN"), cancellable = true)
    public void getWorldBorder(CallbackInfoReturnable<WorldBorder.Settings> cir) {
        cir.setReturnValue(this.worldBorder);
    }
}
