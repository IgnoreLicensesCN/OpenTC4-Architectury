package thaumcraft.common.items.misc;

import com.linearity.opentc4.utils.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.EntityAspectOrb;
import thaumcraft.common.entities.projectile.EntityAlumentum;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketAspectPool;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;
import thaumcraft.common.lib.network.playerdata.PacketResearchCompleteS2C;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.List;
import java.util.Random;

import static thaumcraft.api.aspects.AspectList.addAspectDescriptionToList;

//TODO:Migrate logic
public class ItemResource extends Item implements IEssentiaContainerItem {
   public IIcon[] icon = new IIcon[19];
   public IIcon iconOverlay;

   public ItemResource() {
      this.setMaxStackSize(64);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister ir) {
      this.icon[0] = ir.registerIcon("thaumcraft:alumentum");
      this.icon[1] = ir.registerIcon("thaumcraft:nitor");
      this.icon[2] = ir.registerIcon("thaumcraft:thaumiumingot");
      this.icon[3] = ir.registerIcon("thaumcraft:quicksilver");
      this.icon[4] = ir.registerIcon("thaumcraft:tallow");
      this.icon[5] = ir.registerIcon("thaumcraft:brain");
      this.icon[6] = ir.registerIcon("thaumcraft:amber");
      this.icon[7] = ir.registerIcon("thaumcraft:cloth");
      this.icon[8] = ir.registerIcon("thaumcraft:filter");
      this.icon[9] = ir.registerIcon("thaumcraft:knowledgefragment");
      this.icon[10] = ir.registerIcon("thaumcraft:mirrorglass");
      this.icon[11] = ir.registerIcon("thaumcraft:taint_slime");
      this.icon[12] = ir.registerIcon("thaumcraft:taint_tendril");
      this.icon[13] = ir.registerIcon("thaumcraft:label");
      this.iconOverlay = ir.registerIcon("thaumcraft:label_over");
      this.icon[14] = ir.registerIcon("thaumcraft:dust");
      this.icon[15] = ir.registerIcon("thaumcraft:charm");
      this.icon[16] = ir.registerIcon("thaumcraft:voidingot");
      this.icon[17] = ir.registerIcon("thaumcraft:voidseed");
      this.icon[18] = ir.registerIcon("thaumcraft:coin");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.icon[par1];
   }

   @SideOnly(Side.CLIENT)
   public int getRenderPasses(int metadata) {
      return metadata == 13 ? 2 : 1;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(ItemStack stack, int pass) {
      return pass != 0 && this.getAspects(stack) != null ? this.iconOverlay : this.getIconFromDamage(stack.getItemDamage());
   }

   @SideOnly(Side.CLIENT)
   public int getColorFromItemStack(ItemStack stack, int par2) {
      return par2 == 1 && stack.getItemDamage() == 13 && this.getAspects(stack) != null ? this.getAspects(stack).getAspects()[0].getColor() : 16777215;
   }

   @SideOnly(Side.CLIENT)
   public boolean requiresMultipleRenderPasses() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
      for(int a = 0; a <= 18; ++a) {
         if (a != 5) {
            par3List.add(new ItemStack(this, 1, a));
         }
      }

   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
   }

   public void onUpdate(ItemStack stack, Level world, Entity entity, int par4, boolean par5) {
      super.onUpdate(stack, world, entity, par4, par5);
      if (Platform.getEnvironment() != Env.CLIENT
              && (stack.getItemDamage() == 11 || stack.getItemDamage() == 12)
              && entity instanceof EntityLivingBase && !((EntityLivingBase)entity).isEntityUndead()
              && !((EntityLivingBase)entity).isPotionActive(ThaumcraftEffects.FLUX_TAINT) && world.getRandom().nextInt(4321) <= stack.stackSize) {
         ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(ThaumcraftEffects.FLUX_TAINT, 120, 0, false));
         if (entity instanceof Player) {
            String s = StatCollector.translateToLocal("tc.taint_item_poison").replace("%s", "§5§o" + stack.getDisplayName() + "§r");
            ((Player)entity).addChatMessage(new ChatComponentTranslation(s));
            InventoryUtils.consumeInventoryItem((Player)entity, stack.getItem(), stack.getItemDamage());
         }
      } else if (Platform.getEnvironment() != Env.CLIENT
              && stack.getItemDamage() == 15) {
         int r = world.getRandom().nextInt(20000);
         if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("blurb")) {
            stack.stackTagCompound.removeTag("blurb");
         }

         if (r < 20) {
            Aspect aspect = switch (world.getRandom()
                    .nextInt(6)) {
                case 0 -> Aspect.AIR;
                case 1 -> Aspect.EARTH;
                case 2 -> Aspect.FIRE;
                case 3 -> Aspect.WATER;
                case 4 -> Aspect.ORDER;
                case 5 -> Aspect.ENTROPY;
                default -> null;
            };

             if (aspect != null) {
               EntityAspectOrb orb = new EntityAspectOrb(world, entity.getX(), entity.getY(), entity.getZ(), aspect, 1);
               world.addFreshEntity(orb);
            }
         } else if (r == 42
                 && entity instanceof ServerPlayer player
                 && !ResearchManager.isResearchComplete(player.getName().getString(), "FOCUSPRIMAL")
                 && !ResearchManager.isResearchComplete(player.getName().getString(), "@FOCUSPRIMAL")
         ) {
            player.sendSystemMessage(
                    Component.literal("§5§o" + StatCollector.translateToLocal("tc.primalcharm.trigger")));
            new PacketResearchCompleteS2C("@FOCUSPRIMAL").sendTo(player);
            Thaumcraft.researchManager.completeResearch(player, "@FOCUSPRIMAL");
         }
      }

   }

//   public boolean onItemUse(ItemStack itemstack, Player player, World world, int x, int y, int z, int par7, float par8, float par9, float par10) {
//      if (itemstack.getItemDamage() != 1) {
//         return super.onItemUse(itemstack, player, world, x, y, z, par7, par8, par9, par10);
//      } else {
//         Block var11 = world.getBlock(x, y, z);
//         if (var11 == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1) {
//            par7 = 1;
//         } else if (var11 != Blocks.vine && var11 != Blocks.tallgrass && var11 != Blocks.deadbush && !var11.isReplaceable(world, x, y, z)) {
//            if (par7 == 0) {
//               --y;
//            }
//
//            if (par7 == 1) {
//               ++y;
//            }
//
//            if (par7 == 2) {
//               --z;
//            }
//
//            if (par7 == 3) {
//               ++z;
//            }
//
//            if (par7 == 4) {
//               --x;
//            }
//
//            if (par7 == 5) {
//               ++x;
//            }
//         }
//
//         if (itemstack.stackSize == 0) {
//            return false;
//         } else if (!player.canPlayerEdit(x, y, z, par7, itemstack)) {
//            return false;
//         } else if (world.canPlaceEntityOnSide(ConfigBlocks.blockAiry, x, y, z, false, par7, player, itemstack)) {
//            if (this.placeBlockAt(itemstack, player, world, x, y, z, par7, par8, par9, par10, ConfigBlocks.blockAiry, itemstack.getItemDamage())) {
//               world.playSoundEffect((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, ConfigBlocks.blockAiry.stepSound.getStepResourcePath(), (ConfigBlocks.blockAiry.stepSound.getVolume() + 1.0F) / 2.0F, ConfigBlocks.blockAiry.stepSound.getPitch() * 0.8F);
//               --itemstack.stackSize;
//               return true;
//            } else {
//               return false;
//            }
//         } else {
//            return false;
//         }
//      }
//   }

//   public boolean placeBlockAt(ItemStack stack, Player player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, Block bid, int metadata) {
//      if (!world.setBlock(x, y, z, bid, metadata, 3)) {
//         return false;
//      } else {
//         if (world.getBlock(x, y, z) == bid) {
//            bid.onBlockPlacedBy(world, x, y, z, player, stack);
//            bid.onPostBlockPlaced(world, x, y, z, metadata);
//         }
//
//         return true;
//      }
//   }

//   public ItemStack onItemRightClick(ItemStack itemstack, World world, Player player) {
//      if (itemstack.getItemDamage() == 0) {
//         if (!player.capabilities.isCreativeMode) {
//            --itemstack.stackSize;
//         }
//
//         world.playSoundAtEntity(player, "random.bow", 0.3F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
//         if (Platform.getEnvironment() != Env.CLIENT) {
//            world.spawnEntityInWorld(new EntityAlumentum(world, player));
//         }
//      } else
//      if (itemstack.getItemDamage() == 9) {
//         if (!player.capabilities.isCreativeMode) {
//            --itemstack.stackSize;
//         }
//
//         if (Platform.getEnvironment() != Env.CLIENT) {
//            for(Aspect a : Aspect.getPrimalAspects()) {
//               short q = (short)(world.getRandom().nextInt(2) + 1);
//               Thaumcraft.proxy.playerKnowledge.addAspectPool(player.getCommandSenderName(), a, q);
//               ResearchManager.scheduleSave(player);
//               PacketHandler.INSTANCE.sendTo(new PacketAspectPool(a.getTag(), q, Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(player.getCommandSenderName(), a)), (ServerPlayer)player);
//            }
//         }
//      }
//
//      return itemstack;
//   }

   public void addInformation(ItemStack stack, Player player, List list, boolean par4) {
      AspectList aspects = this.getAspects(stack);
      addAspectDescriptionToList(aspects,player,list);

//      if (stack.getItemDamage() == 15) {
//         Random rand = new Random(stack.hashCode() + player.ticksExisted / 120);
//         int r = rand.nextInt(200);
//         if (r < 25) {
//            list.add("§6" + StatCollector.translateToLocal("tc.primalcharm." + rand.nextInt(5)));
//         }
//      }

      super.addInformation(stack, player, list, par4);
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

   public int getItemStackLimit(ItemStack stack) {
      return stack.getItemDamage() == 15 ? 1 : super.getItemStackLimit(stack);
   }
}
