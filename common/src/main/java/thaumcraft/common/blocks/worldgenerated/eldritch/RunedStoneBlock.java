package thaumcraft.common.blocks.worldgenerated.eldritch;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.eldritch.RunedStoneBlockEntity;

public class RunedStoneBlock extends DropExperienceBlock implements EntityBlock {
    public static final IntProvider RUNED_STONE_EXP_DROP = UniformInt.of(1,4);

    public RunedStoneBlock() {
        this(
                BlockBehaviour.Properties.of()
                        .strength(15,30)
                        .sound(SoundType.STONE)
                        .mapColor(MapColor.COLOR_BLACK)
                        .lightLevel(s -> 4),
                RUNED_STONE_EXP_DROP
        );
    }

    public RunedStoneBlock(Properties properties) {
        this(properties,RUNED_STONE_EXP_DROP);
    }

    public RunedStoneBlock(Properties properties, IntProvider intProvider) {
        super(properties, intProvider);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (!level.isClientSide) {return;}
        if (!(level instanceof ClientLevel clientLevel)) {
            return;
        }
        var randomOffsetPos = blockPos.offset(randomSource.nextInt(3)-1, randomSource.nextInt(3)-1, randomSource.nextInt(3)-1);
        if (level.getBlockState(randomOffsetPos).isAir()) {
            ClientFXUtils.blockRunes(clientLevel,
                    randomOffsetPos.getX() + randomSource.nextFloat(),
                    randomOffsetPos.getY() + randomSource.nextFloat(),
                    randomOffsetPos.getZ() + randomSource.nextFloat(),
                    0.5F + randomSource.nextFloat() * 0.5F, randomSource.nextFloat() * 0.3F, 0.9F + randomSource.nextFloat() * 0.1F, 16 + randomSource.nextInt(4), 0.0F);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() != this){
            return null;
        }
        return new RunedStoneBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState0, BlockEntityType<T> blockEntityType) {
        if (blockState0.getBlock() != this){
            return null;
        }
        if (blockEntityType != ThaumcraftBlockEntities.BlockEntityTypeInstances.RUNED_STONE){
            return null;
        }
        if (level0.isClientSide) {
            return null;
        }
        return (level, blockPos, blockState, blockEntity) ->
        {
            if (blockEntity instanceof RunedStoneBlockEntity runedStone){
                runedStone.serverTick();
            }
        };
    }

}
