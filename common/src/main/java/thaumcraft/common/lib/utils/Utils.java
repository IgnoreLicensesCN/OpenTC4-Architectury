package thaumcraft.common.lib.utils;

import com.linearity.opentc4.mixin.ClientPacketListenerAccessor;
import com.linearity.opentc4.mixin.ServerGamePacketListenerImplAccessor;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;
import tc4tweak.ConfigurationHandler;
import thaumcraft.api.WorldCoordinates;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.internal.WeightedRandomLootCollection;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.baubles.ItemAmuletVis;
import thaumcraft.common.lib.network.fx.PacketFXVisDrainS2C;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import static com.linearity.opentc4.OpenTC4.platformUtils;

public class Utils {
   public static HashMap<Item,ItemStack> specialMiningResult = new HashMap<>();
   public static HashMap<Item,Float> specialMiningChance = new HashMap<>();
   public static final String[] colorNames = new String[]{"White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black"};
   public static final int[] colors = new int[]{15790320, 15435844, 12801229, 6719955, 14602026, 4312372, 14188952, 4408131, 10526880, 2651799, 8073150, 2437522, 5320730, 3887386, 11743532, 1973019};
   public static HashMap<WorldCoordinates,Long> effectBuffer = new HashMap<>();

   public static boolean isChunkLoaded(Level world, int x, int z) {
      int chunkX = x >> 4;
      int chunkZ = z >> 4;
      return world.hasChunk(chunkX, chunkZ);
   }

   public static boolean useBonemealAtLoc(Level world, Player player, int x, int y, int z) {
      BlockPos pos = new BlockPos(x, y, z);

      // 一个假的骨粉堆，不会真正消耗
      ItemStack fake = new ItemStack(Items.BONE_MEAL);

      // 直接调用原版 growCrop（只处理 BonemealableBlock）
      if (BoneMealItem.growCrop(fake, world, pos)) {
         if (!world.isClientSide) {
            world.levelEvent(1505, pos, 0);
         }
         return true;
      }

      return false;
   }

   public static boolean hasColor(byte[] colors) {
      for(byte col : colors) {
         if (col >= 0) {
            return true;
         }
      }

      return false;
   }

   public static int getFirstUncoveredY(Level world, int x, int z) {
      int y = Math.max(5, world.getMinBuildHeight());

      while (y + 1 < world.getMaxBuildHeight() &&
              !world.getBlockState(new BlockPos(x, y + 1, z)).isAir()) {
         y++;
      }

      return y;
   }

   @Deprecated(forRemoval = true,since = "I dont get Equivalent Exchange 3 in 1.20.1")
   public static boolean isEETransmutionItem(Item item) {
//      try {
//         String itemClass = "com.pahimar.ee3.item.ITransmutationStone";
//         Class ee = Class.forName(itemClass);
//         if (ee.isAssignableFrom(item.getClass())) {
//            return true;
//         }
//      } catch (Exception ignored) {
//      }

      return false;
   }
//never used
//   public static void copyFile(File sourceFile, File destFile) throws IOException {
//      if (!destFile.exists()) {
//         destFile.createNewFile();
//      }
//
//       try (FileChannel source = (new FileInputStream(sourceFile)).getChannel(); FileChannel destination = (new FileOutputStream(destFile)).getChannel()) {
//           destination.transferFrom(source, 0L, source.size());
//       }
//
//   }

   public static int getFirstUncoveredBlockHeight(Level world, int par1, int par2) {
      return getFirstUncoveredY(world, par1, par2);
//      int var3;
//      for(var3 = 10; !world.isAirBlock(par1, var3 + 1, par2) || var3 > 250; ++var3) {
//      }
//
//      return var3;
   }

   public static void addSpecialMiningResult(ItemStack in, ItemStack out, float chance) {
      specialMiningResult.put(in.getItem(), out);
      specialMiningChance.put(in.getItem(), chance);
   }

   public static ItemStack findSpecialMiningResult(ItemStack is, float chance, Random rand) {
      ItemStack dropped = is.copy();
      float r = rand.nextFloat();
      var key = is.getItem();
      if (specialMiningResult.containsKey(key) && r <= chance * specialMiningChance.get(key)) {
         dropped = specialMiningResult.get(key).copy();
         dropped.setCount(dropped.getCount() * is.getCount());
      }

      return dropped;
   }

   public static float clamp_float(float par0, float par1, float par2) {
      return par0 < par1 ? par1 : (Math.min(par0, par2));
   }

   private static Method getRenderDistanceChunks;
   public static double getViewDistance(Level level) {
      if (level instanceof ServerLevel serverLevel) {
         return serverLevel.getServer().getPlayerList().getViewDistance() * 16;
      }
      // 客户端逻辑，受本地渲染距离与服务器限制共同影响
      else if (level instanceof ClientLevel clientLevel) {
         var client = Minecraft.getInstance();
         int renderChunks = client.options.renderDistance().get(); // 本地设置
         int serverChunks = renderChunks;

         if (client.player != null) {
            serverChunks = ((ClientPacketListenerAccessor)client.player.connection).opentc4$getServerChunkRadius();
         }

         return Math.min(renderChunks, serverChunks) * 16.0;
      }

      // 兜底
      return 16 * 12;
   }

   //inspired by fillbiome
   public static void setBiomeAt(ServerLevel world, int x, int y, int z, Holder<Biome> biome){
      BlockPos pos = new BlockPos(x, y, z);
      setBiomeAt(world,pos,biome);
   }
   public static void setBiomeAt(ServerLevel world, BlockPos pos, Holder<Biome> biome){
      setBiomeRegion(world, pos, pos, biome, b -> true);
   }
   public static void setBiomeRegion(ServerLevel world, BlockPos from, BlockPos to, Holder<Biome> biome, Predicate<Holder<Biome>> predicate) {
      // 获取区域 bounding box

       int minX = Math.min(from.getX(), to.getX());
      int minY = Math.min(from.getY(), to.getY());
      int minZ = Math.min(from.getZ(), to.getZ());

      int maxX = Math.max(from.getX(), to.getX());
      int maxY = Math.max(from.getY(), to.getY());
      int maxZ = Math.max(from.getZ(), to.getZ());

      // 遍历区域包含的 chunk
      List<ChunkAccess> chunks = new ArrayList<>();
      for (int cz = minZ >> 4; cz <= (maxZ >> 4); cz++) {
         for (int cx = minX >> 4; cx <= (maxX >> 4); cx++) {
            ChunkAccess chunk = world.getChunk(cx, cz, ChunkStatus.FULL, true);
//            if (chunk == null) throw new CommandSyntaxException(null, Component.literal("Chunk not loaded!"));
            if (chunk != null) {
               chunks.add(chunk);
            }
         }
      }

      // 计数器
      MutableInt counter = new MutableInt(0);

      // 获取气候采样器
      var sampler = world.getChunkSource().randomState().sampler();

      // 遍历 chunk 填充 biome
      for (ChunkAccess chunk : chunks) {
         chunk.fillBiomesFromNoise((i, j, k, s) -> {
            BlockPos bp = new BlockPos(i, j, k);
            if (bp.getX() >= minX && bp.getX() <= maxX &&
                    bp.getY() >= minY && bp.getY() <= maxY &&
                    bp.getZ() >= minZ && bp.getZ() <= maxZ &&
                    predicate.test(chunk.getNoiseBiome(i, j, k))) {
               counter.increment();
               return biome;
            }
            return chunk.getNoiseBiome(i, j, k);
         }, sampler);

         chunk.setUnsaved(true);
      }

      // 刷新客户端
      world.getChunkSource().chunkMap.resendBiomesForChunks(chunks);

//      // 可选：在控制台/玩家显示修改数量
//      System.out.println("Modified biomes: " + counter.getValue());
   }

   public static boolean isWoodLog(Level world, BlockPos pos) {
      return world.getBlockState(pos).is(BlockTags.LOGS);
   }
//   public static boolean isWoodLog(IBlockAccess world, int x, int y, int z) {
//      Block bi = world.getBlock(x, y, z);
//      int md = world.getBlockMetadata(x, y, z);
//      if (bi == Blocks.air) {
//         return false;
//      } else if (bi.canSustainLeaves(world, x, y, z)) {
//         return true;
//      } else {
//         return ItemElementalAxe.oreDictLogs.contains(Arrays.asList(bi, md));
//      }
//   }

   public static void resetFloatCounter(ServerPlayer player) {
       ((ServerGamePacketListenerImplAccessor) player.connection).opentc4$setAboveGroundTickCount(0);
   }

   public static boolean getBit(int value, int bit) {
      return (value & 1 << bit) != 0;
   }

   public static int setBit(int value, int bit) {
      return value | 1 << bit;
   }

   public static int clearBit(int value, int bit) {
      return value & ~(1 << bit);
   }

   public static int toggleBit(int value, int bit) {
      return value ^ 1 << bit;
   }

   public static byte pack(boolean[] vals) {
      byte result = 0;

      for(boolean bit : vals) {
         result = (byte)(result << 1 | (bit ? 1 : 0) & 1);
      }

      return result;
   }

   public static boolean[] unpack(byte val) {
      boolean[] result = new boolean[8];

      for(int i = 0; i < 8; ++i) {
         result[i] = (byte)(val >> 7 - i & 1) == 1;
      }

      return result;
   }

   //only for comparing
   public static @Nullable Object getNBTDataFromId(CompoundTag nbt, String key) {
      if (!nbt.contains(key)) return null;

      Tag tag = nbt.get(key);
      if (tag instanceof ByteTag) return ((ByteTag) tag).getAsByte();
      if (tag instanceof ShortTag) return ((ShortTag) tag).getAsShort();
      if (tag instanceof IntTag) return ((IntTag) tag).getAsInt();
      if (tag instanceof LongTag) return ((LongTag) tag).getAsLong();
      if (tag instanceof FloatTag) return ((FloatTag) tag).getAsFloat();
      if (tag instanceof DoubleTag) return ((DoubleTag) tag).getAsDouble();
      if (tag instanceof ByteArrayTag) return ((ByteArrayTag) tag).getAsByteArray();
      if (tag instanceof StringTag) return ((StringTag) tag).getAsString();
      if (tag instanceof IntArrayTag) return ((IntArrayTag) tag).getAsIntArray();
      if (tag instanceof ListTag) return tag; // 可以返回 ListTag 进一步处理
      if (tag instanceof CompoundTag) return tag; // 复合 tag
      return null;
   }

   public static void generateVisEffect(ResourceKey<Level> dim, int x, int y, int z, int x2, int y2, int z2, int color) {
      WorldCoordinates wc = new WorldCoordinates(x, y, z, dim.toString());
      Long time = System.currentTimeMillis();
      Random rand = new Random(time);
      if (effectBuffer.containsKey(wc)) {
         if (effectBuffer.get(wc) < time) {
            effectBuffer.remove(wc);
         }
      } else {
         effectBuffer.put(wc, time + 500L + (long)rand.nextInt(100));

         if (Platform.getEnvironment() == Env.SERVER){
            platformUtils.getServer().getAllLevels().forEach(level -> {
               if (level.dimension() == dim) {
                  new PacketFXVisDrainS2C(x, y, z, x2, y2, z2, color).sendToAllAround(level, new Vec3(x, y, z), 64.0F);
               }
            });
         }
      }

   }

   @Deprecated(forRemoval = true,since = "y dont u use mixin in 2025?")
   public static void setPrivateFinalValue(Class classToAccess, Object instance, Object value, String... fieldNames) {
      throw new RuntimeException("Who told u to use this but mixin?");
//      Field field = ReflectionHelper.findField(classToAccess, ObfuscationReflectionHelper.remapFieldNames(classToAccess.getName(), fieldNames));
//
//      try {
//         Field modifiersField = Field.class.getDeclaredField("modifiers");
//         modifiersField.setAccessible(true);
//         modifiersField.setInt(field, field.getModifiers() & -17);
//         field.set(instance, value);
//      } catch (Exception e) {
//         e.printStackTrace();
//      }

   }

   public static boolean isLyingInCone(double[] x, double[] t, double[] b, float aperture) {
      double halfAperture = aperture / 2.0F;
      double[] apexToXVect = dif(t, x);
      double[] axisVect = dif(t, b);
      boolean isInInfiniteCone = dotProd(apexToXVect, axisVect) / magn(apexToXVect) / magn(axisVect) > Math.cos(halfAperture);
      if (!isInInfiniteCone) {
         return false;
      } else {
         boolean isUnderRoundCap = dotProd(apexToXVect, axisVect) / magn(axisVect) < magn(axisVect);
         return isUnderRoundCap;
      }
   }

   public static double dotProd(double[] a, double[] b) {
      return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
   }

   public static double[] dif(double[] a, double[] b) {
      return new double[]{a[0] - b[0], a[1] - b[1], a[2] - b[2]};
   }

   public static double magn(double[] a) {
      return Math.sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2]);
   }

   public static Vec3 calculateVelocity(Vec3 from, Vec3 to, double heightGain, double gravity) {
      double endGain = to.y() - from.y();
      double horizDist = Math.sqrt(distanceSquared2d(from, to));
      double maxGain = Math.max(heightGain, endGain + heightGain);
      double a = -horizDist * horizDist / ((double)4.0F * maxGain);
      double c = -endGain;
      double slope = -horizDist / ((double)2.0F * a) - Math.sqrt(horizDist * horizDist - (double)4.0F * a * c) / ((double)2.0F * a);
      double vy = Math.sqrt(maxGain * gravity);
      double vh = vy / slope;
      double dx = to.x() - from.x();
      double dz = to.z() - from.z();
      double mag = Math.sqrt(dx * dx + dz * dz);
      double dirx = dx / mag;
      double dirz = dz / mag;
      double vx = vh * dirx;
      double vz = vh * dirz;
      return new Vec3(vx, vy, vz);
   }

   public static double distanceSquared2d(Vec3 from, Vec3 to) {
      double dx = to.x() - from.x();
      double dz = to.z() - from.z();
      return dx * dx + dz * dz;
   }

   public static double distanceSquared3d(Vec3 from, Vec3 to) {
      double dx = to.x() - from.x();
      double dy = to.y() - from.y();
      double dz = to.z() - from.z();
      return dx * dx + dy * dy + dz * dz;
   }

   public static ItemStack mutateGeneratedLoot(ItemStack stack) {
      if (!ConfigurationHandler.INSTANCE.isMoreRandomizedLoot()) return stack;//.copy();
      if (stack.getItem() == ConfigItems.itemAmuletVis) {
         ItemAmuletVis ai = (ItemAmuletVis)stack.getItem();

         for(Aspect a : Aspects.getPrimalAspects()) {
            ai.storeVis(stack, a, ThreadLocalRandom.current().nextInt(5) * 100);
         }
      }
      return stack;
   }
   
   //TODO:LootBag API
   public static ItemStack generateLoot(int rarity, Random rand) {
      ItemStack is = null;
      if (rarity > 0 && rand.nextFloat() < 0.025F * (float)rarity) {
         is = genGear(rarity, rand);
//         if (is == null) {
//            is = generateLoot(rarity, rand);
//         }
      } else {
         AtomicReference<ItemStack> isRef = new AtomicReference<>();
          switch (rarity) {
              case 1 -> WeightedRandomLootCollection.lootBagUncommon.getRandom(RandomSource.create(rand.nextLong())).ifPresent(itemStackWrapper -> isRef.set(itemStackWrapper.getData()));
              case 2 -> WeightedRandomLootCollection.lootBagRare.getRandom(RandomSource.create(rand.nextLong())).ifPresent(itemStackWrapper -> isRef.set(itemStackWrapper.getData()));
              default -> WeightedRandomLootCollection.lootBagCommon.getRandom(RandomSource.create(rand.nextLong())).ifPresent(itemStackWrapper -> isRef.set(itemStackWrapper.getData()));
          };
      }
      if (is == null) {
         is = generateLoot(rarity, rand);
      }

      is = is.copy();
      if (is.getItem() == Items.BOOK) {

         EnchantmentHelper.enchantItem(
                 RandomSource.create(rand.nextLong()),   // RandomSource 替代 Random
                 is,
                 (int)(5.0F + rarity * 0.75F * rand.nextInt(18)),
                 false                    // allow treasure enchantments?
         );
//         EnchantmentHelper.addRandomEnchantment(rand, is, (int)(5.0F + (float)rarity * 0.75F * (float)rand.nextInt(18)));
      }

      return mutateGeneratedLoot(is);
   }

   private static ItemStack genGear(int rarity, Random rand) {
      ItemStack is = null;
      int quality = rand.nextInt(2);
      if (rand.nextFloat() < 0.2F) {
         ++quality;
      }

      if (rand.nextFloat() < 0.15F) {
         ++quality;
      }

      if (rand.nextFloat() < 0.1F) {
         ++quality;
      }

      if (rand.nextFloat() < 0.095F) {
         ++quality;
      }

      if (rand.nextFloat() < 0.095F) {
         ++quality;
      }

      Item item = getGearItemForSlot(rand.nextInt(5), quality);
      if (item != null) {
         is = new ItemStack(item);
         is.setDamageValue(rand.nextInt(1 + item.getMaxDamage() / 6));
         if (rand.nextInt(4) < rarity) {
            EnchantmentHelper.enchantItem(
                    RandomSource.create(rand.nextLong()),   // RandomSource 替代 Random
                    is,
                    (int)(5.0F + rarity * 0.75F * rand.nextInt(18)),
                    false                    // allow treasure enchantments?
            );
//            EnchantmentHelper.addRandomEnchantment(rand, is, (int)(5.0F + (float)rarity * 0.75F * (float)rand.nextInt(18)));
         }

         return is.copy();
      } else {
         return null;
      }
   }

   private static Item getGearItemForSlot(int slot, int quality) {
      switch (slot) {
         case 4: // 头盔
            if (quality == 0) return Items.LEATHER_HELMET;
            else if (quality == 1) return Items.GOLDEN_HELMET;
            else if (quality == 2) return Items.CHAINMAIL_HELMET;
            else if (quality == 3) return Items.IRON_HELMET;
            else if (quality == 4) return ConfigItems.itemHelmetThaumium;
            else if (quality == 5) return Items.DIAMOND_HELMET;
            else if (quality == 6) return ConfigItems.itemHelmetVoid;
            break;
         case 3: // 胸甲
            if (quality == 0) return Items.LEATHER_CHESTPLATE;
            else if (quality == 1) return Items.GOLDEN_CHESTPLATE;
            else if (quality == 2) return Items.CHAINMAIL_CHESTPLATE;
            else if (quality == 3) return Items.IRON_CHESTPLATE;
            else if (quality == 4) return ConfigItems.itemChestThaumium;
            else if (quality == 5) return Items.DIAMOND_CHESTPLATE;
            else if (quality == 6) return ConfigItems.itemChestVoid;
            break;
         case 2: // 护腿
            if (quality == 0) return Items.LEATHER_LEGGINGS;
            else if (quality == 1) return Items.GOLDEN_LEGGINGS;
            else if (quality == 2) return Items.CHAINMAIL_LEGGINGS;
            else if (quality == 3) return Items.IRON_LEGGINGS;
            else if (quality == 4) return ConfigItems.itemLegsThaumium;
            else if (quality == 5) return Items.DIAMOND_LEGGINGS;
            else if (quality == 6) return ConfigItems.itemLegsVoid;
            break;
         case 1: // 靴子
            if (quality == 0) return Items.LEATHER_BOOTS;
            else if (quality == 1) return Items.GOLDEN_BOOTS;
            else if (quality == 2) return Items.CHAINMAIL_BOOTS;
            else if (quality == 3) return Items.IRON_BOOTS;
            else if (quality == 4) return ConfigItems.itemBootsThaumium;
            else if (quality == 5) return Items.DIAMOND_BOOTS;
            else if (quality == 6) return ConfigItems.itemBootsVoid;
            break;
         case 0: // 武器
            if (quality == 0) return Items.IRON_AXE;
            else if (quality == 1) return Items.IRON_SWORD;
            else if (quality == 2) return Items.GOLDEN_AXE;
            else if (quality == 3) return Items.GOLDEN_SWORD;
            else if (quality == 4) return ConfigItems.itemSwordThaumium;
            else if (quality == 5) return Items.DIAMOND_SWORD;
            else if (quality == 6) return ConfigItems.itemSwordVoid;
            break;
         default:
            return null;
      }
      return null;
   }
}
