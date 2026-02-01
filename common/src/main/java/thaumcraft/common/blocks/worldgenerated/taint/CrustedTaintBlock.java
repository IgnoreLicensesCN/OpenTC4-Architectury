package thaumcraft.common.blocks.worldgenerated.taint;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.liquid.FluxGooBlock;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.EntityTaintSporeSwarmer;

import java.util.List;
//blocktaint:0
public class CrustedTaintBlock extends AbstractTaintBlock {
    public CrustedTaintBlock(Properties properties) {
        super(properties);
    }
    public CrustedTaintBlock() {
        super(
                BlockBehaviour.Properties.of()
                        .randomTicks()
                        .strength(1.75F,10)
                        .sound(TAINT_BLOCK_SOUND)
        );
    }

    @Override
    public void animateTick(BlockState blockState, Level world, BlockPos blockPos, RandomSource random) {
        if (world instanceof ClientLevel clientLevel && world.getBlockState(blockPos.below()).isAir()) {
            if (random.nextInt(10) == 0) {
                int i = blockPos.getX();
                int j = blockPos.getY();
                int k = blockPos.getZ();
                ClientFXUtils.dropletFX(clientLevel, (float)i + 0.1F + world.getRandom().nextFloat() * 0.8F, (float)j, (float)k + 0.1F + world.getRandom().nextFloat() * 0.8F, 0.3F, 0.1F, 0.8F);
            }
        }
    }

    @Override
    public void onBlockOutOfTaintBiome(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random) {
        if (random.nextInt(20) == 0) {
            world.setBlock(blockPos, ThaumcraftBlocks.FLUX_GOO.defaultBlockState(), 3);
        }
    }

    @Override
    public void beforeSpreadingFibres(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random) {
        if (this.tryToFall(world, blockPos,blockPos)) {
            return;
        }

        if (world.getBlockState(blockPos.above()).isAir()) {
            boolean doIt = true;
            Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(random);//got trick from chatGPT
            var posNear = blockPos.relative(dir);
            for(int a = 0; a < 4; ++a) {
                var pickBlockState = world.getBlockState(posNear.below(a));
                if (!pickBlockState.isAir()) {
                    doIt = false;
                    break;
                }

                if (world.getBlockState(blockPos.below(a)).getBlock() != this) {
                    doIt = false;
                    break;
                }
            }

            if (doIt && this.tryToFall(world, blockPos, posNear)) {
                return;
            }
        }
    }

    @Override
    public void afterSpreadFibres(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random) {
        var stateAbove = world.getBlockState(blockPos.above());
        if (Config.spawnTaintSpore && stateAbove.isAir() && random.nextInt(200) == 0) {

            AABB box = new AABB(
                    blockPos,
                    blockPos.offset(1,1,1)
            ).inflate(16.0D);

            List<EntityTaintSporeSwarmer> targets =
                    world.getEntitiesOfClass(EntityTaintSporeSwarmer.class, box);
            if (targets.isEmpty()) {
                world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                EntityTaintSporeSwarmer spore = new EntityTaintSporeSwarmer(world);//TODO(we didnt rewrite entity yet.
                spore.moveTo(blockPos,0,0);
                world.addFreshEntity(spore);
                world.playSound(
                        spore,
                        blockPos,
                        ThaumcraftSounds.ROOTS,
                        SoundSource.HOSTILE,
                        0.1F, 0.9F + world.getRandom().nextFloat() * 0.2F
                );
            }
        } else {
            boolean doIt = stateAbove.getBlock() == this;
            if (doIt) {
                for (Direction horizontalDir : Direction.Plane.HORIZONTAL) {
                    var stateNear = world.getBlockState(blockPos.relative(horizontalDir));
                    if (stateNear.getBlock() != this) {
                        doIt = false;
                        break;
                    }
                }
            }

            if (doIt) {
                world.setBlock(blockPos, FluxGooBlock.fullOfGoo(), 3);
            }
        }
    }
}
