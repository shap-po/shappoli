package com.github.shap_po.shappoli.action.type.entity;

import com.github.shap_po.shappoli.action.type.ShappoliEntityActionTypes;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.BiEntityAction;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class SelfBientityActionEntityActionType extends EntityActionType {
    public static final TypedDataObjectFactory<SelfBientityActionEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("action", BiEntityAction.DATA_TYPE),
        data -> new SelfBientityActionEntityActionType(data.get("action")),
        (actionType, serializableData) -> serializableData.instance()
            .set("action", actionType.biEntityAction)
    );

    private final BiEntityAction biEntityAction;

    public SelfBientityActionEntityActionType(BiEntityAction biEntityAction) {
        this.biEntityAction = biEntityAction;
    }

    @Override
    public void execute(Entity entity) {
        biEntityAction.execute(entity, entity);
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShappoliEntityActionTypes.SELF_BIENTITY_ACTION;
    }
}
