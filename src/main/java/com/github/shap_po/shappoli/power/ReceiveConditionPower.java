package com.github.shap_po.shappoli.power;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class ReceiveConditionPower extends Power {
    private final Predicate<Pair<Entity, Entity>> bientityCondition;
    private final Predicate<Entity> entityCondition;
    private final Predicate<Pair<World, ItemStack>> itemCondition;

    public ReceiveConditionPower(PowerType<?> type, LivingEntity entity, Predicate<Pair<Entity, Entity>> bientityCondition, Predicate<Entity> entityCondition, Predicate<Pair<World, ItemStack>> itemCondition) {
        super(type, entity);
        this.bientityCondition = bientityCondition;
        this.entityCondition = entityCondition;
        this.itemCondition = itemCondition;
    }

    public boolean receiveBientity(Pair<Entity, Entity> bientity) {
        return bientityCondition != null && bientityCondition.test(bientity);
    }

    public boolean receiveEntity(Entity entity) {
        return entityCondition != null && entityCondition.test(entity);
    }

    public boolean receiveItem(Pair<World, ItemStack> item) {
        return itemCondition != null && itemCondition.test(item);
    }

    public static PowerFactory createFactory() {
        return new PowerFactory<>(
            Shappoli.identifier("receive_condition"),
            new SerializableData()
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                .add("entity_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
            ,
            data -> (type, entity) -> new ReceiveConditionPower(
                type, entity,
                data.get("bientity_condition"),
                data.get("entity_condition"),
                data.get("item_condition")
            )
        ).allowCondition();
    }
}
