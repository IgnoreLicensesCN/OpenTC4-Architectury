package thaumcraft.common.entities.golems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Marker {
   public final @NotNull BlockPos pos;
   public final @NotNull ResourceLocation dim;
   public final @NotNull Direction side;
   public final byte color;

   public Marker(@NotNull BlockPos pos,@NotNull ResourceLocation dim,@NotNull Direction side, byte color) {
      this.pos = pos;
      this.dim = dim;
      this.side = side;
      this.color = color;
   }

   public boolean equals(Object obj) {
      if (obj instanceof Marker marker) {
          return Objects.equals(this.pos, marker.pos)
                  && Objects.equals(this.dim,marker.dim)
                  && this.side == marker.side
                  && this.color == marker.color
                  ;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int result = pos.hashCode();
      result = 31 * result + side.hashCode();
      return result;
   }

   public boolean equalsFuzzy(Object obj) {
      if (!(obj instanceof Marker marker)) {
         return false;
      } else {
          return Objects.equals(this.pos, marker.pos)
                  && Objects.equals(this.dim,marker.dim)
                  && this.side == marker.side
                  && (this.color == marker.color || this.color == -1 || marker.color == -1);
      }
   }
}
