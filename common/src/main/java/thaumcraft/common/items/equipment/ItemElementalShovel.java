package thaumcraft.common.items.equipment;

import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.BlockCoordinates;
import thaumcraft.api.IArchitect;
import thaumcraft.api.IRepairable;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.ArrayList;
import java.util.Set;

public class ItemElementalShovel extends ItemSpade implements IRepairable, IArchitect {
    private static final Block[] isEffective;
    public IIcon icon;
    int side = 0;

    public ItemElementalShovel(Item.ToolMaterial enumtoolmaterial) {
        super(enumtoolmaterial);
        this.setCreativeTab(Thaumcraft.tabTC);
    }

    @Override
    public Set getToolClasses(ItemStack stack) {
        return ImmutableSet.of("shovel");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.icon = ir.registerIcon("thaumcraft:elementalshovel");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1) {
        return this.icon;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.rare;
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return par2ItemStack.isItemEqual(new ItemStack(ConfigItems.itemResource, 1, 2)) || super.getIsRepairable(par1ItemStack, par2ItemStack);
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        int xm = ForgeDirection.getOrientation(side).offsetX;
        int ym = ForgeDirection.getOrientation(side).offsetY;
        int zm = ForgeDirection.getOrientation(side).offsetZ;
        boolean result = false;
        Block bi = world.getBlock(x, y, z);
        int md = world.getBlockMetadata(x, y, z);
        TileEntity te = world.getTileEntity(x, y, z);
        if (te == null) {
            for (int aa = -1; aa <= 1; ++aa) {
                for (int bb = -1; bb <= 1; ++bb) {
                    int xx = 0;
                    int yy = 0;
                    int zz = 0;
                    byte o = getOrientation(itemstack);
                    if (o == 1) {
                        yy = bb;
                        if (side <= 1) {
                            int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + (double) 0.5F) & 3;
                            if (l != 0 && l != 2) {
                                zz = aa;
                            } else {
                                xx = aa;
                            }
                        } else if (side <= 3) {
                            zz = aa;
                        } else {
                            xx = aa;
                        }
                    } else if (o == 2) {
                        if (side <= 1) {
                            int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + (double) 0.5F) & 3;
                            yy = bb;
                            if (l != 0 && l != 2) {
                                zz = aa;
                            } else {
                                xx = aa;
                            }
                        } else {
                            zz = bb;
                            xx = aa;
                        }
                    } else if (side <= 1) {
                        xx = aa;
                        zz = bb;
                    } else if (side <= 3) {
                        xx = aa;
                        yy = bb;
                    } else {
                        zz = aa;
                        yy = bb;
                    }

                    Block b2 = world.getBlock(x + xx + xm, y + yy + ym, z + zz + zm);
                    if (world.isAirBlock(x + xx + xm, y + yy + ym, z + zz + zm) || b2 == Blocks.vine || b2 == Blocks.tallgrass || b2.getMaterial() == Material.water || b2 == Blocks.deadbush || b2.isReplaceable(world, x + xx + xm, y + yy + ym, z + zz + zm)) {
                        if (!player.capabilities.isCreativeMode && !InventoryUtils.consumeInventoryItem(player, Item.getItemFromBlock(bi), md)) {
                            if (bi == Blocks.grass && (player.capabilities.isCreativeMode || InventoryUtils.consumeInventoryItem(player, Item.getItemFromBlock(Blocks.dirt), 0))) {
                                world.playSound(x + xx + xm, y + yy + ym, z + zz + zm, bi.stepSound.func_150496_b(), 0.6F, 0.9F + world.rand.nextFloat() * 0.2F, false);
                                world.setBlock(x + xx + xm, y + yy + ym, z + zz + zm, Blocks.dirt, 0, 3);
                                result = true;
                                itemstack.damageItem(1, player);
                                Thaumcraft.proxy.blockSparkle(world, x + xx + xm, y + yy + ym, z + zz + zm, 3, 4);
                                player.swingItem();
                            }
                        } else {
                            world.playSound(x + xx + xm, y + yy + ym, z + zz + zm, bi.stepSound.func_150496_b(), 0.6F, 0.9F + world.rand.nextFloat() * 0.2F, false);
                            world.setBlock(x + xx + xm, y + yy + ym, z + zz + zm, bi, md, 3);
                            result = true;
                            itemstack.damageItem(1, player);
                            Thaumcraft.proxy.blockSparkle(world, x + xx + xm, y + yy + ym, z + zz + zm, 8401408, 4);
                            player.swingItem();
                        }
                    }
                }
            }
        }

        return result;
    }

    private boolean isEffectiveAgainst(Block block) {
        for (Block value : isEffective) {
            if (value == block) {
                return true;
            }
        }

        return false;
    }

    public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
        MovingObjectPosition movingobjectposition = BlockUtils.getTargetBlock(player.worldObj, player, true);
        if (movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectType.BLOCK) {
            this.side = movingobjectposition.sideHit;
        }

        return super.onBlockStartBreak(itemstack, X, Y, Z, player);
    }

    public boolean onBlockDestroyed(ItemStack stack, World world, Block bi, int x, int y, int z, EntityLivingBase ent) {
        if (ent.isSneaking()) {
            return super.onBlockDestroyed(stack, world, bi, x, y, z, ent);
        } else {
            if (!ent.worldObj.isRemote) {
                int md = world.getBlockMetadata(x, y, z);
                if (ForgeHooks.isToolEffective(stack, bi, md) || this.isEffectiveAgainst(bi)) {
                    for (int aa = -1; aa <= 1; ++aa) {
                        for (int bb = -1; bb <= 1; ++bb) {
                            int xx = 0;
                            int yy = 0;
                            int zz = 0;
                            if (this.side <= 1) {
                                xx = aa;
                                zz = bb;
                            } else if (this.side <= 3) {
                                xx = aa;
                                yy = bb;
                            } else {
                                zz = aa;
                                yy = bb;
                            }

                            if (!(ent instanceof EntityPlayer) || world.canMineBlock((EntityPlayer) ent, x + xx, y + yy, z + zz)) {
                                Block bl = world.getBlock(x + xx, y + yy, z + zz);
                                md = world.getBlockMetadata(x + xx, y + yy, z + zz);
                                if (bl.getBlockHardness(world, x + xx, y + yy, z + zz) >= 0.0F && (ForgeHooks.isToolEffective(stack, bl, md) || this.isEffectiveAgainst(bl))) {
                                    stack.damageItem(1, ent);
                                    BlockUtils.harvestBlock(world, (EntityPlayer) ent, x + xx, y + yy, z + zz, true, 3);
                                }
                            }
                        }
                    }
                }
            }

            return true;
        }
    }

    public ArrayList<BlockCoordinates> getArchitectBlocks(ItemStack focusstack, World world, int x, int y, int z, int side, EntityPlayer player) {
        ArrayList<BlockCoordinates> b = new ArrayList<>();
        if (!player.isSneaking()) {
            return b;
        } else {
            int xm = ForgeDirection.getOrientation(side).offsetX;
            int ym = ForgeDirection.getOrientation(side).offsetY;
            int zm = ForgeDirection.getOrientation(side).offsetZ;

            for (int aa = -1; aa <= 1; ++aa) {
                for (int bb = -1; bb <= 1; ++bb) {
                    int xx = 0;
                    int yy = 0;
                    int zz = 0;
                    byte o = getOrientation(focusstack);
                    if (o == 1) {
                        yy = bb;
                        if (side <= 1) {
                            int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + (double) 0.5F) & 3;
                            if (l != 0 && l != 2) {
                                zz = aa;
                            } else {
                                xx = aa;
                            }
                        } else if (side <= 3) {
                            zz = aa;
                        } else {
                            xx = aa;
                        }
                    } else if (o == 2) {
                        if (side <= 1) {
                            int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + (double) 0.5F) & 3;
                            yy = bb;
                            if (l != 0 && l != 2) {
                                zz = aa;
                            } else {
                                xx = aa;
                            }
                        } else {
                            zz = bb;
                            xx = aa;
                        }
                    } else if (side <= 1) {
                        xx = aa;
                        zz = bb;
                    } else if (side <= 3) {
                        xx = aa;
                        yy = bb;
                    } else {
                        zz = aa;
                        yy = bb;
                    }

                    Block b2 = world.getBlock(x + xx + xm, y + yy + ym, z + zz + zm);
                    if (world.isAirBlock(x + xx + xm, y + yy + ym, z + zz + zm) || b2 == Blocks.vine || b2 == Blocks.tallgrass || b2.getMaterial() == Material.water || b2 == Blocks.deadbush || b2.isReplaceable(world, x + xx + xm, y + yy + ym, z + zz + zm)) {
                        b.add(new BlockCoordinates(x + xx + xm, y + yy + ym, z + zz + zm));
                    }
                }
            }

            return b;
        }
    }

    public boolean showAxis(ItemStack stack, World world, EntityPlayer player, int side, EnumAxis axis) {
        return false;
    }

    public static byte getOrientation(ItemStack stack) {
        return stack.hasTagCompound() && stack.stackTagCompound.hasKey("or") ? stack.stackTagCompound.getByte("or") : 0;
    }

    public static void setOrientation(ItemStack stack, byte o) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        if (stack.hasTagCompound()) {
            stack.stackTagCompound.setByte("or", (byte) (o % 3));
        }

    }

    static {
        isEffective = new Block[]{Blocks.grass, Blocks.dirt, Blocks.sand, Blocks.gravel, Blocks.snow_layer, Blocks.snow, Blocks.clay, Blocks.farmland, Blocks.soul_sand, Blocks.mycelium};
    }
}
