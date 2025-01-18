# Changelog

## 1.6.0

### Additions

- `[base]` Added `shappoli:active_self` power type. This power works similar to the `apoli:active_self`, but allows specifying multiple keys or key categories. If none of them were specified, action will be triggered if any key was pressed
- `[trinkets]` Added `shappoli:powers` component for trinkets. This component works similar to the `apoli:powers` component, but works with trinkets
    - TODO: rename to `shappoli:trinket_powers`
- `[trinkets]` Added `shappoli:has_trinket_power` item condition. This condition checks if the item has a trinket power
- `[trinkets]` Added `shappoli:equipped_trinket` item condition. This condition checks if the item is equipped by the player as a trinket
- `[trinkets]` Added `shappoli:slot_linked_active` power type and `slot_linked_key` data type. This power type allows creating active powers assigned to specific trinkets; a key for the power to activate is specified by the slot where the trinket is equipped
- `[walkers]` Added `shappoli:execute_shape_ability` entity action type. This action type allows executing a specific shape ability
- `[walkers]` Added `shappoli:shaped` entity condition type. This condition type checks if the player has their shape changed
- Added config for jitpack

### Fixes

- `[base]` Fixed `shappoli:teleport` bi-entity action type having incorrect validator
- `[origins]` Fixed `shappoli:copy_origin` bi-entity action type having incorrect validator
- `[trinkets]` Fixed `shappoli:modify_trinket` entity action type crashing the game

### Changes

- `[walkers]` Improved `shappoli:has_shape_ability` entity condition and `shappoli:prevent_shape_ability_use` power. Both of them now can check for specific shape abilities
- `[walkers]` Replaced Mixins with event handlers for `shappoli:prevent_shape_change`, `shappoli:action_on_shape_change`, `shappoli:prevent_shape_ability_use` powers
- `[walkers]` All shape abilities now use `walkers` as the ability namespace
- `[trinkets]` The `TrinketsSlotModifierUtil` now allows specifying the modifier id. This allows other mods to use this utility for easy trinket slot addition/removal
- Updated the mod to work with Apoli `v2.12.0-alpha.14+mc.1.21.1`
- `[trinkets]` Renamed some stuff. Old names left as aliases:
    - `shappoli:modify_trinket_inventory` entity action type to `shappoli:modify_trinkets_inventory`
    - `shappoli:modify_trinket_slot` and `shappoli:conditioned_modify_trinket_slot` power type to `shappoli:modify_trinkets_slot` and `shappoli:conditioned_modify_trinkets_slot`
- `[base]` Made `shappoli:action_on_entity_collision` ticking power instead of using a Mixin. This also allows tweaking the tick interval.
