# AntiAfk (Fabric 1.21.11)

Client mod: presses a configurable key at a configurable interval.

## Build

Needs JDK 21.

1. Open the folder in IntelliJ IDEA (it imports Gradle automatically), **or**
2. In a terminal: `gradle build` (or `./gradlew build` if you copied the Gradle wrapper from the
   official Fabric example mod: https://github.com/FabricMC/fabric-example-mod).

The jar appears in `build/libs/antiafk-1.0.0.jar` (not the `-sources` one).

If Gradle can't find some version, edit `gradle.properties` using https://fabricmc.net/develop
(and the Mod Menu version from https://modrinth.com/mod/modmenu/versions).

Source layout:
- `config/AntiAfkConfig.java` — JSON config + interval parser (`2h`, `3m`, `20s`, `1h30m`)
- `client/AntiAfkClient.java` — timer and key simulation
- `client/AntiAfkConfigScreen.java` — settings screen
- `client/ModMenuIntegration.java` — Mod Menu entrypoint
- `assets/antiafk/lang/` — en, uk, ru, de, es, fr
