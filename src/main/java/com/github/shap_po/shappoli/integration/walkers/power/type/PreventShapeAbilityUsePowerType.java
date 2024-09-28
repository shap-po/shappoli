package com.github.shap_po.shappoli.integration.walkers.power.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class PreventShapeAbilityUsePowerType extends PowerType {
    private final @Nullable Predicate<Pair<Entity, Entity>> bientityCondition;

    public PreventShapeAbilityUsePowerType(
        Power type,
        LivingEntity entity,
        @Nullable Predicate<Pair<Entity, Entity>> bientityCondition
    ) {
        super(type, entity);
        this.bientityCondition = bientityCondition;
    }

    public boolean doesApply() {
        if (!(entity instanceof PlayerEntity player)) {
            return false;
        }
        return bientityCondition == null || bientityCondition.test(new Pair<>(entity, WalkersUtil.getShape(player)));
    }

    public static PowerTypeFactory getFactory() {
        return new PowerTypeFactory<>(
            Shappoli.identifier("prevent_shape_ability_use"),
            new SerializableData()
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
            ,
            data -> (type1, player) -> new PreventShapeAbilityUsePowerType(type1, player,
                data.get("bientity_condition")
            )
        ).allowCondition();
    }
}
