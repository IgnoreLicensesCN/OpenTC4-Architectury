package thaumcraft.common.items.equipment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnumRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import net.minecraftforge.common.util.EnumHelper;
import thaumcraft.api.IRepairEnchantable;
import thaumcraft.api.IWarpingGear;
import thaumcraft.common.Thaumcraft;

import java.util.List;

public class ItemCrimsonSword extends ItemSword implements IRepairEnchantable, IWarpingGear {
   public static Item.ToolMaterial toolMatCrimsonVoid = EnumHelper.addToolMaterial("CVOID", 4, 200, 8.0F, 3.5F, 20);
   public IIcon icon;

   public ItemCrimsonSword() {
      super(toolMatCrimsonVoid);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:crimson_blade");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.rare;
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

   public boolean hitEntity(ItemStack is, EntityLivingBase target, EntityLivingBase hitter) {
      if (Platform.getEnvironment() != Env.CLIENT && (!(target instanceof Player) || !(hitter instanceof Player) || MinecraftServer.getServer().isPVPEnabled())) {
         try {
            target.addPotionEffect(new PotionEffect(Potion.weakness.getId(), 60));
            target.addPotionEffect(new PotionEffect(Potion.hunger.getId(), 120));
         } catch (Exception ignored) {
         }
      }

      return super.hitEntity(is, target, hitter);
   }

   public int getWarp(ItemStack itemstack, Player player) {
      return 2;
   }

   public void addInformation(ItemStack stack, Player player, List list, boolean par4) {
      list.add(EnumChatFormatting.GOLD + Component.translatable("enchantment.special.sapgreat"));
      super.addInformation(stack, player, list, par4);
   }
}
