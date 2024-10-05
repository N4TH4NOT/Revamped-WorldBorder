package fr.n4th4not.worldborder.mixins;

import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.level.UnmodifiableLevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(UnmodifiableLevelProperties.class)
public abstract class UnmodifiableLevelPropertiesMixin {
    @Unique private WorldBorder.Properties worldBorder;

    @Inject(method = "setWorldBorder", at = @At(value = "HEAD"))
    public void setWorldBorder(WorldBorder.Properties prop, CallbackInfo ci) {
        this.worldBorder = prop;
    }

    @Inject(method = "getWorldBorder", at = @At("RETURN"), cancellable = true)
    public void getWorldBorder(CallbackInfoReturnable<WorldBorder.Properties> cir) {
        cir.setReturnValue(this.worldBorder);
    }
}
