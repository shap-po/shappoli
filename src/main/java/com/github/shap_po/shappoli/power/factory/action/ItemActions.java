package com.github.shap_po.shappoli.power.factory.action;

import com.github.shap_po.shappoli.power.ActionOnEventReceivePower;
import com.github.shap_po.shappoli.power.factory.action.meta.SendEventAction;
import io.github.apace100.apoli.access.EntityLinkedItemStack;
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
                worldAndStack -> ((EntityLinkedItemStack) (Object) worldAndStack.getRight().get()).apoli$getEntity(),
                ActionOnEventReceivePower::receiveItemEvent
            )
        );
        SendEventAction.addAlias(io.github.apace100.apoli.power.factory.action.ItemActions.ALIASES);
    }

    private static void register(ActionFactory<Pair<World, StackReference>> actionFactory) {
        Registry.register(ApoliRegistries.ITEM_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
