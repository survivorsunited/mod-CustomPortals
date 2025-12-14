# Black Portal - No Light Emission

## Feature Overview
The black portal block does not emit any light, regardless of its lit state. This makes it visually distinct from other colored portals which emit light when activated.

## Implementation Details

### Location
- **File**: `src/main/java/dev/custom/portals/registry/CPBlocks.java`
- **Block**: `BLACK_PORTAL`

### Technical Implementation

The black portal uses a custom luminance function that always returns 0:

```java
private static final ToIntFunction<BlockState> NO_LUMINANCE = (state) -> {
    return 0;
};

public static final Block BLACK_PORTAL = new PortalBlock(
    Block.Settings.create()
        .registryKey(getKey("black_portal"))
        .mapColor(MapColor.BLACK)
        .nonOpaque()
        .noCollision()
        .ticksRandomly()
        .strength(-1.0F)
        .sounds(BlockSoundGroup.GLASS)
        .luminance(NO_LUMINANCE)  // Always returns 0 (no light)
);
```

### Behavior

- **Light Level**: Always 0 (no light emission)
- **Lit State**: The portal can still be lit (activated), but it emits no light
- **Visual Effect**: Portal particles still render, but the block itself provides no illumination

### Comparison with Other Portals

Other colored portals use `STATE_TO_LUMINANCE` which emits light level 11 when lit:

```java
private static final ToIntFunction<BlockState> STATE_TO_LUMINANCE = (state) -> {
    return (Boolean)state.get(Properties.LIT) ? 11 : 0;
};
```

- Other portals: Light level 11 when lit, 0 when unlit
- Black portal: Light level 0 always (no light emission)

## User Experience

Players can still:
- ✅ Activate the black portal (it can be lit/unlit)
- ✅ Use the portal for teleportation
- ✅ See portal particles

Players will notice:
- ⚠️ No light emission from the black portal (dark even when active)
- ⚠️ Area around black portal remains dark
- ⚠️ No mob spawning light benefits from the portal

## Testing

To verify this feature:
1. Place and activate a black portal
2. Check light level around the portal (should be 0)
3. Compare with other colored portals (should emit light level 11 when lit)
4. Verify portal still functions for teleportation despite no light

## Notes

- This is a design choice to make the black portal visually distinct
- The portal's functionality is unaffected by the lack of light emission
- Useful for dark-themed builds where light emission would be undesirable

