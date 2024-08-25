package com.github.shap_po.shappoli.power;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.ValueModifyingPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.function.Predicate;

public class ModifyVillagerReputation extends ValueModifyingPower {
    private final Predicate<Pair<Entity, Entity>> bientityCondition;

    public ModifyVillagerReputation(PowerType<?> type, LivingEntity entity, Predicate<Pair<Entity, Entity>> bientityCondition) {
        super(type, entity);
        this.bientityCondition = bientityCondition;
    }

    public boolean doesApply(Entity target) {
        return bientityCondition == null || bientityCondition.test(new Pair<>(entity, target));
    }

    public static PowerFactory createFactory() {
        return new PowerFactory<>(
            Shappoli.identifier("modify_villager_reputation"),
            new SerializableData()
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                .add("modifier", Modifier.DATA_TYPE, null)
                .add("modifiers", Modifier.LIST_TYPE, null)
            ,
            data -> (type, player) -> {
                ModifyVillagerReputation power = new ModifyVillagerReputation(type, player, data.get("bientity_condition"));
                data.ifPresent("modifier", power::addModifier);
                data.<List<Modifier>>ifPresent("modifiers", mods -> mods.forEach(power::addModifier));
                return power;
            }
        ).allowCondition();
    }
}
