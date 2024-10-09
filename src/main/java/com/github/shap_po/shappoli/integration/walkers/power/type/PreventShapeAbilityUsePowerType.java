package com.github.shap_po.shappoli.integration.walkers.power.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.registry.ShappoliWalkersRegistries;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerTypeFactory;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;
import tocraft.walkers.ability.AbilityRegistry;
import tocraft.walkers.ability.ShapeAbility;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class PreventShapeAbilityUsePowerType extends PowerType {
    private final @Nullable Predicate<Pair<Entity, Entity>> bientityCondition;
    private final List<? extends Class<? extends ShapeAbility<?>>> abilities;

    public PreventShapeAbilityUsePowerType(
        Power type,
        LivingEntity entity,
        @Nullable Predicate<Pair<Entity, Entity>> bientityCondition,
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

    public static PowerTypeFactory getFactory() {
        return new PowerTypeFactory<>(
            Shappoli.identifier("prevent_shape_ability_use"),
            new SerializableData()
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                .add("ability", SerializableDataTypes.IDENTIFIER, null)
                .add("abilities", SerializableDataTypes.IDENTIFIERS, null)
            ,
            data -> (type, player) -> new PreventShapeAbilityUsePowerType(type, player,
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
