package thaumcraft.common.items.relics;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileMirror;

public class ItemHandMirror extends Item {
   private IIcon icon;

   public ItemHandMirror() {
      this.setMaxStackSize(1);
      this.setHasSubtypes(false);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister par1IconRegister) {
      this.icon = par1IconRegister.registerIcon("thaumcraft:mirrorhand");
   }

   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this));
   }

   public boolean getShareTag() {
       return super.getShareTag();
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.uncommon;
   }

   public boolean hasEffect(ItemStack par1ItemStack) {
      return par1ItemStack.hasTagCompound();
   }

   public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10) {
      Block bi = world.getBlock(x, y, z);
      if (bi == ConfigBlocks.blockMirror) {
         if (world.isRemote) {
            player.swingItem();
            return super.onItemUseFirst(itemstack, player, world, x, y, z, par7, par8, par9, par10);
         } else {
            TileEntity tm = world.getTileEntity(x, y, z);
            if (tm instanceof TileMirror) {
               itemstack.setTagInfo("linkX", new NBTTagInt(tm.xCoord));
               itemstack.setTagInfo("linkY", new NBTTagInt(tm.yCoord));
               itemstack.setTagInfo("linkZ", new NBTTagInt(tm.zCoord));
               itemstack.setTagInfo("linkDim", new NBTTagInt(world.provider.dimensionId));
               itemstack.setTagInfo("dimname", new NBTTagString(DimensionManager.getProvider(world.provider.dimensionId).getDimensionName()));
               world.playSoundEffect(x, y, z, "thaumcraft:jar", 1.0F, 2.0F);
               player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("tc.handmirrorlinked")));
               player.inventoryContainer.detectAndSendChanges();
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      if (!par2World.isRemote && par1ItemStack.hasTagCompound()) {
         int lx = par1ItemStack.stackTagCompound.getInteger("linkX");
         int ly = par1ItemStack.stackTagCompound.getInteger("linkY");
         int lz = par1ItemStack.stackTagCompound.getInteger("linkZ");
         int ldim = par1ItemStack.stackTagCompound.getInteger("linkDim");
         World targetWorld = MinecraftServer.getServer().worldServerForDimension(ldim);
         if (targetWorld == null) {
            return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
         }

         TileEntity te = targetWorld.getTileEntity(lx, ly, lz);
         if (!(te instanceof TileMirror)) {
            par1ItemStack.setTagCompound(null);
            par2World.playSoundAtEntity(par3EntityPlayer, "thaumcraft:zap", 1.0F, 0.8F);
            par3EntityPlayer.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("tc.handmirrorerror")));
            return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
         }

         par3EntityPlayer.openGui(Thaumcraft.instance, 16, par2World, MathHelper.floor_double(par3EntityPlayer.posX), MathHelper.floor_double(par3EntityPlayer.posY), MathHelper.floor_double(par3EntityPlayer.posZ));
      }

      return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack item, EntityPlayer par2EntityPlayer, List list, boolean par4) {
      if (item.hasTagCompound()) {
         int lx = item.stackTagCompound.getInteger("linkX");
         int ly = item.stackTagCompound.getInteger("linkY");
         int lz = item.stackTagCompound.getInteger("linkZ");
         int ldim = item.stackTagCompound.getInteger("linkDim");
         String dimname = item.stackTagCompound.getString("dimname");
         list.add(StatCollector.translateToLocal("tc.handmirrorlinkedto") + " " + lx + "," + ly + "," + lz + " in " + dimname);
      }

   }

   public static boolean transport(ItemStack mirror, ItemStack items, EntityPlayer player, World worldObj) {
      if (mirror.hasTagCompound()) {
         int lx = mirror.stackTagCompound.getInteger("linkX");
         int ly = mirror.stackTagCompound.getInteger("linkY");
         int lz = mirror.stackTagCompound.getInteger("linkZ");
         int ldim = mirror.stackTagCompound.getInteger("linkDim");
         World targetWorld = MinecraftServer.getServer().worldServerForDimension(ldim);
         if (targetWorld == null) {
            return false;
         } else {
            TileEntity te = targetWorld.getTileEntity(lx, ly, lz);
            if (te instanceof TileMirror) {
               TileMirror tm = (TileMirror)te;
               ForgeDirection linkedFacing = ForgeDirection.getOrientation(targetWorld.getBlockMetadata(lx, ly, lz));
               EntityItem ie2 = new EntityItem(targetWorld, (double)lx + (double)0.5F - (double)linkedFacing.offsetX * 0.3, (double)ly + (double)0.5F - (double)linkedFacing.offsetY * 0.3, (double)lz + (double)0.5F - (double)linkedFacing.offsetZ * 0.3, items.copy());
               ie2.motionX = (float)linkedFacing.offsetX * 0.15F;
               ie2.motionY = (float)linkedFacing.offsetY * 0.15F;
               ie2.motionZ = (float)linkedFacing.offsetZ * 0.15F;
               ie2.timeUntilPortal = 20;
               targetWorld.spawnEntityInWorld(ie2);
               ItemStack var13 = null;
               worldObj.playSoundAtEntity(player, "mob.endermen.portal", 0.1F, 1.0F);
               targetWorld.addBlockEvent(lx, ly, lz, ConfigBlocks.blockMirror, 1, 0);
               return true;
            } else {
               mirror.setTagCompound(null);
               worldObj.playSoundAtEntity(player, "thaumcraft:zap", 1.0F, 0.8F);
               player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("tc.handmirrorerror")));
               return false;
            }
         }
      } else {
         return false;
      }
   }
}
