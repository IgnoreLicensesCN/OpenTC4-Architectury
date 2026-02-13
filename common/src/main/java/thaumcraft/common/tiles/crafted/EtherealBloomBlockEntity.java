package thaumcraft.common.tiles.crafted;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeIDs;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import static com.linearity.opentc4.Consts.TAINT_SPREAD_UP_DISTANCE;

public class EtherealBloomBlockEntity extends TileThaumcraft {//TODO:Render
    public int counter = 0;
    public int growthCounter = 0;

    public EtherealBloomBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public EtherealBloomBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ETHEREAL_BLOOM, blockPos, blockState);
    }


    public void blockEntityTick() {
        
        if (this.counter == 0) {
            this.counter = this.level.random.nextInt(100);
        }

        ++this.counter;
        if (this.level instanceof ServerLevel serverLevel && this.counter % 20 == 0) {
            var pos = this.getBlockPos();
            var atX = pos.getX();
            var atY = pos.getY();
            var atZ = pos.getZ();
            int pickX = this.level.random.nextInt(8) - this.level.random.nextInt(8);
            int pickZ = this.level.random.nextInt(8) - this.level.random.nextInt(8);
            for (int pickY = -9; pickY <= 9; ++pickY) {
                var currentTargetingPos = pos.offset(pickX, pickY, pickZ);
                var biomeID = this.level.getBiome(currentTargetingPos);
                var squaredDistance = pickX * pickX + pickY * pickY + pickZ * pickZ;
                if (
                        (biomeID == ThaumcraftBiomeIDs.TAINT_ID
                                || biomeID == ThaumcraftBiomeIDs.EERIE_ID
                                || biomeID == Config.biomeMagicalForestID
                        )
                                && squaredDistance <= (double)81.0F
                ) {

                    ChunkGenerator generator = serverLevel.getChunkSource().getGenerator();
                    BiomeSource biomeSource = generator.getBiomeSource();

                    Holder<Biome> biomeDuringGeneration = biomeSource.getNoiseBiome(
                            QuartPos.fromBlock(currentTargetingPos.getX()),
                            QuartPos.fromBlock(currentTargetingPos.getY()),
                            QuartPos.fromBlock(currentTargetingPos.getZ()),
                            serverLevel.getChunkSource().randomState().sampler()
                    );
                    var taintFlag = false;

                    if (biomeID == ThaumcraftBiomeIDs.TAINT_ID || biomeDuringGeneration.is(ThaumcraftBiomeIDs.TAINT_ID)) {
                        biomeDuringGeneration = serverLevel.registryAccess()
                                .registryOrThrow(Registries.BIOME)
                                .getHolderOrThrow(Biomes.PLAINS);
                        taintFlag = true;
                    }

                    if (squaredDistance <= 81.F){
                        Utils.setBiomeAt(serverLevel, pickX + atX,atY, pickZ + atZ, biomeDuringGeneration);
                        if (taintFlag) {
                            setBiomeAboveNotTaint(serverLevel,currentTargetingPos);
                        }
                    }
                }
            }
        }

        if ((Platform.getEnvironment() == Env.CLIENT) && this.growthCounter == 0) {
            this.level.playSound(null,this.getBlockPos(), ThaumcraftSounds.ROOTS, SoundSource.BLOCKS);
        }

        ++this.growthCounter;
    }

    private void setBiomeAboveNotTaint(ServerLevel serverLevel, BlockPos base){
        if (this.level == null){return;}
        for (int i=1;i<=TAINT_SPREAD_UP_DISTANCE;i+=1){
            var currentPos = base.offset(0,i,0);
            var biomeID = this.level.getBiome(currentPos);
            if (biomeID.is(ThaumcraftBiomeIDs.TAINT_ID)) {

                ChunkGenerator generator = serverLevel.getChunkSource().getGenerator();
                BiomeSource biomeSource = generator.getBiomeSource();

                Holder<Biome> biomeDuringGeneration = biomeSource.getNoiseBiome(
                        QuartPos.fromBlock(base.getX()),
                        QuartPos.fromBlock(base.getY()),
                        QuartPos.fromBlock(base.getZ()),
                        serverLevel.getChunkSource().randomState().sampler()
                );
                if (biomeDuringGeneration.is(ThaumcraftBiomeIDs.TAINT_ID)) {
                    biomeDuringGeneration = serverLevel.registryAccess()
                            .registryOrThrow(Registries.BIOME)
                            .getHolderOrThrow(Biomes.PLAINS);
                }

                Utils.setBiomeAt(serverLevel, currentPos, biomeDuringGeneration);
            }
        }
    }
}
