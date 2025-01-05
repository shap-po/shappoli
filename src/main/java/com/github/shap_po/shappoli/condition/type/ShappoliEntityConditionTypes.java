package com.github.shap_po.shappoli.condition.type;

import com.github.shap_po.shappoli.condition.type.entity.SendConditionEntityConditionType;
import com.github.shap_po.shappoli.condition.type.meta.SendConditionMetaConditionType;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.type.EntityConditionTypes;

public class ShappoliEntityConditionTypes {
    public static final ConditionConfiguration<SendConditionEntityConditionType> SEND_CONDITION = EntityConditionTypes.register(SendConditionMetaConditionType.createConfiguration(SendConditionEntityConditionType::new));

    public static void register() {
    }
}
