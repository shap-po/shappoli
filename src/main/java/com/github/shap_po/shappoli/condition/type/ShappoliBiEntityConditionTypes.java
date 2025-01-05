package com.github.shap_po.shappoli.condition.type;

import com.github.shap_po.shappoli.condition.type.bientity.SendConditionBiEntityConditionType;
import com.github.shap_po.shappoli.condition.type.meta.SendConditionMetaConditionType;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.type.BiEntityConditionTypes;

public class ShappoliBiEntityConditionTypes {
    public static final ConditionConfiguration<SendConditionBiEntityConditionType> SEND_CONDITION = BiEntityConditionTypes.register(SendConditionMetaConditionType.createConfiguration(SendConditionBiEntityConditionType::new));

    public static void register() {
    }
}
