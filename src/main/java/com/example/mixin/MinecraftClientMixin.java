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
    @Shadow private int itemUseCooldown;

    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo info) {
        if (FastPlaceMod.isFastPlaceEnabled()) {
            itemUseCooldown = 0;
        }
    }
}