package thaumcraft.common.lib.world.biomes;

import com.linearity.colorannotation.annotation.RGBColor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.ThaumcraftEntities;

//TODO:Rewrite biome(GPT made mess and idk what happened before)
public class BiomeGenEerie /*extends BiomeGenBase*/ {
   public static @RGBColor int waterColor = 0x2E535F;
   public static @RGBColor int foliageColorOverride = 0x405340;
   public static @RGBColor int grassColorOverride = 0x404840;
   public static @RGBColor int skyColor = 0x222299;

   public static Biome createEerie() {
      MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

      // 生物生成
      spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.BAT, 3, 1, 1));
      spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 8, 1, 1));
      spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 4, 1, 1));

      if (Config.spawnAngryZombie) {
         spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ThaumcraftEntities.BRAINY_ZOMBIE, 32, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ThaumcraftEntities.GIANT_BRAINY_ZOMBIE, 8, 1, 1));
      }

      if (Config.spawnWisp) {
         spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ThaumcraftEntities.WISP, 3, 1, 1));
      }

      if (Config.spawnElder) {
         spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ThaumcraftEntities.ELDRITCH_GUARDIAN, 1, 1, 1));
      }

      BiomeGenerationSettings.PlainBuilder generation = new BiomeGenerationSettings.PlainBuilder();

      //since this biome is from nodetype DARK,no generations needed here

      // 构建 Biome
      return new Biome.BiomeBuilder()
              .temperature(0.5F)
              .downfall(0.5F)
              .specialEffects(
                      new BiomeSpecialEffects.Builder()
                              .waterColor(waterColor)
                              .foliageColorOverride(foliageColorOverride)
                              .grassColorOverride(grassColorOverride)
                              .skyColor(skyColor)
                              .build()
              )
              .mobSpawnSettings(spawns.build())
              .generationSettings(generation.build())
              .build();
   }

//   public BiomeGenEerie(int par1) {
//      super(par1);
//      this.spawnableCreatureList.clear();
//      this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityBat.class, 3, 1, 1));
//      this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityWitch.class, 8, 1, 1));
//      this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityEnderman.class, 4, 1, 1));
//      if (Config.spawnAngryZombie) {
//         this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityBrainyZombie.class, 32, 1, 1));
//         this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityGiantBrainyZombie.class, 8, 1, 1));
//      }
//
//      if (Config.spawnWisp) {
//         this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityWisp.class, 3, 1, 1));
//      }
//
//      if (Config.spawnElder) {
//         this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityEldritchGuardian.class, 1, 1, 1));
//      }
//
//      this.theBiomeDecorator.treesPerChunk = 2;
//      this.theBiomeDecorator.flowersPerChunk = 1;
//      this.theBiomeDecorator.grassPerChunk = 2;
//      this.setTemperatureRainfall(0.5F, 0.5F);
//      this.setBiomeName("Eerie");
//      this.setColor(4212800);
//   }
//
//   @SideOnly(Side.CLIENT)
//   public int getBiomeGrassColor(int x, int y, int z) {
//      return 4212800;
//   }
//
//   @SideOnly(Side.CLIENT)
//   public int getBiomeFoliageColor(int x, int y, int z) {
//      return 4215616;
//   }
//
//   public int getSkyColorByTemp(float par1) {
//      return 2237081;
//   }
//
//   @RGBColor
//   public int getWaterColorMultiplier() {
//      return 3035999;
//   }
//
//   public BiomeGenBase createMutation() {
//      return null;
//   }
}
