package thaumcraft.common.items.golem.upgrade.decoration;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemDecorationItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemSpeedMultiplierModifierItem;
import thaumcraft.common.lib.resourcelocations.DecorationOccupiedTypeResourceLocation;

import java.util.*;
import java.util.function.Supplier;

import static thaumcraft.common.items.golem.upgrade.abstracts.IGolemDecorationItem.DefaultDecorationTypes.*;

//ItemGolemDecoration:6
public class GolemIronPlatingItem extends Item implements IGolemDecorationItem, IGolemSpeedMultiplierModifierItem {
    public GolemIronPlatingItem() {
        this(new Properties());
    }
    public GolemIronPlatingItem(Properties properties) {
        super(properties);
    }

    public static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("588e1298-71ea-4adf-b41d-0e58a7b4e770");

    public static final Map<Attribute, Collection<Supplier<AttributeModifier>>> ATTRIBUTES = Collections.singletonMap(
            Attributes.ARMOR,
            Collections.singletonList(
                    () -> new AttributeModifier(
                            ARMOR_MODIFIER_UUID,
                            "Golem iron plating armor",
                            4,
                            AttributeModifier.Operation.ADDITION
                    )
            )
    );

    @Override
    public Map<Attribute, Collection<Supplier<AttributeModifier>>> getAttributes(ItemStack upgradeStack, Mob golem) {
        return ATTRIBUTES;
    }
    public static final Set<DecorationOccupiedTypeResourceLocation> OCCUPIED_TYPES = Set.of(NECKLACE,CHESTPLATE);
    @Override
    public @Unmodifiable Set<DecorationOccupiedTypeResourceLocation> getOccupiedTypes(BaseGolemEntity golem, ItemStack stack) {
        return OCCUPIED_TYPES;
    }

    @Override
    public float modifySpeedMultiplier(BaseGolemEntity golem, ItemStack upgradeStack, float speedMultiplier, float baseSpeedMultiplier) {
        return speedMultiplier-0.12F;
    }
}
