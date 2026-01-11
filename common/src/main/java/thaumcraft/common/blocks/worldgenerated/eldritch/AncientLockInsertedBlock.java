package thaumcraft.common.blocks.worldgenerated.eldritch;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thaumcraft.client.fx.migrated.particles.FXSpark;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.eldritch.AncientLockInsertedBlockEntity;

public class AncientLockInsertedBlock extends AncientLockEmptyBlock implements EntityBlock {
    public AncientLockInsertedBlock(Properties properties) {
        super(properties);
    }
    public AncientLockInsertedBlock() {
        super();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() != this){return null;}
        return new AncientLockInsertedBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState0, BlockEntityType<T> blockEntityType) {
        if (blockState0.getBlock() != this){return null;}
        if (blockEntityType != ThaumcraftBlockEntities.ANCIENT_LOCK_INSERTED){return null;}
        return (level, blockPos, blockState, blockEntity) -> {
            if (blockEntity instanceof AncientLockInsertedBlockEntity lock){
                lock.tick();
            }
        };
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (Platform.getEnvironment() != Env.CLIENT) {
            return;
        }
        if (!(level instanceof ClientLevel clientLevel)){
            return;
        }
        final int i = blockPos.getX();
        final int j = blockPos.getY();
        final int k = blockPos.getZ();
        FXSpark ef = new FXSpark(clientLevel, (float)i + level.random.nextFloat(), (float)j + level.random.nextFloat(), (float)k + level.random.nextFloat(), 0.5F);
        ef.setRBGColorF(0.65F + level.random.nextFloat() * 0.1F, 1.0F, 1.0F);
        ef.setAlphaF(0.8F);
        Minecraft.getInstance().particleEngine.add(ef);
    }
}
