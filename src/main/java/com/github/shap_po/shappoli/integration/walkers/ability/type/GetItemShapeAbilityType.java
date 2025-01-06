package com.github.shap_po.shappoli.integration.walkers.ability.type;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import tocraft.walkers.ability.impl.generic.GetItemAbility;

public class GetItemShapeAbilityType extends ShapeAbilityType {
    public static final TypedDataObjectFactory<GetItemShapeAbilityType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("item", SerializableDataTypes.ITEM_STACK),
        data -> new GetItemShapeAbilityType(data.get("item")),
        (actionType, serializableData) -> serializableData.instance()
            .set("item", actionType.itemStack)
    );
    private final ItemStack itemStack;
    private final GetItemAbility<LivingEntity> ability;

    public GetItemShapeAbilityType(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.ability = new GetItemAbility<>(itemStack);
    }

    @Override
    protected void execute(PlayerEntity player, LivingEntity shape) {
        ability.onUse(player, shape, player.getWorld());
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShapeAbilityTypes.GET_ITEM;
    }

}
