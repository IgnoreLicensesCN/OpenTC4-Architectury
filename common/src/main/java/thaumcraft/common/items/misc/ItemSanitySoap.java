package thaumcraft.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnumAction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;

public class ItemSanitySoap extends Item {
   @SideOnly(Side.CLIENT)
   public IIcon icon;

   public ItemSanitySoap() {
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setHasSubtypes(false);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:soap");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public int getMaxItemUseDuration(ItemStack p_77626_1_) {
      return 200;
   }

   public EnumAction getItemUseAction(ItemStack p_77661_1_) {
      return EnumAction.block;
   }

   public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, Player p_77659_3_) {
      p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));
      return p_77659_1_;
   }

   public void onUsingTick(ItemStack stack, Player player, int count) {
      int ticks = this.getMaxItemUseDuration(stack) - count;
      if (ticks > 195) {
         player.stopUsingItem();
      }

      if (player.worldObj.isRemote) {
         if (player.worldObj.rand.nextFloat() < 0.2F) {
            player.worldObj.playSound(player.posX, player.posY, player.posZ, "thaumcraft:roots", 0.1F, 1.5F + player.worldObj.rand.nextFloat() * 0.2F, false);
         }

         for(int a = 0; a < Thaumcraft.proxy.particleCount(5); ++a) {
            Thaumcraft.proxy.crucibleBubble(Thaumcraft.proxy.getClientWorld(), (float)player.posX - 0.5F + player.worldObj.rand.nextFloat(), (float)player.boundingBox.minY + player.worldObj.rand.nextFloat() * player.height, (float)player.posZ - 0.5F + player.worldObj.rand.nextFloat(), 1.0F, 0.8F, 0.9F);
         }
      }

   }

   public void onPlayerStoppedUsing(ItemStack stack, World world, Player player, int par4) {
      int qq = this.getMaxItemUseDuration(stack) - par4;
      if (qq > 195) {
         --stack.stackSize;
         if (!world.isRemote) {
            float chance = 0.33F;
            if (player.isPotionActive(Config.potionWarpWardID)) {
               chance += 0.25F;
            }

            int i = MathHelper.floor_double(player.posX);
            int j = MathHelper.floor_double(player.posY);
            int k = MathHelper.floor_double(player.posZ);
            if (world.getBlock(i, j, k) == ConfigBlocks.blockFluidPure) {
               chance += 0.25F;
            }

            if (world.rand.nextFloat() < chance && Thaumcraft.proxy.getPlayerKnowledge().getWarpSticky(player.getCommandSenderName()) > 0) {
               Thaumcraft.addStickyWarpToPlayer(player, -1);
            }

            if (Thaumcraft.proxy.getPlayerKnowledge().getWarpTemp(player.getCommandSenderName()) > 0) {
               Thaumcraft.addWarpToPlayer(player, -Thaumcraft.proxy.getPlayerKnowledge().getWarpTemp(player.getCommandSenderName()), true);
            }
         } else {
            player.worldObj.playSound(player.posX, player.posY, player.posZ, "thaumcraft:craftstart", 0.25F, 1.0F, false);

            for(int a = 0; a < Thaumcraft.proxy.particleCount(20); ++a) {
               Thaumcraft.proxy.crucibleBubble(Thaumcraft.proxy.getClientWorld(), (float)player.posX - 0.5F + player.worldObj.rand.nextFloat() * 1.5F, (float)player.boundingBox.minY + player.worldObj.rand.nextFloat() * player.height, (float)player.posZ - 0.5F + player.worldObj.rand.nextFloat() * 1.5F, 1.0F, 0.7F, 0.9F);
            }
         }
      }

   }
}
