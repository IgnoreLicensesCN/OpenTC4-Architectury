package thaumcraft.common.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnumRarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.wandtypes.WandCastingItem;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileVisRelay;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.List;

public class ItemAmuletVis extends Item implements IBauble, IRunicArmor {
   public IIcon[] icon = new IIcon[2];
   DecimalFormat myFormatter = new DecimalFormat("#######.##");

   public ItemAmuletVis() {
      this.maxStackSize = 1;
      this.canRepair = false;
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setHasSubtypes(true);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:vis_amulet_lesser");
      this.icon[1] = ir.registerIcon("thaumcraft:vis_amulet");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return par1 >= this.icon.length ? this.icon[0] : this.icon[par1];
   }

   public EnumRarity getRarity(ItemStack itemstack) {
      return itemstack.getItemDamage() == 0 ? EnumRarity.uncommon : EnumRarity.rare;
   }

   public String getUnlocalizedName(ItemStack stack) {
      return super.getUnlocalizedName() + "." + stack.getItemDamage();
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this, 1, 0));
      par3List.add(new ItemStack(this, 1, 1));
   }

   public BaubleType getBaubleType(ItemStack itemstack) {
      return BaubleType.AMULET;
   }

   public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
      if (Platform.getEnvironment() != Env.CLIENT && player.ticksExisted % 5 == 0) {
         if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof WandCastingItem) {
            WandCastingItem wand = (WandCastingItem)player.getHeldItem().getItem();
            AspectList al = wand.getAspectsWithRoom(player.getHeldItem());

            for(Aspect aspect : al.getAspects()) {
               if (aspect != null && this.getVis(itemstack, aspect) > 0) {
                  int amt = Math.min(5, wand.getMaxVis(player.getHeldItem()) - wand.getVis(player.getHeldItem(), aspect));
                  amt = Math.min(amt, this.getVis(itemstack, aspect));
                  this.storeVis(itemstack, aspect, this.getVis(itemstack, aspect) - amt);
                  wand.storeVis(player.getHeldItem(), aspect, this.getVis(player.getHeldItem(), aspect) + amt);
               }
            }
         }

         if (TileVisRelay.nearbyPlayers.containsKey(player.getEntityId())) {
            if (((WeakReference)TileVisRelay.nearbyPlayers.get(player.getEntityId())).get() != null && ((TileVisRelay)((WeakReference)TileVisRelay.nearbyPlayers.get(player.getEntityId())).get()).getDistanceFrom(player.posX, player.posY, player.posZ) < (double)26.0F) {
               AspectList al = this.getAspectsWithRoom(itemstack);

               for(Aspect aspect : al.getAspects()) {
                  if (aspect != null) {
                     int amt = ((TileVisRelay)((WeakReference)TileVisRelay.nearbyPlayers.get(player.getEntityId())).get()).consumeVis(aspect, Math.min(5, this.getMaxVis(itemstack) - this.getVis(itemstack, aspect)));
                     if (amt > 0) {
                        this.addRealVis(itemstack, aspect, amt, true);
                        ((TileVisRelay)((WeakReference)TileVisRelay.nearbyPlayers.get(player.getEntityId())).get()).triggerConsumeEffect(aspect);
                     }
                  }
               }
            } else {
               TileVisRelay.nearbyPlayers.remove(player.getEntityId());
            }
         }
      }

   }

   public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
   }

   public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
   }

   public void addInformation(ItemStack stack, Player player, List list, boolean par4) {
      if (stack.getItemDamage() == 0) {
         list.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("item.ItemAmuletVis.text"));
      }

      list.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("item.capacity.text") + " " + this.getMaxVis(stack) / 100);
      if (stack.hasTagCompound()) {
         for(Aspect aspect : Aspect.getPrimalAspects()) {
            if (stack.stackTagCompound.hasKey(aspect.getTag())) {
               String amount = this.myFormatter.format((float)stack.stackTagCompound.getInteger(aspect.getTag()) / 100.0F);
               list.add(" §" + aspect.getChatcolor() + aspect.getName() + "§r x " + amount);
            }
         }
      }

   }

   public int getMaxVis(ItemStack stack) {
      return stack.getItemDamage() == 1 ? 25000 : 2500;
   }

   public int getVis(ItemStack is, Aspect aspect) {
      int out = 0;
      if (is.hasTagCompound() && is.stackTagCompound.hasKey(aspect.getTag())) {
         out = is.stackTagCompound.getInteger(aspect.getTag());
      }

      return out;
   }

   public void storeVis(ItemStack is, Aspect aspect, int amount) {
      is.setTagInfo(aspect.getTag(), new NBTTagInt(amount));
   }

   public AspectList getAspectsWithRoom(ItemStack wandstack) {
      AspectList out = new AspectList();
      AspectList cur = this.getAllVis(wandstack);

      for(Aspect aspect : cur.getAspects()) {
         if (cur.getAmount(aspect) < this.getMaxVis(wandstack)) {
            out.addAll(aspect, 1);
         }
      }

      return out;
   }

   public AspectList getAllVis(ItemStack is) {
      AspectList out = new AspectList();

      for(Aspect aspect : Aspect.getPrimalAspects()) {
         if (is.hasTagCompound() && is.stackTagCompound.hasKey(aspect.getTag())) {
            out.mergeWithHighest(aspect, is.stackTagCompound.getInteger(aspect.getTag()));
         } else {
            out.mergeWithHighest(aspect, 0);
         }
      }

      return out;
   }

   public boolean consumeAllVis(ItemStack is, Player player, AspectList aspects, boolean doit, boolean crafting) {
      if (aspects != null && aspects.size() != 0) {
         for(Aspect aspect : aspects.getAspects()) {
            if (this.getVis(is, aspect) < aspects.getAmount(aspect)) {
               return false;
            }
         }

         if (doit) {
            for(Aspect aspect : aspects.getAspects()) {
               this.storeVis(is, aspect, this.getVis(is, aspect) - aspects.getAmount(aspect));
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public int addVis(ItemStack is, Aspect aspect, int amount, boolean doit) {
      if (!aspect.isPrimal()) {
         return 0;
      } else {
         int storeAmount = this.getVis(is, aspect) + amount * 100;
         int leftover = Math.max(storeAmount - this.getMaxVis(is), 0);
         if (doit) {
            this.storeVis(is, aspect, Math.min(storeAmount, this.getMaxVis(is)));
         }

         return leftover / 100;
      }
   }

   public int addRealVis(ItemStack is, Aspect aspect, int amount, boolean doit) {
      if (!aspect.isPrimal()) {
         return 0;
      } else {
         int storeAmount = this.getVis(is, aspect) + amount;
         int leftover = Math.max(storeAmount - this.getMaxVis(is), 0);
         if (doit) {
            this.storeVis(is, aspect, Math.min(storeAmount, this.getMaxVis(is)));
         }

         return leftover;
      }
   }

   public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
      return itemstack.getItemDamage() != 1 || ResearchManager.isResearchComplete(player.getCommandSenderName(), "VISAMULET");
   }

   public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }

   public int getRunicCharge(ItemStack itemstack) {
      return 0;
   }
}
