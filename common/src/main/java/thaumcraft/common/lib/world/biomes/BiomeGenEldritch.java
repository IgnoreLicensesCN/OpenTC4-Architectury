package thaumcraft.common.lib.world.biomes;


import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

//it's for outer
public class BiomeGenEldritch /*extends BiomeGenBase*/ {

   public static int waterColor = 0x009900;   // 可以自己调整
   public static int foliageColorOverride = 0x101010;
   public static int grassColorOverride = 0x101010;
   public static int skyColor = 0x000000;//the outer is really dark isn't it?

   public static Biome createEldritch() {
      // 生物生成设置
      MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
      spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ThaumcraftEntities.INHABITED_ZOMBIE, 1, 1, 1));
      spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ThaumcraftEntities.ELDRITCH_GUARDIAN, 1, 1, 1));

      // 地形生成设置
      BiomeGenerationSettings.PlainBuilder generation = new BiomeGenerationSettings.PlainBuilder();

      // 示例：添加一棵树（如果你想留空可以不添加）
//      ConfiguredFeature<TreeConfiguration, ?> treeConfigured = new ConfiguredFeature<>(
//              Feature.TREE,
//              new TreeConfiguration.TreeConfigurationBuilder(
//                      BlockStateProvider.simple(Blocks.DARK_OAK_LOG),
//                      new StraightTrunkPlacer(4, 2, 0),
//                      BlockStateProvider.simple(Blocks.DARK_OAK_LEAVES),
//                      new BlobFoliagePlacer(ConstantInt.ofAspectVisList(2), ConstantInt.ofAspectVisList(0), 3),
//                      new TwoLayersFeatureSize(1, 0, 1)
//              ).ignoreVines().build()
//      );

//      Holder<ConfiguredFeature<?, ?>> treeHolder = Holder.direct(treeConfigured);
//      PlacedFeature treePlaced = new PlacedFeature(
//              treeHolder,
//              List.ofAspectVisList(RarityFilter.onAverageOnceEvery(10), InSquarePlacement.spread())
//      );
//      generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION.ordinal(), Holder.direct(treePlaced));

      // 构建 Biome
      return new Biome.BiomeBuilder()
              .temperature(0.5F)
              .downfall(0.0F) // 禁雨
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

//   public BiomeGenEldritch(int p_i1990_1_) {
//      super(p_i1990_1_);
//      this.spawnableMonsterList.clear();
//      this.spawnableCreatureList.clear();
//      this.spawnableWaterCreatureList.clear();
//      this.spawnableCaveCreatureList.clear();
//      this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityInhabitedZombie.class, 1, 1, 1));
//      this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityEldritchGuardian.class, 1, 1, 1));
//      this.topBlock = Blocks.dirt;
//      this.fillerBlock = Blocks.dirt;
//      this.setBiomeName("Eldritch");
//      this.setDisableRain();
//   }
//
//   @SideOnly(Side.CLIENT)
//   public int getSkyColorByTemp(float p_76731_1_) {
//      return 0;
//   }
//
//   public void decorate(Level world, Random random, int x, int z) {
//   }
//
//   public BiomeGenBase createMutation() {
//      return null;
//   }
}
