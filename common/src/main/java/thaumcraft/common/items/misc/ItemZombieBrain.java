package thaumcraft.common.items.misc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemFood;
import net.minecraft.world.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.IIcon;
import net.minecraft.world.level.Level;
import thaumcraft.common.Thaumcraft;

public class ItemZombieBrain extends ItemFood {
   public IIcon icon;

   public ItemZombieBrain() {
      super(4, 0.2F, true);
      this.setPotionEffect(Potion.hunger.id, 30, 0, 0.8F);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:brain");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int meta) {
      return this.icon;
   }

   public ItemStack onEaten(ItemStack stack, World world, Player player) {
      if (Platform.getEnvironment() != Env.CLIENT && player instanceof ServerPlayer) {
         if (world.getRandom().nextFloat() < 0.1F) {
            Thaumcraft.addStickyWarpToPlayer(player, 1);
         } else {
            Thaumcraft.addWarpToPlayer(player, 1 + world.getRandom().nextInt(3), true);
         }
      }

      return super.onEaten(stack, world, player);
   }
}
