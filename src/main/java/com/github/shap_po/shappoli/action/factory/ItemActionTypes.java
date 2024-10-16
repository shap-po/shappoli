package com.github.shap_po.shappoli.action.factory;

import com.github.shap_po.shappoli.action.type.meta.SendActionActionType;
import com.github.shap_po.shappoli.power.type.ReceiveActionPowerType;
import com.github.shap_po.shappoli.util.InventoryUtil;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.inventory.StackReference;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class ItemActionTypes {
    public static void register() {
        register(
            SendActionActionType.getFactory(
                worldAndStack -> InventoryUtil.getHolder(worldAndStack.getRight().get()),
                ReceiveActionPowerType::receiveItemAction,
                io.github.apace100.apoli.action.type.ItemActionTypes.ALIASES
            )
        );
    }

    private static void register(ActionTypeFactory<Pair<World, StackReference>> ActionTypeFactory) {
        Registry.register(ApoliRegistries.ITEM_ACTION, ActionTypeFactory.getSerializerId(), ActionTypeFactory);
    }
}
