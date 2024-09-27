package com.github.shap_po.shappoli.power.factory.action;

import com.github.shap_po.shappoli.power.ReceiveActionPower;
import com.github.shap_po.shappoli.power.factory.action.meta.SendActionAction;
import com.github.shap_po.shappoli.util.InventoryUtil;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.inventory.StackReference;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class ItemActions {
    public static void register() {
        register(
            SendActionAction.getFactory(
                worldAndStack -> InventoryUtil.getHolder(worldAndStack.getRight().get()),
                ReceiveActionPower::receiveItemAction,
                io.github.apace100.apoli.action.factory.ItemActions.ALIASES
            )
        );
    }

    private static void register(ActionTypeFactory<Pair<World, StackReference>> ActionTypeFactory) {
        Registry.register(ApoliRegistries.ITEM_ACTION, ActionTypeFactory.getSerializerId(), ActionTypeFactory);
    }
}
