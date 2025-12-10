package thaumcraft.common.items.wands;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;

public class ItemFocusPouch extends Item {
   protected IIcon icon;

   public ItemFocusPouch() {
      this.setMaxStackSize(1);
      this.setHasSubtypes(false);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister par1IconRegister) {
      this.icon = par1IconRegister.registerIcon("thaumcraft:focuspouch");
   }

   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public boolean getShareTag() {
       return super.getShareTag();
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.rare;
   }

   public boolean hasEffect(ItemStack par1ItemStack) {
      return false;
   }

   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      if (!par2World.isRemote) {
         par3EntityPlayer.openGui(Thaumcraft.instance, 5, par2World, MathHelper.floor_double(par3EntityPlayer.posX), MathHelper.floor_double(par3EntityPlayer.posY), MathHelper.floor_double(par3EntityPlayer.posZ));
      }

      return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
   }

   public ItemStack[] getInventory(ItemStack item) {
      ItemStack[] stackList = new ItemStack[18];
      if (item.hasTagCompound()) {
         NBTTagList var2 = item.stackTagCompound.getTagList("Inventory", 10);

         for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            int var5 = var4.getByte("Slot") & 255;
            if (var5 >= 0 && var5 < stackList.length) {
               stackList[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
         }
      }

      return stackList;
   }

   public void setInventory(ItemStack item, ItemStack[] stackList) {
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < stackList.length; ++var3) {
         if (stackList[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte)var3);
            stackList[var3].writeToNBT(var4);
            var2.appendTag(var4);
         }
      }

      item.setTagInfo("Inventory", var2);
   }
}
