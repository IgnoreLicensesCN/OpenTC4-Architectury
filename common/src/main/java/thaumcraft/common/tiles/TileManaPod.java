package thaumcraft.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.IAspectContainerBlockEntity;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.ArrayList;

public class TileManaPod extends TileThaumcraft implements IAspectContainerBlockEntity {
   public Aspect aspect = null;

   public boolean canUpdate() {
      return false;
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.aspect = Aspect.getAspect(nbttagcompound.getString("aspect"));
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      if (this.aspect != null) {
         nbttagcompound.setString("aspect", this.aspect.getAspectKey());
      }

   }

   public void checkGrowth() {
      int l = this.level().getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
      if (l < 7) {
         ++l;
         this.level().setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, l, 3);
      }

      if (l > 2) {
         if (l == 3) {
            AspectList<Aspect>al = new AspectList<>();
            if (this.aspect != null) {
               al.addAll(this.aspect, 1);
            }

            for(int d = 2; d < 6; ++d) {
               Direction dir = Direction.getOrientation(d);
               int x = this.xCoord + dir.offsetX;
               int y = this.yCoord + dir.offsetY;
               int z = this.zCoord + dir.offsetZ;
               TileEntity tile = this.level().getTileEntity(x, y, z);
               if (tile instanceof TileManaPod && ((TileManaPod) tile).aspect != null) {
                  al.addAll(((TileManaPod)tile).aspect, 1);
               }
            }

            if (al.size() > 1) {
               Aspect[] aa = al.getAspects();
               ArrayList<Aspect> outlist = new ArrayList<>();

               for(int i = 0; i < aa.length; ++i) {
                  outlist.add(aa[i]);

                  for(int j = 0; j < aa.length; ++j) {
                     if (i != j) {
                        Aspect combo = ResearchManager.getCombinationResult(aa[i], aa[j]);
                        if (combo != null) {
                           outlist.add(combo);
                           outlist.add(combo);
                        }
                     }
                  }
               }

               if (!outlist.isEmpty()) {
                  this.aspect = outlist.get(this.level().rand.nextInt(outlist.size()));
                  this.markDirty();
               }
            }

            if (al.size() >= 1 && this.aspect == null) {
               this.aspect = al.getAspectsSortedAmount()[0];
               this.markDirty();
            }
         }

         if (this.aspect == null) {
            if (this.level().rand.nextInt(8) == 0) {
               this.aspect = Aspects.PLANT;
            } else {
               ArrayList<Aspect> outlist = Aspects.getPrimalAspects();
               this.aspect = outlist.get(this.level().rand.nextInt(outlist.size()));
            }

            this.markDirty();
         }
      }

   }

   public @NotNull AspectList<Aspect>getAspects() {
      return this.aspect != null && this.getBlockMetadata() == 7 ? (new AspectList<>()).addAll(this.aspect, 1) : null;
   }

   public void setAspects(AspectList<Aspect>aspects) {
   }

   public boolean doesContainerAccept(Aspect tag) {
      return false;
   }

   public int addIntoContainer(Aspect tag, int amount) {
      return 0;
   }

   public boolean takeFromContainer(Aspect tag, int amount) {
      return false;
   }

   public boolean takeFromContainer(AspectList<Aspect>ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tag, int amount) {
      return false;
   }

   public boolean doesContainerContain(AspectList<Aspect>ot) {
      return false;
   }

   public int containerContains(Aspect tag) {
      return 0;
   }
}
