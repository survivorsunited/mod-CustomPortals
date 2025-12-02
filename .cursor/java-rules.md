# Java Development Rules - Minecraft Fabric Mod

## Code Style
- Use proper indentation (tabs as shown in existing code)
- Follow existing naming conventions
- Add meaningful comments for complex logic
- Use proper Java logging with LOGGER.info() or LOGGER.debug()
- Package: `dev.custom.portals.*`

## Minecraft Fabric Specific
- Main class: `CustomPortals` implements `ModInitializer` and `WorldComponentInitializer`
- Client class: `CustomPortalsClient` for client-side initialization
- Use `Identifier.of("customportals", "name")` for resource identifiers
- Register blocks/items using Fabric registries: `Registry.register(Registries.BLOCK, ...)`
- Use Cardinal Components API for world data storage
- Use YACL (Yet Another Config Lib) for configuration management

## Portal System Patterns
```java
// Portal data storage using Cardinal Components
public static final ComponentKey<BasePortalComponent> PORTALS = ComponentRegistryV3.INSTANCE
    .getOrCreate(Identifier.of("customportals:portals"), BasePortalComponent.class);

// Portal registration
CustomPortal portal = new CustomPortal(frameId, dimensionId, color, spawnPos, blocks, offsetX, offsetZ, creatorId);
```

## Registry Pattern
```java
// Block registration
Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, "name"), block);
Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "name"), new BlockItem(block, new Item.Settings()));

// Item registration
Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "name"), item);
```

## Configuration Pattern
- Use YACL annotations for config UI generation
- `@AutoGen(category = "category_name")` - Category grouping
- `@Boolean`, `@IntField`, `@EnumCycler()` - Field types
- `@SerialEntry(comment = "...")` - Documentation
- Access config via `CPSettings.instance()`

## Error Handling
- Always check if blocks/items exist before accessing
- Use proper exception handling for mod initialization
- Log errors appropriately with LOGGER.error()
- Handle missing world components gracefully

## Testing
- Test portal creation and linking
- Verify teleportation works correctly
- Test configuration changes
- Verify rune effects (haste, gate, enhancer, infinity)
- Test both client and server functionality
