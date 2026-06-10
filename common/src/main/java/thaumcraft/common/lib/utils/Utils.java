package thaumcraft.common.lib.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.MapMaker;
import com.linearity.colorannotation.annotation.RGBColor;
import com.linearity.opentc4.mixin.ClientPacketListenerAccessor;
import com.linearity.opentc4.mixin.ServerGamePacketListenerImplAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.network.fx.PacketFXVisDrainS2C;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class Utils {
    public static final String[] colorNames = new String[]{"White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black"};
    public static final int[] colors = new int[]{15790320, 15435844, 12801229, 6719955, 14602026, 4312372, 14188952, 4408131, 10526880, 2651799, 8073150, 2437522, 5320730, 3887386, 11743532, 1973019};
//    public static HashMap<WorldCoordinates, Long> effectBuffer = new HashMap<>();
    public static Map<Level,Cache<BlockPos,Boolean>> effectBuffer = new MapMaker().weakKeys().makeMap();

    public static boolean isChunkLoaded(Level world, int x, int z) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        return world.hasChunk(chunkX, chunkZ);
    }

    public static boolean useBonemealAtLoc(Level world, Player player, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        ItemStack fake = new ItemStack(Items.BONE_MEAL);
        if (BoneMealItem.growCrop(fake, world, pos)) {
            if (!world.isClientSide) {
                world.levelEvent(1505, pos, 0);
            }
            return true;
        }

        return false;
    }

    public static boolean hasColor(byte[] colors) {
        for (byte col : colors) {
            if (col >= 0) {
                return true;
            }
        }

        return false;
    }

    public static int getFirstUncoveredY(Level world, int x, int z) {
        int y = Math.max(5, world.getMinBuildHeight());

        while (y + 1 < world.getMaxBuildHeight() &&
                !world.getBlockState(new BlockPos(x, y + 1, z))
                        .isAir()) {
            y++;
        }

        return y;
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

    public static float clamp_float(float par0, float par1, float par2) {
        return par0 < par1 ? par1 : (Math.min(par0, par2));
    }

    private static Method getRenderDistanceChunks;

    public static double getViewDistance(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getServer()
                    .getPlayerList()
                    .getViewDistance() * 16;
        }
        // 客户端逻辑，受本地渲染距离与服务器限制共同影响
        else if (level instanceof ClientLevel clientLevel) {
            var client = Minecraft.getInstance();
            int renderChunks = client.options.renderDistance()
                    .get(); // 本地设置
            int serverChunks = renderChunks;

            if (client.player != null) {
                serverChunks = ((ClientPacketListenerAccessor) client.player.connection).opentc4$getServerChunkRadius();
            }

            return Math.min(renderChunks, serverChunks) * 16.0;
        }

        // 兜底
        return 16 * 12;
    }

    //inspired by fillbiome
    public static void setBiomeAt(ServerLevel world, int x, int y, int z, Holder<Biome> biome) {
        BlockPos pos = new BlockPos(x, y, z);
        setBiomeAt(world, pos, biome);
    }

    public static void setBiomeAt(ServerLevel world, BlockPos pos, Holder<Biome> biome) {
        setBiomeRegion(world, pos, pos, biome, b -> true);
    }

    public static void setBiomeRegion(ServerLevel world, BlockPos from, BlockPos to, Holder<Biome> biome, Predicate<Holder<Biome>> predicate) {


        int minX = Math.min(from.getX(), to.getX());
        int minY = Math.min(from.getY(), to.getY());
        int minZ = Math.min(from.getZ(), to.getZ());

        int maxX = Math.max(from.getX(), to.getX());
        int maxY = Math.max(from.getY(), to.getY());
        int maxZ = Math.max(from.getZ(), to.getZ());

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

        MutableInt counter = new MutableInt(0);

        var sampler = world.getChunkSource()
                .randomState()
                .sampler();

        for (ChunkAccess chunk : chunks) {
            chunk.fillBiomesFromNoise(
                    (i, j, k, s) -> {
                        BlockPos bp = new BlockPos(i, j, k);
                        if (bp.getX() >= minX && bp.getX() <= maxX &&
                                bp.getY() >= minY && bp.getY() <= maxY &&
                                bp.getZ() >= minZ && bp.getZ() <= maxZ &&
                                predicate.test(chunk.getNoiseBiome(i, j, k))) {
                            counter.increment();
                            return biome;
                        }
                        return chunk.getNoiseBiome(i, j, k);
                    }, sampler
            );

            chunk.setUnsaved(true);
        }

        // 刷新客户端
        world.getChunkSource().chunkMap.resendBiomesForChunks(chunks);

//      // 可选：在控制台/玩家显示修改数量
//      System.out.println("Modified biomes: " + counter.getValue());
    }

    public static boolean isWoodLog(Level world, BlockPos pos) {
        return world.getBlockState(pos)
                .is(BlockTags.LOGS);
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

        for (boolean bit : vals) {
            result = (byte) (result << 1 | (bit ? 1 : 0) & 1);
        }

        return result;
    }

    public static boolean[] unpack(byte val) {
        boolean[] result = new boolean[8];

        for (int i = 0; i < 8; ++i) {
            result[i] = (byte) (val >> 7 - i & 1) == 1;
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
        if (tag instanceof StringTag) return tag.getAsString();
        if (tag instanceof IntArrayTag) return ((IntArrayTag) tag).getAsIntArray();
        if (tag instanceof ListTag) return tag; // 可以返回 ListTag 进一步处理
        if (tag instanceof CompoundTag) return tag; // 复合 tag
        return null;
    }

    public static void generateVisEffect(Level level, BlockPos posFrom, BlockPos posTo,@RGBColor int color){
        if (level instanceof ServerLevel serverLevel) {
            try {
                effectBuffer.computeIfAbsent(level,_ignored -> CacheBuilder.newBuilder()
                        .expireAfterWrite(500, TimeUnit.MILLISECONDS)
                        .build()).get(posFrom,() -> {
                    new PacketFXVisDrainS2C(
                            posFrom.getX(),posFrom.getY(),posFrom.getZ(),
                            posTo.getX(),posTo.getY(),posTo.getZ(),
                            color).sendToAllAround(
                            serverLevel,
                            posFrom.getCenter(),
                            64.0F
                    );
                    return Boolean.TRUE;
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Deprecated(forRemoval = true, since = "y dont u use mixin in 2025?")
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
        boolean isInInfiniteCone = dotProd(apexToXVect, axisVect) / magn(apexToXVect) / magn(axisVect) > Math.cos(
                halfAperture);
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
        double a = -horizDist * horizDist / ((double) 4.0F * maxGain);
        double c = -endGain;
        double slope = -horizDist / ((double) 2.0F * a) - Math.sqrt(
                horizDist * horizDist - (double) 4.0F * a * c) / ((double) 2.0F * a);
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

}
