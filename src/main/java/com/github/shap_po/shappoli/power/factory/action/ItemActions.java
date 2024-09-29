package com.github.shap_po.shappoli.power.factory.action;

import com.github.shap_po.shappoli.power.ReceiveActionPower;
import com.github.shap_po.shappoli.power.factory.action.meta.SendActionAction;
import com.github.shap_po.shappoli.util.InventoryUtil;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class ItemActions {
    public static void register() {
        register(
            SendActionAction.getFactory(
                worldAndStack -> InventoryUtil.getHolder(worldAndStack.getRight()),
                ReceiveActionPower::receiveItemAction
            )
        );
    }

    private static void register(ActionFactory<Pair<World, ItemStack>> actionFactory) {
        Registry.register(ApoliRegistries.ITEM_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
