package thaumcraft.common.lib.events;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.Player;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import org.apache.logging.log4j.Level;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.monster.boss.EntityThaumcraftBoss;
import thaumcraft.common.items.ItemEssence;
import thaumcraft.common.items.equipment.ItemElementalPickaxe;
import thaumcraft.common.items.equipment.ItemPrimalCrusher;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.foci.ItemFocusExcavation;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ChunkLoc;
import thaumcraft.common.lib.world.dim.Cell;
import thaumcraft.common.lib.world.dim.CellLoc;
import thaumcraft.common.lib.world.dim.MazeHandler;
import thaumcraft.common.tiles.TileSensor;

import java.util.ArrayList;

public class EventHandlerWorld implements IFuelHandler {
   @SubscribeEvent
   public void worldLoad(WorldEvent.Load event) {
      if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
         MazeHandler.loadMaze(event.world);
      }

   }

   @SubscribeEvent
   public void worldSave(WorldEvent.Save event) {
      if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
         MazeHandler.saveMaze(event.world);
      }

   }

   @SubscribeEvent
   public void worldUnload(WorldEvent.Unload event) {
      if (!event.world.isRemote) {
         VisNetHandler.sources.remove(event.world.provider.dimensionId);

         try {
            TileSensor.noteBlockEvents.remove(event.world);
         } catch (Exception e) {
            FMLCommonHandler.instance().getFMLLogger().log(Level.WARN, "[Thaumcraft] Error unloading noteblock even handlers.", e);
         }

      }
   }

   @SubscribeEvent
   public void chunkSave(ChunkDataEvent.Save event) {
      NBTTagCompound var4 = new NBTTagCompound();
      event.getData().setTag("Thaumcraft", var4);
      var4.setBoolean(Config.regenKey, true);
   }

   @SubscribeEvent
   public void chunkLoad(ChunkDataEvent.Load event) {
      int dim = event.world.provider.dimensionId;
      ChunkCoordIntPair loc = event.getChunk().getChunkCoordIntPair();
      if (!event.getData().getCompoundTag("Thaumcraft").hasKey(Config.regenKey) && (Config.regenAmber || Config.regenAura || Config.regenCinnibar || Config.regenInfusedStone || Config.regenStructure || Config.regenTrees)) {
         FMLCommonHandler.instance().getFMLLogger().log(Level.WARN, "[Thaumcraft] World gen was never run for chunk at {}. Adding to queue for regeneration.",event.getChunk().getChunkCoordIntPair());
         ArrayList<ChunkLoc> chunks = (ArrayList)ServerTickEventsFML.chunksToGenerate.get(dim);
         if (chunks == null) {
            ServerTickEventsFML.chunksToGenerate.put(dim, new ArrayList());
            chunks = (ArrayList)ServerTickEventsFML.chunksToGenerate.get(dim);
         }

         if (chunks != null) {
            chunks.add(new ChunkLoc(loc.chunkXPos, loc.chunkZPos));
            ServerTickEventsFML.chunksToGenerate.put(dim, chunks);
         }
      }

   }

   public int getBurnTime(ItemStack fuel) {
      if (fuel.isItemEqual(new ItemStack(ConfigItems.itemResource, 1, 0))) {
         return 6400;
      } else {
         return fuel.isItemEqual(new ItemStack(ConfigBlocks.blockMagicalLog)) ? 400 : 0;
      }
   }

   @SubscribeEvent
   public void onCrafting(PlayerEvent.ItemCraftedEvent event) {
      int warp = ThaumcraftApi.getWarp(event.crafting);
      if (!Config.wuss && warp > 0 && !event.player.worldObj.isRemote) {
         Thaumcraft.addStickyWarpToPlayer(event.player, warp);
      }

      if (event.crafting.getItem() == ConfigItems.itemResource && event.crafting.getItemDamage() == 13 && event.crafting.hasTagCompound()) {
         for(int var2 = 0; var2 < 9; ++var2) {
            ItemStack var3 = event.craftMatrix.getStackInSlot(var2);
            if (var3 != null && var3.getItem() instanceof ItemEssence) {
               ++var3.stackSize;
               event.craftMatrix.setInventorySlotContents(var2, var3);
            }
         }
      }

      if (event.crafting.getItem() == Item.getItemFromBlock(ConfigBlocks.blockMetalDevice) && event.crafting.getItemDamage() == 3) {
         ItemStack var3 = event.craftMatrix.getStackInSlot(4);
         ++var3.stackSize;
         event.craftMatrix.setInventorySlotContents(4, var3);
      }

   }

   @SubscribeEvent
   public void harvestEvent(BlockEvent.HarvestDropsEvent event) {
      Player player = event.harvester;
      if (event.drops != null && !event.drops.isEmpty() && player != null) {
         ItemStack held = player.inventory.getCurrentItem();
         if (held != null && (held.getItem() instanceof ItemElementalPickaxe || held.getItem() instanceof ItemPrimalCrusher || held.getItem() instanceof ItemWandCasting && ((ItemWandCasting)held.getItem()).getFocus(held) != null && ((ItemWandCasting)held.getItem()).getFocus(held).isUpgradedWith(((ItemWandCasting)held.getItem()).getFocusItem(held), ItemFocusExcavation.dowsing))) {
            int fortune = EnchantmentHelper.getFortuneModifier(player);
            if (held.getItem() instanceof ItemWandCasting) {
               fortune = ((ItemWandCasting)held.getItem()).getFocus(held).getUpgradeLevel(((ItemWandCasting)held.getItem()).getFocusItem(held), FocusUpgradeType.treasure);
            }

            float chance = 0.2F + (float)fortune * 0.075F;

            for(int a = 0; a < event.drops.size(); ++a) {
               ItemStack is = event.drops.get(a);
               ItemStack smr = Utils.findSpecialMiningResult(is, chance, event.world.rand);
               if (!is.isItemEqual(smr)) {
                  event.drops.set(a, smr);
                  if (!event.world.isRemote) {
                     event.world.playSoundEffect((float)event.x + 0.5F, (float)event.y + 0.5F, (float)event.z + 0.5F, "random.orb", 0.2F, 0.7F + event.world.rand.nextFloat() * 0.2F);
                  }
               }
            }
         }

      }
   }

   @SubscribeEvent
   public void noteEvent(NoteBlockEvent.Play event) {
      if (!event.world.isRemote) {
         if (!TileSensor.noteBlockEvents.containsKey(event.world)) {
            TileSensor.noteBlockEvents.put(event.world, new ArrayList());
         }

         ArrayList<Integer[]> list = (ArrayList)TileSensor.noteBlockEvents.get(event.world);
         list.add(new Integer[]{event.x, event.y, event.z, event.instrument.ordinal(), event.getVanillaNoteId()});
         TileSensor.noteBlockEvents.put(event.world, list);
      }
   }

   @SubscribeEvent
   public void fillBucket(FillBucketEvent event) {
      if (event.target.typeOfHit == MovingObjectType.BLOCK) {
         if (event.world.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ) == ConfigBlocks.blockFluidPure && event.world.getBlockMetadata(event.target.blockX, event.target.blockY, event.target.blockZ) == 0) {
            event.world.setBlockToAir(event.target.blockX, event.target.blockY, event.target.blockZ);
            event.result = new ItemStack(ConfigItems.itemBucketPure);
            event.setResult(Result.ALLOW);
            return;
         }

         if (event.world.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ) == ConfigBlocks.blockFluidDeath && event.world.getBlockMetadata(event.target.blockX, event.target.blockY, event.target.blockZ) == 3) {
            event.world.setBlockToAir(event.target.blockX, event.target.blockY, event.target.blockZ);
            event.result = new ItemStack(ConfigItems.itemBucketDeath);
            event.setResult(Result.ALLOW);
            return;
         }
      }

   }

   @SubscribeEvent
   public void placeBlockEvent(BlockEvent.PlaceEvent event) {
      if (this.isNearActiveBoss(event.world, event.player, event.x, event.y, event.z)) {
         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   public void placeBlockEvent(BlockEvent.MultiPlaceEvent event) {
      if (this.isNearActiveBoss(event.world, event.player, event.x, event.y, event.z)) {
         event.setCanceled(true);
      }

   }

   private boolean isNearActiveBoss(World world, Player player, int x, int y, int z) {
      if (world.provider.dimensionId == Config.dimensionOuterId && player != null && !player.capabilities.isCreativeMode) {
         int xx = x >> 4;
         int zz = z >> 4;
         Cell c = MazeHandler.getFromHashMap(new CellLoc(xx, zz));
         if (c != null && c.feature >= 2 && c.feature <= 5) {
            ArrayList<Entity> list = EntityUtils.getEntitiesInRange(world, x, y, z, null, EntityThaumcraftBoss.class, 32.0F);
             return list != null && !list.isEmpty();
         }
      }

      return false;
   }
}
