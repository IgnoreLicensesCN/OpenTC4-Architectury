package thaumcraft.common.items.baubles.belt;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.wispforest.accessories.api.AccessoryItem;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import thaumcraft.common.runicshield.IAugmentationRunicShieldProviderItem;

import java.util.UUID;

import static thaumcraft.common.lib.utils.EntityUtils.ThaumcraftAttributeCategoryInstances.FLYING_SPEED_CONTROL_OVERRIDE;
import static thaumcraft.common.lib.utils.EntityUtils.ThaumcraftAttributeCategoryInstances.HARNESS_FUEL_DURATION_ADD_PERCENT;

public class HoverGirdleItem extends AccessoryItem implements IAugmentationRunicShieldProviderItem {
    public HoverGirdleItem(Properties properties) {
        super(properties);
    }
    public HoverGirdleItem() {
        this(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    public static final UUID HOVER_GIRDLE_FUEL_DURATION_UUID = UUID.fromString("97962631-69c9-409b-b3af-85cf8ad73c2f");
    public static final UUID HOVER_GIRDLE_FLYING_CONTROL_OVERRIDE_UUID = UUID.fromString("97962631-69c9-409b-b3af-85cf8ad73c2f");

    @Override
    public Multimap<Attribute, AttributeModifier> getModifiers(ItemStack stack, SlotReference reference, UUID uuid) {
        Multimap<Attribute, AttributeModifier> modifiers = ArrayListMultimap.create();
        modifiers.put(
                HARNESS_FUEL_DURATION_ADD_PERCENT(),
                new AttributeModifier(
                        HOVER_GIRDLE_FUEL_DURATION_UUID,
                        "Hover girdle fuel duration",
                        -0.2,
                        AttributeModifier.Operation.ADDITION
                )
        );
        modifiers.put(
                FLYING_SPEED_CONTROL_OVERRIDE(),
                new AttributeModifier(
                        HOVER_GIRDLE_FLYING_CONTROL_OVERRIDE_UUID,
                        "Hover girdle flying speed control override",
                        0.21,
                        AttributeModifier.Operation.ADDITION
                )
        );
        return modifiers;
    }

    @Override
    public void tick(ItemStack stack, SlotReference reference) {
        super.tick(stack, reference);
        var user = reference.entity();
        if (user != null && user.fallDistance > 0.0F) {
            user.fallDistance -= 0.33F;
        }
    }
}
