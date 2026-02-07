package thaumcraft.common.items.wands.wandtypes;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.wands.IArcaneCraftingVisDiscountOwner;

import java.util.Map;
import java.util.stream.Collectors;

public class SceptreCastingItem extends WandCastingItem implements IArcaneCraftingVisDiscountOwner {

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
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 6.0, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    @Override
    public CentiVisList<Aspect> getAllCentiVisCapacity(ItemStack usingWand) {
        return new CentiVisList<>(super.getAllCentiVisCapacity(usingWand)
                .entrySet()
                .stream()
                .map(entry -> Map.entry(entry.getKey(),(int)(entry.getValue()*SCEPTRE_CENTIVIS_CAPACITY_MULTIPLIER)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }
}
