package thaumcraft.common.items.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.client.renderers.models.gear.ModelHoverHarness;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.ItemJarFilled;

import java.util.List;

import static thaumcraft.api.aspects.AspectList.addAspectDescriptionToList;

public class ItemHoverHarness extends ItemArmor implements IRepairable, IVisDiscountGear, IRunicArmor {
   ModelBiped model = null;
   public IIcon icon;
   public IIcon iconLightningRing;

   public ItemHoverHarness(ItemArmor.ArmorMaterial enumarmormaterial, int j, int k) {
      super(enumarmormaterial, j, k);
      this.setMaxDamage(400);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
      if (this.model == null) {
         this.model = new ModelHoverHarness();
      }

      return this.model;
   }

   public int getRunicCharge(ItemStack itemstack) {
      return 0;
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:hoverharness");
      this.iconLightningRing = ir.registerIcon("thaumcraft:lightningring");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
      return "thaumcraft:textures/models/hoverharness.png";
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.epic;
   }

   public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.isItemEqual(new ItemStack(Items.gold_ingot)) || super.getIsRepairable(par1ItemStack, par2ItemStack);
   }

   public int getVisDiscount(ItemStack stack, EntityPlayer player, Aspect aspect) {
      return aspect == Aspect.AIR ? 5 : 2;
   }

   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      if (!par2World.isRemote) {
         par3EntityPlayer.openGui(Thaumcraft.instance, 17, par2World, MathHelper.floor_double(par3EntityPlayer.posX), MathHelper.floor_double(par3EntityPlayer.posY), MathHelper.floor_double(par3EntityPlayer.posZ));
      }

      return par1ItemStack;
   }

   public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
      if (!player.capabilities.isCreativeMode) {
         Hover.handleHoverArmor(player, player.inventory.armorItemInSlot(2));
      }

   }

   public void addInformation(ItemStack is, EntityPlayer player, List list, boolean par4) {
      super.addInformation(is, player, list, par4);
      if (is.hasTagCompound() && is.stackTagCompound.hasKey("jar")) {
         ItemStack jar = ItemStack.loadItemStackFromNBT(is.stackTagCompound.getCompoundTag("jar"));

         try {
            AspectList aspects = ((ItemJarFilled)jar.getItem()).getAspects(jar);
            addAspectDescriptionToList(aspects,player,list);
         } catch (Exception ignored) {
         }
      }

      list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": " + this.getVisDiscount(is, player, null) + "%");
      list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + " (Aer): " + this.getVisDiscount(is, player, Aspect.AIR) + "%");
   }
}
