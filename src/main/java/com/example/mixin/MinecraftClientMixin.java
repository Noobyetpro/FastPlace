package com.example.mixin;

import com.example.FastPlaceMod;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    private int itemUseCooldown;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (FastPlaceMod.shouldBreakPattern()) {
            this.itemUseCooldown = Math.max(0, this.itemUseCooldown - 5);
        } else {
            this.itemUseCooldown = Math.max(0, this.itemUseCooldown - (6 + FastPlaceMod.getStealthDelay()));
        }
    }
}
