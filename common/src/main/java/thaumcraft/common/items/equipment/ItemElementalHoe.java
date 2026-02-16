package thaumcraft.common.items.equipment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnumRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemHoe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.IIcon;
import thaumcraft.api.IRepairEnchantable;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.junkbox.BlockCustomPlant;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.Utils;

public class ItemElementalHoe extends ItemHoe implements IRepairEnchantable {
   public IIcon icon;

   public ItemElementalHoe(Item.ToolMaterial enumtoolmaterial) {
      super(enumtoolmaterial);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:elementalhoe");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public int getItemEnchantability() {
      return 5;
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.rare;
   }

   public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.isItemEqual(new ItemStack(ThaumcraftItems.THAUMIUM_INGOT)) || super.getIsRepairable(par1ItemStack, par2ItemStack);
   }

   public boolean onItemUse(ItemStack stack, Player player, World world, int x, int y, int z, int par7, float par8, float par9, float par10) {
      if (player.isSneaking()) {
         return super.onItemUse(stack, player, world, x, y, z, par7, par8, par9, par10);
      } else {
         boolean did = false;

         for(int xx = -1; xx <= 1; ++xx) {
            for(int zz = -1; zz <= 1; ++zz) {
               if (super.onItemUse(stack, player, world, x + xx, y, z + zz, par7, par8, par9, par10)) {
                  ClientFXUtils.blockSparkle(world, x + xx, y, z + zz, 8401408, 2);
                  if (!did) {
                     did = true;
                  }
               }
            }
         }

         if (!did) {
            did = Utils.useBonemealAtLoc(world, player, x, y, z);
            if (!did) {
               Block bi = world.getBlock(x, y, z);
               int md = world.getBlockMetadata(x, y, z);
               if (bi == ConfigBlocks.blockCustomPlant && md == 0 && stack.getItemDamage() + 20 <= stack.getMaxDamage()) {
                  ((BlockCustomPlant)bi).growGreatTree(world, x, y, z, world.getRandom());
                  stack.damageItem(5, player);
                  ClientFXUtils.blockSparkle(world, x, y, z, 0, 2);
                  did = true;
               } else if (bi == ConfigBlocks.blockCustomPlant && md == 1 && stack.getItemDamage() + 150 <= stack.getMaxDamage()) {
                  ((BlockCustomPlant)bi).growSilverTree(world, x, y, z, world.getRandom());
                  stack.damageItem(25, player);
                  ClientFXUtils.blockSparkle(world, x, y, z, 0, 2);
                  did = true;
               }
            } else {
               stack.damageItem(1, player);
               ClientFXUtils.blockSparkle(world, x, y, z, 0, 3);
            }

            if (did) {
               world.playSoundEffect((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "thaumcraft:wand", 0.75F, 0.9F + world.getRandom().nextFloat() * 0.2F);
            }
         }

         return did;
      }
   }
}
