package thaumcraft.common.lib.events;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.utils.CubeChunkedWeakLookups;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.WorldCoordinates;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IRemoteAspectDrainerBlockEntity;
import thaumcraft.api.aspects.IRemoteDrainableAspectSourceBlockEntity;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXEssentiaSourceS2C;
import thaumcraft.common.tiles.TileMirrorEssentia;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class EssentiaRemoteDrainHandler {
   static final int DELAY = 5000;
   private static final Map<Level, CubeChunkedWeakLookups<IRemoteDrainableAspectSourceBlockEntity<? extends Aspect>>> levelledRemoteDrainables
           = new MapMaker().weakKeys().makeMap();
   public static void registerToRemoteDrainables(@NotNull Level level,BlockPos pos,IRemoteDrainableAspectSourceBlockEntity<? extends Aspect> drainable) {
      levelledRemoteDrainables.computeIfAbsent(level,_ignored -> new CubeChunkedWeakLookups<>((byte)4)).store(pos,drainable);
   }
   //return drained
   public static int drainEssentiaRemote(IRemoteAspectDrainerBlockEntity<? extends Aspect> tile, Aspect aspect, int range){
      return drainEssentiaRemote(tile,aspect,1,range);
   }
   //return drained
   public static int drainEssentiaRemote(IRemoteAspectDrainerBlockEntity<? extends Aspect> tile, Aspect aspect,int amount, int range) {
      var level = tile.getLevel();
      if (level == null) return 0;
      var lookup = levelledRemoteDrainables.get(level);
      if (lookup == null) return 0;
      var drainerPos = tile.getBlockPos();
      AtomicInteger toDrain = new AtomicInteger(amount);
      lookup.forItemsNearPosWithBreakWithRange(
              drainerPos,
              drainable -> {
                 Set<IRemoteAspectDrainerBlockEntity<? extends Aspect>> drainerMet = new HashSet<>();
                 drainerMet.add(tile);
                 int drained = ((IRemoteDrainableAspectSourceBlockEntity<Aspect>)drainable)
                         .drainAspectRemote(
                                 aspect,
                                 toDrain.get(),
                                 drainerMet
                         );
                 if (drained > 0){
                    drainable.playDrainEffect(tile,aspect);
                 }
                 return toDrain.addAndGet(
                         -drained
                 ) <= 0;
              },
              range
      );
      return amount - toDrain.get();
   }
}
