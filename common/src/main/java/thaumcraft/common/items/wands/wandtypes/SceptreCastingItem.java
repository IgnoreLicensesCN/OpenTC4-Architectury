package thaumcraft.common.items.wands.wandtypes;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.wands.IArcaneCraftingVisDiscountOwner;

public class SceptreCastingItem extends WandCastingItem implements IArcaneCraftingVisDiscountOwner {
    public SceptreCastingItem() {
        super();
    }

    @Override
    public boolean canApplyFocus() {
        return false;
    }

    @Override
    public Component getComponentName() {
        return Component.translatable("item.Wand.sceptre.obj");
    }

    @Override
    public int getVisCapacityMultiplier() {
        return 150;
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 6.0, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }
}
