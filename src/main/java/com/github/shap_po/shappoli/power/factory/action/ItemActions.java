package com.github.shap_po.shappoli.power.factory.action;

import com.github.shap_po.shappoli.power.ActionOnEventReceivePower;
import com.github.shap_po.shappoli.power.factory.action.meta.SendEventAction;
import com.github.shap_po.shappoli.util.InventoryUtil;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.inventory.StackReference;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class ItemActions {
    public static void register() {
        register(
            SendEventAction.getFactory(
                worldAndStack -> InventoryUtil.getHolder(worldAndStack.getRight().get()),
                ActionOnEventReceivePower::receiveItemEvent
            )
        );
        SendEventAction.addAlias(io.github.apace100.apoli.power.factory.action.ItemActions.ALIASES);
    }

    private static void register(ActionFactory<Pair<World, StackReference>> actionFactory) {
        Registry.register(ApoliRegistries.ITEM_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
