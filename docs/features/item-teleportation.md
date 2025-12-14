# Portal Item Teleportation

## Feature Overview
All portals now teleport **all entities** through portals, including:
- Item entities (dropped items)
- Falling block entities
- Players
- All other entity types

Previously, only entities that could normally use portals or specific entity types (ItemEntity, FallingBlockEntity) were allowed. This change ensures universal entity teleportation through custom portals.

## Implementation Details

### Location
- **File**: `src/main/java/dev/custom/portals/blocks/PortalBlock.java`
- **Method**: `onEntityCollision()`

### Technical Implementation

**Previous Implementation:**
```java
// Only allowed entities that can use portals OR specific entity types
boolean canUse = entity.canUsePortals(false) || 
                 entity instanceof net.minecraft.entity.ItemEntity || 
                 entity instanceof net.minecraft.entity.FallingBlockEntity;
if(canUse) {
    entity.tryUsePortal(this, pos);
    ((EntityMixinAccess) entity).setInCustomPortal(portal);
}
```

**Current Implementation:**
```java
// Allow all entities (including items) to use portals
entity.tryUsePortal(this, pos);
((EntityMixinAccess) entity).setInCustomPortal(portal);
```

### Behavior Changes

- **Before**: Only entities that return `true` from `canUsePortals()` or are ItemEntity/FallingBlockEntity could teleport
- **After**: ALL entities can teleport through portals, regardless of their type

### Entity Types That Can Now Teleport

✅ **Item Entities** - Dropped items, tools, blocks, etc.
✅ **Falling Block Entities** - Falling sand, gravel, anvils, etc.
✅ **Players** - All players
✅ **Mobs** - All mob entities
✅ **Projectiles** - Arrows, fireballs, etc.
✅ **Vehicles** - Boats, minecarts, etc.
✅ **Other Entities** - Any other entity type

## Use Cases

### Item Transport
Players can throw items through portals to quickly transfer items between locations:
- Drop items near portal entrance
- Items automatically teleport to linked portal
- Collect items at destination

### Automatic Sorting Systems
- Set up portals for item routing
- Items thrown into portal go to designated location
- Useful for automated item sorting

### Mob Transportation
- Transport mobs through portals
- Move livestock between locations
- Transfer villagers or other NPCs

## Technical Notes

### Entity Teleportation Process

1. Entity collides with portal block (when portal is lit)
2. `onEntityCollision()` is called
3. Entity's `tryUsePortal()` method is invoked
4. Entity is marked as `inCustomPortal`
5. Portal teleportation system handles the actual teleportation

### Portal Requirements

For entities to teleport:
- Portal must be **lit** (activated)
- Portal must be **linked** to another portal
- Entity must collide with the portal block

### Dimension Changes

Entities can teleport:
- ✅ Within the same dimension
- ✅ Between dimensions (if portals are inter-dimensional)

## Testing

To verify this feature:

1. **Item Teleportation Test**:
   - Create two linked portals
   - Drop items near the first portal
   - Items should teleport to the second portal

2. **Mob Teleportation Test**:
   - Lead mobs into portal
   - Mobs should teleport through portal

3. **Falling Block Test**:
   - Push falling blocks (sand, gravel) into portal
   - Blocks should teleport

4. **Projectile Test**:
   - Shoot arrows through portal
   - Arrows should teleport

## Edge Cases

- **Large Entities**: May need to ensure spawn position at destination can accommodate them
- **Entity Velocity**: Entity velocity is preserved through teleportation
- **Entity State**: Entity states (NBT data) are preserved

## Future Considerations

- Add configuration option to control which entity types can teleport
- Add teleportation cooldown for items to prevent item spam
- Consider entity size limits for destination portal spawn positions

