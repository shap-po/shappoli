package com.github.shap_po.shappoli.integration.walkers.ability.type;

import com.github.shap_po.shappoli.util.MiscUtil;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.NotNull;
import tocraft.walkers.ability.impl.generic.ThrowPotionsAbility;

import java.util.List;

public class ThrowPotionsShapeAbilityType extends ShapeAbilityType {
    public static final TypedDataObjectFactory<ThrowPotionsShapeAbilityType> DATA_FACTORY = TypedDataObjectFactory.simple(
        new SerializableData()
            .add("potion", SerializableDataType.registryEntry(Registries.POTION), null)
            .add("potions", SerializableDataType.registryEntry(Registries.POTION).list(), null)
            .validate(data -> MiscUtil.checkAtLeastOneFieldExists(data, "potion", "potions")),
        data -> new ThrowPotionsShapeAbilityType(MiscUtil.listFromData(data, "potion", "potions")),
        (abilityType, serializableData) -> serializableData.instance()
            .set("potions", abilityType.potions)
    );

    private final List<RegistryEntry<Potion>> potions;
    private final ThrowPotionsAbility<LivingEntity> ability;

    public ThrowPotionsShapeAbilityType(List<RegistryEntry<Potion>> potions) {
        this.potions = potions;
        this.ability = new ThrowPotionsAbility<>(potions);
    }

    @Override
    protected void execute(PlayerEntity player, LivingEntity shape) {
        ability.onUse(player, shape, player.getWorld());
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ShapeAbilityTypes.THROW_POTIONS;
    }
}
