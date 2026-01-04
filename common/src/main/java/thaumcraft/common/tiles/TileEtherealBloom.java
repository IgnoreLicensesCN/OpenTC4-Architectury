package thaumcraft.common.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.biome.BiomeGenBase;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

public class TileEtherealBloom extends TileEntity {
   public int counter = 0;
   public int growthCounter = 0;

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      super.updateEntity();
      if (this.counter == 0) {
         this.counter = this.level().rand.nextInt(100);
      }

      ++this.counter;
      if (Platform.getEnvironment() != Env.CLIENT && this.counter % 20 == 0) {
         int x = this.level().rand.nextInt(8) - this.level().rand.nextInt(8);
         int z = this.level().rand.nextInt(8) - this.level().rand.nextInt(8);
         int biomeID = this.level().getBiomeGenForCoords(x + this.xCoord, z + this.zCoord).biomeID;
         if (
                 (biomeID == Config.biomeTaintID
                         || biomeID == Config.biomeEerieID
                         || biomeID == Config.biomeMagicalForestID)
                   && this.getDistanceFrom((double)(x + this.xCoord) + (double)0.5F, this.yCoord, (double)(z + this.zCoord) + (double)0.5F) <= (double)81.0F
         ) {
            BiomeGenBase[] biomesForGeneration = null;
            biomesForGeneration = this.level().getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, x + this.xCoord, z + this.zCoord, 1, 1);
            if (biomesForGeneration != null && biomesForGeneration[0] != null) {
               BiomeGenBase biome = biomesForGeneration[0];
               if (biome.biomeID == ThaumcraftWorldGenerator.biomeTaint.biomeID) {
                  biome = BiomeGenBase.plains;
               }

               Utils.setBiomeAt(this.level(), x + this.xCoord, z + this.zCoord, biome);
            }
         }
      }

      if ((Platform.getEnvironment() == Env.CLIENT) && this.growthCounter == 0) {
         this.level().playSound((double)this.xCoord + (double)0.5F, (double)this.yCoord + (double)0.5F, (double)this.zCoord + (double)0.5F, "thaumcraft:roots", 1.0F, 0.6F, false);
      }

      ++this.growthCounter;
   }
}
