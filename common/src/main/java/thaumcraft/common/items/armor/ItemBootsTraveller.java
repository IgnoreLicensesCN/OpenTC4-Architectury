package thaumcraft.common.items.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnumRarity;
import net.minecraft.world.item.ItemArmor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.IIcon;
import thaumcraft.api.IRepairEnchantable;
import thaumcraft.api.IRunicArmor;
import thaumcraft.common.Thaumcraft;

public class ItemBootsTraveller extends ItemArmor implements IRepairEnchantable, IRunicArmor {
   public IIcon icon;

   public ItemBootsTraveller(ItemArmor.ArmorMaterial enumarmormaterial, int j, int k) {
      super(enumarmormaterial, j, k);
      this.setMaxDamage(350);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:bootstraveler");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
      return "thaumcraft:textures/models/bootstraveler.png";
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.rare;
   }

   public int getRunicCharge(ItemStack itemstack) {
      return 0;
   }

   public void onArmorTick(World world, Player player, ItemStack itemStack) {
      if (!player.capabilities.isFlying && player.moveForward > 0.0F) {
         if ((Platform.getEnvironment() == Env.CLIENT) && !player.isSneaking()) {
            if (!Thaumcraft.instance.entityEventHandler.prevStep.containsKey(player.getEntityId())) {
               Thaumcraft.instance.entityEventHandler.prevStep.put(player.getEntityId(), player.stepHeight);
            }

            player.stepHeight = 1.0F;
         }

         if (player.onGround) {
            float bonus = 0.055F;
            if (player.isInWater()) {
               bonus /= 4.0F;
            }

            player.moveFlying(0.0F, 1.0F, bonus);
         } else if (Hover.getHover(player.getEntityId())) {
            player.jumpMovementFactor = 0.03F;
         } else {
            player.jumpMovementFactor = 0.05F;
         }
      }

      if (player.fallDistance > 0.0F) {
         player.fallDistance -= 0.25F;
      }

   }
}
