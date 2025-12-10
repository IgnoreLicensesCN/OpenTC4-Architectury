package thaumcraft.common.lib.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import thaumcraft.api.WorldCoordinates;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXEssentiaSource;
import thaumcraft.common.tiles.TileMirrorEssentia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class EssentiaHandler {
   static final int DELAY = 5000;
   private static HashMap<WorldCoordinates, List<WorldCoordinates>> sources = new HashMap<>();
   private static HashMap<WorldCoordinates,Long> sourcesDelay = new HashMap<>();
   public static HashMap<String, EssentiaSourceFX> sourceFX = new HashMap<>();

   public static boolean drainEssentia(BlockEntity tile, Aspect aspect, Direction direction, int range) {
      return drainEssentia(tile, aspect, direction, range, false);
   }

   public static boolean drainEssentia(BlockEntity tile, Aspect aspect, Direction direction, int range, boolean ignoreMirror) {
      BlockPos pos = tile.getBlockPos();
      WorldCoordinates tileLoc = new WorldCoordinates(pos.getX(), pos.getY(), pos.getZ(), tile.getLevel().dimension().location().toString());
      if (!sources.containsKey(tileLoc)) {
         getSources(tile.getLevel(), tileLoc, direction, range);
         return sources.containsKey(tileLoc) && drainEssentia(tile, aspect, direction, range);
      } else {
         for(WorldCoordinates source : sources.get(tileLoc)) {
            BlockEntity sourceTile = tile.getLevel().getBlockEntity(new BlockPos(source.x, source.y, source.z));
            if (!(sourceTile instanceof IAspectSource)) {
               break;
            }

            if (!ignoreMirror || !(sourceTile instanceof TileMirrorEssentia)) {
               IAspectSource as = (IAspectSource)sourceTile;
               if (as.takeFromContainer(aspect, 1)) {
                  SimpleNetworkWrapper var10000 = PacketHandler.INSTANCE;
                  PacketFXEssentiaSource var10001 = new PacketFXEssentiaSource(
                          pos.getX(), pos.getY(), pos.getZ(),
                          (byte)(pos.getX() - source.x),
                          (byte)(pos.getY() - source.y),
                          (byte)(pos.getZ() - source.z),
                          aspect.getColor()
                  );
                  double var10005 = pos.getX();
                  double var10006 = pos.getY();
                  double var10007 = pos.getZ();
                  var10000.sendToAllAround(var10001,
                          new NetworkRegistry.TargetPoint(tile.getLevel().dimension(),
                          var10005, var10006, var10007, 32.0F)
                  );
                  return true;
               }
            }
         }

         sources.remove(tileLoc);
         sourcesDelay.put(tileLoc, System.currentTimeMillis() + 5000L);
         return false;
      }
   }

   public static boolean findEssentia(BlockEntity tile, Aspect aspect, Direction direction, int range) {
      BlockPos pos = tile.getBlockPos();
      var world = tile.getLevel();
      if (world == null) return false;
      WorldCoordinates tileLoc = new WorldCoordinates(pos.getX(), pos.getY(), pos.getZ(), world.dimension().location().toString());
      if (!sources.containsKey(tileLoc)) {
         getSources(world, tileLoc, direction, range);
         return sources.containsKey(tileLoc) && findEssentia(tile, aspect, direction, range);
      } else {
         for(WorldCoordinates source : sources.get(tileLoc)) {
            BlockEntity sourceTile = world.getBlockEntity(new BlockPos(source.x, source.y, source.z));
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

   private static void getSources(Level world, WorldCoordinates tileLoc, Direction direction, int range) {
      if (sourcesDelay.containsKey(tileLoc)) {
         long d = sourcesDelay.get(tileLoc);
         if (d > System.currentTimeMillis()) {
            return;
         }

         sourcesDelay.remove(tileLoc);
      }

      BlockEntity sourceTile = world.getBlockEntity(new BlockPos(tileLoc.x, tileLoc.y, tileLoc.z));
      if (sourceTile == null) {
         return;
      }
      BlockPos sourcePos = sourceTile.getBlockPos();
      ArrayList<WorldCoordinates> sourceList = new ArrayList<>();
      int start = 0;
      if (direction == null) {
         start = -range;
         direction = Direction.UP;
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
                  if (direction.getStepY() != 0) {
                     xx += aa;
                     yy += cc * direction.getStepY();
                     zz += bb;
                  } else if (direction.getStepX() == 0) {
                     xx += aa;
                     yy += bb;
                     zz += cc * direction.getStepZ();
                  } else {
                     xx += cc * direction.getStepX();
                     yy += aa;
                     zz += bb;
                  }

                  BlockEntity te = world.getBlockEntity(new BlockPos(xx, yy, zz));
                  String worldId = world.dimension().location().toString();

                  if (te instanceof IAspectSource && (
                          !(sourceTile instanceof TileMirrorEssentia)
                          || !(te instanceof TileMirrorEssentia)
                                  || sourcePos.getX() != ((TileMirrorEssentia) te).linkX
                                  || sourcePos.getY() != ((TileMirrorEssentia) te).linkY
                                  || sourcePos.getZ() != ((TileMirrorEssentia) te).linkZ
                                  || !Objects.equals(worldId, (TileMirrorEssentia) te).linkDim
                  )
                  ) {
                     sourceList.add(new WorldCoordinates(xx, yy, zz, worldId));
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

   public static void refreshSources(BlockEntity tile) {
      BlockPos pos = tile.getBlockPos();
      var world = tile.getLevel();
      if (world == null) {
         return;
      }
      var dimId = world.dimension().location().toString();
      sources.remove(new WorldCoordinates(pos.getX(), pos.getY(), pos.getZ(), dimId));
   }

   public static class EssentiaSourceFX {
      public BlockPos start;
      public BlockPos end;
      public int ticks;
      public int color;

      public EssentiaSourceFX(BlockPos start, BlockPos end, int ticks, int color) {
         this.start = start;
         this.end = end;
         this.ticks = ticks;
         this.color = color;
      }
   }
}
