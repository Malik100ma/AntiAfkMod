package com.antiafk.client;

import com.antiafk.config.AntiAfkConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class AntiAfkClient implements ClientModInitializer {
	public static final String MOD_ID = "antiafk";

	private static KeyBinding toggleKey;

	private static long ticksLeft = -1;
	private static int releaseIn = 0;
	private static InputUtil.Key heldKey = null;
	private static boolean pending = false;

	@Override
	public void onInitializeClient() {
		AntiAfkConfig.load();

		toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.antiafk.toggle",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN,
				KeyBinding.Category.create(Identifier.of(MOD_ID, "main"))
		));

		ClientTickEvents.END_CLIENT_TICK.register(AntiAfkClient::tick);
	}

	/** Called by the settings screen after saving so the new interval applies immediately. */
	public static void resetTimer() {
		ticksLeft = -1;
		pending = false;
	}

	private static void tick(MinecraftClient client) {
		AntiAfkConfig cfg = AntiAfkConfig.get();

		while (toggleKey.wasPressed()) {
			cfg.enabled = !cfg.enabled;
			AntiAfkConfig.save();
			resetTimer();
			if (client.player != null) {
				client.player.sendMessage(Text.translatable(
						cfg.enabled ? "antiafk.message.on" : "antiafk.message.off"), true);
			}
		}

		// release a previously "pressed" key after 2 ticks
		if (releaseIn > 0 && --releaseIn == 0 && heldKey != null) {
			KeyBinding.setKeyPressed(heldKey, false);
			heldKey = null;
		}

		if (client.player == null || client.world == null || !cfg.enabled) {
			ticksLeft = -1;
			pending = false;
			return;
		}

		if (ticksLeft < 0) {
			ticksLeft = cfg.intervalSeconds() * 20L;
		}
		if (ticksLeft > 0) {
			ticksLeft--;
		}
		if (ticksLeft == 0) {
			pending = true;
		}

		// don't press while a screen (chat, inventory, menu) is open - wait until it closes
		if (pending && client.currentScreen == null && releaseIn == 0) {
			press(cfg);
			pending = false;
			ticksLeft = cfg.intervalSeconds() * 20L;
		}
	}

	private static void press(AntiAfkConfig cfg) {
		InputUtil.Key key;
		try {
			key = InputUtil.fromTranslationKey(cfg.key);
		} catch (Exception e) {
			key = InputUtil.Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_SPACE);
		}
		KeyBinding.setKeyPressed(key, true);
		KeyBinding.onKeyPressed(key);
		heldKey = key;
		releaseIn = 2;
	}
}
