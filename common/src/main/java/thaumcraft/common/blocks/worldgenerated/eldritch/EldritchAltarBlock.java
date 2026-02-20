package thaumcraft.common.blocks.worldgenerated.eldritch;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.lib.world.dim.MazeHandler;
import thaumcraft.common.lib.world.dim.MazeThread;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.eldritch.EldritchAltarBlockEntity;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;

public class EldritchAltarBlock extends SuppressedWarningBlock implements EntityBlock {
    public static final int EYES_LIMIT = 4;
    public static final int AS_SPAWNER_REQUIRES_EYE = 2;

    public static final IntegerProperty ELDRITCH_ALTAR_EYES = IntegerProperty.create("eyes", 0, EYES_LIMIT);
    public static final BooleanProperty SPAWNED_CLERICS = BooleanProperty.create("spawned_clerics");
    public static final BooleanProperty IS_SPAWNER = BooleanProperty.create("is_spawner");
    public static final BooleanProperty OPENED = BooleanProperty.create("opened");
    public static final IntegerProperty SPAWNER_TYPE = IntegerProperty.create("spawner_type", 0, 3);
    public EldritchAltarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(ELDRITCH_ALTAR_EYES,0)
                        .setValue(SPAWNED_CLERICS, false)
                        .setValue(IS_SPAWNER, false)
                        .setValue(OPENED, false)
                        .setValue(SPAWNER_TYPE, 0)
        );
    }

    public EldritchAltarBlock() {
        super(Properties.of()
                .strength(50F,20000F)
                .sound(SoundType.STONE)
                .mapColor(MapColor.COLOR_BLACK)
                .lightLevel(s -> 8)
                .requiresCorrectToolForDrops()
        );

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(ELDRITCH_ALTAR_EYES,0)
                        .setValue(SPAWNED_CLERICS, false)
                        .setValue(IS_SPAWNER, false)
                        .setValue(OPENED, false)
                        .setValue(SPAWNER_TYPE, 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ELDRITCH_ALTAR_EYES).add(IS_SPAWNER).add(OPENED).add(SPAWNED_CLERICS);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == ThaumcraftBlocks.ELDRITCH_ALTAR) {
            return new EldritchAltarBlockEntity(blockPos, blockState);
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState0, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == ThaumcraftBlockEntities.ELDRITCH_ALTAR) {
            return (level, blockPos, blockState, blockEntity) -> {
                if (blockEntity instanceof EldritchAltarBlockEntity eldritchAltarBlockEntity && Platform.getEnvironment() == Env.SERVER) {
                    eldritchAltarBlockEntity.serverTick();
                }
            };
        }
        return null;
    }

    public boolean checkForMaze(Level level, BlockState state, BlockPos pos) {
        int w = 15 + level.random.nextInt(8) * 2;
        int h = 15 + level.random.nextInt(8) * 2;
        int x = pos.getX();
        int z = pos.getZ();
        if (!MazeHandler.mazesInRange(x >> 4, z >> 4, w, h)) {
            Thread t = new Thread(new MazeThread(x >> 4, z >> 4, w, h, level.random.nextLong()));
            t.start();
            return false;
        } else {
            return true;
        }

    }

    public boolean addEye(Level level, BlockState state, BlockPos pos){
        int eyes = state.getValue(ELDRITCH_ALTAR_EYES);
        if (eyes >= EYES_LIMIT){return false;}
        int newEyeCount = eyes + 1;
        var setState = state.setValue(ELDRITCH_ALTAR_EYES,eyes + 1);
        if (newEyeCount >= AS_SPAWNER_REQUIRES_EYE){
            setState.setValue(IS_SPAWNER, true);
            setState.setValue(SPAWNER_TYPE,1);
            checkForMaze(level,state,pos);
        }
        return level.setBlock(pos,setState, 3);
    }
}
