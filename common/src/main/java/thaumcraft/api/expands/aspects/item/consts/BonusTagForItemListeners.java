package thaumcraft.api.expands.aspects.item.consts;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagList;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.expands.UnmodifiableAspectList;
import thaumcraft.api.expands.aspects.item.listeners.BonusTagForItemListener;
import thaumcraft.common.config.Config;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class BonusTagForItemListeners {

    public static final BonusTagForItemListener DEFAULT_ON_ESSENTIA_CONTAINER = new BonusTagForItemListener(10) {
        @Override
        @ParametersAreNonnullByDefault
        public void onItem(@Nullable Item item, ItemStack itemstack, UnmodifiableAspectList sourceTags, AspectList currentAspects) {
            if (item instanceof IEssentiaContainerItem) {
                AspectList aspectsFromContainer = ((IEssentiaContainerItem) item).getAspects(itemstack);
                if (aspectsFromContainer != null && aspectsFromContainer.size() > 0) {
                    for (Aspect tag : aspectsFromContainer.copy().getAspects()) {
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
        @ParametersAreNonnullByDefault
        public void onItem(@Nullable Item item, ItemStack itemstack, UnmodifiableAspectList sourceTags, AspectList currentAspects) {
            if (item instanceof ItemArmor) {
                currentAspects.merge(Aspect.ARMOR, ((ItemArmor) item).damageReduceAmount);
            }
        }
    };

    public static final BonusTagForItemListener DEFAULT_ON_SWORD = new BonusTagForItemListener(30) {
        @Override
        @ParametersAreNonnullByDefault
        public void onItem(@Nullable Item item, ItemStack itemstack, UnmodifiableAspectList sourceTags, AspectList currentAspects) {
            if (item instanceof ItemSword && ((ItemSword) item).func_150931_i() + 1.0F > 0.0F) {
                currentAspects.merge(Aspect.WEAPON, (int) (((ItemSword) item).func_150931_i() + 1.0F));
            }
        }
    };

    public static final BonusTagForItemListener DEFAULT_ON_BOW = new BonusTagForItemListener(40) {
        @Override
        @ParametersAreNonnullByDefault
        public void onItem(@Nullable Item item, ItemStack itemstack, UnmodifiableAspectList sourceTags, AspectList currentAspects) {
            if (item instanceof ItemBow) {
                currentAspects.merge(Aspect.WEAPON, 3).merge(Aspect.FLIGHT, 1);
            }
        }
    };

    public static final BonusTagForItemListener DEFAULT_ON_PICKAXE = new BonusTagForItemListener(50) {
        @Override
        @ParametersAreNonnullByDefault
        public void onItem(@Nullable Item item, ItemStack itemstack, UnmodifiableAspectList sourceTags, AspectList currentAspects) {
            if (item instanceof ItemPickaxe) {
                String mat = ((ItemTool) item).getToolMaterialName();

                for (Item.ToolMaterial tm : Item.ToolMaterial.values()) {
                    if (tm.toString().equals(mat)) {
                        currentAspects.merge(Aspect.MINE, tm.getHarvestLevel() + 1);
                    }
                }
            }
        }
    };
    public static final BonusTagForItemListener DEFAULT_ON_TOOL = new BonusTagForItemListener(60) {
        @Override
        @ParametersAreNonnullByDefault
        public void onItem(@Nullable Item item, ItemStack itemstack, UnmodifiableAspectList sourceTags, AspectList currentAspects) {
            if (item instanceof ItemTool) {
                String mat = ((ItemTool) item).getToolMaterialName();

                for (Item.ToolMaterial tm : Item.ToolMaterial.values()) {
                    if (tm.toString().equals(mat)) {
                        currentAspects.merge(Aspect.TOOL, tm.getHarvestLevel() + 1);
                    }
                }
            }
        }
    };

    public static final BonusTagForItemListener DEFAULT_ON_SHEARS = new BonusTagForItemListener(70) {
        @Override
        @ParametersAreNonnullByDefault
        public void onItem(@Nullable Item item, ItemStack itemstack, UnmodifiableAspectList sourceTags, AspectList currentAspects) {
            if (item instanceof ItemShears) {
                if (item.getMaxDamage() <= Item.ToolMaterial.WOOD.getMaxUses()) {
                    currentAspects.merge(Aspect.HARVEST, 1);
                } else if (item.getMaxDamage() > Item.ToolMaterial.STONE.getMaxUses() && item.getMaxDamage() > Item.ToolMaterial.GOLD.getMaxUses()) {
                    if (item.getMaxDamage() <= Item.ToolMaterial.IRON.getMaxUses()) {
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
        @ParametersAreNonnullByDefault
        public void onItem(@Nullable Item item, ItemStack itemstack, UnmodifiableAspectList sourceTags, AspectList currentAspects) {
            if (item instanceof ItemHoe) {
                if (item.getMaxDamage() <= Item.ToolMaterial.WOOD.getMaxUses()) {
                    currentAspects.merge(Aspect.HARVEST, 1);
                } else if (item.getMaxDamage() > Item.ToolMaterial.STONE.getMaxUses() && item.getMaxDamage() > Item.ToolMaterial.GOLD.getMaxUses()) {
                    if (item.getMaxDamage() <= Item.ToolMaterial.IRON.getMaxUses()) {
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

    //hint:if you want to check you enchantment,you add your own listener.
    public static final BonusTagForItemListener DEFAULT_ENCHANTMENTS = new BonusTagForItemListener(80){

        @Override
        @ParametersAreNonnullByDefault
        public void onItem(@Nullable Item item, ItemStack itemstack, UnmodifiableAspectList sourceTags, AspectList currentAspects) {
            if (item != null) {

                NBTTagList ench = itemstack.getEnchantmentTagList();
                if (item instanceof ItemEnchantedBook) {
                    ench = ((ItemEnchantedBook) item).func_92110_g(itemstack);
                }

                if (ench != null) {
                    int var5 = 0;

                    for (int var3 = 0; var3 < ench.tagCount(); ++var3) {
                        short eid = ench.getCompoundTagAt(var3).getShort("id");
                        short lvl = ench.getCompoundTagAt(var3).getShort("lvl");
                        if (eid == Enchantment.aquaAffinity.effectId) {
                            currentAspects.merge(Aspect.WATER, lvl);
                        } else if (eid == Enchantment.baneOfArthropods.effectId) {
                            currentAspects.merge(Aspect.BEAST, lvl);
                        } else if (eid == Enchantment.blastProtection.effectId) {
                            currentAspects.merge(Aspect.ARMOR, lvl);
                        } else if (eid == Enchantment.efficiency.effectId) {
                            currentAspects.merge(Aspect.TOOL, lvl);
                        } else if (eid == Enchantment.featherFalling.effectId) {
                            currentAspects.merge(Aspect.FLIGHT, lvl);
                        } else if (eid == Enchantment.fireAspect.effectId) {
                            currentAspects.merge(Aspect.FIRE, lvl);
                        } else if (eid == Enchantment.fireProtection.effectId) {
                            currentAspects.merge(Aspect.ARMOR, lvl);
                        } else if (eid == Enchantment.flame.effectId) {
                            currentAspects.merge(Aspect.FIRE, lvl);
                        } else if (eid == Enchantment.fortune.effectId) {
                            currentAspects.merge(Aspect.GREED, lvl);
                        } else if (eid == Enchantment.infinity.effectId) {
                            currentAspects.merge(Aspect.CRAFT, lvl);
                        } else if (eid == Enchantment.knockback.effectId) {
                            currentAspects.merge(Aspect.AIR, lvl);
                        } else if (eid == Enchantment.looting.effectId) {
                            currentAspects.merge(Aspect.GREED, lvl);
                        } else if (eid == Enchantment.power.effectId) {
                            currentAspects.merge(Aspect.WEAPON, lvl);
                        } else if (eid == Enchantment.projectileProtection.effectId) {
                            currentAspects.merge(Aspect.ARMOR, lvl);
                        } else if (eid == Enchantment.protection.effectId) {
                            currentAspects.merge(Aspect.ARMOR, lvl);
                        } else if (eid == Enchantment.punch.effectId) {
                            currentAspects.merge(Aspect.AIR, lvl);
                        } else if (eid == Enchantment.respiration.effectId) {
                            currentAspects.merge(Aspect.AIR, lvl);
                        } else if (eid == Enchantment.sharpness.effectId) {
                            currentAspects.merge(Aspect.WEAPON, lvl);
                        } else if (eid == Enchantment.silkTouch.effectId) {
                            currentAspects.merge(Aspect.EXCHANGE, lvl);
                        } else if (eid == Enchantment.thorns.effectId) {
                            currentAspects.merge(Aspect.WEAPON, lvl);
                        } else if (eid == Enchantment.smite.effectId) {
                            currentAspects.merge(Aspect.ENTROPY, lvl);
                        } else if (eid == Enchantment.unbreaking.effectId) {
                            currentAspects.merge(Aspect.EARTH, lvl);
                        } else if (eid == Enchantment.field_151370_z.effectId) {
                            currentAspects.merge(Aspect.GREED, lvl);
                        } else if (eid == Enchantment.field_151369_A.effectId) {
                            currentAspects.merge(Aspect.BEAST, lvl);
                        } else if (eid == Config.enchHaste.effectId) {
                            currentAspects.merge(Aspect.MOTION, lvl);
                        } else if (eid == Config.enchRepair.effectId) {
                            currentAspects.merge(Aspect.TOOL, lvl);
                        }

                        var5 += lvl;
                    }

                    if (var5 > 0) {
                        currentAspects.merge(Aspect.MAGIC, var5);
                    }
                }
            }
        }
    };
}
