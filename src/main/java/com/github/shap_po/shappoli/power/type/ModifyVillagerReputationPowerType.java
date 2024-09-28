package com.github.shap_po.shappoli.power.type;

import com.github.shap_po.shappoli.Shappoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.type.ValueModifyingPowerType;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class ModifyVillagerReputationPowerType extends ValueModifyingPowerType {
    private final @Nullable Predicate<Pair<Entity, Entity>> bientityCondition;

    public ModifyVillagerReputationPowerType(
        Power power, LivingEntity entity,
        @Nullable Predicate<Pair<Entity, Entity>> bientityCondition
    ) {
        super(power, entity);
        this.bientityCondition = bientityCondition;
    }

    public boolean doesApply(Entity target) {
        return bientityCondition == null || bientityCondition.test(new Pair<>(entity, target));
    }

    public static PowerTypeFactory getFactory() {
        return new PowerTypeFactory<>(
            Shappoli.identifier("modify_villager_reputation"),
            new SerializableData()
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                .add("modifier", Modifier.DATA_TYPE, null)
                .add("modifiers", Modifier.LIST_TYPE, null)
            ,
            data -> (type, player) -> {
                ModifyVillagerReputationPowerType power = new ModifyVillagerReputationPowerType(type, player, data.get("bientity_condition"));
                data.ifPresent("modifier", power::addModifier);
                data.<List<Modifier>>ifPresent("modifiers", mods -> mods.forEach(power::addModifier));
                return power;
            }
        ).allowCondition();
    }
}
