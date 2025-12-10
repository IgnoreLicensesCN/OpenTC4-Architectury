package thaumcraft.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import thaumcraft.api.IScribeTools;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.projectile.EntityBottleTaint;

public class ItemBottleTaint extends Item implements IScribeTools {
   @SideOnly(Side.CLIENT)
   public IIcon icon;

   public ItemBottleTaint() {
      this.maxStackSize = 8;
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setHasSubtypes(false);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:bottle_taint");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, Player player) {
      if (!player.capabilities.isCreativeMode) {
         --stack.stackSize;
      }

      world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
      if (!world.isRemote) {
         world.spawnEntityInWorld(new EntityBottleTaint(world, player));
      }

      return stack;
   }
}
