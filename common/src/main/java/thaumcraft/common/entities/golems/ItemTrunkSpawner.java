package thaumcraft.common.entities.golems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;

public class ItemTrunkSpawner extends Item {
   private IIcon icon;

   public ItemTrunkSpawner() {
      this.setMaxStackSize(1);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister par1IconRegister) {
      this.icon = par1IconRegister.registerIcon("thaumcraft:blank");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this, 1, 0));
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
      if (stack.hasTagCompound()) {
         if (stack.stackTagCompound.hasKey("upgrade")) {
            byte ba = stack.stackTagCompound.getByte("upgrade");
            String text = "§9";
            if (ba > -1) {
               text = text + StatCollector.translateToLocal("item.ItemGolemUpgrade." + ba + ".name") + " ";
            }

            list.add(text);
         }

         if (stack.stackTagCompound.hasKey("inventory")) {
            list.add(StatCollector.translateToLocal("item.TrunkSpawner.text.1"));
         }
      }

      super.addInformation(stack, player, list, par4);
   }

   public boolean onItemUse(ItemStack stack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
       if (!par3World.isRemote) {
           Block i1 = par3World.getBlock(par4, par5, par6);
           par4 += Facing.offsetsXForSide[par7];
           par5 += Facing.offsetsYForSide[par7];
           par6 += Facing.offsetsZForSide[par7];
           double d0 = 0.0F;
           if (par7 == 1 && !i1.isAir(par3World, par4, par5, par6) && i1.getRenderType() == 11) {
               d0 = 0.5F;
           }

           EntityTravelingTrunk entity = new EntityTravelingTrunk(par3World);
           if (entity != null && entity instanceof EntityLivingBase) {
               EntityLiving entityliving = entity;
               entity.setLocationAndAngles(par4, (double) par5 + d0, par6, MathHelper.wrapAngleTo180_float(par3World.rand.nextFloat() * 360.0F), 0.0F);
               entityliving.rotationYawHead = entityliving.rotationYaw;
               entityliving.renderYawOffset = entityliving.rotationYaw;
               entity.setOwner(par2EntityPlayer.getCommandSenderName());
               if (stack.hasDisplayName()) {
                   entity.setCustomNameTag(stack.getDisplayName());
               }

               if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("upgrade")) {
                   entity.setUpgrade(stack.stackTagCompound.getByte("upgrade"));
                   entity.setInvSize();
               }

               if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("inventory")) {
                   NBTTagList nbttaglist = stack.stackTagCompound.getTagList("inventory", 10);
                   entity.inventory.readFromNBT(nbttaglist);
               }

               entityliving.onSpawnWithEgg(null);
               par3World.spawnEntityInWorld(entity);
               entityliving.playLivingSound();
               if (!par2EntityPlayer.capabilities.isCreativeMode) {
                   --stack.stackSize;
               }
           }

       }
       return true;
   }
}
