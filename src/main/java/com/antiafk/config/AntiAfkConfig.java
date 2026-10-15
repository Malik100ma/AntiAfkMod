package com.antiafk.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Config stored in .minecraft/config/antiafk.json */
public class AntiAfkConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("antiafk.json");
	private static final Pattern PART = Pattern.compile("(\\d+)\\s*([hms]?)");

	public static final long MIN_SECONDS = 1;
	public static final long DEFAULT_SECONDS = 60;

	private static AntiAfkConfig instance = new AntiAfkConfig();

	/** Is the mod active. */
	public boolean enabled = true;
	/** Interval as typed by the user: "2h", "3m", "20s", "1h30m". A bare number means seconds. */
	public String interval = "1m";
	/** Translation key of the key/mouse button to press, e.g. key.keyboard.space */
	public String key = "key.keyboard.space";

	public static AntiAfkConfig get() {
		return instance;
	}

	public static void load() {
		if (Files.exists(PATH)) {
			try (Reader r = Files.newBufferedReader(PATH)) {
				AntiAfkConfig loaded = GSON.fromJson(r, AntiAfkConfig.class);
				if (loaded != null) {
					instance = loaded;
				}
			} catch (Exception e) {
				System.err.println("[AntiAfk] Failed to read config, using defaults: " + e);
			}
		}
		if (instance.interval == null || parseSeconds(instance.interval) < 0) {
			instance.interval = "1m";
		}
		if (instance.key == null || instance.key.isBlank()) {
			instance.key = "key.keyboard.space";
		}
		save();
	}

	public static void save() {
		try {
			Files.createDirectories(PATH.getParent());
			try (Writer w = Files.newBufferedWriter(PATH)) {
				GSON.toJson(instance, w);
			}
		} catch (IOException e) {
			System.err.println("[AntiAfk] Failed to save config: " + e);
		}
	}

	/**
	 * Parses "2h", "3m", "20s", "1h 30m", "90" (seconds).
	 *
	 * @return total seconds, or -1 if the text is invalid / below the minimum
	 */
	public static long parseSeconds(String text) {
		if (text == null) {
			return -1;
		}
		String s = text.trim().toLowerCase();
		if (s.isEmpty()) {
			return -1;
		}
		Matcher m = PART.matcher(s);
		long total = 0;
		int pos = 0;
		boolean any = false;
		while (pos < s.length()) {
			if (!m.find(pos) || m.start() != pos) {
				return -1;
			}
			long value;
			try {
				value = Long.parseLong(m.group(1));
			} catch (NumberFormatException e) {
				return -1;
			}
			String unit = m.group(2);
			long mult = switch (unit) {
				case "h" -> 3600L;
				case "m" -> 60L;
				default -> 1L;
			};
			total += value * mult;
			any = true;
			pos = m.end();
			while (pos < s.length() && s.charAt(pos) == ' ') {
				pos++;
			}
			if (total > 7L * 24 * 3600) {
				return -1;
			}
		}
		if (!any || total < MIN_SECONDS) {
			return -1;
		}
		return total;
	}

	public long intervalSeconds() {
		long v = parseSeconds(interval);
		return v < 0 ? DEFAULT_SECONDS : v;
	}
}
