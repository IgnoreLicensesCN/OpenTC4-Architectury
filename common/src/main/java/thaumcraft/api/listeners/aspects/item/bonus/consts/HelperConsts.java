package thaumcraft.api.listeners.aspects.item.bonus.consts;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.UnmodifiableAspectList;
import thaumcraft.common.lib.enchantment.ThaumcraftEnchantments;

import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class HelperConsts {

    public static final Map<Enchantment, UnmodifiableAspectList<Aspect>> ENCHANTMENT_ASPECT_MAP = new ConcurrentHashMap<>();

    static {
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.AQUA_AFFINITY, UnmodifiableAspectList.of(Aspects.WATER));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.BANE_OF_ARTHROPODS, UnmodifiableAspectList.of(Aspects.BEAST));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.BLAST_PROTECTION, UnmodifiableAspectList.of(Aspects.ARMOR));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.BLOCK_EFFICIENCY, UnmodifiableAspectList.of(Aspects.TOOL));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.FALL_PROTECTION, UnmodifiableAspectList.of(Aspects.FLIGHT));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.FIRE_ASPECT, UnmodifiableAspectList.of(Aspects.FIRE));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.FIRE_PROTECTION, UnmodifiableAspectList.of(Aspects.ARMOR));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.FLAMING_ARROWS, UnmodifiableAspectList.of(Aspects.FIRE));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.BLOCK_FORTUNE, UnmodifiableAspectList.of(Aspects.GREED));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.INFINITY_ARROWS, UnmodifiableAspectList.of(Aspects.CRAFT));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.KNOCKBACK, UnmodifiableAspectList.of(Aspects.AIR));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.MOB_LOOTING, UnmodifiableAspectList.of(Aspects.GREED));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.POWER_ARROWS, UnmodifiableAspectList.of(Aspects.WEAPON));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.PROJECTILE_PROTECTION, UnmodifiableAspectList.of(Aspects.ARMOR));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.ALL_DAMAGE_PROTECTION, UnmodifiableAspectList.of(Aspects.ARMOR));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.PUNCH_ARROWS, UnmodifiableAspectList.of(Aspects.AIR));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.RESPIRATION, UnmodifiableAspectList.of(Aspects.AIR));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.SHARPNESS, UnmodifiableAspectList.of(Aspects.WEAPON));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.SILK_TOUCH, UnmodifiableAspectList.of(Aspects.EXCHANGE));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.THORNS, UnmodifiableAspectList.of(Aspects.WEAPON));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.SMITE, UnmodifiableAspectList.of(Aspects.ENTROPY));
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.UNBREAKING, UnmodifiableAspectList.of(Aspects.EARTH));

        ENCHANTMENT_ASPECT_MAP.put(Enchantments.SOUL_SPEED, UnmodifiableAspectList.of(Aspects.SOUL, Aspects.MOTION));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.DEPTH_STRIDER, UnmodifiableAspectList.of(Aspects.MOTION, Aspects.WATER));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.FROST_WALKER, UnmodifiableAspectList.of(Aspects.COLD));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.BINDING_CURSE, UnmodifiableAspectList.of(Aspects.TRAP, Aspects.TRAP));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.VANISHING_CURSE, UnmodifiableAspectList.of(Aspects.TRAP, Aspects.VOID));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.SWIFT_SNEAK, UnmodifiableAspectList.of(Aspects.VOID, Aspects.MOTION));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.SWEEPING_EDGE, UnmodifiableAspectList.of(Aspects.WEAPON, Aspects.AIR));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.LOYALTY, UnmodifiableAspectList.of(Aspects.MIND));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.IMPALING, UnmodifiableAspectList.of(Aspects.WEAPON, Aspects.MOTION));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.RIPTIDE, UnmodifiableAspectList.of(Aspects.WATER, Aspects.MOTION));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.CHANNELING, UnmodifiableAspectList.of(Aspects.WEATHER, Aspects.ENTROPY));//added,a source ofAspectVisList weather aspect
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.MULTISHOT,UnmodifiableAspectList.of(Aspects.WEAPON, Aspects.ORDER));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.QUICK_CHARGE,UnmodifiableAspectList.of(Aspects.MECHANISM, Aspects.MOTION));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.PIERCING,UnmodifiableAspectList.of(Aspects.WEAPON, Aspects.ENTROPY));//added
        ENCHANTMENT_ASPECT_MAP.put(Enchantments.MENDING,UnmodifiableAspectList.of(Aspects.MAGIC, Aspects.CRAFT));//added

        ENCHANTMENT_ASPECT_MAP.put(ThaumcraftEnchantments.HASTE, UnmodifiableAspectList.of(Aspects.MOTION));
        ENCHANTMENT_ASPECT_MAP.put(ThaumcraftEnchantments.REPAIR, UnmodifiableAspectList.of(Aspects.TOOL));

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
    
    public static final Map<MobEffect, UnmodifiableAspectList<Aspect>> ASPECTS_FOR_EFFECT_PER_LEVEL = new ConcurrentHashMap<>();
    static {
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.BLINDNESS,UnmodifiableAspectList.of(Aspects.DARKNESS,2));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.CONFUSION,UnmodifiableAspectList.of(Aspects.ELDRITCH, 3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.DAMAGE_BOOST,UnmodifiableAspectList.of(Aspects.WEAPON, 3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.DIG_SLOWDOWN,UnmodifiableAspectList.of(Aspects.TRAP, 3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.DIG_SPEED,UnmodifiableAspectList.of(Aspects.TOOL, 3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.FIRE_RESISTANCE,UnmodifiableAspectList.of(Aspects.ARMOR, 1,Aspects.FIRE,2));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.HARM,UnmodifiableAspectList.of(Aspects.DEATH, 3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.HEAL,UnmodifiableAspectList.of(Aspects.HEAL, 3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.HUNGER,UnmodifiableAspectList.of(Aspects.DEATH, 3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.INVISIBILITY,UnmodifiableAspectList.of(Aspects.SENSES, 3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.JUMP,UnmodifiableAspectList.of(Aspects.FLESH, 3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.MOVEMENT_SLOWDOWN,UnmodifiableAspectList.of(Aspects.TRAP, 3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.MOVEMENT_SPEED,UnmodifiableAspectList.of(Aspects.MOTION, 3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.NIGHT_VISION,UnmodifiableAspectList.of(Aspects.SENSES, 3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.POISON,UnmodifiableAspectList.of(Aspects.POISON, 3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.REGENERATION,UnmodifiableAspectList.of(Aspects.HEAL, 3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.DAMAGE_RESISTANCE,UnmodifiableAspectList.of(Aspects.ARMOR, 3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.WATER_BREATHING,UnmodifiableAspectList.of(Aspects.AIR, 3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.WEAKNESS,UnmodifiableAspectList.of(Aspects.DEATH, 3));
        //added
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.WITHER,UnmodifiableAspectList.of(Aspects.DEATH, 3,Aspects.UNDEAD,3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.HEALTH_BOOST,UnmodifiableAspectList.of(Aspects.LIFE,6));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.ABSORPTION,UnmodifiableAspectList.of(Aspects.HEAL, 3,Aspects.LIFE,3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.SATURATION,UnmodifiableAspectList.of(Aspects.HUNGER, 3,Aspects.CROP,3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.GLOWING,UnmodifiableAspectList.of(Aspects.SENSES, 1,Aspects.LIGHT,2));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.LEVITATION,UnmodifiableAspectList.of(Aspects.TRAP,1,Aspects.FLIGHT, 2));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.LUCK,UnmodifiableAspectList.of(Aspects.GREED,3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.UNLUCK,UnmodifiableAspectList.of(Aspects.ENTROPY,3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.SLOW_FALLING,UnmodifiableAspectList.of(Aspects.FLIGHT,3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.CONDUIT_POWER,UnmodifiableAspectList.of(Aspects.WATER,3,Aspects.AIR,1,Aspects.SENSES,1,Aspects.TOOL,1));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.DOLPHINS_GRACE,UnmodifiableAspectList.of(Aspects.WATER,3,Aspects.MOTION,3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.BAD_OMEN,UnmodifiableAspectList.of(Aspects.WEAPON,3,Aspects.DEATH,3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.HERO_OF_THE_VILLAGE,UnmodifiableAspectList.of(Aspects.WEAPON,3,Aspects.LIFE,3));
        ASPECTS_FOR_EFFECT_PER_LEVEL.put(MobEffects.DARKNESS,UnmodifiableAspectList.of(Aspects.DARKNESS,6));
        //TODO:[maybe wont finished]bonus aspects for TC4 effects

    }
}
