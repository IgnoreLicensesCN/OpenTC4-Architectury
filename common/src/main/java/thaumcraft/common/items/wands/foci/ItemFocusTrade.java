package thaumcraft.common.items.wands.foci;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.BlockCoordinates;
import thaumcraft.api.IArchitect;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.events.ServerTickEventsFML;
import thaumcraft.common.lib.utils.BlockUtils;

import java.util.ArrayList;

public class ItemFocusTrade extends ItemFocusBasic implements IArchitect {
   public IIcon iconOrnament;
   private static final AspectList cost;
   private static AspectList cost2;
   ArrayList<BlockCoordinates> checked = new ArrayList<>();

   public ItemFocusTrade() {
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   public String getSortingHelper(ItemStack itemstack) {
      return "BT" + super.getSortingHelper(itemstack);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:focus_trade");
      this.iconOrnament = ir.registerIcon("thaumcraft:focus_trade_orn");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamageForRenderPass(int par1, int renderPass) {
      return renderPass == 1 ? this.icon : this.iconOrnament;
   }

   @SideOnly(Side.CLIENT)
   public boolean requiresMultipleRenderPasses() {
      return true;
   }

   public IIcon getOrnament(ItemStack itemstack) {
      return this.iconOrnament;
   }

   protected MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World, EntityPlayer par2EntityPlayer) {
      float f = 1.0F;
      float f1 = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * f;
      float f2 = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * f;
      double d0 = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * (double)f;
      double d1 = par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * (double)f + (double)(par1World.isRemote ? par2EntityPlayer.getEyeHeight() - par2EntityPlayer.getDefaultEyeHeight() : par2EntityPlayer.getEyeHeight());
      double d2 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * (double)f;
      Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
      float f3 = MathHelper.cos(-f2 * ((float)Math.PI / 180F) - (float)Math.PI);
      float f4 = MathHelper.sin(-f2 * ((float)Math.PI / 180F) - (float)Math.PI);
      float f5 = -MathHelper.cos(-f1 * ((float)Math.PI / 180F));
      float f6 = MathHelper.sin(-f1 * ((float)Math.PI / 180F));
      float f7 = f4 * f5;
      float f8 = f3 * f5;
      double d3 = 5.0F;
      if (par2EntityPlayer instanceof EntityPlayerMP) {
         d3 = ((EntityPlayerMP)par2EntityPlayer).theItemInWorldManager.getBlockReachDistance();
      }

      Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
      return par1World.rayTraceBlocks(vec3, vec31, false);
   }

   public ItemStack onFocusRightClick(ItemStack itemstack, World world, EntityPlayer player, MovingObjectPosition movingobjectposition) {
      MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player);
      ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();
      if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
         int x = mop.blockX;
         int y = mop.blockY;
         int z = mop.blockZ;
         Block bi = world.getBlock(x, y, z);
         int md = world.getBlockMetadata(x, y, z);
         if (player.isSneaking()) {
            if (!world.isRemote && world.getTileEntity(x, y, z) == null) {
               ItemStack isout = new ItemStack(bi, 1, md);

               try {
                  if (bi != Blocks.air) {
                     ItemStack is = BlockUtils.createStackedBlock(bi, md);
                     if (is != null) {
                        isout = is.copy();
                     }
                  }
               } catch (Exception ignored) {
               }

               this.storePickedBlock(itemstack, isout);
            } else {
               player.swingItem();
            }
         } else {
            ItemStack pb = this.getPickedBlock(itemstack);
            if (pb != null && world.isRemote) {
               player.swingItem();
            } else if (pb != null && world.getTileEntity(x, y, z) == null && world.getBlock(x, y, z).getMaterial() != Config.taintMaterial) {
               if (this.isUpgradedWith(wand.getFocusItem(itemstack), FocusUpgradeType.architect)) {
                  int sizeX = WandManager.getAreaX(itemstack);
                  int sizeZ = WandManager.getAreaZ(itemstack);

                  for(BlockCoordinates c : this.getArchitectBlocks(itemstack, world, x, y, z, mop.sideHit, player)) {
                     ServerTickEventsFML.addSwapper(world, c.x, c.y, c.z, world.getBlock(c.x, c.y, c.z), world.getBlockMetadata(c.x, c.y, c.z), pb, 0, player, player.inventory.currentItem);
                  }
               } else {
                  ServerTickEventsFML.addSwapper(world, x, y, z, world.getBlock(x, y, z), world.getBlockMetadata(x, y, z), pb, 3 + wand.getFocusEnlarge(itemstack), player, player.inventory.currentItem);
               }
            }
         }
      }

      return itemstack;
   }

   public float func_150893_a(ItemStack itemstack, Block block) {
      return 0.0F;
   }

   public boolean onEntitySwing(EntityLivingBase player, ItemStack stack) {
      if (!player.worldObj.isRemote && player instanceof EntityPlayer) {
         ItemStack pb = this.getPickedBlock(stack);
         MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(player.worldObj, (EntityPlayer)player);
         if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
            int x = mop.blockX;
            int y = mop.blockY;
            int z = mop.blockZ;
            if (pb != null && player.worldObj.getTileEntity(x, y, z) == null && player.worldObj.getBlock(x, y, z).getMaterial() != Config.taintMaterial) {
               ServerTickEventsFML.addSwapper(player.worldObj, x, y, z, player.worldObj.getBlock(x, y, z), player.worldObj.getBlockMetadata(x, y, z), pb, 0, (EntityPlayer)player, ((EntityPlayer)player).inventory.currentItem);
            }
         }
      }

      return super.onEntitySwing(player, stack);
   }

   public void storePickedBlock(ItemStack stack, ItemStack stackout) {
      NBTTagCompound item = new NBTTagCompound();
      stack.setTagInfo("picked", stackout.writeToNBT(item));
   }

   public ItemStack getPickedBlock(ItemStack stack) {
      ItemStack out = null;
      if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("picked")) {
         out = new ItemStack(Blocks.air);
         out.readFromNBT(stack.stackTagCompound.getCompoundTag("picked"));
      }

      return out;
   }

   public int getFocusColor(ItemStack itemstack) {
      return 8747923;
   }

   public AspectList getVisCost(ItemStack itemstack) {
      if (this.isUpgradedWith(itemstack, FocusUpgradeType.silktouch)) {
         if (cost2 == null) {
            cost2 = (new AspectList()).add(Aspect.AIR, 1).add(Aspect.FIRE, 1).add(Aspect.EARTH, 1).add(Aspect.WATER, 1).add(Aspect.ORDER, 1).add(Aspect.ENTROPY, 1);
            cost2.add(cost);
         }

         return cost2;
      } else {
         return cost;
      }
   }

   public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack itemstack, int rank) {
      switch (rank) {
         case 1:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.enlarge};
         case 2:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.enlarge};
         case 3:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.enlarge, FocusUpgradeType.treasure, FocusUpgradeType.architect};
         case 4:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.enlarge};
         case 5:
            return new FocusUpgradeType[]{FocusUpgradeType.frugal, FocusUpgradeType.enlarge, FocusUpgradeType.silktouch};
         default:
            return null;
      }
   }

   public int getMaxAreaSize(ItemStack focusstack) {
      return 3 + this.getUpgradeLevel(focusstack, FocusUpgradeType.enlarge) * 2;
   }

   public ArrayList<BlockCoordinates> getArchitectBlocks(ItemStack stack, World world, int x, int y, int z, int side, EntityPlayer player) {
      ItemWandCasting wand = (ItemWandCasting)stack.getItem();
      wand.getFocus(stack);
      Block bi = world.getBlock(x, y, z);
      int md = world.getBlockMetadata(x, y, z);
      ArrayList<BlockCoordinates> out = new ArrayList<>();
      this.checked.clear();
      if (side != 2 && side != 3) {
         this.checkNeighbours(world, x, y, z, bi, md, new BlockCoordinates(x, y, z), side, WandManager.getAreaX(stack), WandManager.getAreaY(stack), WandManager.getAreaZ(stack), out, player);
      } else {
         this.checkNeighbours(world, x, y, z, bi, md, new BlockCoordinates(x, y, z), side, WandManager.getAreaZ(stack), WandManager.getAreaY(stack), WandManager.getAreaX(stack), out, player);
      }

      return out;
   }

   public void checkNeighbours(World world, int x, int y, int z, Block bi, int md, BlockCoordinates pos, int side, int sizeX, int sizeY, int sizeZ, ArrayList list, EntityPlayer player) {
      if (!this.checked.contains(pos)) {
         this.checked.add(pos);
         switch (side) {
            case 0:
            case 1:
               if (Math.abs(pos.x - x) > sizeX) {
                  return;
               }

               if (Math.abs(pos.z - z) > sizeZ) {
                  return;
               }
               break;
            case 2:
            case 3:
               if (Math.abs(pos.x - x) > sizeX) {
                  return;
               }

               if (Math.abs(pos.y - y) > sizeZ) {
                  return;
               }
               break;
            case 4:
            case 5:
               if (Math.abs(pos.y - y) > sizeX) {
                  return;
               }

               if (Math.abs(pos.z - z) > sizeZ) {
                  return;
               }
         }

         if (world.getBlock(pos.x, pos.y, pos.z) == bi && world.getBlockMetadata(pos.x, pos.y, pos.z) == md && BlockUtils.isBlockExposed(world, pos.x, pos.y, pos.z) && !world.isAirBlock(pos.x, pos.y, pos.z) && world.getBlock(pos.x, pos.y, pos.z).getBlockHardness(world, pos.x, pos.y, pos.z) >= 0.0F && world.canMineBlock(player, pos.x, pos.y, pos.z)) {
            list.add(pos);

            for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
               if (dir.ordinal() != side && dir.getOpposite().ordinal() != side) {
                  BlockCoordinates cc = new BlockCoordinates(pos.x + dir.offsetX, pos.y + dir.offsetY, pos.z + dir.offsetZ);
                  this.checkNeighbours(world, x, y, z, bi, md, cc, side, sizeX, sizeY, sizeZ, list, player);
               }
            }

         }
      }
   }

   public boolean showAxis(ItemStack stack, World world, EntityPlayer player, int side, EnumAxis axis) {
      int dim = WandManager.getAreaDim(stack);
      switch (side) {
         case 0:
         case 1:
            if (axis == EnumAxis.X && (dim == 0 || dim == 1) || axis == EnumAxis.Z && (dim == 0 || dim == 2)) {
               return true;
            }
            break;
         case 2:
         case 3:
            if (axis == EnumAxis.Y && (dim == 0 || dim == 1) || axis == EnumAxis.X && (dim == 0 || dim == 2)) {
               return true;
            }
            break;
         case 4:
         case 5:
            if (axis == EnumAxis.Y && (dim == 0 || dim == 1) || axis == EnumAxis.Z && (dim == 0 || dim == 2)) {
               return true;
            }
      }

      return false;
   }

   static {
      cost = (new AspectList()).add(Aspect.ENTROPY, 5).add(Aspect.EARTH, 5).add(Aspect.ORDER, 5);
      cost2 = null;
   }
}
