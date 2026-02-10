package thaumcraft.api.listeners.aspects.item.bonus.consts;

import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.lib.enchantment.ThaumcraftEnchantments;

import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class HelperConsts {

    public static final Map<Enchantment, AspectList<Aspect>> ENCHANTMENT_ASPECT_MAP = new ConcurrentHashMap<>();

    static {
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.AQUA_AFFINITY, AspectList.of(Aspects.WATER));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.BANE_OF_ARTHROPODS, AspectList.of(Aspects.BEAST));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.BLAST_PROTECTION, AspectList.of(Aspects.ARMOR));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.BLOCK_EFFICIENCY, AspectList.of(Aspects.TOOL));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.FALL_PROTECTION, AspectList.of(Aspects.FLIGHT));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.FIRE_ASPECT, AspectList.of(Aspects.FIRE));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.FIRE_PROTECTION, AspectList.of(Aspects.ARMOR));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.FLAMING_ARROWS, AspectList.of(Aspects.FIRE));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.BLOCK_FORTUNE, AspectList.of(Aspects.GREED));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.INFINITY_ARROWS, AspectList.of(Aspects.CRAFT));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.KNOCKBACK, AspectList.of(Aspects.AIR));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.MOB_LOOTING, AspectList.of(Aspects.GREED));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.POWER_ARROWS, AspectList.of(Aspects.WEAPON));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.PROJECTILE_PROTECTION, AspectList.of(Aspects.ARMOR));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.ALL_DAMAGE_PROTECTION, AspectList.of(Aspects.ARMOR));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.PUNCH_ARROWS, AspectList.of(Aspects.AIR));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.RESPIRATION, AspectList.of(Aspects.AIR));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.SHARPNESS, AspectList.of(Aspects.WEAPON));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.SILK_TOUCH, AspectList.of(Aspects.EXCHANGE));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.THORNS, AspectList.of(Aspects.WEAPON));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.SMITE, AspectList.of(Aspects.ENTROPY));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.UNBREAKING, AspectList.of(Aspects.EARTH));

        ENCHANTMENT_ASPECT_MAP.put(Enchantments.SOUL_SPEED, AspectList.of(Aspects.SOUL, Aspects.MOTION));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.DEPTH_STRIDER, AspectList.of(Aspects.MOTION, Aspects.WATER));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.FROST_WALKER, AspectList.of(Aspects.COLD));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.BINDING_CURSE, AspectList.of(Aspects.TRAP, Aspects.TRAP));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.VANISHING_CURSE, AspectList.of(Aspects.TRAP, Aspects.VOID));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.SWIFT_SNEAK, AspectList.of(Aspects.VOID, Aspects.MOTION));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.SWEEPING_EDGE, AspectList.of(Aspects.WEAPON, Aspects.AIR));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.LOYALTY, AspectList.of(Aspects.MIND));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.IMPALING, AspectList.of(Aspects.WEAPON, Aspects.MOTION));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.RIPTIDE, AspectList.of(Aspects.WATER, Aspects.MOTION));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.CHANNELING, AspectList.of(Aspects.WEATHER, Aspects.ENTROPY));//added,a source ofAspectVisList weather aspect
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.MULTISHOT,AspectList.of(Aspects.WEAPON, Aspects.ORDER));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.QUICK_CHARGE,AspectList.of(Aspects.MECHANISM, Aspects.MOTION));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.PIERCING,AspectList.of(Aspects.WEAPON, Aspects.ENTROPY));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.MENDING,AspectList.of(Aspects.MAGIC, Aspects.CRAFT));//added

        ENCHANTMENT_ASPECT_MAP.put(ThaumcraftEnchantments.HASTE, AspectList.of(Aspects.MOTION));
        ENCHANTMENT_ASPECT_MAP.put(ThaumcraftEnchantments.REPAIR, AspectList.of(Aspects.TOOL));

    }


    //call aspectValueByDurabilityMap.higherEntry(maxDamage).getValue();
    //then we have (for input maxDamage)(wood,stone,gold,iron are their Tiers#xxxxx#getUses()):
    //-inf,wood -> 1
    //if not match:
    //-inf,stone -> 2
    //if not match:
    //-inf,gold -> 2
    //if not match:
    //-inf,iron -> 3
    //if not match:
    //-inf,Integer.MAX_VALUE -> 3
    //we got Integer.MAX_VALUE so never mismatch
    //also easy to insert points
    public static final NavigableMap<Integer, Integer> aspectValueByDurabilityMap = new ConcurrentSkipListMap<>();

    static {
        aspectValueByDurabilityMap.put(Tiers.WOOD.getUses(), 1);
        aspectValueByDurabilityMap.put(Tiers.STONE.getUses(), 2);
        aspectValueByDurabilityMap.put(Tiers.GOLD.getUses(), 2);
        aspectValueByDurabilityMap.put(Tiers.IRON.getUses(), 3);
        aspectValueByDurabilityMap.put(Integer.MAX_VALUE, 4);
    }
}
