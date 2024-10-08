package com.github.shap_po.shappoli.integration.walkers.power.factory.action.bientity;

import com.github.shap_po.shappoli.Shappoli;
import com.github.shap_po.shappoli.integration.walkers.util.WalkersUtil;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.action.BiEntityActions;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

import java.util.function.Consumer;

public class SwitchShapeAction {
    public static void action(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {
        if (!(actorAndTarget.getLeft() instanceof ServerPlayerEntity player)) {
            return;
        }
        if (!(actorAndTarget.getRight() instanceof LivingEntity form)) {
            return;
        }

        boolean result = WalkersUtil.switchShape(player, form, data.getBoolean("ignore_nbt"));

        Consumer<Pair<Entity, Entity>> action = data.get("action_on_success");
        if (result && action != null) {
            action.accept(actorAndTarget);
        }
    }

    public static ActionFactory<Pair<Entity, Entity>> getFactory() {
        ActionFactory<Pair<Entity, Entity>> factory = new ActionFactory<>(
            Shappoli.identifier("switch_shape"),
            new SerializableData()
                .add("ignore_nbt", SerializableDataTypes.BOOLEAN, false)
                .add("action_on_success", ApoliDataTypes.BIENTITY_ACTION, null)
            ,
            SwitchShapeAction::action
        );

        BiEntityActions.ALIASES.addPathAlias("change_shape", factory.getSerializerId().getPath());
        BiEntityActions.ALIASES.addPathAlias("morph", factory.getSerializerId().getPath());
        return factory;
    }
}
