package thaumcraft.common.items.equipment;

import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnumRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemPickaxe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IIcon;
import net.minecraft.world.level.Level;
import thaumcraft.api.IRepairable;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;

import java.util.Set;

public class ItemElementalPickaxe extends ItemPickaxe implements IRepairable {
   public IIcon icon;

   public ItemElementalPickaxe(Item.ToolMaterial enumtoolmaterial) {
      super(enumtoolmaterial);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   public Set getToolClasses(ItemStack stack) {
      return ImmutableSet.of("pickaxe");
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:elementalpick");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.rare;
   }

   public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return par2ItemStack.isItemEqual(new ItemStack(ThaumcraftItems.THAUMIUM_INGOT)) || super.getIsRepairable(par1ItemStack, par2ItemStack);
   }

   public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
      if (Platform.getEnvironment() != Env.CLIENT && (!(entity instanceof Player) || MinecraftServer.getServer().isPVPEnabled())) {
         entity.setFire(2);
      }

      return super.onLeftClickEntity(stack, player, entity);
   }

   public boolean onItemUse(ItemStack itemstack, Player player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
      itemstack.damageItem(5, player);
      if (Platform.getEnvironment() != Env.CLIENT) {
         world.playSoundEffect((double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, "thaumcraft:wandfail", 0.2F, 0.2F + world.getRandom().nextFloat() * 0.2F);
      } else {
         Minecraft mc = Minecraft.getMinecraft();
         Thaumcraft.instance.renderEventHandler.startScan(player, x, y, z, System.currentTimeMillis() + 5000L, 8);
         player.swingItem();
      }
       return super.onItemUse(itemstack, player, world, x, y, z, side, par8, par9, par10);
   }
}
