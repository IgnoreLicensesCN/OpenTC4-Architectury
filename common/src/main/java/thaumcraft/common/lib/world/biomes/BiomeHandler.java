package thaumcraft.common.lib.world.biomes;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import thaumcraft.api.aspects.Aspect;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BiomeHandler {

   public static Map<BiomeDictionary.Type,BiomeInfo> biomeInfo = new HashMap<>();

   public static void registerBiomeInfo(BiomeDictionary.Type type, int auraLevel, Aspect tag, boolean greatwood, float greatwoodchance) {
      biomeInfo.put(type, new BiomeInfo(auraLevel, tag, greatwood, greatwoodchance));
   }

   public static void registerBiomeInfo(BiomeDictionary.Type type, BiomeInfo info) {
      biomeInfo.put(type, info);
   }

   public static int getBiomeAura(BiomeGenBase biome) {
      BiomeDictionary.Type[] types = BiomeDictionary.getTypesForBiome(biome);
      int average = 0;
      int count = 0;

      for(BiomeDictionary.Type type : types) {
         BiomeInfo info = biomeInfo.get(type);
         if (info == null) {
            continue;
         }
         average += info.getAuraLevel();
         ++count;
      }
      if (count == 0){return 100;}
      return average / count;
   }

   public static Aspect getRandomBiomeTag(int biomeId, Random random) {
      BiomeDictionary.Type[] types = BiomeDictionary.getTypesForBiome(BiomeGenBase.getBiome(biomeId));
      BiomeDictionary.Type type = types[random.nextInt(types.length)];
      BiomeInfo info = biomeInfo.get(type);
      if (info == null) {
         return null;
      }
      return info.getTag();
   }

   public static float getBiomeSupportsGreatwood(int biomeId) {
      BiomeDictionary.Type[] types = BiomeDictionary.getTypesForBiome(BiomeGenBase.getBiome(biomeId));

      for(BiomeDictionary.Type type : types) {
         BiomeInfo info = biomeInfo.get(type);
         if (info == null) {
            continue;
         }
         if (info.isGreatwood()) {
            return info.getGreatwoodchance();
         }
      }

      return 0.0F;
   }
}
