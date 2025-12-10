package thaumcraft.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemFood;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketAspectPool;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileManaPod;

import java.util.List;
import java.util.Random;

import static thaumcraft.api.aspects.AspectList.addAspectDescriptionToList;

public class ItemManaBean extends ItemFood implements IEssentiaContainerItem {
   public final int itemUseDuration = 10;
   public IIcon icon;
   Random rand = new Random();
   static Aspect[] displayAspects;

   public ItemManaBean() {
      super(1, 0.5F, true);
      this.setMaxStackSize(64);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
      this.setAlwaysEdible();
   }

   public int getMaxItemUseDuration(ItemStack par1ItemStack) {
      return this.itemUseDuration;
   }

   protected void onFoodEaten(ItemStack stack, World world, Player player) {
      if (!world.isRemote) {
         Potion p = Potion.potionTypes[world.rand.nextInt(Potion.potionTypes.length)];
         if (p != null) {
            if (p.isInstant()) {
               p.affectEntity(player, player, 2, 3.0F);
            } else {
               player.addPotionEffect(new PotionEffect(p.id, 160 + world.rand.nextInt(80), 0));
            }
         }

         if (world.rand.nextFloat() < 0.25F) {
            AspectList al = ((ItemManaBean)stack.getItem()).getAspects(stack);
            if (al != null && al.size() > 0) {
               Thaumcraft.proxy.playerKnowledge.addAspectPool(player.getCommandSenderName(), al.getAspects()[0], (short)1);
               ResearchManager.scheduleSave(player);
               PacketHandler.INSTANCE.sendTo(new PacketAspectPool(al.getAspects()[0].getTag(), (short) 1, Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(player.getCommandSenderName(), al.getAspects()[0])), (ServerPlayer)player);
            }
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon = ir.registerIcon("thaumcraft:mana_bean");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon;
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(this, 1, 0));
   }

   public void addInformation(ItemStack stack, Player player, List list, boolean par4) {
      AspectList aspects = this.getAspects(stack);
      addAspectDescriptionToList(aspects,player,list);

      super.addInformation(stack, player, list, par4);
   }

   @SideOnly(Side.CLIENT)
   public int getColorFromItemStack(ItemStack stack, int par2) {
      if (this.getAspects(stack) != null) {
         return this.getAspects(stack).getAspects()[0].getColor();
      } else {
         int idx = (int)(System.currentTimeMillis() / 500L % (long)displayAspects.length);
         return displayAspects[idx].getColor();
      }
   }

   public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
      if (!par2World.isRemote && !par1ItemStack.hasTagCompound()) {
         this.setAspects(par1ItemStack, (new AspectList()).add(displayAspects[this.rand.nextInt(displayAspects.length)], 1));
      }

      super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
   }

   public void onCreated(ItemStack par1ItemStack, World par2World, Player par3Player) {
      if (!par1ItemStack.hasTagCompound()) {
         this.setAspects(par1ItemStack, (new AspectList()).add(displayAspects[this.rand.nextInt(displayAspects.length)], 1));
      }

   }

   public AspectList getAspects(ItemStack itemstack) {
      if (itemstack.hasTagCompound()) {
         AspectList aspects = new AspectList();
         aspects.readFromNBT(itemstack.getTagCompound());
         return aspects.size() > 0 ? aspects : null;
      } else {
         return null;
      }
   }

   public void setAspects(ItemStack itemstack, AspectList aspects) {
      if (!itemstack.hasTagCompound()) {
         itemstack.setTagCompound(new NBTTagCompound());
      }

      aspects.writeToNBT(itemstack.getTagCompound());
   }

   public boolean onItemUse(ItemStack par1ItemStack, Player par2Player, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
      if (par2Player.canPlayerEdit(par4, par5, par6, par7, par1ItemStack) && par7 == 0) {
         BiomeGenBase biome = par3World.getBiomeGenForCoords(par4, par6);
         boolean magicBiome = false;
         if (biome != null) {
            magicBiome = BiomeDictionary.isBiomeOfType(biome, Type.MAGICAL);
         }

         if (!magicBiome) {
            return false;
         } else {
            Block i1 = par3World.getBlock(par4, par5, par6);
            if (i1 != Blocks.log && i1 != Blocks.log2 && i1 != ConfigBlocks.blockMagicalLog) {
               return false;
            } else {
               --par5;
               if (par3World.isAirBlock(par4, par5, par6)) {
                  int k1 = ConfigBlocks.blockManaPod.onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, 0);
                  par3World.setBlock(par4, par5, par6, ConfigBlocks.blockManaPod, k1, 2);
                  TileEntity tile = par3World.getTileEntity(par4, par5, par6);
                  if (tile instanceof TileManaPod && this.getAspects(par1ItemStack) != null && this.getAspects(par1ItemStack).size() > 0) {
                     ((TileManaPod)tile).aspect = this.getAspects(par1ItemStack).getAspects()[0];
                  }

                  if (!par2Player.capabilities.isCreativeMode) {
                     --par1ItemStack.stackSize;
                  }
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   static {
      displayAspects = Aspect.aspects.values().toArray(new Aspect[0]);
   }
}
