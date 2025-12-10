package thaumcraft.common.entities.golems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.TileEssentiaReservoir;
import thaumcraft.common.tiles.TileJarFillable;
import thaumcraft.common.tiles.TileJarFillableVoid;

import javax.annotation.Nonnull;

public class GolemHelper {
   public static final double ADJACENT_RANGE = 4.0F;
   static HashMap<String,TileJarFillable> jarlist = new HashMap<>();
   private static ArrayList<Integer> reggedLiquids = null;
   static ArrayList<SortingItemTimeout> itemTimeout = new ArrayList<>();

   public static ArrayList<IInventory> getMarkedContainers(World world, EntityGolemBase golem) {
      ArrayList<IInventory> results = new ArrayList<>();

      for(Marker marker : golem.getMarkers()) {
         TileEntity te = world.getTileEntity(marker.x, marker.y, marker.z);
         if (marker.dim == world.provider.dimensionId && te instanceof IInventory) {
            results.add((IInventory)te);
            if (InventoryUtils.getDoubleChest(te) != null) {
               results.add(InventoryUtils.getDoubleChest(te));
            }
         }
      }

      return results;
   }

   public static ArrayList<IInventory> getMarkedContainersAdjacentToGolem(World world, EntityGolemBase golem) {
      ArrayList<IInventory> results = new ArrayList<>();

      for(IInventory inventory : getMarkedContainers(world, golem)) {
         TileEntity te = (TileEntity)inventory;
         if (golem.getDistanceSq((double)te.xCoord + (double)0.5F, (double)te.yCoord + (double)0.5F, (double)te.zCoord + (double)0.5F) < (double)4.0F) {
            results.add(inventory);
            if (InventoryUtils.getDoubleChest(te) != null) {
               results.add(InventoryUtils.getDoubleChest(te));
            }
         }
      }

      return results;
   }

   public static ArrayList<ChunkCoordinates> getMarkedBlocksAdjacentToGolem(World world, EntityGolemBase golem, byte color) {
      ArrayList<ChunkCoordinates> results = new ArrayList<>();

      for(Marker marker : golem.getMarkers()) {
         if ((marker.color == color || color == -1) && (golem.worldObj.getTileEntity(marker.x, marker.y, marker.z) == null || !(golem.worldObj.getTileEntity(marker.x, marker.y, marker.z) instanceof IInventory)) && golem.getDistanceSq((double)marker.x + (double)0.5F, (double)marker.y + (double)0.5F, (double)marker.z + (double)0.5F) < (double)4.0F) {
            results.add(new ChunkCoordinates(marker.x, marker.y, marker.z));
         }
      }

      return results;
   }

   public static ArrayList<IInventory> getContainersWithRoom(World world, EntityGolemBase golem, byte color) {
      ArrayList<IInventory> results = new ArrayList<>();

      for(IInventory inventory : getMarkedContainers(world, golem)) {
         boolean hasRoom = false;

         for(Integer side : getMarkedSides(golem, (TileEntity)inventory, color)) {
            ItemStack result = InventoryUtils.placeItemStackIntoInventory(golem.getCarried(), inventory, side, false);
            if (!ItemStack.areItemStacksEqual(result, golem.itemCarried)) {
               results.add(inventory);
               break;
            }

            if (InventoryUtils.getDoubleChest((TileEntity)inventory) != null) {
               result = InventoryUtils.placeItemStackIntoInventory(golem.getCarried(), InventoryUtils.getDoubleChest((TileEntity)inventory), side, false);
               if (!ItemStack.areItemStacksEqual(result, golem.itemCarried)) {
                  results.add(InventoryUtils.getDoubleChest((TileEntity)inventory));
               }
            }
         }
      }

      return results;
   }

   public static ArrayList<IInventory> getContainersWithRoom(World world, EntityGolemBase golem, byte color, ItemStack itemToMatch) {
      ArrayList<IInventory> results = new ArrayList<>();

      for(IInventory inventory : getMarkedContainers(world, golem)) {
         boolean hasRoom = false;

         for(Integer side : getMarkedSides(golem, (TileEntity)inventory, color)) {
            ItemStack result = InventoryUtils.placeItemStackIntoInventory(itemToMatch, inventory, side, false);
            if (!ItemStack.areItemStacksEqual(result, itemToMatch)) {
               results.add(inventory);
               break;
            }

            if (InventoryUtils.getDoubleChest((TileEntity)inventory) != null) {
               result = InventoryUtils.placeItemStackIntoInventory(itemToMatch, InventoryUtils.getDoubleChest((TileEntity)inventory), side, false);
               if (!ItemStack.areItemStacksEqual(result, itemToMatch)) {
                  results.add(InventoryUtils.getDoubleChest((TileEntity)inventory));
               }
            }
         }
      }

      return results;
   }

   public static List<Integer> getMarkedSides(EntityGolemBase golem, TileEntity tile, byte color) {
      return getMarkedSides(golem, tile.xCoord, tile.yCoord, tile.zCoord, tile.getWorldObj().provider.dimensionId, color);
   }

   public static List<Integer> getMarkedSides(EntityGolemBase golem, int x, int y, int z, int dim, byte color) {
      List<Integer> out = new ArrayList<>();
      ArrayList<Marker> gm = golem.getMarkers();
      if (gm != null && !gm.isEmpty()) {
         for(int a = 0; a < 6; ++a) {
            Marker marker = new Marker(x, y, z, dim, (byte)a, color);
            if (contained(gm, marker)) {
               out.add(a);
            }
         }

      }
       return out;
   }

   public static boolean contained(ArrayList<Marker> l, Marker m) {
      for(Marker mark : l) {
         if (m.equalsFuzzy(mark)) {
            return true;
         }
      }

      return false;
   }

   public static ArrayList<IInventory> getContainersWithGoods(World world, EntityGolemBase golem, ItemStack goods, byte color) {
      ArrayList<IInventory> results = new ArrayList<>();

      for(IInventory inventory : getMarkedContainers(world, golem)) {
         try {
            for(Integer side : getMarkedSides(golem, (TileEntity)inventory, color)) {
               if (InventoryUtils.extractStack(inventory, goods, side, golem.checkOreDict(), golem.ignoreDamage(), golem.ignoreNBT(), false) != null) {
                  results.add(inventory);
                  break;
               }

               if (InventoryUtils.getDoubleChest((TileEntity)inventory) != null && InventoryUtils.extractStack(InventoryUtils.getDoubleChest((TileEntity)inventory), goods, side, golem.checkOreDict(), golem.ignoreDamage(), golem.ignoreNBT(), false) != null) {
                  results.add(InventoryUtils.getDoubleChest((TileEntity)inventory));
                  break;
               }
            }
         } catch (Exception ignored) {
         }
      }

      return results;
   }

   public static ArrayList getMissingItems(EntityGolemBase golem) {
      ForgeDirection facing = ForgeDirection.getOrientation(golem.homeFacing);
      ChunkCoordinates home = golem.getHomePosition();
      int cX = home.posX - facing.offsetX;
      int cY = home.posY - facing.offsetY;
      int cZ = home.posZ - facing.offsetZ;
      int slotCount = golem.inventory.slotCount;
      if (golem.getToggles()[0]) {
         ArrayList<ItemStack> qr = new ArrayList<>();

         for(int q = 0; q < slotCount; ++q) {
            ItemStack toCheck = golem.inventory.inventory[q];
            if (toCheck != null) {
               ItemStack ret = toCheck.copy();
               qr.add(ret);
            }
         }

         return qr;
      } else {
         TileEntity tile = golem.worldObj.getTileEntity(cX, cY, cZ);
         if (tile == null) {
            return null;
         } else {
            ArrayList<ItemStack> qr = new ArrayList<>();

            label105:
            for(int q = 0; q < slotCount; ++q) {
               ItemStack toCheck = golem.inventory.inventory[q];
               if (toCheck != null) {
                  int foundAmount = 0;
                  boolean repeat = true;
                  boolean didRepeat = false;

                  while(repeat) {
                     if (didRepeat) {
                        repeat = false;
                     }

                     if (tile instanceof ISidedInventory && facing.ordinal() > -1) {
                        ISidedInventory isidedinventory = (ISidedInventory)tile;
                        int[] aint = isidedinventory.getAccessibleSlotsFromSide(facing.ordinal());

                         for (int i : aint) {
                             if (InventoryUtils.areItemStacksEqual(((ISidedInventory) tile).getStackInSlot(i), toCheck, golem.checkOreDict(), golem.ignoreDamage(), golem.ignoreNBT())) {
                                 foundAmount += ((ISidedInventory) tile).getStackInSlot(i).stackSize;
                                 if (foundAmount >= golem.inventory.getAmountNeededSmart(((ISidedInventory) tile).getStackInSlot(i), golem.getUpgradeAmount(5) > 0)) {
                                     continue label105;
                                 }
                             }
                         }
                     } else {
                        if (!(tile instanceof IInventory)) {
                           break;
                        }

                        int k = ((IInventory)tile).getSizeInventory();

                        for(int l = 0; l < k; ++l) {
                           if (InventoryUtils.areItemStacksEqual(((IInventory)tile).getStackInSlot(l), toCheck, golem.checkOreDict(), golem.ignoreDamage(), golem.ignoreNBT())) {
                              foundAmount += ((IInventory)tile).getStackInSlot(l).stackSize;
                              if (foundAmount >= golem.inventory.getAmountNeededSmart(((IInventory)tile).getStackInSlot(l), golem.getUpgradeAmount(5) > 0)) {
                                 continue label105;
                              }
                           }
                        }
                     }

                     if (!didRepeat && InventoryUtils.getDoubleChest(tile) != null) {
                        tile = InventoryUtils.getDoubleChest(tile);
                        didRepeat = true;
                     } else {
                        repeat = false;
                     }
                  }

                  ItemStack ret = toCheck.copy();
                  ret.stackSize -= foundAmount;
                  qr.add(ret);
               }
            }

            return qr;
         }
      }
   }

   public static ChunkCoordinates findJarWithRoom(EntityGolemBase golem) {
      ChunkCoordinates dest = null;
      World world = golem.worldObj;
      float dmod = golem.getRange();
      dmod *= dmod;
      ArrayList<TileEntity> jars = new ArrayList<>();
      ArrayList<TileEntity> others = new ArrayList<>();

      for(Marker marker : golem.getMarkers()) {
         TileEntity te = world.getTileEntity(marker.x, marker.y, marker.z);
         if (marker.dim == world.provider.dimensionId && te instanceof TileJarFillable) {
            if (te.getDistanceFrom(golem.getHomePosition().posX, golem.getHomePosition().posY, golem.getHomePosition().posZ) <= (double)dmod) {
               jars.add(te);
            }
         } else if (marker.dim == world.provider.dimensionId && te instanceof TileEssentiaReservoir) {
            TileEssentiaReservoir res = (TileEssentiaReservoir)te;
            if (res.getSuctionAmount(res.facing) > 0 && (res.getSuctionType(res.facing) == null || res.getSuctionType(res.facing) == golem.essentia) && te.getDistanceFrom(golem.getHomePosition().posX, golem.getHomePosition().posY, golem.getHomePosition().posZ) <= (double)dmod) {
               others.add(te);
            }
         } else if (marker.dim == world.provider.dimensionId && te instanceof IEssentiaTransport) {
            IEssentiaTransport trans = (IEssentiaTransport)te;
            if (golem.essentia != null && golem.essentiaAmount > 0 && trans.canInputFrom(ForgeDirection.getOrientation(marker.side)) && trans.getSuctionAmount(ForgeDirection.getOrientation(marker.side)) > 0 && (trans.getSuctionType(ForgeDirection.getOrientation(marker.side)) == null || trans.getSuctionType(ForgeDirection.getOrientation(marker.side)) == golem.essentia) && te.getDistanceFrom(golem.getHomePosition().posX, golem.getHomePosition().posY, golem.getHomePosition().posZ) <= (double)dmod) {
               others.add(te);
            }
         }
      }

      if (!jars.isEmpty()) {
         jarlist.clear();

         for(TileEntity jar : jars) {
            jarlist.put(jar.xCoord + ":" + jar.yCoord + ":" + jar.zCoord, (TileJarFillable)jar);
            getConnectedJars((TileJarFillable)jar);
         }
      } else if (others.isEmpty()) {
         return null;
      }

       jars = new ArrayList<>(others);

      for(TileJarFillable jar : jarlist.values()) {
         if (jar.aspect != null && jar.amount > 0 && jar.amount < jar.maxAmount && jar.aspectFilter != null && golem.essentiaAmount > 0 && jar.aspect.equals(golem.essentia) && jar.doesContainerAccept(golem.essentia)) {
            jars.add(jar);
         }
      }

      if (jars.isEmpty()) {
         for(TileJarFillable jar : jarlist.values()) {
            if ((jar.aspect == null || jar.amount == 0) && jar.aspectFilter != null && jar.doesContainerAccept(golem.essentia)) {
               jars.add(jar);
            }
         }
      }

      if (jars.isEmpty()) {
         for(TileJarFillable jar : jarlist.values()) {
            if (jar.aspect != null && jar.amount >= jar.maxAmount && jar instanceof TileJarFillableVoid && jar.aspectFilter != null && golem.essentiaAmount > 0 && jar.aspect.equals(golem.essentia) && jar.doesContainerAccept(golem.essentia)) {
               jars.add(jar);
            }
         }
      }

      if (jars.isEmpty()) {
         for(TileJarFillable jar : jarlist.values()) {
            if (jar.aspect != null && jar.amount > 0 && jar.amount < jar.maxAmount && jar.aspectFilter == null && golem.essentiaAmount > 0 && jar.aspect.equals(golem.essentia) && jar.doesContainerAccept(golem.essentia)) {
               jars.add(jar);
            }
         }
      }

      if (jars.isEmpty()) {
         for(TileJarFillable jar : jarlist.values()) {
            if ((jar.aspect == null || jar.amount == 0) && jar.aspectFilter == null && !(jar instanceof TileJarFillableVoid) && jar.doesContainerAccept(golem.essentia)) {
               jars.add(jar);
            }
         }
      }

      if (jars.isEmpty()) {
         for(TileJarFillable jar : jarlist.values()) {
            if (jar.aspect != null && jar instanceof TileJarFillableVoid && jar.aspectFilter == null && golem.essentiaAmount > 0 && jar.aspect.equals(golem.essentia) && jar.doesContainerAccept(golem.essentia)) {
               jars.add(jar);
            }
         }
      }

      if (jars.isEmpty()) {
         for(TileJarFillable jar : jarlist.values()) {
            if ((jar.aspect == null || jar.amount == 0) && jar.aspectFilter == null && jar instanceof TileJarFillableVoid && jar.doesContainerAccept(golem.essentia)) {
               jars.add(jar);
            }
         }
      }

      double dist = Double.MAX_VALUE;

      for(TileEntity jar : jars) {
         double d = jar.getDistanceFrom(golem.getHomePosition().posX, golem.getHomePosition().posY, golem.getHomePosition().posZ);
         if (jar instanceof TileJarFillableVoid) {
            d += dmod;
         }

         if (d < dist) {
            dist = d;
            dest = new ChunkCoordinates(jar.xCoord, jar.yCoord, jar.zCoord);
         }
      }

      jarlist.clear();
      return dest;
   }

   private static void getConnectedJars(TileJarFillable jar) {
      World world = jar.getWorldObj();

      for(int dir = 0; dir < 6; ++dir) {
         ForgeDirection fd = ForgeDirection.getOrientation(dir);
         int xx = jar.xCoord + fd.offsetX;
         int yy = jar.yCoord + fd.offsetY;
         int zz = jar.zCoord + fd.offsetZ;
         if (!jarlist.containsKey(xx + ":" + yy + ":" + zz)) {
            TileEntity te = world.getTileEntity(xx, yy, zz);
            if (te instanceof TileJarFillable) {
               jarlist.put(te.xCoord + ":" + te.yCoord + ":" + te.zCoord, (TileJarFillable)te);
               getConnectedJars((TileJarFillable)te);
            }
         }
      }

   }

   public static ArrayList<Integer> getReggedLiquids() {
      if (reggedLiquids == null) {
         reggedLiquids = new ArrayList<>();

          reggedLiquids.addAll(FluidRegistry.getRegisteredFluidIDs().values());
      }

      return reggedLiquids;
   }

   public static ArrayList<FluidStack> getMissingLiquids(EntityGolemBase golem) {
      ArrayList<FluidStack> out = new ArrayList<>();
      ForgeDirection facing = ForgeDirection.getOrientation(golem.homeFacing);
      ChunkCoordinates home = golem.getHomePosition();
      int cX = home.posX - facing.offsetX;
      int cY = home.posY - facing.offsetY;
      int cZ = home.posZ - facing.offsetZ;
      TileEntity tile = golem.worldObj.getTileEntity(cX, cY, cZ);
      if (tile instanceof IFluidHandler) {
         IFluidHandler fluidhandler = (IFluidHandler)tile;
         Iterator<Integer> i$ = getReggedLiquids().iterator();

         while(true) {
            Integer id;
            while(true) {
               if (!i$.hasNext()) {
                  return out;
               }

               id = i$.next();
               if ((golem.fluidCarried == null || golem.fluidCarried.amount <= 0 || golem.fluidCarried.getFluidID() == id) && fluidhandler.canFill(facing, FluidRegistry.getFluid(id))) {
                  FluidStack fs = new FluidStack(FluidRegistry.getFluid(id), Integer.MAX_VALUE);
                  if (!golem.inventory.hasSomething()) {
                     break;
                  }

                  FluidStack fis = null;
                  boolean found = false;

                  for(int a = 0; a < golem.inventory.slotCount; ++a) {
                     fis = FluidContainerRegistry.getFluidForFilledItem(golem.inventory.getStackInSlot(a));
                     if (fis != null && fis.isFluidEqual(fs)) {
                        found = true;
                        break;
                     }
                  }

                  if (found) {
                     break;
                  }
               }
            }

            out.add(new FluidStack(id, Integer.MAX_VALUE));
         }
      } else {
         return out;
      }
   }

   public static Vec3 findPossibleLiquid(FluidStack ls, EntityGolemBase golem) {
      ForgeDirection facing = ForgeDirection.getOrientation(golem.homeFacing);
      ChunkCoordinates home = golem.getHomePosition();
      int var10000 = home.posX - facing.offsetX;
      var10000 = home.posY - facing.offsetY;
      var10000 = home.posZ - facing.offsetZ;
      float dmod = golem.getRange();
      ChunkCoordinates v = null;
      ArrayList<IFluidHandler> fluidhandlers = getMarkedFluidHandlers(ls, golem.worldObj, golem);
      double dd = Double.MAX_VALUE;
      if (fluidhandlers != null) {
         for(IFluidHandler fluidhandler : fluidhandlers) {
            if (fluidhandler != null) {
               TileEntity tile = (TileEntity)fluidhandler;
               double d = golem.getDistanceSq((double)tile.xCoord + (double)0.5F, (double)tile.yCoord + (double)0.5F, (double)tile.zCoord + (double)0.5F);
               if (d <= (double)(dmod * dmod) && d < dd) {
                  dd = d;
                  v = new ChunkCoordinates(tile.xCoord, tile.yCoord, tile.zCoord);
               }
            }
         }
      }

      if (v == null) {
         ArrayList<ChunkCoordinates> inworld = getMarkedFluidBlocks(ls, golem.worldObj, golem);
         dd = Double.MAX_VALUE;
         if (inworld != null) {
            for(ChunkCoordinates coord : inworld) {
               if (coord != null) {
                  double d = golem.getDistanceSq((double)coord.posX + (double)0.5F, (double)coord.posY + (double)0.5F, (double)coord.posZ + (double)0.5F);
                  if (d <= (double)(dmod * dmod) && d < dd) {
                     dd = d;
                     v = new ChunkCoordinates(coord.posX, coord.posY, coord.posZ);
                  }
               }
            }
         }
      }

      return v != null ? Vec3.createVectorHelper(v.posX, v.posY, v.posZ) : null;
   }

   public static ArrayList<Marker> getMarkedFluidHandlersAdjacentToGolem(FluidStack ls, World world, EntityGolemBase golem) {
      ArrayList<Marker> results = new ArrayList<>();

      for(Marker marker : golem.getMarkers()) {
         TileEntity te = world.getTileEntity(marker.x, marker.y, marker.z);
         if (marker.dim == world.provider.dimensionId && te instanceof IFluidHandler && golem.getDistanceSq((double) te.xCoord + (double) 0.5F, (double) te.yCoord + (double) 0.5F, (double) te.zCoord + (double) 0.5F) < (double) 4.0F) {
            FluidStack fs = ((IFluidHandler)te).drain(ForgeDirection.getOrientation(marker.side), new FluidStack(ls.getFluid(), 1), false);
            if (fs != null && fs.amount > 0) {
               results.add(marker);
            }
         }
      }

      return results;
   }

   public static ArrayList getMarkedFluidHandlers(FluidStack ls, World world, EntityGolemBase golem) {
      ArrayList<IFluidHandler> results = new ArrayList<>();

      for(Marker marker : golem.getMarkers()) {
         TileEntity te = world.getTileEntity(marker.x, marker.y, marker.z);
         if (marker.dim == world.provider.dimensionId && te instanceof IFluidHandler) {
            FluidStack fs = ((IFluidHandler)te).drain(ForgeDirection.getOrientation(marker.side), new FluidStack(ls.getFluid(), 1), false);
            if (fs != null && fs.amount > 0) {
               results.add((IFluidHandler)te);
            }
         }
      }

      return results;
   }

   public static ArrayList getMarkedFluidBlocks(FluidStack ls, World world, EntityGolemBase golem) {
      ArrayList<ChunkCoordinates> results = new ArrayList<>();

      for(Marker marker : golem.getMarkers()) {
         Block bi = world.getBlock(marker.x, marker.y, marker.z);
         if (marker.dim == world.provider.dimensionId && FluidRegistry.getFluid(ls.getFluidID()).getBlock() == bi) {
            if (bi instanceof BlockFluidBase && ((BlockFluidBase)bi).canDrain(world, marker.x, marker.y, marker.z)) {
               results.add(new ChunkCoordinates(marker.x, marker.y, marker.z));
            } else if (ls.getFluidID() == FluidRegistry.WATER.getID() || ls.getFluidID() == FluidRegistry.LAVA.getID()) {
               int wmd = world.getBlockMetadata(marker.x, marker.y, marker.z);
               if ((FluidRegistry.lookupFluidForBlock(bi) == FluidRegistry.WATER && ls.getFluidID() == FluidRegistry.WATER.getID() || FluidRegistry.lookupFluidForBlock(bi) == FluidRegistry.LAVA && ls.getFluidID() == FluidRegistry.LAVA.getID()) && wmd == 0) {
                  results.add(new ChunkCoordinates(marker.x, marker.y, marker.z));
               }
            }
         }
      }

      return results;
   }

   public static ArrayList getItemsNeeded(EntityGolemBase golem, boolean fuzzy) {
      ArrayList<ItemStack> needed = null;
      switch (golem.getCore()) {
         case 1:
            needed = golem.inventory.getItemsNeeded(golem.getUpgradeAmount(5) > 0);
            if (needed.isEmpty()) {
               return null;
            }

            return filterEmptyCore(golem, needed);
         case 8:
            needed = golem.inventory.getItemsNeeded(golem.getUpgradeAmount(5) > 0);
            if (needed.isEmpty()) {
               return null;
            }

            return filterUseCore(golem, needed);
         case 10:
            needed = getItemsInHomeContainer(golem);
            return filterSortCore(golem, needed);
         default:
            return needed;
      }
   }

   private static ArrayList<ItemStack> filterEmptyCore(EntityGolemBase golem, ArrayList<ItemStack> in) {
      ArrayList<ItemStack> out = new ArrayList<>();

      for(ItemStack itemToMatch : in) {
         if (!isOnTimeOut(golem, itemToMatch) && findSomethingEmptyCore(golem, itemToMatch)) {
            out.add(itemToMatch);
         }
      }

      return out;
   }

   private static ArrayList<ItemStack> filterUseCore(EntityGolemBase golem, ArrayList<ItemStack> in) {
      ArrayList<ItemStack> out = new ArrayList<>();

      for(ItemStack itemToMatch : in) {
         if (!isOnTimeOut(golem, itemToMatch) && findSomethingUseCore(golem, itemToMatch)) {
            out.add(itemToMatch);
         }
      }

      return out;
   }

   private static ArrayList<ItemStack> filterSortCore(EntityGolemBase golem, ArrayList<ItemStack> in) {
      ArrayList<ItemStack> out = new ArrayList<>();

      for(ItemStack itemToMatch : in) {
         if (!isOnTimeOut(golem, itemToMatch) && findSomethingSortCore(golem, itemToMatch)) {
            out.add(itemToMatch);
         }
      }

      return out;
   }

   public static boolean findSomethingUseCore(EntityGolemBase golem, ItemStack itemToMatch) {
      for(byte col : golem.getColorsMatching(itemToMatch)) {
         for(Marker marker : golem.getMarkers()) {
            if ((marker.color == col || col == -1) && (!golem.getToggles()[0] || golem.worldObj.isAirBlock(marker.x, marker.y, marker.z)) && (golem.getToggles()[0] || !golem.worldObj.isAirBlock(marker.x, marker.y, marker.z))) {
               ForgeDirection opp = ForgeDirection.getOrientation(marker.side);
               if (golem.worldObj.isAirBlock(marker.x + opp.offsetX, marker.y + opp.offsetY, marker.z + opp.offsetZ)) {
                  return true;
               }
            }
         }
      }

      itemTimeout.add(new SortingItemTimeout(golem.getEntityId(), itemToMatch.copy(), System.currentTimeMillis() + (long)Config.golemIgnoreDelay));
      return false;
   }

   public static boolean findSomethingEmptyCore(EntityGolemBase golem, ItemStack itemToMatch) {
      ArrayList<Byte> matchingColors = golem.getColorsMatching(itemToMatch);

      for(byte color : matchingColors) {
         ArrayList<IInventory> markers = getContainersWithRoom(golem.worldObj, golem, color, itemToMatch);
         if (!markers.isEmpty()) {
            ForgeDirection i$1 = ForgeDirection.getOrientation(golem.homeFacing);
            ChunkCoordinates marker = golem.getHomePosition();
            int cX = marker.posX - i$1.offsetX;
            int cY = marker.posY - i$1.offsetY;
            int cZ = marker.posZ - i$1.offsetZ;
            double range = Double.MAX_VALUE;
            float dmod = golem.getRange();

            for(IInventory te : markers) {
               double distance = golem.getDistanceSq((double)((TileEntity)te).xCoord + (double)0.5F, (double)((TileEntity)te).yCoord + (double)0.5F, (double)((TileEntity)te).zCoord + (double)0.5F);
               if (distance < range && distance <= (double)(dmod * dmod) && (((TileEntity)te).xCoord != cX || ((TileEntity)te).yCoord != cY || ((TileEntity)te).zCoord != cZ)) {
                  return true;
               }
            }
         }
      }

      for(byte color : matchingColors) {
         for(Marker marker1 : golem.getMarkers()) {
            if ((marker1.color == color || color == -1) && (golem.worldObj.getTileEntity(marker1.x, marker1.y, marker1.z) == null || !(golem.worldObj.getTileEntity(marker1.x, marker1.y, marker1.z) instanceof IInventory))) {
               return true;
            }
         }
      }

      itemTimeout.add(new SortingItemTimeout(golem.getEntityId(), itemToMatch.copy(), System.currentTimeMillis() + (long)Config.golemIgnoreDelay));
      return false;
   }

   public static boolean findSomethingSortCore(EntityGolemBase golem, ItemStack itemToMatch) {
      ArrayList<IInventory> markers = getContainersWithRoom(golem.worldObj, golem, (byte)-1, itemToMatch);
      if (!markers.isEmpty()) {
         ForgeDirection i$1 = ForgeDirection.getOrientation(golem.homeFacing);
         ChunkCoordinates marker = golem.getHomePosition();
         int cX = marker.posX - i$1.offsetX;
         int cY = marker.posY - i$1.offsetY;
         int cZ = marker.posZ - i$1.offsetZ;
         double range = Double.MAX_VALUE;
         float dmod = golem.getRange();

         for(IInventory te : markers) {
            double distance = golem.getDistanceSq((double)((TileEntity)te).xCoord + (double)0.5F, (double)((TileEntity)te).yCoord + (double)0.5F, (double)((TileEntity)te).zCoord + (double)0.5F);
            if (distance < range && distance <= (double)(dmod * dmod) && (((TileEntity)te).xCoord != cX || ((TileEntity)te).yCoord != cY || ((TileEntity)te).zCoord != cZ)) {
               for(int side : getMarkedSides(golem, (TileEntity)te, (byte)-1)) {
                  if (InventoryUtils.inventoryContains(te, itemToMatch, side, golem.checkOreDict(), golem.ignoreDamage(), golem.ignoreNBT())) {
                     return true;
                  }
               }
            }
         }
      }

      itemTimeout.add(new SortingItemTimeout(golem.getEntityId(), itemToMatch.copy(), System.currentTimeMillis() + (long)Config.golemIgnoreDelay));
      return false;
   }

   public static boolean isOnTimeOut(EntityGolemBase golem, ItemStack stack) {
      SortingItemTimeout tos = new SortingItemTimeout(golem.getEntityId(), stack, 0L);
      if (itemTimeout.contains(tos)) {
         int q = itemTimeout.indexOf(tos);
         SortingItemTimeout tos2 = itemTimeout.get(q);
         if (System.currentTimeMillis() < tos2.time) {
            return true;
         }

         itemTimeout.remove(q);
      }

      return false;
   }

   public static boolean validTargetForItem(EntityGolemBase golem, ItemStack stack) {
      if (isOnTimeOut(golem, stack)) {
         return false;
      } else {
         ForgeDirection facing = ForgeDirection.getOrientation(golem.homeFacing);
         ChunkCoordinates home = golem.getHomePosition();
         int cX = home.posX - facing.offsetX;
         int cY = home.posY - facing.offsetY;
         int cZ = home.posZ - facing.offsetZ;
         switch (golem.getCore()) {
            case 1:
               return findSomethingEmptyCore(golem, stack);
            case 8:
               return findSomethingUseCore(golem, stack);
            case 10:
               return findSomethingSortCore(golem, stack);
            default:
               golem.worldObj.getTileEntity(cX, cY, cZ);
               ArrayList<ItemStack> neededList = getItemsNeeded(golem, golem.getUpgradeAmount(5) > 0);
               if (neededList != null && !neededList.isEmpty()) {
                  for(ItemStack ss : neededList) {
                     if (InventoryUtils.areItemStacksEqual(ss, golem.itemCarried, golem.checkOreDict(), golem.ignoreDamage(), golem.ignoreNBT())) {
                        return true;
                     }
                  }
               }

               itemTimeout.add(new SortingItemTimeout(golem.getEntityId(), stack.copy(), System.currentTimeMillis() + (long)Config.golemIgnoreDelay));
               return false;
         }
      }
   }

   public static ItemStack getFirstItemUsingTimeout(EntityGolemBase golem, IInventory inventory, int side, boolean doit) {
      ItemStack stack1 = null;
      if (inventory instanceof ISidedInventory && side > -1) {
         ISidedInventory isidedinventory = (ISidedInventory)inventory;
         int[] aint = isidedinventory.getAccessibleSlotsFromSide(side);

          for (int i : aint) {
              if (stack1 == null && inventory.getStackInSlot(i) != null) {
                  if (isOnTimeOut(golem, inventory.getStackInSlot(i))) {
                      continue;
                  }

                  stack1 = inventory.getStackInSlot(i).copy();
                  stack1.stackSize = golem.getCarrySpace();
              }

              if (stack1 != null) {
                  stack1 = InventoryUtils.attemptExtraction(inventory, stack1, i, side, false, false, false, doit);
              }

              if (stack1 != null) {
                  break;
              }
          }
      } else {
         int k = inventory.getSizeInventory();

         for(int l = 0; l < k; ++l) {
            if (stack1 == null && inventory.getStackInSlot(l) != null) {
               if (isOnTimeOut(golem, inventory.getStackInSlot(l))) {
                  continue;
               }

               stack1 = inventory.getStackInSlot(l).copy();
               stack1.stackSize = golem.getCarrySpace();
            }

            if (stack1 != null) {
               stack1 = InventoryUtils.attemptExtraction(inventory, stack1, l, side, false, false, false, doit);
            }

            if (stack1 != null) {
               break;
            }
         }
      }

      if (stack1 != null && stack1.stackSize != 0) {
         return stack1.copy();
      } else {
         if (doit) {
            inventory.markDirty();
         }

         return null;
      }
   }

   public static ArrayList getItemsInHomeContainer(EntityGolemBase golem) {
      ForgeDirection facing = ForgeDirection.getOrientation(golem.homeFacing);
      ChunkCoordinates home = golem.getHomePosition();
      int cX = home.posX - facing.offsetX;
      int cY = home.posY - facing.offsetY;
      int cZ = home.posZ - facing.offsetZ;
      TileEntity tile = golem.worldObj.getTileEntity(cX, cY, cZ);
      if (tile instanceof IInventory) {
         int[] aint = null;
         ArrayList<ItemStack> out = new ArrayList<>();
         IInventory inv = (IInventory)tile;
         if (tile instanceof ISidedInventory && facing.ordinal() > -1) {
            aint = ((ISidedInventory)inv).getAccessibleSlotsFromSide(facing.ordinal());
         } else {
            aint = new int[inv.getSizeInventory()];

            for(int a = 0; a < inv.getSizeInventory(); aint[a] = a++) {
            }
         }

         if (aint != null) {
             for (int i : aint) {
                 if (inv.getStackInSlot(i) != null) {
                     out.add(inv.getStackInSlot(i).copy());
                 }
             }
         }

         return out;
      } else {
         return null;
      }
   }

   public static class SortingItemTimeout implements Comparable<SortingItemTimeout> {
      ItemStack stack = null;
      int golemId = 0;
      long time = 0L;

      public SortingItemTimeout(int golemId, ItemStack stack, long time) {
         this.stack = stack;
         this.golemId = golemId;
         this.time = time;
      }

      public int compareTo(@Nonnull SortingItemTimeout arg0) {
         return this.equals(arg0) ? 0 : -1;
      }

      public boolean equals(Object obj) {
         if (obj instanceof SortingItemTimeout) {
            SortingItemTimeout t = (SortingItemTimeout)obj;
            if (this.golemId != t.golemId) {
               return false;
            }

            if (!this.stack.isItemEqual(t.stack)) {
               return false;
            }

             return ItemStack.areItemStackTagsEqual(this.stack, t.stack);
         }

         return true;
      }
   }
}
