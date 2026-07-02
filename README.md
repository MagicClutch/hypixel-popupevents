# Hypixel Request Popups

Client-side Fabric mod for Minecraft `26.1.2` that detects Hypixel party invites, trade requests, friend requests, guild invites, and duel requests from real chat `Component` data and shows a non-pausing HUD popup with configurable accept/decline keybinds.

## Features

- Detects trade requests by recursively inspecting chat component styles, siblings, click events, and hover text.
- Executes the exact captured `ClickEvent.RunCommand` accept command for trade requests.
- Detects party, friend, guild, and duel requests and uses captured clickable commands when present.
- Generates fallback accept/deny commands from the extracted player name when no clickable command exists.
- Shows one HUD popup at a time; new requests replace old ones.
- Smooth fade in/out, configurable duration, position, size, opacity, and debug logging.
- Configurable Controls entries:
  - Accept: `Y`
  - Decline: `N`
- YACL configuration screen exposed through ModMenu.

## Requirements

- Java 25
- Minecraft `26.1.2`
- Fabric Loader `0.19.3`
- Fabric API `0.154.0+26.1.2`
- Fabric Language Kotlin `1.13.12+kotlin.2.4.0`
- YACL `3.9.4+26.1-fabric` is bundled inside the mod jar.
- Mod Menu `18.0.0-alpha.8`

## Build

This repository targets the Fabric 26.1 unobfuscated toolchain:

```powershell
.\gradlew.bat build
```

If the wrapper files are missing in your checkout, use Gradle `9.5.0` or restore a standard Gradle wrapper for the `gradle/wrapper/gradle-wrapper.properties` distribution.

The built jar is written to:

```text
build/libs/hypixel-popupevents-1.0-SNAPSHOT.jar
```

## Dev Account Auth

`runClient` includes DevAuth for local testing with a real Minecraft/Microsoft account:

```powershell
.\gradlew.bat runClient
```

On the first launch, DevAuth opens a browser OAuth flow. Tokens are stored locally in your DevAuth config directory, not in this repository. The default account key is configured in `gradle.properties`:

```properties
devauth_account=main
```

To use an alt account, add it to your local DevAuth config as another account, then run:

```powershell
.\gradlew.bat runClient -Pdevauth_account=alt
```

## Configuration

Open Mod Menu, select `Hypixel Popupevents`, and press the config button. The YACL screen exposes:

- General: enable toggles for the mod and each request popup type.
- Popup: top-center default position, manual position, dimensions, scale, duration, opacity, background, text alignment, and an editor.
- Popup editor: drag the popup to move it, use the scroll wheel over the popup to resize it, or use `Center X`, `Top`, and `Reset`.
- Animation: animation toggle, speed, and fade duration.
- Debug: raw component logging, JSON logging, and extracted command logging.

The config file is stored at:

```text
config/popupevents.json
```

## Architecture

- `PopupeventsClient`: client entrypoint.
- `HypixelChatListener`: Fabric chat receive hook.
- `ChatComponentParser`: recursive `Component` parser for text, siblings, click events, and hover text.
- `PopupManager`: active popup lifecycle and actions.
- `PopupHudRenderer`: HUD overlay rendering.
- `PopupKeybinds`: configurable Controls keybind registration.
- `CommandExecutor`: exact client command execution.
- `ConfigManager` and `ConfigScreenFactory`: persistence and YACL UI.

## Notes

This mod is client-only. It does not cancel or rewrite Hypixel chat; it only adds a HUD popup and sends the stored command when the user presses the accept/decline key.
