package thaumcraft.common.items.golem.upgrade.primal;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemAbilitiesData;
import thaumcraft.common.entities.golems.TravelingTrunkEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemCarryItemPowerProviderItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemStrengthModifierItem;

import java.util.*;
import java.util.function.Supplier;

//GolemUpgrade:1
public class EarthGolemUpgradeItem extends Item
        implements
        TravelingTrunkEntity.ITravelingTrunkUpgradeItem,
        IGolemStrengthModifierItem,
        IGolemCarryItemPowerProviderItem
{
    public EarthGolemUpgradeItem(Properties properties) {
        super(properties);
    }
    public EarthGolemUpgradeItem() {
        this(new Properties().rarity(Rarity.UNCOMMON));
    }


    public static final UUID DAMAGE_MODIFIER_UUID = UUID.fromString("c3678482-5780-4d40-8baf-555ac4005657");

    public static final Int2ObjectMap<Map<Attribute, Collection<Supplier<AttributeModifier>>>> ATTRIBUTES = new Int2ObjectArrayMap<>();

    public static Map<Attribute, Collection<Supplier<AttributeModifier>>> getAttributeModifiersCached(int damage) {
        var result = ATTRIBUTES.get(damage);
        if (result == null) {
            result = Collections.singletonMap(
                    Attributes.ATTACK_DAMAGE,
                    Collections.singletonList(
                            () -> new AttributeModifier(
                                    DAMAGE_MODIFIER_UUID,
                                    "Golem earth upgrade damage",
                                    damage,
                                    AttributeModifier.Operation.ADDITION
                            )
                    )
            );
            ATTRIBUTES.put(damage, result);
        }
        return result;
    }

    @Override
    public Map<Attribute, Collection<Supplier<AttributeModifier>>> getAttributes(ItemStack upgradeStack, Mob golem) {
        Map<Attribute, Collection<Supplier<AttributeModifier>>> result = null;
        var attrs = golem.getAttributes();

        var damageInstance = attrs.getInstance(Attributes.ATTACK_DAMAGE);
        if (damageInstance != null){
            var modifier = damageInstance.getModifier(DAMAGE_MODIFIER_UUID);
            if (modifier != null){
                result = getAttributeModifiersCached((int) (modifier.getAmount() + 1));
            }
        }
        if (result == null){
            return getAttributeModifiersCached(1);
        }
        return result;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(Component.translatable("golem_upgrade.earth.desc"));
    }

    @Override
    public int travelingTrunkUpgrade$inventorySizeAddition(TravelingTrunkEntity trunk, ItemStack upgradeStack) {
        return 9;
    }

    @Override
    public float getStrengthModified(BaseGolemEntity golem, ItemStack upgradeStack, float strength, float defaultStrength) {
        return strength+1;
    }

    @Override
    public float modifyCarryCount(BaseGolemEntity golem, ItemStack upgradeStack, float carry, float defaultCarry) {
        return carry + Math.min(16, Math.max(4, defaultCarry));
    }

    @Override
    public void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, GolemAbilitiesData abilitiesData) {
        IGolemStrengthModifierItem.super.modifyGolemAbility(golem, upgradeStack, abilitiesData);
        IGolemCarryItemPowerProviderItem.super.modifyGolemAbility(golem, upgradeStack, abilitiesData);
    }
}
