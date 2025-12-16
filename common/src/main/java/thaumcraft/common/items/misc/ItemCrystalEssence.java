package thaumcraft.common.items.misc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.common.Thaumcraft;

import java.util.List;
import java.util.Random;

import static thaumcraft.api.aspects.AspectList.addAspectDescriptionToList;

public class ItemCrystalEssence extends Item implements IEssentiaContainerItem {
   public IIcon icon;
   static Aspect[] displayAspects;
   Random rand = new Random();

   public ItemCrystalEssence() {
      this.setMaxStackSize(64);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:crystalessence");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this, 1, 0));
   }

   public void addInformation(ItemStack stack, Player player, List list, boolean par4) {
      AspectList aspects = this.getAspects(stack);
      addAspectDescriptionToList(aspects,player,list);

      super.addInformation(stack, player, list, par4);
   }

   @SideOnly(Side.CLIENT)
   public int getColorFromItemStack(ItemStack stack, int par2) {
      if (this.getAspects(stack) != null) {
         return this.getAspects(stack).getAspects()[0].getColor();
      } else {
         int idx = (int)(System.currentTimeMillis() / 500L % (long)displayAspects.length);
         return displayAspects[idx].getColor();
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

   public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
      if (!(Platform.getEnvironment() == Env.CLIENT) && !par1ItemStack.hasTagCompound()) {
         this.setAspects(par1ItemStack, (new AspectList()).addAll(displayAspects[this.rand.nextInt(displayAspects.length)], 1));
      }

      super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
   }

   public void onCreated(ItemStack par1ItemStack, World par2World, Player par3Player) {
      if (!par1ItemStack.hasTagCompound()) {
         this.setAspects(par1ItemStack, (new AspectList()).addAll(displayAspects[this.rand.nextInt(displayAspects.length)], 1));
      }

   }

   static {
      displayAspects = Aspect.aspects.values().toArray(new Aspect[0]);
   }
}
