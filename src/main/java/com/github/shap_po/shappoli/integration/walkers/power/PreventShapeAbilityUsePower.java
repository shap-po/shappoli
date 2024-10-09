package com.github.shap_po.shappoli.integration.walkers.power;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.registry.ShappoliWalkersRegistries;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import tocraft.walkers.ability.AbilityRegistry;
import tocraft.walkers.ability.ShapeAbility;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class PreventShapeAbilityUsePower extends Power {
    private final Predicate<Pair<Entity, Entity>> bientityCondition;
    private final List<? extends Class<? extends ShapeAbility<?>>> abilities;

    public PreventShapeAbilityUsePower(
        PowerType<?> type,
        LivingEntity entity,
        Predicate<Pair<Entity, Entity>> bientityCondition,
        List<? extends Class<? extends ShapeAbility<?>>> abilities
    ) {
        super(type, entity);
        this.bientityCondition = bientityCondition;
        this.abilities = abilities;
    }

    public boolean doesApply() {
        if (!(entity instanceof PlayerEntity player)) {
            return false;
        }

        LivingEntity shape = WalkersUtil.getShape(player);
        if (bientityCondition != null && bientityCondition.test(new Pair<>(entity, shape))) {
            return true;
        }

        ShapeAbility<?> shapeAbility = AbilityRegistry.get(shape);
        if (shapeAbility == null) {
            return false;
        }

        return abilities.stream().anyMatch(a -> a.isInstance(shapeAbility));
    }

    public static PowerFactory createFactory() {
        return new PowerFactory<>(
            Shappoli.identifier("prevent_shape_ability_use"),
            new SerializableData()
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                .add("ability", SerializableDataTypes.IDENTIFIER, null)
                .add("abilities", SerializableDataTypes.IDENTIFIERS, null)
            ,
            data -> (type, player) -> new PreventShapeAbilityUsePower(type, player,
                data.get("bientity_condition"),
                MiscUtil.<Identifier>listFromData(data, "ability", "abilities")
                    .stream()
                    .map(ShappoliWalkersRegistries.SHAPE_ABILITY_TYPE::get)
                    .filter(Objects::nonNull)
                    .toList()
            )
        ).allowCondition();
    }
}
