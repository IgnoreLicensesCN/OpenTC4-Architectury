package thaumcraft.common.items.golem.upgrade.decoration;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemDecorationItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.IGolemCauseDamageListenerItem;
import thaumcraft.common.lib.resourcelocations.DecorationOccupiedTypeResourceLocation;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.*;
import java.util.function.Supplier;

import static thaumcraft.common.items.golem.upgrade.abstracts.IGolemDecorationItem.DefaultDecorationTypes.*;

//ItemGolemDecoration:5
public class GolemVisorItem extends Item implements IGolemDecorationItem, IGolemCauseDamageListenerItem {
    public GolemVisorItem() {
        this(new Properties());
    }
    public GolemVisorItem(Properties properties) {
        super(properties);
    }

    public static final Set<DecorationOccupiedTypeResourceLocation> OCCUPIED_TYPES = Set.of(EYE_LEFT,EYE_RIGHT,FACE);
    @Override
    public @Unmodifiable Set<DecorationOccupiedTypeResourceLocation> getOccupiedTypes(BaseGolemEntity golem, ItemStack stack) {
        return OCCUPIED_TYPES;
    }



    public static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("7fcaa35b-4616-4125-8efc-9f789d84c15c");

    public static final Map<Attribute, Collection<Supplier<AttributeModifier>>> ATTRIBUTES = Collections.singletonMap(
            Attributes.ARMOR,
            Collections.singletonList(
                    () -> new AttributeModifier(
                            ARMOR_MODIFIER_UUID,
                            "Golem visor armor",
                            1,
                            AttributeModifier.Operation.ADDITION
                    )
            )
    );

    @Override
    public Map<Attribute, Collection<Supplier<AttributeModifier>>> getAttributes(ItemStack upgradeStack, Mob golem) {
        return ATTRIBUTES;
    }

    @Override
    public void onGolemCausedDamage(BaseGolemEntity golem, ItemStack stack, LivingEntity victim, DamageSource damageSource, float f) {
        EntityUtils.setRecentlyHit(victim,100);
    }
}
