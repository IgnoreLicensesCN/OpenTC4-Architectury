package thaumcraft.common.items.equipment;

import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnumRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemSpade;
import net.minecraft.world.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IIcon;
import thaumcraft.api.IRepairEnchantable;
import thaumcraft.api.IWarpingGear;
import thaumcraft.common.Thaumcraft;

import java.util.Set;

public class ItemVoidShovel extends ItemSpade implements IRepairEnchantable, IWarpingGear {
   public IIcon icon;

   public ItemVoidShovel(Item.ToolMaterial enumtoolmaterial) {
      super(enumtoolmaterial);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   public Set getToolClasses(ItemStack stack) {
      return ImmutableSet.of("shovel");
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:voidshovel");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.uncommon;
   }

   public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.isItemEqual(new ItemStack(ThaumcraftItems.PRIMAL_CHARM, 1)) || super.getIsRepairable(par1ItemStack, par2ItemStack);
   }

   public void onUpdate(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
      super.onUpdate(stack, world, entity, p_77663_4_, p_77663_5_);
      if (stack.isItemDamaged() && entity != null && entity.ticksExisted % 20 == 0 && entity instanceof EntityLivingBase) {
         stack.damageItem(-1, (EntityLivingBase)entity);
      }

   }

   public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
      if (Platform.getEnvironment() != Env.CLIENT && entity instanceof EntityLivingBase && (!(entity instanceof Player) || MinecraftServer.getServer().isPVPEnabled())) {
         ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.weakness.getId(), 80));
      }

      return super.onLeftClickEntity(stack, player, entity);
   }

   public int getWarp(ItemStack itemstack, Player player) {
      return 1;
   }
}
