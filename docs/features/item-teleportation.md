# Portal Item Teleportation

Every dropped item can use a custom portal, including items dropped by players,
droppers, dispensers, broken blocks, or mobs. Ownership and pickup delay do not
affect eligibility.

An item teleports as soon as it touches an active portal, without the player
portal delay. A server-side check at the start of each item tick also handles
stationary items and items spawned directly inside the portal. This means an
item already overlapping the portal teleports on its next server tick.

The portal must be lit and linked to an available destination. Both same-world
and cross-dimension portals use the existing destination calculation and
Minecraft's item teleportation, preserving the stack and velocity.

Minecraft's normal portal cooldown remains in effect after arrival. While an
item stays inside the arrival portal, the cooldown is refreshed so the item
does not bounce back. Move it clear of the portal before routing it again.

Player and mob portal delays are unchanged.

## In-game verification

1. Create two linked, active portals in the same dimension.
2. Throw an item into the first portal and confirm immediate arrival.
3. Point a dropper into the portal and dispense an item; confirm arrival without
   needing a player to pick up and drop the item.
4. Repeat with a dispenser, block drops, and mob loot.
5. Spawn an item inside the portal with zero motion and confirm arrival on the
   next server tick. Repeat near a portal block boundary.
6. Confirm the item stays at the destination rather than bouncing back.
7. Repeat with linked portals in different dimensions (with gate runes or the
   appropriate configuration). Confirm stack count and item data are preserved.
8. Confirm inactive/unlinked portals do not move items and players still use
   their configured portal delay.
