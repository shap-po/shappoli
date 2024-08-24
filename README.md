# Shappoli

An [Origins](https://modrinth.com/mod/origins) addon that provides [Trinkets](https://modrinth.com/mod/trinkets)
compatibility.

## Requirements

- Minecraft 1.20.4
- [Fabric](https://fabricmc.net/)
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [Origins](https://modrinth.com/mod/origins)
- [Trinkets](https://modrinth.com/mod/trinkets) (optional)

## Data types

### Trinket slot

All fields are optional. If field is not present, it will not be checked.

```jsonc
{
  "group": "", // Group name, eg "chest"
  "name": "" // Slot name, eg "necklace"
  "index: 0 // Slot index, if you have multiple slots
}
```

## Actions

### Action on trinket update

Type: `Entity action`

This action is triggered when a trinket is equipped or unequipped. Note that when slot is emptied, this action will be
triggered with `minecraft:air` item. Use `inverted` `origins:empty` item condition to trigger action only on non-empty
items.

```jsonc
{
  "type": "shappoli:action_on_trinket_update",
  "entity_action_on_equip": {
  // Entity action
  },
  "entity_action_on_unequip": {
    // Entity action
  },
  "item_action_on_equip": {
    // Item action
  },
  // entity_action_on_unequip does not exists yet
  "item_condition": {
    // Item condition
    "type": "origins:empty",
    "inverted": true
  },
  "slot": {
    // Trinket slot
  }
}
```

## Conditions

### Equipped trinket

Type: `Item condition`

Use `inverted` `origins:empty` item condition to check if slot is not empty.

```jsonc
{
  "type": "shappoli:equipped_trinket",
    "slot": {
        // Trinket slot, optional, if not present, will check if any trinket is equipped
    },
    "item_condition": {
        // Item condition, optional
        "type": "origins:empty",
        "inverted": true
    }
}
```

### Is block

Type: `Item condition`

Checks if the item can be placed as a block.

```jsonc
{
  "type": "shappoli:is_block"
}
```
