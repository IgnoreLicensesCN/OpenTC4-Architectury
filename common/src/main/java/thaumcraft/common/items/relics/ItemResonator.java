package thaumcraft.common.items.relics;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnumRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.world.phys.HitResult;

import net.minecraft.core.Direction;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IAspectContainerBlockEntity;
import thaumcraft.api.aspects.IEssentiaTransportBlockEntity;
import thaumcraft.codechicken.lib.raytracer.RayTracer;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.TileTubeBuffer;

import java.util.List;

public class ItemResonator extends Item {
   private IIcon icon;

   public ItemResonator() {
      this.setMaxStackSize(1);
      this.setHasSubtypes(false);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister par1IconRegister) {
      this.icon = par1IconRegister.registerIcon("thaumcraft:resonator");
   }

   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this));
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.uncommon;
   }

   public boolean hasEffect(ItemStack par1ItemStack) {
      return par1ItemStack.hasTagCompound();
   }

   public boolean onItemUseFirst(ItemStack itemstack, Player player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if (tile instanceof IEssentiaTransportBlockEntity) {
         if ((Platform.getEnvironment() == Env.CLIENT)) {
            player.swingItem();
            return super.onItemUseFirst(itemstack, player, world, x, y, z, side, par8, par9, par10);
         } else {
            IEssentiaTransportBlockEntity et = (IEssentiaTransportBlockEntity)tile;
            Direction face = Direction.getOrientation(side);
            HitResult hit = RayTracer.retraceBlock(world, player, x, y, z);
            if (hit != null && hit.subHit >= 0 && hit.subHit < 6) {
               face = Direction.getOrientation(hit.subHit);
            }

            if (!(tile instanceof TileTubeBuffer) && et.getEssentiaType(face) != null) {
               player.addChatMessage(new ChatComponentTranslation("tc.resonator1", "" + et.getEssentiaAmount(face), et.getEssentiaType(face).getName()));
            } else if (tile instanceof TileTubeBuffer && ((IAspectContainerBlockEntity)tile).getAspects().size() > 0) {
               for(Aspect aspect : ((IAspectContainerBlockEntity)tile).getAspects().getAspectsSorted()) {
                  player.addChatMessage(new ChatComponentTranslation("tc.resonator1", "" + ((IAspectContainerBlockEntity)tile).getAspects().getAmount(aspect), aspect.getName()));
               }
            }

            String s = Component.translatable("tc.resonator3");
            if (et.getSuctionType(face) != null) {
               s = et.getSuctionType(face).getName();
            }

            player.addChatMessage(new ChatComponentTranslation("tc.resonator2", "" + et.getSuctionAmount(face), s));
            world.playSoundEffect(x, y, z, "thaumcraft:alembicknock", 0.5F, 1.9F + world.getRandom().nextFloat() * 0.1F);
            return true;
         }
      } else {
         return false;
      }
   }
}
