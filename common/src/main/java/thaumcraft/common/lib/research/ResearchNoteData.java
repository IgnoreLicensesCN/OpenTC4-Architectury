package thaumcraft.common.lib.research;

import net.minecraft.resources.ResourceLocation;
import thaumcraft.common.lib.utils.HexCoordUtils;

import java.util.HashMap;

public class ResearchNoteData {
   public ResourceLocation key;
   public int color;
   public HashMap<String,ResearchManager.HexEntry> hexEntries = new HashMap<>();
   public HashMap<String, HexCoordUtils.HexCoord> hexes = new HashMap<>();
   public boolean complete;
   public int copies;

   public boolean isComplete() {
      return this.complete;
   }
}
