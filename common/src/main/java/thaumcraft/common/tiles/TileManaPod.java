package thaumcraft.common.tiles;

import java.util.ArrayList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.common.lib.research.ResearchManager;

public class TileManaPod extends TileThaumcraft implements IAspectContainer {
   public Aspect aspect = null;

   public boolean canUpdate() {
      return false;
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.aspect = Aspect.getAspect(nbttagcompound.getString("aspect"));
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      if (this.aspect != null) {
         nbttagcompound.setString("aspect", this.aspect.getTag());
      }

   }

   public void checkGrowth() {
      int l = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
      if (l < 7) {
         ++l;
         this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, l, 3);
      }

      if (l > 2) {
         if (l == 3) {
            AspectList al = new AspectList();
            if (this.aspect != null) {
               al.add(this.aspect, 1);
            }

            for(int d = 2; d < 6; ++d) {
               ForgeDirection dir = ForgeDirection.getOrientation(d);
               int x = this.xCoord + dir.offsetX;
               int y = this.yCoord + dir.offsetY;
               int z = this.zCoord + dir.offsetZ;
               TileEntity tile = this.worldObj.getTileEntity(x, y, z);
               if (tile instanceof TileManaPod && ((TileManaPod) tile).aspect != null) {
                  al.add(((TileManaPod)tile).aspect, 1);
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
                  this.aspect = outlist.get(this.worldObj.rand.nextInt(outlist.size()));
                  this.markDirty();
               }
            }

            if (al.size() >= 1 && this.aspect == null) {
               this.aspect = al.getAspectsSortedAmount()[0];
               this.markDirty();
            }
         }

         if (this.aspect == null) {
            if (this.worldObj.rand.nextInt(8) == 0) {
               this.aspect = Aspect.PLANT;
            } else {
               ArrayList<Aspect> outlist = Aspect.getPrimalAspects();
               this.aspect = outlist.get(this.worldObj.rand.nextInt(outlist.size()));
            }

            this.markDirty();
         }
      }

   }

   public AspectList getAspects() {
      return this.aspect != null && this.getBlockMetadata() == 7 ? (new AspectList()).add(this.aspect, 1) : null;
   }

   public void setAspects(AspectList aspects) {
   }

   public boolean doesContainerAccept(Aspect tag) {
      return false;
   }

   public int addToContainer(Aspect tag, int amount) {
      return 0;
   }

   public boolean takeFromContainer(Aspect tag, int amount) {
      return false;
   }

   public boolean takeFromContainer(AspectList ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tag, int amount) {
      return false;
   }

   public boolean doesContainerContain(AspectList ot) {
      return false;
   }

   public int containerContains(Aspect tag) {
      return 0;
   }
}
