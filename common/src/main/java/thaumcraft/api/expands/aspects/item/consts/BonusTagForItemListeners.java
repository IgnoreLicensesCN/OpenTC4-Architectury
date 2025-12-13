package thaumcraft.api.expands.aspects.item.consts;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.expands.UnmodifiableAspectList;
import thaumcraft.api.expands.aspects.item.listeners.BonusTagForItemListener;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.enchantment.ThaumcraftEnchantments;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;


public class BonusTagForItemListeners {

    public static final BonusTagForItemListener DEFAULT_ON_ESSENTIA_CONTAINER = new BonusTagForItemListener(10) {
        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList sourceTags, @NotNull AspectList currentAspects) {
            if (item instanceof IEssentiaContainerItem) {
                AspectList aspectsFromContainer = ((IEssentiaContainerItem) item).getAspects(itemstack);
                if (aspectsFromContainer != null && aspectsFromContainer.size() > 0) {
                    for (Aspect tag : aspectsFromContainer.copy().getAspectTypes()) {
                        int amountInContainer = currentAspects.getAmount(tag);
                        if (amountInContainer > 0) {
                            currentAspects.add(tag, amountInContainer);
                        }
                    }
                }
            }
        }
    };

    public static final BonusTagForItemListener DEFAULT_ON_ARMOR = new BonusTagForItemListener(20) {
        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList sourceTags, @NotNull AspectList currentAspects) {
            if (item instanceof ArmorItem armorItem) {
                currentAspects.merge(Aspect.ARMOR, armorItem.getMaterial().getDefenseForType(armorItem.getType()));
            }
        }
    };

    public static final BonusTagForItemListener DEFAULT_ON_SWORD = new BonusTagForItemListener(30) {
        @Override
        public void onItem(@NotNull Item item,@NotNull  ItemStack itemstack,@NotNull  UnmodifiableAspectList sourceTags,@NotNull  AspectList currentAspects) {
            if (item instanceof SwordItem swordItem) {
                float damage = swordItem.getTier().getAttackDamageBonus() + swordItem.getDamage();
                if (damage > 0.F) {
                    currentAspects.merge(Aspect.WEAPON, (int) damage);
                }
            }
        }
    };

    public static final BonusTagForItemListener DEFAULT_ON_BOW = new BonusTagForItemListener(40) {
        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList sourceTags, @NotNull AspectList currentAspects) {
            if (item instanceof BowItem) {
                currentAspects.merge(Aspect.WEAPON, 3).merge(Aspect.FLIGHT, 1);
            }
        }
    };

    public static final BonusTagForItemListener DEFAULT_ON_PICKAXE = new BonusTagForItemListener(50) {
        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList sourceTags, @NotNull AspectList currentAspects) {
            if (item instanceof PickaxeItem pickaxe) {
                Tier tier = pickaxe.getTier();
                currentAspects.merge(Aspect.MINE, tier.getLevel() + 1);//yeah harvest lvl
            }
        }
    };
    public static final BonusTagForItemListener DEFAULT_ON_TOOL = new BonusTagForItemListener(60) {
        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList sourceTags, @NotNull AspectList currentAspects) {
            if (item instanceof TieredItem tiered) {
                Tier tier = tiered.getTier();
                currentAspects.merge(Aspect.MINE, tier.getLevel() + 1);//yeah harvest lvl
            }
        }
    };

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

    public static final BonusTagForItemListener DEFAULT_ON_SHEARS = new BonusTagForItemListener(70) {
        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList sourceTags, @NotNull AspectList currentAspects) {
            if (item instanceof ShearsItem shears) {
                int maxDamage = shears.getMaxDamage();
                currentAspects.merge(Aspect.HARVEST, aspectValueByDurabilityMap.higherEntry(maxDamage).getValue());
            }
        }
    };
    public static final BonusTagForItemListener DEFAULT_ON_HOE = new BonusTagForItemListener(70) {
        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList sourceTags, @NotNull AspectList currentAspects) {
            if (item instanceof HoeItem hoe) {
                int maxDamage = hoe.getMaxDamage();
                currentAspects.merge(Aspect.HARVEST, aspectValueByDurabilityMap.higherEntry(maxDamage).getValue());
            }
        }
    };

    public static final Map<Enchantment,Aspect> ENCHANTMENT_ASPECT_MAP = new ConcurrentHashMap<>();
    static {
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.AQUA_AFFINITY, Aspect.WATER);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.BANE_OF_ARTHROPODS, Aspect.BEAST);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.BLAST_PROTECTION, Aspect.ARMOR);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.BLOCK_EFFICIENCY, Aspect.TOOL);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.FALL_PROTECTION, Aspect.FLIGHT);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.FIRE_ASPECT, Aspect.FIRE);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.FIRE_PROTECTION, Aspect.ARMOR);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.FLAMING_ARROWS, Aspect.FIRE);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.BLOCK_FORTUNE, Aspect.GREED);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.INFINITY_ARROWS, Aspect.CRAFT);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.KNOCKBACK, Aspect.AIR);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.MOB_LOOTING, Aspect.GREED);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.POWER_ARROWS, Aspect.WEAPON);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.PROJECTILE_PROTECTION, Aspect.ARMOR);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.ALL_DAMAGE_PROTECTION, Aspect.ARMOR);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.PUNCH_ARROWS, Aspect.AIR);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.RESPIRATION, Aspect.AIR);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.SHARPNESS, Aspect.WEAPON);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.SILK_TOUCH, Aspect.EXCHANGE);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.THORNS, Aspect.WEAPON);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.SMITE, Aspect.ENTROPY);
            ENCHANTMENT_ASPECT_MAP.put(Enchantments.UNBREAKING, Aspect.EARTH);
            // 自定义附魔
            ENCHANTMENT_ASPECT_MAP.put(ThaumcraftEnchantments.HASTE, Aspect.MOTION);
            ENCHANTMENT_ASPECT_MAP.put(ThaumcraftEnchantments.REPAIR, Aspect.TOOL);
    }
    public static final BonusTagForItemListener DEFAULT_ENCHANTMENTS = new BonusTagForItemListener(80){

        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList sourceTags, @NotNull AspectList currentAspects) {
            if (!itemstack.isEmpty()) {

                //what i will replace the default read nbt ways with
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemstack);

                int totalLevel = 0;
                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    Enchantment ench = entry.getKey();
                    int lvl = entry.getValue();

                    Aspect aspect = ENCHANTMENT_ASPECT_MAP.get(ench);
                    if (aspect != null) {
                        currentAspects.merge(aspect, lvl);
                    }

                    totalLevel += lvl;
                }

                if (totalLevel > 0) {
                    currentAspects.merge(Aspect.MAGIC, totalLevel);
                }
            }
        }
    };
}
