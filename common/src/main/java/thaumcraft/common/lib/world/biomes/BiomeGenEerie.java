package thaumcraft.common.lib.world.biomes;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.world.biome.BiomeGenBase;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.EntityBrainyZombie;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityGiantBrainyZombie;
import thaumcraft.common.entities.monster.EntityWisp;

public class BiomeGenEerie extends BiomeGenBase {
   public BiomeGenEerie(int par1) {
      super(par1);
      this.spawnableCreatureList.clear();
      this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityBat.class, 3, 1, 1));
      this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityWitch.class, 8, 1, 1));
      this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityEnderman.class, 4, 1, 1));
      if (Config.spawnAngryZombie) {
         this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityBrainyZombie.class, 32, 1, 1));
         this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityGiantBrainyZombie.class, 8, 1, 1));
      }

      if (Config.spawnWisp) {
         this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityWisp.class, 3, 1, 1));
      }

      if (Config.spawnElder) {
         this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityEldritchGuardian.class, 1, 1, 1));
      }

      this.theBiomeDecorator.treesPerChunk = 2;
      this.theBiomeDecorator.flowersPerChunk = 1;
      this.theBiomeDecorator.grassPerChunk = 2;
      this.setTemperatureRainfall(0.5F, 0.5F);
      this.setBiomeName("Eerie");
      this.setColor(4212800);
   }

   @SideOnly(Side.CLIENT)
   public int getBiomeGrassColor(int x, int y, int z) {
      return 4212800;
   }

   @SideOnly(Side.CLIENT)
   public int getBiomeFoliageColor(int x, int y, int z) {
      return 4215616;
   }

   public int getSkyColorByTemp(float par1) {
      return 2237081;
   }

   public int getWaterColorMultiplier() {
      return 3035999;
   }

   public BiomeGenBase createMutation() {
      return null;
   }
}
