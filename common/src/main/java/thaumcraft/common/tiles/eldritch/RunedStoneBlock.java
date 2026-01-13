package thaumcraft.common.tiles.eldritch;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ClientFXUtils;

import java.util.Random;

public class RunedStoneBlock extends DropExperienceBlock implements EntityBlock {
    public static final IntProvider RUNED_STONE_EXP_DROP = UniformInt.of(1,4);
    public static final IntegerProperty FACE_STATE = IntegerProperty.create("face_state", 0, 63);

    public RunedStoneBlock() {
        super(
                BlockBehaviour.Properties.of()
                        .strength(15,30)
                        .sound(SoundType.STONE)
                        .mapColor(MapColor.COLOR_BLACK)
                        .lightLevel(s -> 4),
                RUNED_STONE_EXP_DROP
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(FACE_STATE, 0));
    }

    public RunedStoneBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACE_STATE, 0));
    }

    public RunedStoneBlock(Properties properties, IntProvider intProvider) {
        super(properties, intProvider);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACE_STATE, 0));
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (Platform.getEnvironment() != Env.CLIENT) {return;}
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
        return ;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (blockState.getBlock() != this){
            return null;
        }
        return ;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACE_STATE);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        var pos = blockPlaceContext.getClickedPos();
        var level = blockPlaceContext.getLevel();
        var hasher = ""+pos.getX() + pos.getY() + pos.getZ() + level.dimension().location();
        var random = new Random(hasher.hashCode());

        return defaultBlockState().setValue(FACE_STATE, random.nextInt(64));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos,
                        BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (oldState.getBlock() != this){
            var hasher = ""+pos.getX() + pos.getY() + pos.getZ() + level.dimension().location();
            var random = new Random(hasher.hashCode());

            level.setBlock(pos,state.setValue(FACE_STATE, random.nextInt(64)),3);
        }
    }


}
