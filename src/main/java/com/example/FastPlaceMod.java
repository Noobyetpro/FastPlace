package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mojang.brigadier.arguments.StringArgumentType;

import java.util.Random;

public class FastPlaceMod implements ClientModInitializer {
    public static final String MOD_ID = "fastplace";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static KeyBinding fastPlaceToggle;
    private static boolean fastPlaceEnabled = false;
    private static boolean showMessages = true;
    private static final Random random = new Random();
    private static int tickCounter = 0;

    @Override
    public void onInitializeClient() {
        LOGGER.info("[FastPlace] Mod loaded");

        fastPlaceToggle = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fastplace.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                "category.fastplace"
        ));

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("fastplace")
                    .then(ClientCommandManager.literal("logs")
                            .then(ClientCommandManager.argument("state", StringArgumentType.string())
                                    .suggests((context, builder) -> {
                                        builder.suggest("on");
                                        builder.suggest("off");
                                        return builder.buildFuture();
                                    })
                                    .executes(context -> {
                                        String state = StringArgumentType.getString(context, "state");
                                        if (state.equalsIgnoreCase("on")) {
                                            showMessages = true;
                                            context.getSource().sendFeedback(Text.literal("§6[FastPlace]§r Messages §aENABLED"));
                                        } else if (state.equalsIgnoreCase("off")) {
                                            showMessages = false;
                                            context.getSource().sendFeedback(Text.literal("§6[FastPlace]§r Messages §cDISABLED"));
                                        } else {
                                            context.getSource().sendError(Text.literal("§cUsage: /fastplace logs <on|off>"));
                                        }
                                        return 1;
                                    })
                            )
                    )
            );
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            tickCounter++;

            while (fastPlaceToggle.wasPressed()) {
                fastPlaceEnabled = !fastPlaceEnabled;
                LOGGER.info("[FastPlace] Toggled: {}", fastPlaceEnabled);

                if (client.player != null && showMessages) {
                    String status = fastPlaceEnabled ? "§aON" : "§cOFF";
                    client.player.sendMessage(Text.literal("§6[FastPlace]§r " + status), true);
                }
            }
        });
    }

    public static boolean isFastPlaceEnabled() {
        return fastPlaceEnabled;
    }

    public static boolean areMessagesEnabled() {
        return showMessages;
    }

    public static int getStealthDelay() {

        int rand = random.nextInt(100);
        if (rand < 90) return 0;
        if (rand < 98) return 1;
        return 2;
    }

    public static boolean shouldBreakPattern() {
        return tickCounter % 337 == 0;
    }
}