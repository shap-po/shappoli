package com.github.shap_po.shappoli.power.type;

import io.github.apace100.apoli.condition.BiEntityCondition;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.condition.context.BiEntityConditionContext;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.ValueModifyingPowerType;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ModifyVillagerReputationPowerType extends ValueModifyingPowerType {
    public static final TypedDataObjectFactory<ModifyVillagerReputationPowerType> DATA_FACTORY = createConditionedModifyingRequiredDataFactory(
        new SerializableData()
            .add("bientity_condition", BiEntityCondition.DATA_TYPE.optional(), Optional.empty())
            .add("modifier", Modifier.DATA_TYPE, null)
            .add("modifiers", Modifier.LIST_TYPE, null),
        (data, modifiers, condition) -> new ModifyVillagerReputationPowerType(
            data.get("bientity_condition"),
            modifiers,
            condition
        ),
        (powerType, serializableData) -> serializableData.instance()
            .set("bientity_condition", powerType.biEntityCondition)

    );

    private final Optional<BiEntityCondition> biEntityCondition;

    public ModifyVillagerReputationPowerType(Optional<BiEntityCondition> biEntityCondition, List<Modifier> modifiers, Optional<EntityCondition> condition) {
        super(modifiers, condition);
        this.biEntityCondition = biEntityCondition;
    }

    public boolean doesApply(Entity entity, Entity target) {
        return biEntityCondition.map(biEntityCondition -> biEntityCondition.test(new BiEntityConditionContext(entity, target))).orElse(true);
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return ShappoliPowerTypes.MODIFY_VILLAGER_REPUTATION;
    }
}
