package thaumcraft.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockAiryItem extends ItemBlock {
   public IIcon icon;

   public BlockAiryItem(Block b) {
      super(b);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:taint_over_2");
   }

   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public int getMetadata(int par1) {
      return par1;
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
   }

   public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List list, boolean par4) {
      List<String> loreList = (List<String>) list;
      if (par1ItemStack.getItemDamage() == 0) {
         loreList.add("§5Place a randomly generated node");
         loreList.add("§oCreative Mode Only");
      }

      super.addInformation(par1ItemStack, par2EntityPlayer, loreList, par4);
   }
}
