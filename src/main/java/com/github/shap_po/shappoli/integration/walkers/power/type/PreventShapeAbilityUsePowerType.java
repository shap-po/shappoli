package com.github.shap_po.shappoli.integration.walkers.power.type;

import com.github.shap_po.shappoli.integration.walkers.registry.ShappoliWalkersShapeAbilityClassRegistry;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.condition.BiEntityCondition;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import tocraft.walkers.ability.AbilityRegistry;
import tocraft.walkers.ability.ShapeAbility;

import java.util.List;
import java.util.Optional;

public class PreventShapeAbilityUsePowerType extends PowerType {
    public static final TypedDataObjectFactory<PreventShapeAbilityUsePowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
        new SerializableData()
            .add("bientity_condition", BiEntityCondition.DATA_TYPE.optional(), Optional.empty())
            .add("ability", ShappoliWalkersShapeAbilityClassRegistry.DATA_TYPE, null)
            .add("abilities", ShappoliWalkersShapeAbilityClassRegistry.DATA_TYPE.list(), null),
        (data, condition) -> new PreventShapeAbilityUsePowerType(
            data.get("bientity_condition"),
            MiscUtil.listFromData(data, "ability", "abilities"),
            condition
        ),
        (powerType, serializableData) -> serializableData.instance()
            .set("bientity_condition", powerType.bientityCondition)
            .set("abilities", powerType.abilities)
    );

    private final Optional<BiEntityCondition> bientityCondition;
    private final List<? extends Class<? extends ShapeAbility<?>>> abilities;

    public PreventShapeAbilityUsePowerType(
        Optional<BiEntityCondition> biEntityCondition,
        List<? extends Class<? extends ShapeAbility<?>>> abilities,
        Optional<EntityCondition> condition
    ) {
        super(condition);
        this.bientityCondition = biEntityCondition;
        this.abilities = abilities;
    }

    public boolean doesApply() {
        if (!(getHolder() instanceof PlayerEntity player)) {
            return false;
        }

        LivingEntity shape = WalkersUtil.getShape(player);
        if (!bientityCondition.map(bientityCondition -> bientityCondition.test(player, shape)).orElse(false)) {
            return true;
        }

        ShapeAbility<?> shapeAbility = AbilityRegistry.get(shape);
        if (shapeAbility == null) {
            return false;
        }

        return abilities.stream().anyMatch(a -> a.isInstance(shapeAbility));
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return ShappoliWalkersPowerTypes.PREVENT_SHAPE_ABILITY_USE;
    }
}
