package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileMirror;
import thaumcraft.common.tiles.TileMirrorEssentia;

import java.util.List;

public class BlockMirrorItem extends ItemBlock {
   public IIcon[] icon = new IIcon[5];

   public BlockMirrorItem(Block par1) {
      super(par1);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister par1IconRegister) {
      this.icon[0] = par1IconRegister.registerIcon("thaumcraft:mirrorframe");
      this.icon[1] = par1IconRegister.registerIcon("thaumcraft:mirrorpane");
      this.icon[2] = par1IconRegister.registerIcon("thaumcraft:mirrorpanetrans");
      this.icon[3] = par1IconRegister.registerIcon("thaumcraft:mirrorpaneopen");
      this.icon[4] = par1IconRegister.registerIcon("thaumcraft:mirrorframe2");
   }

   public IIcon getIconFromDamageForRenderPass(int par1, int par2) {
      return par2 == 0 ? this.icon[par1 <= 1 ? 0 : 4] : this.icon[par2 + par1 % 2 * 2];
   }

   public boolean getShareTag() {
       return super.getShareTag();
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      int d = par1ItemStack.getItemDamage() < 6 ? 0 : 6;
      return super.getUnlocalizedName() + "." + d;
   }

   public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
      if (world.getBlock(x, y, z) == ConfigBlocks.blockMirror) {
         if (world.isRemote) {
            player.swingItem();
            return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
         }

         if (stack.getItemDamage() <= 5) {
            TileEntity tm = world.getTileEntity(x, y, z);
            if (tm instanceof TileMirror && !((TileMirror) tm).isLinkValid()) {
               ItemStack st = stack.copy();
               st.stackSize = 1;
               st.setItemDamage(1);
               st.setTagInfo("linkX", new NBTTagInt(tm.xCoord));
               st.setTagInfo("linkY", new NBTTagInt(tm.yCoord));
               st.setTagInfo("linkZ", new NBTTagInt(tm.zCoord));
               st.setTagInfo("linkDim", new NBTTagInt(world.provider.dimensionId));
               st.setTagInfo("dimname", new NBTTagString(DimensionManager.getProvider(world.provider.dimensionId).getDimensionName()));
               world.playSoundEffect(x, y, z, "thaumcraft:jar", 1.0F, 2.0F);
               if (!player.inventory.addItemStackToInventory(st) && !world.isRemote) {
                  world.spawnEntityInWorld(new EntityItem(world, player.posX, player.posY, player.posZ, st));
               }

               if (!player.capabilities.isCreativeMode) {
                  --stack.stackSize;
               }

               player.inventoryContainer.detectAndSendChanges();
            } else if (tm instanceof TileMirror) {
               player.addChatMessage(new ChatComponentTranslation("§5§oThat mirror is already linked to a valid destination."));
            }
         } else {
            TileEntity tm = world.getTileEntity(x, y, z);
            if (tm instanceof TileMirrorEssentia && !((TileMirrorEssentia) tm).isLinkValid()) {
               ItemStack st = stack.copy();
               st.stackSize = 1;
               st.setItemDamage(7);
               st.setTagInfo("linkX", new NBTTagInt(tm.xCoord));
               st.setTagInfo("linkY", new NBTTagInt(tm.yCoord));
               st.setTagInfo("linkZ", new NBTTagInt(tm.zCoord));
               st.setTagInfo("linkDim", new NBTTagInt(world.provider.dimensionId));
               st.setTagInfo("dimname", new NBTTagString(DimensionManager.getProvider(world.provider.dimensionId).getDimensionName()));
               world.playSoundEffect(x, y, z, "thaumcraft:jar", 1.0F, 2.0F);
               if (!player.inventory.addItemStackToInventory(st) && !world.isRemote) {
                  world.spawnEntityInWorld(new EntityItem(world, player.posX, player.posY, player.posZ, st));
               }

               if (!player.capabilities.isCreativeMode) {
                  --stack.stackSize;
               }

               player.inventoryContainer.detectAndSendChanges();
            } else if (tm instanceof TileMirrorEssentia) {
               player.addChatMessage(new ChatComponentTranslation("§5§oThat mirror is already linked to a valid destination."));
            }
         }
      }

      return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
   }

   public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
      boolean ret = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
      if (ret && !world.isRemote) {
         if (metadata <= 5) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileMirror && stack.hasTagCompound()) {
               ((TileMirror)te).linkX = stack.stackTagCompound.getInteger("linkX");
               ((TileMirror)te).linkY = stack.stackTagCompound.getInteger("linkY");
               ((TileMirror)te).linkZ = stack.stackTagCompound.getInteger("linkZ");
               ((TileMirror)te).linkDim = stack.stackTagCompound.getInteger("linkDim");
               ((TileMirror)te).restoreLink();
            }
         } else {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileMirrorEssentia && stack.hasTagCompound()) {
               ((TileMirrorEssentia)te).linkX = stack.stackTagCompound.getInteger("linkX");
               ((TileMirrorEssentia)te).linkY = stack.stackTagCompound.getInteger("linkY");
               ((TileMirrorEssentia)te).linkZ = stack.stackTagCompound.getInteger("linkZ");
               ((TileMirrorEssentia)te).linkDim = stack.stackTagCompound.getInteger("linkDim");
               ((TileMirrorEssentia)te).restoreLink();
            }
         }
      }

      return ret;
   }

   @SideOnly(Side.CLIENT)
   public int getRenderPasses(int metadata) {
      return 2;
   }

   public boolean requiresMultipleRenderPasses() {
      return true;
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.uncommon;
   }

   public int getMetadata(int par1) {
      return par1;
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack item, EntityPlayer par2EntityPlayer, List list, boolean par4) {
      if (item.hasTagCompound()) {
         int lx = item.stackTagCompound.getInteger("linkX");
         int ly = item.stackTagCompound.getInteger("linkY");
         int lz = item.stackTagCompound.getInteger("linkZ");
         int ldim = item.stackTagCompound.getInteger("linkDim");
         String dimname = item.stackTagCompound.getString("dimname");
         list.add("Linked to " + lx + "," + ly + "," + lz + " in " + dimname);
      }

   }
}
