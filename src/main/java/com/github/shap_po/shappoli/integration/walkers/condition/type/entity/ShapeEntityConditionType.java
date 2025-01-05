package com.github.shap_po.shappoli.integration.walkers.condition.type.entity;

import com.github.shap_po.shappoli.integration.walkers.condition.type.ShappoliWalkersEntityConditionTypes;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.condition.BiEntityCondition;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.BiEntityConditionContext;
import io.github.apace100.apoli.condition.type.EntityConditionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class ShapeEntityConditionType extends EntityConditionType {
    public static final TypedDataObjectFactory<ShapeEntityConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("bientity_condition", BiEntityCondition.DATA_TYPE),
        data -> new ShapeEntityConditionType(data.get("bientity_condition")),
        (conditionType, serializableData) -> serializableData.instance()
            .set("bientity_condition", conditionType.biEntityCondition)
    );

    private final BiEntityCondition biEntityCondition;

    public ShapeEntityConditionType(BiEntityCondition biEntityCondition) {
        this.biEntityCondition = biEntityCondition;
    }

    @Override
    public boolean test(Entity entity) {
        if (!(entity instanceof PlayerEntity player)) {
            return false;
        }

        return biEntityCondition.test(new BiEntityConditionContext(player, WalkersUtil.getShape(player)));
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ShappoliWalkersEntityConditionTypes.SHAPE;
    }
}
