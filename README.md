# Shappoli

![](https://raw.githubusercontent.com/shap-po/shappoli/main/src/main/resources/assets/shappoli/icon.png)

An [Origins](https://modrinth.com/mod/origins) addon that provides [Trinkets](https://modrinth.com/mod/trinkets) compatibility, event system and more.

See [Test powers](https://github.com/shap-po/shappoli/tree/main/src/test/resources/data/shappoli/powers) for some examples.

## Requirements

- Minecraft 1.20.4
- [Fabric](https://fabricmc.net/)
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [Origins](https://modrinth.com/mod/origins)
- [Trinkets](https://modrinth.com/mod/trinkets) (optional, enables trinkets support)

## Data types

### Trinket slot

All fields are optional. If field is not present, it will not be checked.

```json5
{
  // Group name, eg "chest"
  "group": "",
  // Slot name, eg "necklace"
  "name": "",
  // Slot index, if you have multiple slots
  "index": 0
}
```

## Powers

### Action on trinket update

This action is triggered when a trinket is equipped or unequipped. Note that when slot is emptied, this action will be triggered with `minecraft:air` item. Use `inverted` `origins:empty` item condition to trigger action only on non-empty items.

```json5
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
  // entity_action_on_unequip does not exist yet
  "item_condition": {
    // Item condition
    "type": "origins:empty",
    "inverted": true
  },
  "slot": {
    // Trinket slot
  },
  "slots": [
    // List of trinket slots
  ]
}
```

### Action on event receive

This action is triggered when the player receives an event from the [Send event](#send-event) action.

All fields are optional.

```json5
{
  "type": "shappoli:action_on_event_receive",
  "action": {
    // Generic entity action, triggered when the player receives any event and the condition is met
  },

  "bientity_action": {
    // Bi-entity action, triggered when the player receives a bi-entity event and the bi-entity condition is met
  },
  "bientity_condition": {
    // Bi-entity condition
  },

  "entity_action": {
    // Entity action, triggered when the player receives an entity event and the entity condition is met
  },
  "entity_condition": {
    // Entity condition
  },

  "item_action": {
    // Item action, triggered when the player receives an item event and the item condition is met
  },
  "item_condition": {
    // Item condition
  }
}
```

### Prevent trinket equip/unequip

This power prevents the player from equipping or unequipping a trinket.

```json5
{
  // or shappoli:prevent_trinket_unequip
  "type": "shappoli:prevent_trinket_equip",
  "slot": {
    // Trinket slot, optional
  },
  "slots": [
    // List of trinket slots, optional
  ],
  "item_condition": {
    // Item condition, optional
  },
  "entity_condition": {
    // Entity condition, optional
  },
  // Allow in creative mode, default true
  "allow_in_creative": true
}
```

### Modify villager reputation

This power modifies the player's reputation with a villager, which is used to determine the prices of trades.

```json5
{
  "type": "shappoli:modify_villager_reputation",
  "bientity_condition": {
    // Bi-entity condition, optional
  },
  "modifier": {
    // Attribute Modifier, optional
  },
  "modifiers": [
    // List of attribute modifiers, optional
  ]
}
```

## Actions

### Modify trinket

Type: `Entity action`

Modifies the items from entity's trinket slots.

Extremely similar to the `origins:modify_inventory` action, but for trinkets.

```json5
{
  "type": "shappoli:modify_trinket",
  "slot": {
    // Trinket slot, optional
  },
  "slots": [
    // List of trinket slots, optional
  ],
  "item_condition": {
    // Item condition, optional
    // Checking if not empty is recommended
    "type": "origins:empty",
    "inverted": true
  },
  "item_action": {
    // Item action
  },
  "entity_action": {
    // Entity action, optional
  },
  // "stacks" or "items", default "stacks"
  "process_mode": "stacks",
  // Limit of items to process, default 1, zero or negative for no limit
  "limit": 1
}
```

### Send event

Type: `Meta action` (`Bi-entity action`, `Entity action`, `Item action`)

Sends an event with the bi-entity/entity/item payload to an [Action on event receive](#action-on-event-receive) power.

```json5
{
  // shappoli:emit_event is an alias
  "type": "shappoli:send_event",
  // name of the power to trigger
  "receiver": "*:event_name",
  // an alias for receiver
  "listener": "",
}
```

## Conditions

### Equipped trinket

Type: `Item condition`

Use `inverted` `origins:empty` item condition to check if slot is not empty.

If no slots are provided, it will check if any trinket is equipped.

```json5
{
  "type": "shappoli:equipped_trinket",
  "slot": {
    // Trinket slot, optional
  },
  "slots": [
    // List of trinket slots, optional
  ],
  "item_condition": {
    // Item condition, optional
    // Checking if not empty is recommended
    "type": "origins:empty",
    "inverted": true
  }
}
```

### Is block

Type: `Item condition`

Checks if the item can be placed as a block.

```json5
{
  "type": "shappoli:is_block"
}
```

### Holder

Type: `Item condition`

Checks if the condition on the item holder is met.

```json5
{
  "type": "shappoli:holder",
  "condition": {
    // Entity condition
  }
}
```
