package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileJarFillable;

import java.util.List;

import static thaumcraft.api.aspects.AspectList.addAspectDescriptionToList;

public class ItemJarFilled extends Item implements IEssentiaContainerItem {
   @SideOnly(Side.CLIENT)
   public IIcon icon;

   public ItemJarFilled() {
      this.setMaxDamage(0);
      this.setMaxStackSize(1);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:blank");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public int getMetadata(int par1) {
      return par1;
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      AspectList aspects = this.getAspects(stack);
      addAspectDescriptionToList(aspects,player,list);

      if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("AspectFilter")) {
         String tf = stack.stackTagCompound.getString("AspectFilter");
         Aspect tag = Aspect.getAspect(tf);
         if (Thaumcraft.proxy.playerKnowledge.hasDiscoveredAspect(player.getCommandSenderName(), tag)) {
            list.add("§5" + tag.getName());
         } else {
            list.add("§5" + StatCollector.translateToLocal("tc.aspect.unknown"));
         }
      }

      super.addInformation(stack, player, list, par4);
   }

   public String getUnlocalizedName(ItemStack stack) {
      return stack.getItemDamage() == 3 ? super.getUnlocalizedName(stack) + ".void" : super.getUnlocalizedName(stack);
   }

   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
      Block block = world.getBlock(x, y, z);
      if (block == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1) {
         side = 1;
      } else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, x, y, z)) {
         if (side == 0) {
            --y;
         }

         if (side == 1) {
            ++y;
         }

         if (side == 2) {
            --z;
         }

         if (side == 3) {
            ++z;
         }

         if (side == 4) {
            --x;
         }

         if (side == 5) {
            ++x;
         }
      }

      if (stack.stackSize == 0) {
         return false;
      } else if (!player.canPlayerEdit(x, y, z, side, stack)) {
         return false;
      } else if (y == 255 && ConfigBlocks.blockJar.getMaterial().isSolid()) {
         return false;
      } else if (world.canPlaceEntityOnSide(ConfigBlocks.blockJar, x, y, z, false, side, player, stack)) {
         Block var12 = ConfigBlocks.blockJar;
         int var13 = this.getMetadata(stack.getItemDamage());
         int var14 = ConfigBlocks.blockJar.onBlockPlaced(world, x, y, z, side, par8, par9, par10, var13);
         if (this.placeBlockAt(stack, player, world, x, y, z, side, par8, par9, par10, var14)) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileJarFillable && stack.hasTagCompound()) {
               AspectList aspects = this.getAspects(stack);
               if (aspects != null && aspects.size() == 1) {
                  ((TileJarFillable)te).amount = aspects.getAmount(aspects.getAspects()[0]);
                  ((TileJarFillable)te).aspect = aspects.getAspects()[0];
               }

               String tf = stack.stackTagCompound.getString("AspectFilter");
               if (tf != null) {
                  ((TileJarFillable)te).aspectFilter = Aspect.getAspect(tf);
               }
            }

            world.playSoundEffect((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, var12.stepSound.func_150496_b(), (var12.stepSound.getVolume() + 1.0F) / 2.0F, var12.stepSound.getPitch() * 0.8F);
            --stack.stackSize;
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
      if (!world.setBlock(x, y, z, ConfigBlocks.blockJar, metadata, 3)) {
         return false;
      } else {
         if (world.getBlock(x, y, z) == ConfigBlocks.blockJar) {
            ConfigBlocks.blockJar.onBlockPlacedBy(world, x, y, z, player, stack);
            ConfigBlocks.blockJar.onPostBlockPlaced(world, x, y, z, metadata);
         }

         return true;
      }
   }

   public AspectList getAspects(ItemStack itemstack) {
      if (itemstack.hasTagCompound()) {
         AspectList aspects = new AspectList();
         aspects.readFromNBT(itemstack.getTagCompound());
         return aspects.size() > 0 ? aspects : null;
      } else {
         return null;
      }
   }

   public Aspect getFilter(ItemStack itemstack) {
      return itemstack.hasTagCompound() ? Aspect.getAspect(itemstack.stackTagCompound.getString("AspectFilter")) : null;
   }

   public void setAspects(ItemStack itemstack, AspectList aspects) {
      if (!itemstack.hasTagCompound()) {
         itemstack.setTagCompound(new NBTTagCompound());
      }

      aspects.writeToNBT(itemstack.getTagCompound());
   }
}
