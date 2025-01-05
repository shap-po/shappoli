package com.github.shap_po.shappoli.integration.walkers.power.type;

import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.action.BiEntityAction;
import io.github.apace100.apoli.condition.BiEntityCondition;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ActionOnShapeAbilityUsePowerType extends PowerType {
    public static final TypedDataObjectFactory<ActionOnShapeAbilityUsePowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
        new SerializableData()
            .add("bientity_action", BiEntityAction.DATA_TYPE)
            .add("bientity_condition", BiEntityCondition.DATA_TYPE.optional(), Optional.empty()),
        (data, condition) -> new ActionOnShapeAbilityUsePowerType(
            data.get("bientity_action"),
            data.get("bientity_condition"),
            condition
        ),
        (powerType, serializableData) -> serializableData.instance()
            .set("bientity_action", powerType.biEntityAction)
            .set("bientity_condition", powerType.biEntityCondition)
    );

    private final BiEntityAction biEntityAction;
    private final Optional<BiEntityCondition> biEntityCondition;

    public ActionOnShapeAbilityUsePowerType(
        BiEntityAction biEntityAction,
        Optional<BiEntityCondition> biEntityCondition,
        Optional<EntityCondition> condition
    ) {
        super(condition);
        this.biEntityAction = biEntityAction;
        this.biEntityCondition = biEntityCondition;
    }

    public boolean doesApply() {
        if (!(getHolder() instanceof PlayerEntity player)) {
            return false;
        }
        return biEntityCondition.map(biEntityCondition -> biEntityCondition.test(player, WalkersUtil.getShape(player))).orElse(true);
    }

    public void apply() {
        if (!(getHolder() instanceof PlayerEntity player)) {
            return;
        }
        biEntityAction.execute(player, WalkersUtil.getShape(player));
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return ShappoliWalkersPowerTypes.ACTION_ON_SHAPE_ABILITY_USE;
    }
}
