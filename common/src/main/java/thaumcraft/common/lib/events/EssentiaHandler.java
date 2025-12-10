package thaumcraft.common.lib.events;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.WorldCoordinates;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXEssentiaSource;
import thaumcraft.common.tiles.TileMirrorEssentia;

import java.util.ArrayList;
import java.util.HashMap;

public class EssentiaHandler {
   static final int DELAY = 5000;
   private static HashMap<WorldCoordinates,ArrayList<WorldCoordinates>> sources = new HashMap<>();
   private static HashMap<WorldCoordinates,Long> sourcesDelay = new HashMap<>();
   public static HashMap<String, EssentiaSourceFX> sourceFX = new HashMap<>();

   public static boolean drainEssentia(TileEntity tile, Aspect aspect, ForgeDirection direction, int range) {
      return drainEssentia(tile, aspect, direction, range, false);
   }

   public static boolean drainEssentia(TileEntity tile, Aspect aspect, ForgeDirection direction, int range, boolean ignoreMirror) {
      WorldCoordinates tileLoc = new WorldCoordinates(tile.xCoord, tile.yCoord, tile.zCoord, tile.getWorldObj().provider.dimensionId);
      if (!sources.containsKey(tileLoc)) {
         getSources(tile.getWorldObj(), tileLoc, direction, range);
         return sources.containsKey(tileLoc) && drainEssentia(tile, aspect, direction, range);
      } else {
         for(WorldCoordinates source : sources.get(tileLoc)) {
            TileEntity sourceTile = tile.getWorldObj().getTileEntity(source.x, source.y, source.z);
            if (!(sourceTile instanceof IAspectSource)) {
               break;
            }

            if (!ignoreMirror || !(sourceTile instanceof TileMirrorEssentia)) {
               IAspectSource as = (IAspectSource)sourceTile;
               if (as.takeFromContainer(aspect, 1)) {
                  SimpleNetworkWrapper var10000 = PacketHandler.INSTANCE;
                  PacketFXEssentiaSource var10001 = new PacketFXEssentiaSource(tile.xCoord, tile.yCoord, tile.zCoord, (byte)(tile.xCoord - source.x), (byte)(tile.yCoord - source.y), (byte)(tile.zCoord - source.z), aspect.getColor());
                  double var10005 = tile.xCoord;
                  double var10006 = tile.yCoord;
                  double var10007 = tile.zCoord;
                  var10000.sendToAllAround(var10001, new NetworkRegistry.TargetPoint(tile.getWorldObj().provider.dimensionId, var10005, var10006, var10007, 32.0F));
                  return true;
               }
            }
         }

         sources.remove(tileLoc);
         sourcesDelay.put(tileLoc, System.currentTimeMillis() + 5000L);
         return false;
      }
   }

   public static boolean findEssentia(TileEntity tile, Aspect aspect, ForgeDirection direction, int range) {
      WorldCoordinates tileLoc = new WorldCoordinates(tile.xCoord, tile.yCoord, tile.zCoord, tile.getWorldObj().provider.dimensionId);
      if (!sources.containsKey(tileLoc)) {
         getSources(tile.getWorldObj(), tileLoc, direction, range);
         return sources.containsKey(tileLoc) && findEssentia(tile, aspect, direction, range);
      } else {
         for(WorldCoordinates source : sources.get(tileLoc)) {
            TileEntity sourceTile = tile.getWorldObj().getTileEntity(source.x, source.y, source.z);
            if (!(sourceTile instanceof IAspectSource)) {
               break;
            }

            IAspectSource as = (IAspectSource)sourceTile;
            if (as.doesContainerContainAmount(aspect, 1)) {
               return true;
            }
         }

         sources.remove(tileLoc);
         sourcesDelay.put(tileLoc, System.currentTimeMillis() + 5000L);
         return false;
      }
   }

   private static void getSources(World world, WorldCoordinates tileLoc, ForgeDirection direction, int range) {
      if (sourcesDelay.containsKey(tileLoc)) {
         long d = sourcesDelay.get(tileLoc);
         if (d > System.currentTimeMillis()) {
            return;
         }

         sourcesDelay.remove(tileLoc);
      }

      TileEntity sourceTile = world.getTileEntity(tileLoc.x, tileLoc.y, tileLoc.z);
      ArrayList<WorldCoordinates> sourceList = new ArrayList<>();
      int start = 0;
      if (direction == ForgeDirection.UNKNOWN) {
         start = -range;
         direction = ForgeDirection.UP;
      }

      int xx = 0;
      int yy = 0;
      int zz = 0;

      for(int aa = -range; aa <= range; ++aa) {
         for(int bb = -range; bb <= range; ++bb) {
            for(int cc = start; cc < range; ++cc) {
               if (aa != 0 || bb != 0 || cc != 0) {
                  xx = tileLoc.x;
                  yy = tileLoc.y;
                  zz = tileLoc.z;
                  if (direction.offsetY != 0) {
                     xx += aa;
                     yy += cc * direction.offsetY;
                     zz += bb;
                  } else if (direction.offsetX == 0) {
                     xx += aa;
                     yy += bb;
                     zz += cc * direction.offsetZ;
                  } else {
                     xx += cc * direction.offsetX;
                     yy += aa;
                     zz += bb;
                  }

                  TileEntity te = world.getTileEntity(xx, yy, zz);
                  if (te instanceof IAspectSource && (!(sourceTile instanceof TileMirrorEssentia) || !(te instanceof TileMirrorEssentia) || sourceTile.xCoord != ((TileMirrorEssentia) te).linkX || sourceTile.yCoord != ((TileMirrorEssentia) te).linkY || sourceTile.zCoord != ((TileMirrorEssentia) te).linkZ || sourceTile.getWorldObj().provider.dimensionId != ((TileMirrorEssentia) te).linkDim)) {
                     sourceList.add(new WorldCoordinates(xx, yy, zz, world.provider.dimensionId));
                  }
               }
            }
         }
      }

      if (!sourceList.isEmpty()) {
         sources.put(tileLoc, sourceList);
      } else {
         sourcesDelay.put(tileLoc, System.currentTimeMillis() + 5000L);
      }

   }

   public static void refreshSources(TileEntity tile) {
      sources.remove(new WorldCoordinates(tile.xCoord, tile.yCoord, tile.zCoord, tile.getWorldObj().provider.dimensionId));
   }

   public static class EssentiaSourceFX {
      public ChunkCoordinates start;
      public ChunkCoordinates end;
      public int ticks;
      public int color;

      public EssentiaSourceFX(ChunkCoordinates start, ChunkCoordinates end, int ticks, int color) {
         this.start = start;
         this.end = end;
         this.ticks = ticks;
         this.color = color;
      }
   }
}
