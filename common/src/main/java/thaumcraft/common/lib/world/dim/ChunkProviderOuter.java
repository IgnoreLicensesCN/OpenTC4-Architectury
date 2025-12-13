package thaumcraft.common.lib.world.dim;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockFalling;
import net.minecraft.world.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.level.Level;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChunkProviderOuter implements IChunkProvider {
    private Random rand;
    private Level worldObj;
    private WorldType worldType;
    private BiomeGenBase[] biomesForGeneration;

    public ChunkProviderOuter(World p_i2006_1_, long p_i2006_2_, boolean p_i2006_4_) {
        this.level() = p_i2006_1_;
        this.worldType = p_i2006_1_.getWorldInfo().getTerrainType();
        this.rand = new Random(p_i2006_2_);
    }

   @Override
    public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
        return this.provideChunk(p_73158_1_, p_73158_2_);
    }

    @Override
    public Chunk provideChunk(int chunkX, int chunkZ) {
        this.rand.setSeed((long) chunkX * 341873128712L + (long) chunkZ * 132897987541L);
        Block[] ablock = new Block[32768];
        byte[] meta = new byte[ablock.length];

        Arrays.fill(ablock, null);
        Arrays.fill(meta, (byte) 0);

        this.biomesForGeneration = this.level().getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration
                , chunkX * 16, chunkZ * 16, 16, 16);
        Chunk chunk = new Chunk(this.level(), ablock, meta, chunkX, chunkZ);
        byte[] abyte = chunk.getBiomeArray();

        for (int k = 0; k < abyte.length; ++k) {
            abyte[k] = (byte) this.biomesForGeneration[k].biomeID;
        }

        chunk.generateSkylightMap();
        return chunk;
    }

   @Override
    public boolean chunkExists(int p_73149_1_, int p_73149_2_) {
        return true;
    }

   @Override
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
        BlockFalling.fallInstantly = true;
        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(p_73153_1_, this.level(), this.getRandom(), p_73153_2_, p_73153_3_, false));
        int k = p_73153_2_ * 16;
        int l = p_73153_3_ * 16;
        BiomeGenBase biomegenbase = this.level().getBiomeGenForCoords(k + 16, l + 16);
        biomegenbase.decorate(this.level(), this.getRandom(), k, l);
        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(p_73153_1_, this.level(), this.getRandom(), p_73153_2_, p_73153_3_, false));
        BlockFalling.fallInstantly = false;
    }

   @Override
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_) {
        return true;
    }

   @Override
    public void saveExtraData() {
    }

   @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

   @Override
    public boolean canSave() {
        return true;
    }

   @Override
    public String makeString() {
        return "RandomLevelSource";
    }

   @Override
    public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_) {
        BiomeGenBase biomegenbase = this.level().getBiomeGenForCoords(p_73155_2_, p_73155_4_);
        return biomegenbase.getSpawnableList(p_73155_1_);
    }

   @Override
    public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_) {
        return null;
    }

   @Override
    public int getLoadedChunkCount() {
        return 0;
    }

   @Override
    public void recreateStructures(int p_82695_1_, int p_82695_2_) {
    }
}
