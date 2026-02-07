package thaumcraft.common.items.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnumRarity;
import net.minecraft.world.item.ItemArmor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.IIcon;
import thaumcraft.api.IRepairEnchantable;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.IWarpingGear;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;

public class ItemVoidArmor extends ItemArmor implements IRepairEnchantable, IRunicArmor, IWarpingGear {
   public IIcon iconHelm;
   public IIcon iconChest;
   public IIcon iconLegs;
   public IIcon iconBoots;

   public ItemVoidArmor(ItemArmor.ArmorMaterial enumarmormaterial, int j, int k) {
      super(enumarmormaterial, j, k);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   public int getRunicCharge(ItemStack itemstack) {
      return 0;
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.iconHelm = ir.registerIcon("thaumcraft:voidhelm");
      this.iconChest = ir.registerIcon("thaumcraft:voidchest");
      this.iconLegs = ir.registerIcon("thaumcraft:voidlegs");
      this.iconBoots = ir.registerIcon("thaumcraft:voidboots");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.armorType == 0 ? this.iconHelm : (this.armorType == 1 ? this.iconChest : (this.armorType == 2 ? this.iconLegs : this.iconBoots));
   }

   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
      if (stack.getItem() != ConfigItems.itemHelmetVoid && stack.getItem() != ConfigItems.itemChestVoid && stack.getItem() != ConfigItems.itemBootsVoid) {
         return stack.getItem() == ConfigItems.itemLegsVoid ? "thaumcraft:textures/models/void_2.png" : "thaumcraft:textures/models/void_1.png";
      } else {
         return "thaumcraft:textures/models/void_1.png";
      }
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.uncommon;
   }

   public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.isItemEqual(new ItemStack(ThaumcraftItems.VOID_INGOT,1)) || super.getIsRepairable(par1ItemStack, par2ItemStack);
   }

   public void onUpdate(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
      super.onUpdate(stack, world, entity, p_77663_4_, p_77663_5_);
      if (Platform.getEnvironment() != Env.CLIENT && stack.isItemDamaged() && entity.ticksExisted % 20 == 0 && entity instanceof EntityLivingBase) {
         stack.damageItem(-1, (EntityLivingBase)entity);
      }

   }

   public void onArmorTick(World world, Player player, ItemStack armor) {
      super.onArmorTick(world, player, armor);
      if (Platform.getEnvironment() != Env.CLIENT && armor.getItemDamage() > 0 && player.ticksExisted % 20 == 0) {
         armor.damageItem(-1, player);
      }

   }

   public int getWarp(ItemStack itemstack, Player player) {
      return 1;
   }
}
