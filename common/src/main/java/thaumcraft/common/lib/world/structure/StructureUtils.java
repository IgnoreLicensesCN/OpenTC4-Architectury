package thaumcraft.common.lib.world.structure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;

public class StructureUtils {

    public static RandomSource randomSourceFromChunkPosAndSeed(ChunkPos chunkPos,long seed) {
        return randomSourceFromChunkPosAndSeed(chunkPos.x, chunkPos.z, seed);
    }
    public static RandomSource randomSourceFromChunkPosAndSeed(int x,int z,long seed){
        long hash = seed
                ^ (x * 341873128712L)
                ^ (z * 132897987541L);

        return RandomSource.create(hash);
    }
}
