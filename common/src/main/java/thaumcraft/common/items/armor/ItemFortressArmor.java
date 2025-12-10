package thaumcraft.common.items.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ISpecialArmor;
import thaumcraft.api.IGoggles;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.nodes.IRevealer;
import thaumcraft.client.renderers.models.gear.ModelFortressArmor;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;

public class ItemFortressArmor extends ItemArmor implements IRepairable, IRunicArmor, ISpecialArmor, IGoggles, IRevealer {
   public IIcon iconHelm;
   public IIcon iconChest;
   public IIcon iconLegs;
   ModelBiped model1 = null;
   ModelBiped model2 = null;
   ModelBiped model = null;

   public ItemFortressArmor(ItemArmor.ArmorMaterial enumarmormaterial, int j, int k) {
      super(enumarmormaterial, j, k);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.iconHelm = ir.registerIcon("thaumcraft:thaumiumfortresshelm");
      this.iconChest = ir.registerIcon("thaumcraft:thaumiumfortresschest");
      this.iconLegs = ir.registerIcon("thaumcraft:thaumiumfortresslegs");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.armorType == 0 ? this.iconHelm : (this.armorType == 1 ? this.iconChest : this.iconLegs);
   }

   @SideOnly(Side.CLIENT)
   public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
      int type = ((ItemArmor)itemStack.getItem()).armorType;
      if (this.model1 == null) {
         this.model1 = new ModelFortressArmor(1.0F);
      }

      if (this.model2 == null) {
         this.model2 = new ModelFortressArmor(0.5F);
      }

      if (type != 1 && type != 3) {
         this.model = this.model2;
      } else {
         this.model = this.model1;
      }

      if (this.model != null) {
         this.model.bipedHead.showModel = armorSlot == 0;
         this.model.bipedHeadwear.showModel = armorSlot == 0;
         this.model.bipedBody.showModel = armorSlot == 1 || armorSlot == 2;
         this.model.bipedRightArm.showModel = armorSlot == 1;
         this.model.bipedLeftArm.showModel = armorSlot == 1;
         this.model.bipedRightLeg.showModel = armorSlot == 2;
         this.model.bipedLeftLeg.showModel = armorSlot == 2;
         this.model.isSneak = entityLiving.isSneaking();
         this.model.isRiding = entityLiving.isRiding();
         this.model.isChild = entityLiving.isChild();
         this.model.aimedBow = false;
         this.model.heldItemRight = entityLiving.getHeldItem() != null ? 1 : 0;
         if (entityLiving instanceof EntityPlayer && ((EntityPlayer)entityLiving).getItemInUseDuration() > 0) {
            EnumAction enumaction = ((EntityPlayer)entityLiving).getItemInUse().getItemUseAction();
            if (enumaction == EnumAction.block) {
               this.model.heldItemRight = 3;
            } else if (enumaction == EnumAction.bow) {
               this.model.aimedBow = true;
            }
         }
      }

      return this.model;
   }

   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
      return "thaumcraft:textures/models/fortress_armor.png";
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.rare;
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("goggles")) {
         list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("item.ItemGoggles.name"));
      }

      if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("mask")) {
         list.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("item.HelmetFortress.mask." + stack.stackTagCompound.getInteger("mask")));
      }

      super.addInformation(stack, player, list, par4);
   }

   public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.isItemEqual(new ItemStack(ConfigItems.itemResource, 1, 2)) || super.getIsRepairable(par1ItemStack, par2ItemStack);
   }

   public int getRunicCharge(ItemStack itemstack) {
      return 0;
   }

   public ISpecialArmor.ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
      int priority = 0;
      double ratio = (double)this.damageReduceAmount / (double)25.0F;
      if (source.isMagicDamage()) {
         priority = 1;
         ratio = (double)this.damageReduceAmount / (double)35.0F;
      } else if (!source.isFireDamage() && !source.isExplosion()) {
         if (source.isUnblockable()) {
            priority = 0;
            ratio = 0.0F;
         }
      } else {
         priority = 1;
         ratio = (double)this.damageReduceAmount / (double)20.0F;
      }

      if (player instanceof EntityPlayer) {
         double set = 0.875F;

         for(int a = 1; a < 4; ++a) {
            ItemStack piece = ((EntityPlayer)player).inventory.armorInventory[a];
            if (piece != null && piece.getItem() instanceof ItemFortressArmor) {
               set += 0.125F;
               if (piece.hasTagCompound() && piece.stackTagCompound.hasKey("mask")) {
                  set += 0.05;
               }
            }
         }

         ratio *= set;
      }

      return new ISpecialArmor.ArmorProperties(priority, ratio, armor.getMaxDamage() + 1 - armor.getItemDamage());
   }

   public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
      return this.damageReduceAmount;
   }

   public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
      if (source != DamageSource.fall) {
         stack.damageItem(damage, entity);
      }

   }

   public boolean showNodes(ItemStack itemstack, EntityLivingBase player) {
      return itemstack.hasTagCompound() && itemstack.stackTagCompound.hasKey("goggles");
   }

   public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player) {
      return itemstack.hasTagCompound() && itemstack.stackTagCompound.hasKey("goggles");
   }
}
