package thaumcraft.common.items.wands.wandtypes;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.aspects.UnmodifiableCentiVisList;
import thaumcraft.api.wands.IArcaneCraftingVisDiscountOwnerItem;

public class SceptreCastingItem extends WandCastingItem implements IArcaneCraftingVisDiscountOwnerItem {

    public static final float SCEPTRE_CENTIVIS_CAPACITY_MULTIPLIER = 1.5f;

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
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (equipmentSlot.equals(EquipmentSlot.OFFHAND)) {
            return ImmutableMultimap.of();
        }
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 6.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -3, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    @Override
    public CentiVisList<Aspect> getAllCentiVisCapacity(ItemStack usingWand) {
        var originalResult = super.getAllCentiVisCapacity(usingWand);
        Object2IntLinkedOpenHashMap<Aspect> centiVisCapacity = new Object2IntLinkedOpenHashMap<>(originalResult.size(),1);
        originalResult.forEach((key, value) -> centiVisCapacity.put(
                key,
                (int) (value*SCEPTRE_CENTIVIS_CAPACITY_MULTIPLIER)
        ));
        return UnmodifiableCentiVisList.viewOf(centiVisCapacity);
    }
}
