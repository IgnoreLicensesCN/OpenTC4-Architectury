package thaumcraft.common.entities.golems;

import it.unimi.dsi.fastutil.bytes.ByteSet;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.linearity.opentc4.OpenTC4.DEBUG_FLAG;
import static com.linearity.opentc4.OpenTC4.DebugFlags.VALIDATE_MARKER_COLOR;

@Getter
public class Marker {
   public static final byte INVALID_MARKER_COLOR = 0;
   public static final byte MARKER_COLOR_MAX = 16;

   protected @NotNull BlockPos pos;
   protected @NotNull ResourceLocation dim;
   protected @NotNull Direction side;
   protected byte color;

   public static class MutableMarker extends Marker {
      public MutableMarker(@NotNull BlockPos pos, @NotNull ResourceLocation dim, @NotNull Direction side, byte color) {
         super(pos, dim, side, color);
      }

      public void setPos(@NotNull BlockPos pos) {
         this.pos = pos;
      }

      public void setDim(@NotNull ResourceLocation dim) {
         this.dim = dim;
      }

      public void setSide(@NotNull Direction side) {
         this.side = side;
      }

      public void setColor(byte color) {
         this.color = color;
      }
   }

   public Marker(@NotNull BlockPos pos,@NotNull ResourceLocation dim,@NotNull Direction side, byte color) {
      this.pos = pos;
      this.dim = dim;
      this.side = side;
      this.color = color;
      if (DEBUG_FLAG && VALIDATE_MARKER_COLOR){
         if (color < INVALID_MARKER_COLOR || color > MARKER_COLOR_MAX){
            throw new IllegalArgumentException("Invalid marker color: " + color);
         }
      }
   }

   public boolean equals(Object obj) {
      if (obj instanceof Marker marker) {
          return Objects.equals(this.getPos(), marker.getPos())
                  && Objects.equals(this.getDim(), marker.getDim())
                  && this.getSide() == marker.getSide()
                  && this.getColor() == marker.getColor()
                  ;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int result = getPos().hashCode();
      result = 31 * result + getSide().hashCode();
      return result;
   }

   public boolean equalsFuzzy(Object obj) {
      if (!(obj instanceof Marker marker)) {
         return false;
      }
      return equalsFuzzy(marker.getPos().getX(), marker.getPos().getY(), marker.getPos().getZ(), marker.getDim(), marker.getSide(), marker.getColor());
   }

   public boolean equalsFuzzy(int x, int y, int z, ResourceLocation dimResLoc, @Nullable("null -> any") Direction side, byte color) {
      return x == this.getPos().getX()
              && y == this.getPos().getY()
              && z == this.getPos().getZ()
              && dimResLoc.equals(this.getDim())
              && sideEqualsFuzzy(side)
              && colorEqualsFuzzy(color);
   }

   private boolean sideEqualsFuzzy(@Nullable("null -> any") Direction side) {
      return side == null || side == this.getSide();
   }

   public boolean equalsFuzzy(Vec3i blockPos, ResourceLocation dimResLoc, @Nullable("null -> any") Direction side, byte color) {
      return equalsFuzzy(blockPos.getX(),blockPos.getY(),blockPos.getZ(),dimResLoc,side,color);
   }
   public boolean colorEqualsFuzzy(byte color) {
      return this.getColor() == color || this.getColor() == INVALID_MARKER_COLOR || color == INVALID_MARKER_COLOR;
   }
   public boolean colorEqualsFuzzy(ByteSet colors) {
      if (this.getColor() == INVALID_MARKER_COLOR){
         return !colors.isEmpty();
      }
      if (colors.contains(this.getColor())) {
         return true;
      }
       return colors.contains(INVALID_MARKER_COLOR);
   }
}
