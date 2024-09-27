package com.github.shap_po.shappoli.power.factory.action;

import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

public class BlockActions {
    public static void register() {
    }

    private static void register(ActionTypeFactory<Triple<World, BlockPos, Direction>> ActionTypeFactory) {
        Registry.register(ApoliRegistries.BLOCK_ACTION, ActionTypeFactory.getSerializerId(), ActionTypeFactory);
    }
}
