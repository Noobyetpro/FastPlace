package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FastPlaceMod implements ClientModInitializer {
    public static final String MOD_ID = "fastplace";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static KeyBinding fastPlaceToggle;
    private static boolean fastPlaceEnabled = false;

    @Override
    public void onInitializeClient() {
        LOGGER.info("[FastPlace] Mod wird initialisiert");

        fastPlaceToggle = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fastplace.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                "category.fastplace"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (fastPlaceToggle.wasPressed()) {
                fastPlaceEnabled = !fastPlaceEnabled;
                LOGGER.info("[FastPlace] Status geändert: {}", fastPlaceEnabled);

                if (client.player != null) {
                    String status = fastPlaceEnabled ? "§aAN" : "§cAUS";
                    client.player.sendMessage(Text.literal("§6[FastPlace]§r " + status), true);
                }
            }
        });

        LOGGER.info("[FastPlace] Mod erfolgreich initialisiert");
    }

    public static boolean isFastPlaceEnabled() {
        return fastPlaceEnabled;
    }
}