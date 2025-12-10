package thaumcraft.common.lib.world.biomes;

import com.linearity.opentc4.utils.vanilla1710.BiomeType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import thaumcraft.api.aspects.Aspect;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static com.linearity.opentc4.OpenTC4.platformUtils;
import static com.linearity.opentc4.utils.vanilla1710.BiomeWithTypes.*;

public class BiomeHandler {

   
   public static final Map<BiomeType, BiomeInfo> biomeInfo = new ConcurrentHashMap<>();

   public static void registerBiomeInfo(BiomeType type, int auraLevel, Aspect tag, boolean greatwood, float greatwoodchance) {
      biomeInfo.put(type, new BiomeInfo(auraLevel, tag, greatwood, greatwoodchance));
   }

   public static void registerBiomeInfo(BiomeType type, BiomeInfo info) {
      biomeInfo.put(type, info);
   }

   public static int getBiomeAura(Biome biome) {
      ResourceKey<Biome> biomeResKey = getBiomeResKey(biome);
      BiomeType[] types = getBiomeTypes(biomeResKey).toArray(new BiomeType[0]);
      int average = 0;
      int count = 0;

      for(BiomeType type : types) {
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

   public static Aspect getRandomBiomeTag(Biome biome, Random random) {
      return getRandomBiomeTag(getBiomeResKey(biome), random);
   }
   public static Aspect getRandomBiomeTag(ResourceKey<Biome> biomeResKey, Random random) {
      BiomeType[] types = getBiomeTypes(biomeResKey).toArray(new BiomeType[0]);
      BiomeType type = types[random.nextInt(types.length)];
      BiomeInfo info = biomeInfo.get(type);
      if (info == null) {
         return null;
      }
      return info.getTag();
   }


   public static float getBiomeSupportsGreatwood(Biome biome) {
      return getBiomeSupportsGreatwood(getBiomeResKey(biome));
   }
   public static float getBiomeSupportsGreatwood(ResourceKey<Biome> biomeResKey) {
      BiomeType[] types = getBiomeTypes(biomeResKey).toArray(new BiomeType[0]);

      for(BiomeType type : types) {
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
