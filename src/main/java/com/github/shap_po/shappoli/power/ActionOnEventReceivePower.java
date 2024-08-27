package com.github.shap_po.shappoli.power;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActionOnEventReceivePower extends Power {
    private final Consumer<Entity> action;
    private final Consumer<Pair<Entity, Entity>> bientityAction;
    private final Predicate<Pair<Entity, Entity>> bientityCondition;
    private final Consumer<Entity> entityAction;
    private final Predicate<Entity> entityCondition;
    private final Consumer<Pair<World, StackReference>> itemAction;
    private final Predicate<Pair<World, ItemStack>> itemCondition;

    public ActionOnEventReceivePower(
        PowerType<?> type,
        LivingEntity entity,
        Consumer<Entity> action,
        Consumer<Pair<Entity, Entity>> bientityAction,
        Predicate<Pair<Entity, Entity>> bientityCondition,
        Consumer<Entity> entityAction,
        Predicate<Entity> entityCondition,
        Consumer<Pair<World, StackReference>> itemAction,
        Predicate<Pair<World, ItemStack>> itemCondition
    ) {
        super(type, entity);
        this.action = action;
        this.bientityAction = bientityAction;
        this.bientityCondition = bientityCondition;
        this.entityAction = entityAction;
        this.entityCondition = entityCondition;
        this.itemAction = itemAction;
        this.itemCondition = itemCondition;
    }


    public void receiveBientityEvent(Pair<Entity, Entity> entities) {
        if (bientityCondition == null || bientityCondition.test(entities)) {
            bientityAction.accept(entities);
            receiveAnyEvent();
        }
    }

    public void receiveEntityEvent(Entity entity) {
        if (entityCondition == null || entityCondition.test(entity)) {
            entityAction.accept(entity);
            receiveAnyEvent();
        }
    }

    public void receiveItemEvent(Pair<World, StackReference> worldAndStack) {
        if (itemCondition == null || itemCondition.test(new Pair<>(worldAndStack.getLeft(), worldAndStack.getRight().get()))) {
            itemAction.accept(worldAndStack);
            receiveAnyEvent();
        }
    }

    public void receiveAnyEvent() {
        action.accept(this.entity);
    }

    public static PowerFactory createFactory() {
        return new PowerFactory<>(Shappoli.identifier("action_on_event_receive"),
            new SerializableData()
                .add("action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("entity_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                .add("item_action", ApoliDataTypes.ITEM_ACTION, null)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
            ,
            data -> (type, player) -> new ActionOnEventReceivePower(type, player,
                data.get("action"),
                data.get("bientity_action"),
                data.get("bientity_condition"),
                data.get("entity_action"),
                data.get("entity_condition"),
                data.get("item_action"),
                data.get("item_condition")
            )
        ).allowCondition();
    }
}
