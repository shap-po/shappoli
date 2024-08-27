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
  "name": "", // Slot name, eg "necklace"
  "index: 0 // Slot index, if you have multiple slots
}
```

## Powers

### Action on trinket update

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
  },
  "slots": [
    // List of trinket slots
  ]
}
```

### Action on event receive

This action is triggered when the player receives an event from the [Send event](#send-event) action.

All fields are optional.

```jsonc
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

### Modify villager reputation

This power modifies the player's reputation with a villager, which is used to determine the prices of trades.

```jsonc
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

```jsonc
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
      "type": "origins:empty",
      "inverted": true
    },
    "item_action": {
      // Item action
    },
    "entity_action": {
      // Entity action, optional
    },
    "process_mode": "stacks", // "stacks" or "items", default "stacks"
    "limit": 0 // Limit of items to process, default 1
}
```

### Send event

Type: `Meta action` (`Bi-entity action`, `Entity action`, `Item action`)

Sends an event with the bi-entity/entity/item payload to an [Action on event receive](#action-on-event-receive) power.

```jsonc
{
  "type": "shappoli:send_event", // shappoli:emit_event is an alias
  "receiver": "*:event_name", // name of the power to trigger
  "listener": "", // an alias for receiver
  "condition": {
    // Condtion to check before sending the event
  }
}
```

## Conditions

### Equipped trinket

Type: `Item condition`

Use `inverted` `origins:empty` item condition to check if slot is not empty.

If no slots are provided, it will check if any trinket is equipped.

```jsonc
{
  "type": "shappoli:equipped_trinket",
    "slot": {
        // Trinket slot, optional
    },
    "slots": [
        // List of trinket slots, optional
    ]
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

### Holder

Type: `Item condition`

Checks if the condition on the item holder is met.

```jsonc
{
  "type": "shappoli:holder",
  "condition": {
    // Entity condition
  }
}
```
