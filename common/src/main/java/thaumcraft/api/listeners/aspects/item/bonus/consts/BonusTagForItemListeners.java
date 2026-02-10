package thaumcraft.api.listeners.aspects.item.bonus.consts;

import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.aspects.UnmodifiableAspectList;
import thaumcraft.api.listeners.aspects.item.bonus.listeners.BonusTagForItemListener;
import thaumcraft.common.lib.enchantment.ThaumcraftEnchantments;

import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static thaumcraft.api.listeners.aspects.item.bonus.consts.HelperConsts.ENCHANTMENT_ASPECT_MAP;
import static thaumcraft.api.listeners.aspects.item.bonus.consts.HelperConsts.aspectValueByDurabilityMap;


public enum BonusTagForItemListeners {

    DEFAULT_ON_ESSENTIA_CONTAINER(new BonusTagForItemListener(10) {
        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList<Aspect> sourceTags, @NotNull AspectList<Aspect> currentAspects) {
            if (item instanceof IEssentiaContainerItem essentiaContainer) {
                AspectList<Aspect> aspectsFromContainer = essentiaContainer.getAspects(itemstack);
                if (aspectsFromContainer != null && !aspectsFromContainer.isEmpty()) {
                    for (Aspect tag : aspectsFromContainer.copy()
                            .getAspectTypes()) {
                        int amountInContainer = currentAspects.getAmount(tag);
                        if (amountInContainer > 0) {
                            currentAspects.addAll(tag, amountInContainer);
                        }
                    }
                }
            }
        }
    }),

    DEFAULT_ON_ARMOR(new BonusTagForItemListener(20) {
        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList<Aspect> sourceTags, @NotNull AspectList<Aspect> currentAspects) {
            if (item instanceof ArmorItem armorItem) {
                currentAspects.mergeWithHighest(
                        Aspects.ARMOR, armorItem.getMaterial()
                        .getDefenseForType(armorItem.getType())
                );
            }
        }
    }),

    DEFAULT_ON_SWORD(new BonusTagForItemListener(30) {
        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList<Aspect> sourceTags, @NotNull AspectList<Aspect> currentAspects) {
            if (item instanceof SwordItem swordItem) {
                float damage = swordItem.getTier()
                        .getAttackDamageBonus() + swordItem.getDamage();
                if (damage > 0.F) {
                    currentAspects.mergeWithHighest(Aspects.WEAPON, (int) damage);
                }
            }
        }
    }),

    DEFAULT_ON_BOW(new BonusTagForItemListener(40) {
        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList<Aspect> sourceTags, @NotNull AspectList<Aspect> currentAspects) {
            if (item instanceof BowItem) {
                currentAspects.mergeWithHighest(Aspects.WEAPON, 3)
                        .mergeWithHighest(Aspects.FLIGHT, 1);
            }
        }
    }),

    DEFAULT_ON_PICKAXE(new BonusTagForItemListener(50) {
        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList<Aspect> sourceTags, @NotNull AspectList<Aspect> currentAspects) {
            if (item instanceof PickaxeItem pickaxe) {
                Tier tier = pickaxe.getTier();
                currentAspects.mergeWithHighest(Aspects.MINE, tier.getLevel() + 1);//yeah harvest lvl
            }
        }
    }),
    DEFAULT_ON_TOOL(new BonusTagForItemListener(60) {
        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList<Aspect> sourceTags, @NotNull AspectList<Aspect> currentAspects) {
            if (item instanceof TieredItem tiered) {
                Tier tier = tiered.getTier();
                currentAspects.mergeWithHighest(Aspects.MINE, tier.getLevel() + 1);//yeah harvest lvl
            }
        }
    }),

    DEFAULT_ON_SHEARS(new BonusTagForItemListener(70) {
        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList<Aspect> sourceTags, @NotNull AspectList<Aspect> currentAspects) {
            if (item instanceof ShearsItem shears) {
                int maxDamage = shears.getMaxDamage();
                currentAspects.mergeWithHighest(
                        Aspects.HARVEST, aspectValueByDurabilityMap.higherEntry(maxDamage)
                        .getValue()
                );
            }
        }
    }),
    DEFAULT_ON_HOE(new BonusTagForItemListener(70) {
        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList<Aspect> sourceTags, @NotNull AspectList<Aspect> currentAspects) {
            if (item instanceof HoeItem hoe) {
                int maxDamage = hoe.getMaxDamage();
                currentAspects.mergeWithHighest(
                        Aspects.HARVEST, aspectValueByDurabilityMap.higherEntry(maxDamage)
                        .getValue()
                );
            }
        }
    }),

    //yes yes you can put your own enchantment XD

    DEFAULT_ENCHANTMENTS(new BonusTagForItemListener(80) {

        @Override
        public void onItem(@NotNull Item item, @NotNull ItemStack itemstack, @NotNull UnmodifiableAspectList<Aspect>sourceTags, @NotNull AspectList<Aspect>currentAspects) {
            if (!itemstack.isEmpty()) {

                //what i will replace the default read nbt ways with
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemstack);

                int totalLevel = 0;
                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    Enchantment ench = entry.getKey();
                    int lvl = entry.getValue();

                    AspectList<Aspect>aspects = ENCHANTMENT_ASPECT_MAP.get(ench);
                    if (aspects != null) {
                        for (var addEntry : aspects
                                .entrySet()) {
                            currentAspects.mergeWithHighest(addEntry.getKey(), lvl * addEntry.getValue());
                        }
                    }

                    totalLevel += lvl;
                }

                if (totalLevel > 0) {
                    currentAspects.mergeWithHighest(Aspects.MAGIC, totalLevel);
                }
            }
        }
    }),
    ;
    
    public final BonusTagForItemListener listener;

    BonusTagForItemListeners(BonusTagForItemListener listener) {
        this.listener = listener;
    }
}
