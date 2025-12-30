# Plugin Development Assistance Minecraft Mod (PDAM)

A mod for Minecraft 1.8.9, designed to assist with server plugin development and configuration.

## Features
- **Configurable Settings**: All features can be enabled or disabled through the Forge mod configuration menu.
- **Item NBT Inspector**: Displays an item's NBT data in its tooltip.
- **Sound Debugger**: Shows the name of sounds in chat as they are played.
- **Invisible Entity Highlighter**: Makes invisible entities visible for easier debugging.
- **Entity Information Panel**: Displays an on-screen panel with real-time information about the entity you are looking at.
- **Skin Extraction**: Extract skins from players and heads.
- **Copy Chat Messages**: Allows copying chat messages to clipboard by holding CTRL and clicking on the message.

## Installation
1. Ensure you have Minecraft Forge for version 1.8.9 installed.
2. Download the latest version of the mod from the GitHub Releases page.
3. Place the downloaded `.jar` file into your `.minecraft/mods` folder.
4. Launch Minecraft using the Forge profile.

## Building from Source
1. Clone this repository to your local machine.
2. Navigate to the project's root directory.
3. Run the appropriate command for your operating system to build the project:
   - **Windows**: `gradlew clean build`
   - **Linux/Mac**: `./gradlew clean build`
4. The compiled `.jar` file will be located in the `build/libs/` directory.

---
*Project template: [Forge1.8.9Template](https://github.com/nea89o/Forge1.8.9Template)*