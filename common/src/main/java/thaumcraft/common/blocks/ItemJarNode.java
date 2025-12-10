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
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileJarNode;

import java.util.List;

public class ItemJarNode extends Item implements IEssentiaContainerItem {
   @SideOnly(Side.CLIENT)
   public IIcon icon;

   public ItemJarNode() {
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

   @Override
   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      String desc = "§9" + StatCollector.translateToLocal("nodetype." + this.getNodeType(stack) + ".name");
      if (this.getNodeModifier(stack) != null) {
         desc = desc + ", " + StatCollector.translateToLocal("nodemod." + this.getNodeModifier(stack) + ".name");
      }

      list.add(desc);
      AspectList.addAspectDescriptionToList(this.getAspects(stack), player, list);

      super.addInformation(stack, player, list, par4);
   }

   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
      Block var11 = world.getBlock(x, y, z);
      if (var11 == Blocks.snow_layer) {
         side = 1;
      } else if (var11 != Blocks.vine && var11 != Blocks.tallgrass && var11 != Blocks.deadbush && (var11.isAir(world, x, y, z) || !var11.isReplaceable(world, x, y, z))) {
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
         int var13 = 2;
         int var14 = ConfigBlocks.blockJar.onBlockPlaced(world, x, y, z, side, par8, par9, par10, var13);
         if (this.placeBlockAt(stack, player, world, x, y, z, side, par8, par9, par10, var14)) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileJarNode && stack.hasTagCompound()) {
               AspectList aspects = this.getAspects(stack);
               if (aspects != null) {
                  ((TileJarNode)te).setAspects(aspects);
                  ((TileJarNode)te).setNodeType(this.getNodeType(stack));
                  ((TileJarNode)te).setNodeModifier(this.getNodeModifier(stack));
                  ((TileJarNode)te).setId(this.getNodeId(stack));
               }
            }

            world.playSoundEffect((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, var12.stepSound.getStepResourcePath(), (var12.stepSound.getVolume() + 1.0F) / 2.0F, var12.stepSound.getPitch() * 0.8F);
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

   public void setAspects(ItemStack itemstack, AspectList aspects) {
      if (!itemstack.hasTagCompound()) {
         itemstack.setTagCompound(new NBTTagCompound());
      }

      aspects.writeToNBT(itemstack.getTagCompound());
   }

   public void setNodeAttributes(ItemStack itemstack, NodeType type, NodeModifier mod, String id) {
      if (!itemstack.hasTagCompound()) {
         itemstack.setTagCompound(new NBTTagCompound());
      }

      itemstack.setTagInfo("nodetype", new NBTTagInt(type.ordinal()));
      if (mod != null) {
         itemstack.setTagInfo("nodemod", new NBTTagInt(mod.ordinal()));
      }

      itemstack.setTagInfo("nodeid", new NBTTagString(id));
   }

   public NodeType getNodeType(ItemStack itemstack) {
      return !itemstack.hasTagCompound() ? null : NodeType.values()[itemstack.getTagCompound().getInteger("nodetype")];
   }

   public NodeModifier getNodeModifier(ItemStack itemstack) {
      return itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("nodemod") ? NodeModifier.values()[itemstack.getTagCompound().getInteger("nodemod")] : null;
   }

   public String getNodeId(ItemStack itemstack) {
      return !itemstack.hasTagCompound() ? "0" : itemstack.getTagCompound().getString("nodeid");
   }
}
