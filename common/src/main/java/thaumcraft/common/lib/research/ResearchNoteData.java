package thaumcraft.common.lib.research;

import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;
import thaumcraft.common.lib.utils.HexCoord;

import java.util.HashMap;
import java.util.Map;

public class ResearchNoteData {
   public ResearchItemResourceLocation key;
   public int color;
   public Map<HexCoord, HexEntry> hexGrid = new HashMap<>();
   @Deprecated(forRemoval = true)
   public Map<String, HexCoord> hexes = new HashMap<>();
   public boolean completed;
   public int copies;

   public boolean isCompleted() {
      return this.completed;
   }
}
