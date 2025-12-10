package thaumcraft.common.items.relics;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.EnumRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategoryList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncAspects;
import thaumcraft.common.lib.network.playerdata.PacketSyncResearch;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.List;

public class ItemThaumonomicon extends Item {
   @SideOnly(Side.CLIENT)
   public IIcon icon;
   @SideOnly(Side.CLIENT)
   public IIcon iconCheat;

   public ItemThaumonomicon() {
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setMaxStackSize(1);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:thaumonomicon");
      this.iconCheat = ir.registerIcon("thaumcraft:thaumonomiconcheat");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return par1 != 42 ? this.icon : this.iconCheat;
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this, 1, 0));
      if (Config.allowCheatSheet) {
         par3List.add(new ItemStack(this, 1, 42));
      }

   }

   public ItemStack onItemRightClick(ItemStack stack, World par2World, Player player) {
      if (!(Platform.getEnvironment() == Env.CLIENT)) {
         if (Config.allowCheatSheet && stack.getItemDamage() == 42) {
            for(ResearchCategoryList cat : ResearchCategories.researchCategories.values()) {
               for(ResearchItem ri : cat.research.values()) {
                  if (!ResearchManager.isResearchComplete(player.getCommandSenderName(), ri.key)) {
                     Thaumcraft.proxy.getResearchManager().completeResearch(player, ri.key);
                  }
               }
            }

            for(Aspect aspect : Aspect.aspects.values()) {
               if (!Thaumcraft.proxy.getPlayerKnowledge().hasDiscoveredAspect(player.getCommandSenderName(), aspect)) {
                  Thaumcraft.proxy.researchManager.completeAspect(player, aspect, (short)50);
               }
            }
         } else {
            for(ResearchCategoryList cat : ResearchCategories.researchCategories.values()) {
               for(ResearchItem ri : cat.research.values()) {
                  if (ResearchManager.isResearchComplete(player.getCommandSenderName(), ri.key) && ri.siblings != null) {
                     for(String sib : ri.siblings) {
                        if (!ResearchManager.isResearchComplete(player.getCommandSenderName(), sib)) {
                           Thaumcraft.proxy.getResearchManager().completeResearch(player, sib);
                        }
                     }
                  }
               }
            }
         }

         PacketHandler.INSTANCE.sendTo(new PacketSyncResearch(player), (ServerPlayer)player);
         PacketHandler.INSTANCE.sendTo(new PacketSyncAspects(player), (ServerPlayer)player);
      } else {
         par2World.playSound(player.posX, player.posY, player.posZ, "thaumcraft:page", 1.0F, 1.0F, false);
      }

      player.openGui(Thaumcraft.instance, 12, par2World, 0, 0, 0);
      return stack;
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return itemstack.getItemDamage() != 42 ? EnumRarity.uncommon : EnumRarity.epic;
   }

   public void addInformation(ItemStack par1ItemStack, Player par2Player, List par3List, boolean par4) {
      if (par1ItemStack.getItemDamage() == 42) {
         par3List.add("Cheat Sheet");
      }

      super.addInformation(par1ItemStack, par2Player, par3List, par4);
   }
}
