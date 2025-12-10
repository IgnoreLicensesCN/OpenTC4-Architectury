package thaumcraft.common.lib.world.biomes;

import com.linearity.colorannoation.annoation.ARGBColor;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.EntityBrainyZombie;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityGiantBrainyZombie;
import thaumcraft.common.entities.monster.EntityWisp;

import java.util.ArrayList;
import java.util.List;

public class BiomeGenEerie /*extends BiomeGenBase*/ {
   public static final Biome INSTANCE = create();
   public static int waterColor = 0x2E91FF;
   public static int foliageColorOverride = 0x404040;
   public static int grassColorOverride = 0x404040;
   public static int skyColor = 0x220011;

   private static Biome create() {
      MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

      // 生物生成
      spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.BAT, 3, 1, 1));
      spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 8, 1, 1));
      spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 4, 1, 1));

      if (Config.spawnAngryZombie) {
         spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntities.BRAINY_ZOMBIE.get(), 32, 1, 1));
         spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntities.GIANT_BRAINY_ZOMBIE.get(), 8, 1, 1));
      }

      if (Config.spawnWisp) {
         spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntities.WISP.get(), 3, 1, 1));
      }

      if (Config.spawnElder) {
         spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntities.ELDRITCH_GUARDIAN.get(), 1, 1, 1));
      }

      BiomeGenerationSettings.PlainBuilder generation = new BiomeGenerationSettings.PlainBuilder();
      ConfiguredFeature<TreeConfiguration, ?> birchTreeConfigured = new ConfiguredFeature<>(
              Feature.TREE,
              new TreeConfiguration.TreeConfigurationBuilder(
                      BlockStateProvider.simple(Blocks.BIRCH_LOG),
                      new StraightTrunkPlacer(5, 2, 0),
                      BlockStateProvider.simple(Blocks.BIRCH_LEAVES),
                      new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                      new TwoLayersFeatureSize(1, 0, 1)
              ).ignoreVines().build()
      );
      Holder<ConfiguredFeature<?, ?>> birchTreeHolder = Holder.direct(birchTreeConfigured);
      PlacedFeature birchTreePlaced = new PlacedFeature(
              birchTreeHolder,
              List.of(RarityFilter.onAverageOnceEvery(10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR)
      );

      generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION.ordinal(), Holder.direct(birchTreePlaced));
// 1️⃣ 草
      ConfiguredFeature<SimpleBlockConfiguration, Feature<SimpleBlockConfiguration>> grassFeature =
              new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                      new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.GRASS_BLOCK)));

// 2️⃣ 包装成 PlacedFeature
      PlacedFeature grassPlaced = new PlacedFeature(
              Holder.direct(grassFeature),
              List.of(InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP)
      );
      Holder<PlacedFeature> grassHolder = Holder.direct(grassPlaced);

// 3️⃣ 生成 RandomPatchConfiguration
      RandomPatchConfiguration grassPatchConfig = FeatureUtils.simpleRandomPatchConfiguration(2, grassHolder);

// 4️⃣ 构造最终 ConfiguredFeature<RandomPatchConfiguration, Feature<RandomPatchConfiguration>>
      ConfiguredFeature<RandomPatchConfiguration, Feature<RandomPatchConfiguration>> grassConfigured =
              new ConfiguredFeature<>(Feature.RANDOM_PATCH, grassPatchConfig);

      generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION.ordinal(), Holder.direct(grassPlaced));


      ConfiguredFeature<SimpleBlockConfiguration, Feature<SimpleBlockConfiguration>> flowerFeature =
              new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                      new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.DANDELION)));
// 3️⃣ 构造 PlacedFeature
      PlacedFeature flowerPlaced = new PlacedFeature(
              Holder.direct(flowerFeature),
              List.of(InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP)
      );

      generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION.ordinal(), Holder.direct(flowerPlaced));

      // 构建 Biome
      return new Biome.BiomeBuilder()
//              .precipitation(Biome.Precipitation.RAIN)
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
//   public int getWaterColorMultiplier() {
//      return 3035999;
//   }
//
//   public BiomeGenBase createMutation() {
//      return null;
//   }
}
