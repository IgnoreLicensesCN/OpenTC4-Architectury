package thaumcraft.common.items.armor;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagShort;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.blocks.ItemJarFilled;
import thaumcraft.common.config.Config;
import thaumcraft.common.items.baubles.ItemGirdleHover;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketFlyToServer;
import thaumcraft.common.lib.utils.Utils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static baubles.api.expanded.BaubleExpandedSlots.getTypeFromBaubleType;
import static com.linearity.opentc4.simpleutils.bauble.BaubleUtils.forEachBaubleWithBaubleType;

public class Hover {
    public static final int EFFICIENCY = 360;
    static Map<String, Boolean> hovering = new ConcurrentHashMap<>();
    static Map<String, Long> timing = new ConcurrentHashMap<>();

    public static boolean toggleHover(Player player, int playerId, @Nonnull ItemStack armor) {
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
            if ((Platform.getEnvironment() == Env.CLIENT)) {
                PacketHandler.INSTANCE.sendToServer(new PacketFlyToServer(player, !hover));
                player.level().playSound(player.posX, player.posY, player.posZ, !hover ? "thaumcraft:hhon" : "thaumcraft:hhoff", 0.33F, 1.0F, false);
            }

            return !hover;
        }
    }

    public static void setHover(String playerName, boolean hover) {
        hovering.put(playerName, hover);
    }

    public static boolean getHover(String playerName) {
        return hovering.getOrDefault(playerName, false);
    }

    public static void handleHoverArmor(Player player, ItemStack armor) {
        if (hovering.get(player.getName().getString()) == null && armor.hasTagCompound() && armor.stackTagCompound.hasKey("hover")) {
            hovering.put(player.getName().getString(), armor.stackTagCompound.getByte("hover") == 1);
            if ((Platform.getEnvironment() == Env.CLIENT)) {
                PacketHandler.INSTANCE.sendToServer(new PacketFlyToServer(player, armor.stackTagCompound.getByte("hover") == 1));
            }
        }

        boolean hover = hovering.get(player.getName().getString()) != null && hovering.get(player.getName().getString());
        World world = player.level();
        player.capabilities.isFlying = hover;
        if ((Platform.getEnvironment() == Env.CLIENT) && player instanceof LocalPlayer) {
            if (hover && expendCharge(player, armor)) {
                long currenttime = System.currentTimeMillis();
                long time = 0L;
                if (timing.get(player.getName().getString()) != null) {
                    time = timing.get(player.getName().getString());
                }

                if (time < currenttime) {
                    time = currenttime + 1200L;
                    timing.put(player.getName().getString(), time);
                    player.level().playSound(player.posX, player.posY, player.posZ, "thaumcraft:jacobs", 0.05F, 1.0F + player.level().rand.nextFloat() * 0.05F, false);
                }

                int haste = EnchantmentHelper.getEnchantmentLevel(ThaumcraftEnchantments.HASTE, armor);
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
                toggleHover(player, player.getName().getString(), armor);
            }
        } else {
            if (armor.hasTagCompound() && !armor.stackTagCompound.hasKey("hover")) {
                armor.setTagInfo("hover", new NBTTagByte((byte) (hover ? 1 : 0)));
            }

            if (hover && expendCharge(player, armor)) {
                if (player instanceof ServerPlayer) {
                    Utils.resetFloatCounter((ServerPlayer) player);
                }

                player.fallDistance = 0.0F;
                if (armor.hasTagCompound()
                        && armor.stackTagCompound.hasKey("hover")
                        && armor.stackTagCompound.getByte("hover") != 1) {
                    armor.setTagInfo("hover", new NBTTagByte((byte) 1));
                }
            } else {
                if (hover) {
                    toggleHover(player, player.getName().getString(), armor);
                }

                player.fallDistance *= 0.75F;
                if (armor.hasTagCompound() && armor.stackTagCompound.hasKey("hover") && armor.stackTagCompound.getByte("hover") == 1) {
                    armor.setTagInfo("hover", new NBTTagByte((byte) (hover ? 1 : 0)));
                }
            }
        }

    }

    public static boolean expendCharge(Player player, ItemStack is) {
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
