package thaumcraft.api.expands.aspects.item.consts;

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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


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

    public static final BonusTagForItemListener DEFAULT_ON_SHEARS = new BonusTagForItemListener(70) {
        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList sourceTags, @NotNull AspectList currentAspects) {
            if (item instanceof ShearsItem shears) {
                int maxDamage = shears.getMaxDamage();
                if (maxDamage <= Tiers.WOOD.getUses()) {
                    currentAspects.merge(Aspect.HARVEST, 1);
                } else if (maxDamage > Tiers.STONE.getUses() && maxDamage > Tiers.GOLD.getUses()) {
                    if (maxDamage <= Tiers.IRON.getUses()) {
                        currentAspects.merge(Aspect.HARVEST, 3);
                    } else {
                        currentAspects.merge(Aspect.HARVEST, 4);
                    }
                } else {
                    currentAspects.merge(Aspect.HARVEST, 2);
                }
            }
        }
    };
    public static final BonusTagForItemListener DEFAULT_ON_HOE = new BonusTagForItemListener(70) {
        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList sourceTags, @NotNull AspectList currentAspects) {
            if (item instanceof HoeItem hoe) {
                int maxDamage = hoe.getMaxDamage();
                if (maxDamage <= Tiers.WOOD.getUses()) {
                    currentAspects.merge(Aspect.HARVEST, 1);
                } else if (maxDamage > Tiers.STONE.getUses() && maxDamage > Tiers.GOLD.getUses()) {
                    if (maxDamage <= Tiers.IRON.getUses()) {
                        currentAspects.merge(Aspect.HARVEST, 3);
                    } else {
                        currentAspects.merge(Aspect.HARVEST, 4);
                    }
                } else {
                    currentAspects.merge(Aspect.HARVEST, 2);
                }
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
             ENCHANTMENT_ASPECT_MAP.put(Config.enchHaste, Aspect.MOTION);
             ENCHANTMENT_ASPECT_MAP.put(Config.enchRepair, Aspect.TOOL);
    }
    public static final BonusTagForItemListener DEFAULT_ENCHANTMENTS = new BonusTagForItemListener(80){

        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList sourceTags, @NotNull AspectList currentAspects) {
            if (item != null) {

                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemstack);//what i will replace the default read nbt ways with

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
