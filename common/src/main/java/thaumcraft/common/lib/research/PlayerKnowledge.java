package thaumcraft.common.lib.research;

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

   public void wipePlayerKnowledge(String player) {
      this.researchCompleted.remove(player);
      this.clueCompleted.remove(player);
      this.aspectsDiscovered.remove(player);
      this.objectsScanned.remove(player);
      this.entitiesScanned.remove(player);
      this.phenomenaScanned.remove(player);
      this.warp.remove(player);
      this.warpTemp.remove(player);
      this.warpSticky.remove(player);
   }

   public AspectList<Aspect> getAspectsDiscovered(String player) {
      AspectList<Aspect> known = this.aspectsDiscovered.get(player);
      if (known == null || known.size() <= 6) {
         this.addDiscoveredPrimalAspects(player);
         known = this.aspectsDiscovered.get(player);
      }

      return known;
   }

   public boolean hasDiscoveredAspect(String player, Aspect aspect) {
      return this.getAspectsDiscovered(player).getAspects().containsKey(aspect);
   }

   public boolean hasDiscoveredParentAspects(String player, Aspect aspect) {
      if (!(aspect instanceof CompoundAspect compoundAspect)) {
         return false;
      }
      var components = compoundAspect.components;
      var discovered = this.getAspectsDiscovered(player).getAspects();
      return discovered.containsKey(components.aspectA()) && discovered.containsKey(components.aspectB());
   }

   public void addDiscoveredPrimalAspects(String player) {
      AspectList<Aspect> known = this.aspectsDiscovered.get(player);
      if (known == null) {
         known = new AspectList<>();
      }

      if (!known.getAspects().containsKey(Aspects.AIR)) {
         known.addAll(Aspects.AIR, 0);
      }

      if (!known.getAspects().containsKey(Aspects.FIRE)) {
         known.addAll(Aspects.FIRE, 0);
      }

      if (!known.getAspects().containsKey(Aspects.EARTH)) {
         known.addAll(Aspects.EARTH, 0);
      }

      if (!known.getAspects().containsKey(Aspects.WATER)) {
         known.addAll(Aspects.WATER, 0);
      }

      if (!known.getAspects().containsKey(Aspects.ORDER)) {
         known.addAll(Aspects.ORDER, 0);
      }

      if (!known.getAspects().containsKey(Aspects.ENTROPY)) {
         known.addAll(Aspects.ENTROPY, 0);
      }

      this.aspectsDiscovered.put(player, known);
   }

   /**
    * @return true if succeed added aspect,false if already exists
    */
   public boolean addDiscoveredAspect(String player, Aspect aspect) {
      AspectList<Aspect> known = this.getAspectsDiscovered(player);
      if (!known.getAspects().containsKey(aspect)) {
         known.addAll(aspect, 0);
         this.aspectsDiscovered.put(player, known);
         return true;
      } else {
         return false;
      }
   }

   public short getAspectPoolFor(String username, Aspect aspect) {
      AspectList<Aspect> known = this.getAspectsDiscovered(username);
      return known != null ? (short)known.getAmount(aspect) : 0;
   }

   public boolean addAspectPool(String username, Aspect aspect, int amount) {
      AspectList<Aspect> al = this.getAspectsDiscovered(username);
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
            this.aspectsDiscovered.put(username, al);
         }

         return ret;
      } else {
         return false;
      }
   }

   public boolean setAspectPool(String username, Aspect aspect, int amount) {
      AspectList<Aspect> al = this.getAspectsDiscovered(username);
      if (al == null) {
         al = new AspectList<>();
      }

      if (aspect != null) {
         al.set(aspect,  amount);
         this.aspectsDiscovered.put(username, al);
         return true;
      } else {
         return false;
      }
   }

   public int getWarpCounter(String player) {
      int known = 0;
      if (!this.warpCount.containsKey(player)) {
         this.warpCount.put(player, 0);
      } else {
         known = this.warpCount.get(player);
      }

      return known;
   }

   public void setWarpCounter(String player, int amount) {
      this.warpCount.put(player, amount);
   }

   public int getWarpTotal(String player) {
      return this.getWarpPerm(player) + this.getWarpTemp(player) + this.getWarpSticky(player);
   }

   public int getWarpPerm(String player) {
      int known = 0;
      if (!this.warp.containsKey(player)) {
         this.warp.put(player, 0);
      } else {
         known = this.warp.get(player);
      }

      return known;
   }

   public int getWarpTemp(String player) {
      int known = 0;
      if (!this.warpTemp.containsKey(player)) {
         this.warpTemp.put(player, 0);
      } else {
         known = this.warpTemp.get(player);
      }

      return known;
   }

   public int getWarpSticky(String player) {
      int known = 0;
      if (!this.warpSticky.containsKey(player)) {
         this.warpSticky.put(player, 0);
      } else {
         known = this.warpSticky.get(player);
      }

      return known;
   }

   public void addWarpTemp(String player, int amount) {
      int er = this.getWarpTemp(player) + amount;
      this.warpTemp.put(player, Math.max(0, er));
   }

   public void addWarpPerm(String player, int amount) {
      int er = this.getWarpPerm(player) + amount;
      this.warp.put(player, Math.max(0, er));
   }

   public void addWarpSticky(String player, int amount) {
      int er = this.getWarpSticky(player) + amount;
      this.warpSticky.put(player, Math.max(0, er));
   }

   public void setWarpSticky(String player, int amount) {
      this.warpSticky.put(player, Math.max(0, amount));
   }

   public void setWarpPerm(String player, int amount) {
      this.warp.put(player, Math.max(0, amount));
   }

   public void setWarpTemp(String player, int amount) {
      this.warpTemp.put(player, Math.max(0, amount));
   }
}
