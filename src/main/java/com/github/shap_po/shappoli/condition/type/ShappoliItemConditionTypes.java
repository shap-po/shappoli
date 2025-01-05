package com.github.shap_po.shappoli.condition.type;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.condition.type.item.BlockItemConditionType;
import com.github.shap_po.shappoli.condition.type.item.HolderItemConditionType;
import com.github.shap_po.shappoli.condition.type.item.SendConditionItemConditionType;
import com.github.shap_po.shappoli.condition.type.meta.SendConditionMetaConditionType;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.type.ItemConditionTypes;

public class ShappoliItemConditionTypes {
    public static final ConditionConfiguration<SendConditionItemConditionType> SEND_CONDITION = ItemConditionTypes.register(SendConditionMetaConditionType.createConfiguration(SendConditionItemConditionType::new));

    public static final ConditionConfiguration<BlockItemConditionType> BLOCK = ItemConditionTypes.register(ConditionConfiguration.simple(Shappoli.identifier("block"), BlockItemConditionType::new));
    public static final ConditionConfiguration<HolderItemConditionType> HOLDER = ItemConditionTypes.register(ConditionConfiguration.of(Shappoli.identifier("holder"), HolderItemConditionType.DATA_FACTORY));

    public static void register() {
        ItemConditionTypes.ALIASES.addPathAlias("is_block", BLOCK.id().getPath());
    }
}
