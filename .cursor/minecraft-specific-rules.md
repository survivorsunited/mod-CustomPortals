# Minecraft-Specific Rules - Custom Portals Mod

## Mod Development Guidelines

### Fabric Mod Structure
- Main class: `CustomPortals.java` implements `ModInitializer` and `WorldComponentInitializer`
- Client class: `CustomPortalsClient.java` for client-side code
- Register components in `onInitialize()` method
- Register world components in `registerWorldComponentFactories()`
- Use proper Fabric API patterns

### Portal System Architecture

#### Portal Components
- **Portal Blocks**: Custom blocks that form the portal frame
- **Portal Catalysts**: Items used to activate portals
- **Rune Blocks**: Enhancement blocks that modify portal behavior
  - Haste Rune: Increases portal activation speed
  - Gate Rune: Allows portal linking
  - Enhancer Runes (Weak/Strong): Increase portal range
  - Infinity Rune: Enables unlimited range

#### Portal Data Storage
- Uses Cardinal Components API for world-level data
- Portal data persisted across world saves
- Each portal stores: frame ID, dimension, color, position, blocks, runes
- Portal linking handled server-side

#### Portal Mechanics
- Players can build portals from any block
- Portals link automatically based on color and range
- Support for inter-dimensional travel
- Configurable range limits and private portal options
- Redstone control available

### Block Registration
- Register in `CPBlocks.registerBlocks()`
- Portal blocks extend `PortalBlock` class
- Rune blocks extend `AbstractRuneBlock` or specific rune classes
- Use proper block properties and settings

### Item Registration
- Register in `CPItems.registerItems()`
- Portal catalysts are items used to activate portals
- Items can have custom behaviors via mixins

### Configuration System
- Configuration file: `customportals.json`
- Managed via YACL (Yet Another Config Lib)
- ModMenu integration for in-game UI
- Settings include: range limits, interdim travel, private portals, redstone control

### Mixins
- `EntityMixin.java` - Entity teleportation handling
- `ClientPlayerEntityMixin.java` - Client-side player behavior
- `ServerPlayerEntityMixin.java` - Server-side player handling
- `InGameHudMixin.java` - HUD modifications
- `LevelLoadingScreenMixin.java` - Loading screen customization

### Testing Requirements
- Test portal creation with different block types
- Verify portal linking with various configurations
- Test teleportation in same dimension and cross-dimension
- Verify rune effects work correctly
- Test configuration changes and persistence
- Ensure compatibility with other mods

### Version Compatibility
- Target Minecraft 1.21.10+
- Use Fabric Loader 0.17.3+
- Require Fabric API 0.136.0+
- Requires Java 21
- Uses Cardinal Components 7.2.0
- Uses YACL 3.8.0+
