# Minecraft Fabric Mod - Custom Portals
# Main Cursor AI Rules

## Project Overview
This is a Minecraft Fabric mod that allows players to construct inter- and intradimensional portals out of any block. The mod includes portal blocks, catalysts, runes, and a configurable portal system with various enhancement options.

## Key Project Structure
- `src/main/java/dev/custom/portals/` - Main mod code
- `src/main/java/dev/custom/portals/blocks/` - Portal block implementations
- `src/main/java/dev/custom/portals/items/` - Portal catalyst items
- `src/main/java/dev/custom/portals/config/` - Configuration system using YACL
- `src/main/java/dev/custom/portals/data/` - Portal data storage using Cardinal Components
- `src/main/java/dev/custom/portals/mixin/` - Mixins for client/server behavior
- `src/main/java/dev/custom/portals/registry/` - Block, item, and particle registries
- `scripts/` - PowerShell build and deployment scripts
- `gradle.properties` - Centralized mod configuration

## Development Guidelines

### Java Code (Fabric Mod)
- Follow existing code style and patterns
- Package structure: `dev.custom.portals.*`
- Main mod class: `CustomPortals` implements `ModInitializer` and `WorldComponentInitializer`
- Client class: `CustomPortalsClient` for client-side initialization
- Use Cardinal Components API for world data storage
- Use YACL (Yet Another Config Lib) for configuration UI

### Portal System
- Portals are stored using Cardinal Components world components
- Portal data includes: frame ID, dimension, color, spawn position, blocks, runes
- Portal linking and range logic handled in `PortalBlock.java`
- Support for inter-dimensional and intra-dimensional travel
- Redstone control and private portal options via config

### Registry Pattern
- Blocks registered in `CPBlocks.registerBlocks()`
- Items registered in `CPItems.registerItems()`
- Particles registered in `CParticles.registerParticles()`
- Use proper Identifier creation: `Identifier.of("customportals", "name")`

### Configuration
- Configuration file: `customportals.json` in config directory
- Settings managed via `CPSettings` class with YACL annotations
- Key settings: unlimited range, always interdim, private portals, redstone control
- ModMenu integration for in-game config UI

### Build and Deployment
- Use PowerShell scripts in /scripts/ directory
- Configuration is centralized in gradle.properties
- Build with `.\build.ps1` on Windows
- Test with `.\build.ps1 -StartServer` for local server testing

## Key Classes
- `CustomPortals.java` - Main mod initialization
- `CustomPortalsClient.java` - Client-side initialization
- `PortalBlock.java` - Core portal block logic
- `CustomPortal.java` - Portal data model
- `WorldPortals.java` - World component for portal storage
- `CPSettings.java` - Configuration management
- `PortalHelper.java` - Portal utility functions

## Testing
- Build using `.\build.ps1`
- Test server can be started with `.\build.ps1 -StartServer`
- Test portal creation, linking, and teleportation
- Verify rune effects (haste, gate, enhancer, infinity)
- Test configuration options in-game via ModMenu

## Important Notes
- Mod ID: `customportals`
- Maven group: `net.custom.portals`
- Main package: `dev.custom.portals`
- Uses Cardinal Components for world data persistence
- Client and server code separated appropriately
- Mixins used for entity and HUD modifications
