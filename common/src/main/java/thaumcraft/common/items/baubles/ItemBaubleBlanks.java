package thaumcraft.common.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;

import java.util.List;

public class ItemBaubleBlanks extends Item implements IBauble, IVisDiscountGear, IRunicArmor {
   public IIcon[] icon = new IIcon[4];

   public ItemBaubleBlanks() {
      this.maxStackSize = 1;
      this.canRepair = false;
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setHasSubtypes(true);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:bauble_amulet");
      this.icon[1] = ir.registerIcon("thaumcraft:bauble_ring");
      this.icon[2] = ir.registerIcon("thaumcraft:bauble_belt");
      this.icon[3] = ir.registerIcon("thaumcraft:bauble_ring_iron");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return par1 <= 2 ? this.icon[par1] : this.icon[3];
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this, 1, 0));
      par3List.add(new ItemStack(this, 1, 1));
      par3List.add(new ItemStack(this, 1, 2));
   }

   public BaubleType getBaubleType(ItemStack itemstack) {
      switch (itemstack.getItemDamage()) {
         case 1:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
            return BaubleType.RING;
         case 2:
            return BaubleType.BELT;
         default:
            return BaubleType.AMULET;
      }
   }

   public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
   }

   public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
   }

   public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
   }

   public int getVisDiscount(ItemStack stack, EntityPlayer player, Aspect aspect) {
      return stack.getItemDamage() >= 3 && stack.getItemDamage() <= 8 && Aspect.getPrimalAspects().get(stack.getItemDamage() - 3) == aspect ? 1 : 0;
   }

   public int getColorFromItemStack(ItemStack stack, int par2) {
      return stack.getItemDamage() >= 3 && stack.getItemDamage() <= 8 ? Aspect.getPrimalAspects().get(stack.getItemDamage() - 3).getColor() : super.getColorFromItemStack(stack, par2);
   }

   public String getItemStackDisplayName(ItemStack stack) {
      if (stack.getItemDamage() >= 3 && stack.getItemDamage() <= 8) {
         Aspect aspect = Aspect.getPrimalAspects().get(stack.getItemDamage() - 3);
         return StatCollector.translateToLocal("item.ItemBaubleBlanks.3.name").replace("%TYPE", aspect.getName());
      } else {
         return super.getItemStackDisplayName(stack);
      }
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      if (stack.getItemDamage() >= 3 && stack.getItemDamage() <= 8) {
         Aspect aspect = Aspect.getPrimalAspects().get(stack.getItemDamage() - 3);
         list.add(EnumChatFormatting.DARK_PURPLE + aspect.getName() + " " + StatCollector.translateToLocal("tc.discount") + ": 1%");
      }

   }

   public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }

   public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }

   public int getRunicCharge(ItemStack itemstack) {
      return 0;
   }
}
