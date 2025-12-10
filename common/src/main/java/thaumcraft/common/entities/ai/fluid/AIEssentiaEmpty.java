package thaumcraft.common.entities.ai.fluid;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.GolemHelper;
import thaumcraft.common.tiles.TileEssentiaReservoir;
import thaumcraft.common.tiles.TileJarFillable;

public class AIEssentiaEmpty extends EntityAIBase {
   private EntityGolemBase theGolem;
   private int jarX;
   private int jarY;
   private int jarZ;
   private ForgeDirection markerOrientation;
   private World theWorld;

   public AIEssentiaEmpty(EntityGolemBase par1EntityCreature) {
      this.theGolem = par1EntityCreature;
      this.theWorld = par1EntityCreature.worldObj;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      ChunkCoordinates home = this.theGolem.getHomePosition();
      if (this.theGolem.getNavigator().noPath() && this.theGolem.essentia != null && this.theGolem.essentiaAmount != 0) {
         ChunkCoordinates jarloc = GolemHelper.findJarWithRoom(this.theGolem);
         if (jarloc == null) {
            return false;
         } else if (this.theGolem.getDistanceSq((double)jarloc.posX + (double)0.5F, (double)jarloc.posY + (double)0.5F, (double)jarloc.posZ + (double)0.5F) > (double)4.0F) {
            return false;
         } else {
            this.jarX = jarloc.posX;
            this.jarY = jarloc.posY;
            this.jarZ = jarloc.posZ;
            return true;
         }
      } else {
         return false;
      }
   }

   public boolean continueExecuting() {
      return false;
   }

   public void startExecuting() {
      TileEntity tile = this.theWorld.getTileEntity(this.jarX, this.jarY, this.jarZ);
      if (tile instanceof TileJarFillable) {
         TileJarFillable jar = (TileJarFillable)tile;
         this.theGolem.essentiaAmount = jar.addToContainer(this.theGolem.essentia, this.theGolem.essentiaAmount);
         if (this.theGolem.essentiaAmount == 0) {
            this.theGolem.essentia = null;
         }

         this.theWorld.playSoundAtEntity(this.theGolem, "game.neutral.swim", 0.2F, 1.0F + (this.theWorld.rand.nextFloat() - this.theWorld.rand.nextFloat()) * 0.3F);
         this.theGolem.updateCarried();
         this.theWorld.markBlockForUpdate(this.jarX, this.jarY, this.jarZ);
      } else if (tile instanceof TileEssentiaReservoir) {
         TileEssentiaReservoir trans = (TileEssentiaReservoir)tile;
         if (trans.getSuctionAmount(trans.facing) > 0 && (trans.getSuctionType(trans.facing) == null || trans.getSuctionType(trans.facing) == this.theGolem.essentia)) {
            int added = trans.addEssentia(this.theGolem.essentia, this.theGolem.essentiaAmount, trans.facing);
            if (added > 0) {
               EntityGolemBase var9 = this.theGolem;
               var9.essentiaAmount -= added;
               if (this.theGolem.essentiaAmount == 0) {
                  this.theGolem.essentia = null;
               }

               this.theWorld.playSoundAtEntity(this.theGolem, "game.neutral.swim", 0.2F, 1.0F + (this.theWorld.rand.nextFloat() - this.theWorld.rand.nextFloat()) * 0.3F);
               this.theGolem.updateCarried();
               this.theWorld.markBlockForUpdate(this.jarX, this.jarY, this.jarZ);
            }
         }
      } else if (tile instanceof IEssentiaTransport) {
         for(Integer side : GolemHelper.getMarkedSides(this.theGolem, tile, (byte)-1)) {
            IEssentiaTransport trans = (IEssentiaTransport)tile;
            if (trans.canInputFrom(ForgeDirection.getOrientation(side)) && trans.getSuctionAmount(ForgeDirection.getOrientation(side)) > 0 && (trans.getSuctionType(ForgeDirection.getOrientation(side)) == null || trans.getSuctionType(ForgeDirection.getOrientation(side)) == this.theGolem.essentia)) {
               int added = trans.addEssentia(this.theGolem.essentia, this.theGolem.essentiaAmount, ForgeDirection.getOrientation(side));
               if (added > 0) {
                  EntityGolemBase var10000 = this.theGolem;
                  var10000.essentiaAmount -= added;
                  if (this.theGolem.essentiaAmount == 0) {
                     this.theGolem.essentia = null;
                  }

                  this.theWorld.playSoundAtEntity(this.theGolem, "game.neutral.swim", 0.2F, 1.0F + (this.theWorld.rand.nextFloat() - this.theWorld.rand.nextFloat()) * 0.3F);
                  this.theGolem.updateCarried();
                  this.theWorld.markBlockForUpdate(this.jarX, this.jarY, this.jarZ);
                  break;
               }
            }
         }
      }

   }
}
