package thaumcraft.common.items.equipment;

import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Set;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IWarpingGear;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;

public class ItemVoidPickaxe extends ItemPickaxe implements IRepairable, IWarpingGear {
   public IIcon icon;

   public ItemVoidPickaxe(Item.ToolMaterial enumtoolmaterial) {
      super(enumtoolmaterial);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   public Set getToolClasses(ItemStack stack) {
      return ImmutableSet.of("pickaxe");
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:voidpick");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.uncommon;
   }

   public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.isItemEqual(new ItemStack(ConfigItems.itemResource, 1, 15)) || super.getIsRepairable(par1ItemStack, par2ItemStack);
   }

   public void onUpdate(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
      super.onUpdate(stack, world, entity, p_77663_4_, p_77663_5_);
      if (stack.isItemDamaged() && entity != null && entity.ticksExisted % 20 == 0 && entity instanceof EntityLivingBase) {
         stack.damageItem(-1, (EntityLivingBase)entity);
      }

   }

   public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
      if (!player.worldObj.isRemote && entity instanceof EntityLivingBase && (!(entity instanceof EntityPlayer) || MinecraftServer.getServer().isPVPEnabled())) {
         ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.weakness.getId(), 80));
      }

      return super.onLeftClickEntity(stack, player, entity);
   }

   public int getWarp(ItemStack itemstack, EntityPlayer player) {
      return 1;
   }
}
