# AsTech Industrial
## Overview
Industrial Processing is a tech mod for Minecraft 1.20.1 focused on implementing industrial processing chains without the grindy complexity of GregTech-style mods. This mod adds a variety of advanced machines and end-game items to enhance your Minecraft experience.

## Features
- Chemical reaction chamber
- Chemical mixer
- Various computing elements (including quantum)
- Circuit assembly and production machines
- End-game items inspired by classic mods/modpacks like Enigmatic 2 Expert and Avaritia

## Getting Started
To use this mod, you'll need Minecraft 1.20.1 and Forge installed. Download the latest release from the Releases page and place it in your Minecraft mods folder.

## Development Setup
To set up the development environment for this mod, follow these steps:

Clone this repository
Open IntelliJ IDEA and import the project by selecting the build.gradle file
Run the following command in the project directory:
gradlew genIntellijRuns
(Use ./gradlew genIntellijRuns on Mac/Linux)
Refresh the Gradle project in IntelliJ if required

If you encounter any issues with missing libraries or other problems, try running:
gradlew --refresh-dependencies
or
gradlew clean
to reset everything (this does not affect your code) and then start the process again.
Additional Development Information
CopySource installation information for modders
-------------------------------------------
This code follows the Minecraft Forge installation methodology. It will apply
some small patches to the vanilla MCP source code, giving you and it access 
to some of the data and functions you need to build a successful mod.

Note also that the patches are built against "un-renamed" MCP source code (aka
SRG Names) - this means that you will not be able to read them directly against
normal code.

Mapping Names:
=============================
By default, the MDK is configured to use the official mapping names from Mojang for methods and fields 
in the Minecraft codebase. These names are covered by a specific license. All modders should be aware of this
license, if you do not agree with it you can change your mapping names to other crowdsourced names in your 
build.gradle. For the latest license text, refer to the mapping file itself, or the reference copy here:
https://github.com/MinecraftForge/MCPConfig/blob/master/Mojang.md

Contributing
We welcome contributions! Please see our Contributing Guidelines for more information.
License
This project is licensed under the MIT License.
