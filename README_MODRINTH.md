# AntiAfk

**Don't get kicked for being AFK.** AntiAfk presses a key of your choice at an interval you set — by default, the **Space** bar once every **minute**.

Client-side only. Works on any server and in singleplayer.

## Features

- ⏱ **Custom interval** — type it the human way: `20s`, `3m`, `2h`, or combine them: `1h30m`. A plain number means seconds. Minimum is 1 second.
- ⌨ **Any key** — click the key button in the settings and press the key (or button) you want. Default: Space.
- ⚙ **Built-in settings screen** — no extra config library needed, opens from **Mod Menu**.
- 🔘 **Toggle keybind** — turn the mod on/off in-game (unbound by default, set it in *Options → Controls → AntiAfk*).
- 💬 Won't press while a screen (chat, inventory, menu) is open — it waits until you're back in the game.
- 🌍 **6 languages:** English, Українська, Русский, Deutsch, Español, Français.

## How to use

1. Install [Fabric Loader](https://fabricmc.net/use/) and [Fabric API](https://modrinth.com/mod/fabric-api) for Minecraft **1.21.11**.
2. Put `antiafk-1.0.0.jar` into your `mods` folder.
3. (Recommended) Install [Mod Menu](https://modrinth.com/mod/modmenu).
4. Open **Mods → AntiAfk → Configure**, set the interval and the key, press **Save**.

### Interval examples

| You type | Meaning |
|----------|---------|
| `20s`    | every 20 seconds |
| `3m`     | every 3 minutes |
| `2h`     | every 2 hours |
| `1h30m`  | every 1 hour 30 minutes |
| `45`     | every 45 seconds |

With the default key (Space) and `20s`, your character will jump every 20 seconds.

## Config file

`.minecraft/config/antiafk.json`

```json
{
  "enabled": true,
  "interval": "1m",
  "key": "key.keyboard.space"
}
```

## Requirements

- Minecraft 1.21.11
- Fabric Loader 0.18.1+
- Fabric API
- Mod Menu *(optional, for the settings screen button)*

## Note

Many servers forbid AFK-bypass tools. Check the rules of the server you play on — use at your own risk.

---

## 🇷🇺 Русский

**AntiAfk** нажимает выбранную вами клавишу через заданный интервал (по умолчанию — пробел раз в минуту), чтобы вас не кикнуло за AFK.

- Интервал вводится как `20s`, `3m`, `2h` или `1h30m`.
- Любая клавиша на выбор, настройки открываются через Mod Menu.
- Языки: English, Українська, Русский, Deutsch, Español, Français.
- Нужны: Fabric Loader, Fabric API, (опционально) Mod Menu. Minecraft 1.21.11.

## 🇺🇦 Українська

**AntiAfk** натискає обрану вами клавішу через заданий інтервал (за замовчуванням — пробіл раз на хвилину), щоб вас не кікнуло за AFK.

- Інтервал вводиться як `20s`, `3m`, `2h` або `1h30m`.
- Будь-яка клавіша на вибір, налаштування відкриваються через Mod Menu.
- Мови: English, Українська, Русский, Deutsch, Español, Français.
- Потрібні: Fabric Loader, Fabric API, (опціонально) Mod Menu. Minecraft 1.21.11.
