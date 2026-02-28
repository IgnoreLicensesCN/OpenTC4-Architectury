package thaumcraft.common.lib.research;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.CompoundAspect;
import thaumcraft.common.lib.resourcelocations.ClueResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerKnowledge {
   public final Map<String, List<ResearchItemResourceLocation>> researchCompleted = new ConcurrentHashMap<>();
   public final Map<String, List<ClueResourceLocation>> clueCompleted = new ConcurrentHashMap<>();
   public final Map<String, AspectList<Aspect>> aspectsDiscovered = new ConcurrentHashMap<>();
   public final Map<String,List<String>> objectsScanned = new ConcurrentHashMap<>();
   public final Map<String,List<String>> entitiesScanned = new ConcurrentHashMap<>();
   public final Map<String,List<String>> phenomenaScanned = new ConcurrentHashMap<>();
   public final Map<String,Integer> warpCount = new ConcurrentHashMap<>();
   public final Map<String,Integer> warp = new ConcurrentHashMap<>();
   public final Map<String,Integer> warpSticky = new ConcurrentHashMap<>();
   public final Map<String,Integer> warpTemp = new ConcurrentHashMap<>();

   public void wipePlayerKnowledge(Player player) {
      this.researchCompleted.remove(player.getGameProfile().getName());
      this.clueCompleted.remove(player.getGameProfile().getName());
      this.aspectsDiscovered.remove(player.getGameProfile().getName());
      this.objectsScanned.remove(player.getGameProfile().getName());
      this.entitiesScanned.remove(player.getGameProfile().getName());
      this.phenomenaScanned.remove(player.getGameProfile().getName());
      this.warp.remove(player.getGameProfile().getName());
      this.warpTemp.remove(player.getGameProfile().getName());
      this.warpSticky.remove(player.getGameProfile().getName());
   }

   public AspectList<Aspect> getAspectsDiscovered(Player player) {
      AspectList<Aspect> known = this.aspectsDiscovered.get(player.getGameProfile().getName());
      if (known == null || known.size() <= 6) {
         this.addDiscoveredPrimalAspects(player);
         known = this.aspectsDiscovered.get(player.getGameProfile().getName());
      }

      return known;
   }

   //use Aspect#hasDiscoveredAspect
   @ApiStatus.Internal
   public boolean hasDiscoveredAspect(Player player, Aspect aspect) {
      return this.getAspectsDiscovered(player).getAspectView().containsKey(aspect);
   }

   public boolean hasDiscoveredParentAspects(Player player, Aspect aspect) {
      if (!(aspect instanceof CompoundAspect compoundAspect)) {
         return false;
      }
      var components = compoundAspect.components;
      var discovered = this.getAspectsDiscovered(player).getAspectView();
      return discovered.containsKey(components.aspectA()) && discovered.containsKey(components.aspectB());
   }

   public void addDiscoveredPrimalAspects(Player player) {
      AspectList<Aspect> known = this.aspectsDiscovered.get(player.getGameProfile().getName());
      if (known == null) {
         known = new AspectList<>();
      }

      if (!known.getAspectView().containsKey(Aspects.AIR)) {
         known.addAll(Aspects.AIR, 0);
      }

      if (!known.getAspectView().containsKey(Aspects.FIRE)) {
         known.addAll(Aspects.FIRE, 0);
      }

      if (!known.getAspectView().containsKey(Aspects.EARTH)) {
         known.addAll(Aspects.EARTH, 0);
      }

      if (!known.getAspectView().containsKey(Aspects.WATER)) {
         known.addAll(Aspects.WATER, 0);
      }

      if (!known.getAspectView().containsKey(Aspects.ORDER)) {
         known.addAll(Aspects.ORDER, 0);
      }

      if (!known.getAspectView().containsKey(Aspects.ENTROPY)) {
         known.addAll(Aspects.ENTROPY, 0);
      }

      this.aspectsDiscovered.put(player.getGameProfile().getName(), known);
   }

   /**
    * @return true if succeed added aspect,false if already exists
    */
   public boolean addDiscoveredAspect(Player player, Aspect aspect) {
      AspectList<Aspect> known = this.getAspectsDiscovered(player);
      if (!known.getAspectView().containsKey(aspect)) {
         known.addAll(aspect, 0);
         this.aspectsDiscovered.put(player.getGameProfile().getName(), known);
         return true;
      } else {
         return false;
      }
   }

   public short getAspectPoolFor(Player player, Aspect aspect) {
      AspectList<Aspect> known = this.getAspectsDiscovered(player);
      return known != null ? (short)known.getAmount(aspect) : 0;
   }

   public boolean addAspectPool(Player player, Aspect aspect, int amount) {
      AspectList<Aspect> al = this.getAspectsDiscovered(player);
      if (al == null) {
         al = new AspectList<>();
      }

      if (aspect != null && amount != 0) {
         boolean ret = false;
         if (amount > 0) {
            al.addAll(aspect, amount);
            ret = true;
         } else if (al.getAmount(aspect) > 0) {
            al.tryReduce(aspect, -amount);
            ret = true;
         }

         if (ret) {
            this.aspectsDiscovered.put(player.getGameProfile().getName(), al);
         }

         return ret;
      } else {
         return false;
      }
   }

   public boolean setAspectPool(Player player, Aspect aspect, int amount) {
      AspectList<Aspect> al = this.getAspectsDiscovered(player);
      if (al == null) {
         al = new AspectList<>();
      }

      if (aspect != null) {
         al.set(aspect,  amount);
         this.aspectsDiscovered.put(player.getGameProfile().getName(), al);
         return true;
      } else {
         return false;
      }
   }

   public int getWarpCounter(Player player) {
      int known = 0;
      var playerName = player.getGameProfile().getName();
      if (!this.warpCount.containsKey(playerName)) {
         this.warpCount.put(playerName, 0);
      } else {
         known = this.warpCount.get(playerName);
      }

      return known;
   }

   public void setWarpCounter(Player player, int amount) {
      this.warpCount.put(player.getGameProfile().getName(), amount);
   }

   public int getWarpTotal(Player player) {
      return this.getWarpPerm(player) + this.getWarpTemp(player) + this.getWarpSticky(player);
   }

   public int getWarpPerm(Player player) {
      int known = 0;
      var playerName = player.getGameProfile().getName();
      if (!this.warp.containsKey(playerName)) {
         this.warp.put(playerName, 0);
      } else {
         known = this.warp.get(playerName);
      }

      return known;
   }

   public int getWarpTemp(Player player) {
      int known = 0;
      var playerName = player.getGameProfile().getName();
      if (!this.warpTemp.containsKey(playerName)) {
         this.warpTemp.put(playerName, 0);
      } else {
         known = this.warpTemp.get(playerName);
      }

      return known;
   }

   public int getWarpSticky(Player player) {
      int known = 0;
      var playerName = player.getGameProfile().getName();
      if (!this.warpSticky.containsKey(playerName)) {
         this.warpSticky.put(playerName, 0);
      } else {
         known = this.warpSticky.get(playerName);
      }

      return known;
   }

   public void addWarpTemp(Player player, int amount) {
      int er = this.getWarpTemp(player) + amount;
      this.warpTemp.put(player.getGameProfile().getName(), Math.max(0, er));
   }

   public void addWarpPerm(Player player, int amount) {
      int er = this.getWarpPerm(player) + amount;
      this.warp.put(player.getGameProfile().getName(), Math.max(0, er));
   }

   public void addWarpSticky(Player player, int amount) {
      int er = this.getWarpSticky(player) + amount;
      this.warpSticky.put(player.getGameProfile().getName(), Math.max(0, er));
   }

   public void setWarpSticky(Player player, int amount) {
      this.warpSticky.put(player.getGameProfile().getName(), Math.max(0, amount));
   }

   public void setWarpPerm(Player player, int amount) {
      this.warp.put(player.getGameProfile().getName(), Math.max(0, amount));
   }

   public void setWarpTemp(Player player, int amount) {
      this.warpTemp.put(player.getGameProfile().getName(), Math.max(0, amount));
   }
}
