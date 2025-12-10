package thaumcraft.common.items.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;

public class ItemRobeArmor extends ItemArmor implements IRepairable, IVisDiscountGear, IRunicArmor {
   public IIcon iconChest;
   public IIcon iconLegs;
   public IIcon iconBoots;
   public IIcon iconChestOver;
   public IIcon iconLegsOver;
   public IIcon iconBootsOver;

   public ItemRobeArmor(ItemArmor.ArmorMaterial enumarmormaterial, int j, int k) {
      super(enumarmormaterial, j, k);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   public int getRunicCharge(ItemStack itemstack) {
      return 0;
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.iconChest = ir.registerIcon("thaumcraft:clothchest");
      this.iconLegs = ir.registerIcon("thaumcraft:clothlegs");
      this.iconBoots = ir.registerIcon("thaumcraft:clothboots");
      this.iconChestOver = ir.registerIcon("thaumcraft:clothchestover");
      this.iconLegsOver = ir.registerIcon("thaumcraft:clothlegsover");
      this.iconBootsOver = ir.registerIcon("thaumcraft:clothbootsover");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.armorType == 1 ? this.iconChest : (this.armorType == 2 ? this.iconLegs : this.iconBoots);
   }

   @SideOnly(Side.CLIENT)
   public boolean requiresMultipleRenderPasses() {
      return true;
   }

   public boolean hasColor(ItemStack par1ItemStack) {
      return true;
   }

   public IIcon getIconFromDamageForRenderPass(int par1, int par2) {
      return par2 == 0 ? (this.armorType == 1 ? this.iconChest : (this.armorType == 2 ? this.iconLegs : this.iconBoots)) : (this.armorType == 1 ? this.iconChestOver : (this.armorType == 2 ? this.iconLegsOver : this.iconBootsOver));
   }

   public int getColor(ItemStack par1ItemStack) {
      NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();
      if (nbttagcompound == null) {
         return 6961280;
      } else {
         NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
         return nbttagcompound1 == null ? 6961280 : (nbttagcompound1.hasKey("color") ? nbttagcompound1.getInteger("color") : 6961280);
      }
   }

   public void removeColor(ItemStack par1ItemStack) {
      NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();
      if (nbttagcompound != null) {
         NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
         if (nbttagcompound1.hasKey("color")) {
            nbttagcompound1.removeTag("color");
         }
      }

   }

   public void func_82813_b(ItemStack par1ItemStack, int par2) {
      NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();
      if (nbttagcompound == null) {
         nbttagcompound = new NBTTagCompound();
         par1ItemStack.setTagCompound(nbttagcompound);
      }

      NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
      if (!nbttagcompound.hasKey("display")) {
         nbttagcompound.setTag("display", nbttagcompound1);
      }

      nbttagcompound1.setInteger("color", par2);
   }

   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
      if (stack.getItem() != ConfigItems.itemChestRobe && stack.getItem() != ConfigItems.itemBootsRobe) {
         if (stack.getItem() == ConfigItems.itemLegsRobe) {
            return type == null ? "thaumcraft:textures/models/robes_2.png" : "thaumcraft:textures/models/robes_2_overlay.png";
         } else {
            return type == null ? "thaumcraft:textures/models/robes_1.png" : "thaumcraft:textures/models/robes_1_overlay.png";
         }
      } else {
         return type == null ? "thaumcraft:textures/models/robes_1.png" : "thaumcraft:textures/models/robes_1_overlay.png";
      }
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.uncommon;
   }

   public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.isItemEqual(new ItemStack(ConfigItems.itemResource, 1, 7)) || super.getIsRepairable(par1ItemStack, par2ItemStack);
   }

   public int getVisDiscount(ItemStack stack, EntityPlayer player, Aspect aspect) {
      return this.armorType == 3 ? 1 : 2;
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": " + this.getVisDiscount(stack, player, null) + "%");
   }

   public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
      if (!world.isRemote && world.getBlock(x, y, z) == Blocks.cauldron && world.getBlockMetadata(x, y, z) > 0) {
         this.removeColor(stack);
         world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) - 1, 2);
         world.func_147453_f(x, y, z, Blocks.cauldron);
         return true;
      } else {
         return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
      }
   }
}
