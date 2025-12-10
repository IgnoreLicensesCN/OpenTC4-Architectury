package thaumcraft.common.items.armor;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;

import java.util.HashMap;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.blocks.ItemJarFilled;
import thaumcraft.common.config.Config;
import thaumcraft.common.items.baubles.ItemGirdleHover;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketFlyToServer;
import thaumcraft.common.lib.utils.Utils;

import javax.annotation.Nonnull;

import static baubles.api.expanded.BaubleExpandedSlots.getTypeFromBaubleType;
import static simpleutils.bauble.BaubleUtils.forEachBaubleWithBaubleType;

public class Hover {
    public static final int EFFICIENCY = 360;
    static HashMap<Integer, Boolean> hovering = new HashMap<>();
    static HashMap<Integer, Long> timing = new HashMap<>();

    public static boolean toggleHover(EntityPlayer player, int playerId, @Nonnull ItemStack armor) {
        boolean hover = hovering.get(playerId) != null && hovering.get(playerId);
        short fuel = 0;
        if (armor.hasTagCompound() && armor.stackTagCompound.hasKey("jar")) {
            ItemStack jar = ItemStack.loadItemStackFromNBT(armor.stackTagCompound.getCompoundTag("jar"));
            if (jar != null && jar.getItem() instanceof ItemJarFilled && jar.hasTagCompound()) {
                AspectList aspects = ((ItemJarFilled) jar.getItem()).getAspects(jar);
                if (aspects != null && aspects.size() > 0 && aspects.getAmount(Aspect.ENERGY) > 0) {
                    fuel = (short) aspects.getAmount(Aspect.ENERGY);
                }
            }
        }

        if (!hover && fuel <= 0) {
            return false;
        } else {
            hovering.put(playerId, !hover);
            if (player.worldObj.isRemote) {
                PacketHandler.INSTANCE.sendToServer(new PacketFlyToServer(player, !hover));
                player.worldObj.playSound(player.posX, player.posY, player.posZ, !hover ? "thaumcraft:hhon" : "thaumcraft:hhoff", 0.33F, 1.0F, false);
            }

            return !hover;
        }
    }

    public static void setHover(int playerId, boolean hover) {
        hovering.put(playerId, hover);
    }

    public static boolean getHover(int playerId) {
        return hovering.containsKey(playerId) ? hovering.get(playerId) : false;
    }

    public static void handleHoverArmor(EntityPlayer player, ItemStack armor) {
        if (hovering.get(player.getEntityId()) == null && armor.hasTagCompound() && armor.stackTagCompound.hasKey("hover")) {
            hovering.put(player.getEntityId(), armor.stackTagCompound.getByte("hover") == 1);
            if (player.worldObj.isRemote) {
                PacketHandler.INSTANCE.sendToServer(new PacketFlyToServer(player, armor.stackTagCompound.getByte("hover") == 1));
            }
        }

        boolean hover = hovering.get(player.getEntityId()) != null && hovering.get(player.getEntityId());
        World world = player.worldObj;
        player.capabilities.isFlying = hover;
        if (world.isRemote && player instanceof EntityPlayerSP) {
            if (hover && expendCharge(player, armor)) {
                long currenttime = System.currentTimeMillis();
                long time = 0L;
                if (timing.get(player.getEntityId()) != null) {
                    time = timing.get(player.getEntityId());
                }

                if (time < currenttime) {
                    time = currenttime + 1200L;
                    timing.put(player.getEntityId(), time);
                    player.worldObj.playSound(player.posX, player.posY, player.posZ, "thaumcraft:jacobs", 0.05F, 1.0F + player.worldObj.rand.nextFloat() * 0.05F, false);
                }

                int haste = EnchantmentHelper.getEnchantmentLevel(Config.enchHaste.effectId, armor);
                final float[] mod = {0.7F + 0.075F * (float) haste};
                if (!forEachBaubleWithBaubleType(getTypeFromBaubleType(BaubleType.AMULET), player, ItemGirdleHover.class,
                        (slot, stack, item) -> {
                            mod[0] += 0.21F;
                            return true;
                        })) {
                    try {
                        forEachBaubleWithBaubleType(getTypeFromBaubleType(BaubleType.UNIVERSAL), player, ItemGirdleHover.class,
                                (slot, stack, item) -> {
                                    mod[0] += 0.21F;
                                    return true;
                                });
                    }catch (Exception ignore) {
                        //BaubleType.UNIVERSAL may not defined,it's defined in GTNH ver
                    }
                }
                if (mod[0] > 1.0F) {
                    mod[0] = 1.0F;
                }

                player.motionX *= mod[0];
                player.motionZ *= mod[0];
            } else if (hover) {
                toggleHover(player, player.getEntityId(), armor);
            }
        } else {
            if (armor.hasTagCompound() && !armor.stackTagCompound.hasKey("hover")) {
                armor.setTagInfo("hover", new NBTTagByte((byte) (hover ? 1 : 0)));
            }

            if (hover && expendCharge(player, armor)) {
                if (player instanceof EntityPlayerMP) {
                    Utils.resetFloatCounter((EntityPlayerMP) player);
                }

                player.fallDistance = 0.0F;
                if (armor.hasTagCompound()
                        && armor.stackTagCompound.hasKey("hover")
                        && armor.stackTagCompound.getByte("hover") != 1) {
                    armor.setTagInfo("hover", new NBTTagByte((byte) 1));
                }
            } else {
                if (hover) {
                    toggleHover(player, player.getEntityId(), armor);
                }

                player.fallDistance *= 0.75F;
                if (armor.hasTagCompound() && armor.stackTagCompound.hasKey("hover") && armor.stackTagCompound.getByte("hover") == 1) {
                    armor.setTagInfo("hover", new NBTTagByte((byte) (hover ? 1 : 0)));
                }
            }
        }

    }

    public static boolean expendCharge(EntityPlayer player, ItemStack is) {
        if (is.hasTagCompound() && is.stackTagCompound.hasKey("jar")) {
            ItemStack jar = ItemStack.loadItemStackFromNBT(is.stackTagCompound.getCompoundTag("jar"));
            short fuel = 0;
            if (jar != null && jar.getItem() instanceof ItemJarFilled && jar.hasTagCompound()) {
                AspectList aspects = ((ItemJarFilled) jar.getItem()).getAspects(jar);
                if (aspects != null && aspects.size() > 0 && aspects.getAmount(Aspect.ENERGY) > 0) {
                    fuel = (short) aspects.getAmount(Aspect.ENERGY);
                }
            }

            float mod = 1.0F;
            if (BaublesApi.getBaubles(player).getStackInSlot(3) != null && BaublesApi.getBaubles(player).getStackInSlot(3).getItem() instanceof ItemGirdleHover) {
                mod = 0.8F;
            }

            if (!is.stackTagCompound.hasKey("charge")) {
                is.setTagInfo("charge", new NBTTagShort((short) 0));
            }

            if (fuel > 0 && is.stackTagCompound.hasKey("charge")) {
                short charge = is.stackTagCompound.getShort("charge");
                if ((float) charge < 360.0F * mod) {
                    is.setTagInfo("charge", new NBTTagShort((short) (charge + 1)));
                    return true;
                }

                is.setTagInfo("charge", new NBTTagShort((short) 0));
                --fuel;
                if (fuel > 0) {
                    ((ItemJarFilled) jar.getItem()).setAspects(jar, (new AspectList()).add(Aspect.ENERGY, fuel));
                } else {
                    ((ItemJarFilled) jar.getItem()).setAspects(jar, (new AspectList()).remove(Aspect.ENERGY));
                }

                NBTTagCompound var4 = new NBTTagCompound();
                jar.writeToNBT(var4);
                is.setTagInfo("jar", var4);
                return fuel > 0;
            }
        }

        return false;
    }
}
