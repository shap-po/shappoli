package com.github.shap_po.shappoli.power.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ReceiveActionPowerType extends PowerType {
    private final @Nullable Consumer<Entity> action;
    private final @Nullable Consumer<Pair<Entity, Entity>> bientityAction;
    private final @Nullable Predicate<Pair<Entity, Entity>> bientityCondition;
    private final @Nullable Consumer<Entity> entityAction;
    private final @Nullable Predicate<Entity> entityCondition;
    private final @Nullable Consumer<Pair<World, StackReference>> itemAction;
    private final @Nullable Predicate<Pair<World, ItemStack>> itemCondition;

    public ReceiveActionPowerType(
        Power type,
        LivingEntity entity,
        @Nullable Consumer<Entity> action,
        @Nullable Consumer<Pair<Entity, Entity>> bientityAction,
        @Nullable Predicate<Pair<Entity, Entity>> bientityCondition,
        @Nullable Consumer<Entity> entityAction,
        @Nullable Predicate<Entity> entityCondition,
        @Nullable Consumer<Pair<World, StackReference>> itemAction,
        @Nullable Predicate<Pair<World, ItemStack>> itemCondition
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


    public void receiveBientityAction(Pair<Entity, Entity> entities) {
        if (bientityCondition == null || bientityCondition.test(entities)) {
            maybeAccept(bientityAction, entities);
            receiveAnyAction();
        }
    }

    public void receiveEntityAction(Entity entity) {
        if (entityCondition == null || entityCondition.test(entity)) {
            maybeAccept(entityAction, entity);
            receiveAnyAction();
        }
    }

    public void receiveItemAction(Pair<World, StackReference> worldAndStack) {
        if (itemCondition == null || itemCondition.test(new Pair<>(worldAndStack.getLeft(), worldAndStack.getRight().get()))) {
            maybeAccept(itemAction, worldAndStack);
            receiveAnyAction();
        }
    }

    public void receiveAnyAction() {
        maybeAccept(action, this.entity);
    }

    private <T> void maybeAccept(Consumer<T> consumer, T t) {
        if (consumer != null && t != null) {
            consumer.accept(t);
        }
    }

    public static PowerTypeFactory getFactory() {
        return new PowerTypeFactory<>(
            Shappoli.identifier("receive_action"),
            new SerializableData()
                .add("action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("entity_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                .add("item_action", ApoliDataTypes.ITEM_ACTION, null)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                .validate(data -> MiscUtil.checkAtLeastOneFieldExists(data, "action", "bientity_action", "entity_action", "item_action"))
            ,
            data -> (type, entity) -> new ReceiveActionPowerType(type, entity,
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
