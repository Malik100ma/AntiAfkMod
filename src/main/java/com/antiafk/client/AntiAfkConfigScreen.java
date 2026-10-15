package com.antiafk.client;

import com.antiafk.config.AntiAfkConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class AntiAfkConfigScreen extends Screen {
	private final Screen parent;

	private boolean enabled;
	private String keyTranslation;
	private boolean waitingForKey = false;

	private TextFieldWidget intervalField;
	private ButtonWidget enabledButton;
	private ButtonWidget keyButton;

	public AntiAfkConfigScreen(Screen parent) {
		super(Text.translatable("antiafk.config.title"));
		this.parent = parent;
		AntiAfkConfig cfg = AntiAfkConfig.get();
		this.enabled = cfg.enabled;
		this.keyTranslation = cfg.key;
	}

	@Override
	protected void init() {
		int w = 200;
		int x = this.width / 2 - w / 2;
		int y = this.height / 2 - 55;

		enabledButton = addDrawableChild(ButtonWidget.builder(enabledText(), b -> {
			enabled = !enabled;
			b.setMessage(enabledText());
		}).dimensions(x, y, w, 20).build());

		intervalField = new TextFieldWidget(this.textRenderer, x, y + 45, w, 20,
				Text.translatable("antiafk.config.interval"));
		intervalField.setMaxLength(16);
		intervalField.setText(AntiAfkConfig.get().interval);
		intervalField.setPlaceholder(Text.literal("1m"));
		addDrawableChild(intervalField);

		keyButton = addDrawableChild(ButtonWidget.builder(keyText(), b -> {
			waitingForKey = true;
			b.setMessage(keyText());
		}).dimensions(x, y + 90, w, 20).build());

		addDrawableChild(ButtonWidget.builder(Text.translatable("antiafk.config.done"), b -> saveAndClose())
				.dimensions(x, y + 125, w / 2 - 2, 20).build());
		addDrawableChild(ButtonWidget.builder(Text.translatable("antiafk.config.cancel"), b -> close())
				.dimensions(x + w / 2 + 2, y + 125, w / 2 - 2, 20).build());
	}

	private Text enabledText() {
		return Text.translatable("antiafk.config.enabled",
				Text.translatable(enabled ? "antiafk.config.on" : "antiafk.config.off"));
	}

	private Text keyText() {
		if (waitingForKey) {
			return Text.literal("> ").append(Text.translatable("antiafk.config.press_key")).append(" <");
		}
		Text keyName;
		try {
			keyName = InputUtil.fromTranslationKey(keyTranslation).getLocalizedText();
		} catch (Exception e) {
			keyName = Text.literal(keyTranslation);
		}
		return Text.translatable("antiafk.config.key", keyName);
	}

	@Override
	public boolean keyPressed(KeyInput input) {
		if (waitingForKey) {
			int code = input.key();
			if (code != GLFW.GLFW_KEY_ESCAPE) {
				keyTranslation = InputUtil.Type.KEYSYM.createFromCode(code).getTranslationKey();
			}
			waitingForKey = false;
			keyButton.setMessage(keyText());
			return true;
		}
		return super.keyPressed(input);
	}

	private void saveAndClose() {
		AntiAfkConfig cfg = AntiAfkConfig.get();
		String text = intervalField.getText().trim();
		if (AntiAfkConfig.parseSeconds(text) < 0) {
			return; // invalid interval: stay on screen (the field shows a red hint)
		}
		cfg.enabled = enabled;
		cfg.interval = text;
		cfg.key = keyTranslation;
		AntiAfkConfig.save();
		AntiAfkClient.resetTimer();
		close();
	}

	@Override
	public void close() {
		this.client.setScreen(parent);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		int cx = this.width / 2;
		int y = this.height / 2 - 55;

		context.drawCenteredTextWithShadow(this.textRenderer, this.title, cx, y - 30, 0xFFFFFFFF);
		context.drawCenteredTextWithShadow(this.textRenderer,
				Text.translatable("antiafk.config.interval"), cx, y + 32, 0xFFAAAAAA);

		long secs = AntiAfkConfig.parseSeconds(intervalField.getText());
		Text hint;
		int color;
		if (secs < 0) {
			hint = Text.translatable("antiafk.config.invalid");
			color = 0xFFFF5555;
		} else {
			hint = Text.translatable("antiafk.config.every", formatSeconds(secs));
			color = 0xFF55FF55;
		}
		context.drawCenteredTextWithShadow(this.textRenderer, hint, cx, y + 70, color);
	}

	private static String formatSeconds(long s) {
		long h = s / 3600;
		long m = (s % 3600) / 60;
		long sec = s % 60;
		StringBuilder sb = new StringBuilder();
		if (h > 0) sb.append(h).append("h ");
		if (m > 0) sb.append(m).append("m ");
		if (sec > 0 || sb.length() == 0) sb.append(sec).append("s");
		return sb.toString().trim();
	}
}
