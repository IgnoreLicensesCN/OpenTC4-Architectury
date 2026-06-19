package thaumcraft.common.blocks.crafted.arcanebore;

import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.items.abstracts.wandabstraction.wandinteractable.IWandInteractableBlockOrBlockEntity;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.tiles.crafted.ArcaneBoreBlockEntity;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;
import static dev.architectury.registry.menu.MenuRegistry.openExtendedMenu;
import static thaumcraft.common.blocks.crafted.arcanebore.ArcaneBoreBaseBlock.FACING_TO_DRILL;


public class ArcaneBoreDrillBlock extends SuppressedWarningBlock implements IWandInteractableBlockOrBlockEntity {
     public static final DirectionProperty FACING_TO_BASE = BlockStateProperties.FACING;
     public static final DirectionProperty DRILL_FACING = DirectionProperty.create(
             "drill_facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN
     );
    public ArcaneBoreDrillBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                .setValue(FACING_TO_BASE, Direction.DOWN).setValue(DRILL_FACING, Direction.UP)
        );
    }
    public ArcaneBoreDrillBlock() {
        this(
                Properties.of()
                        .sound(SoundType.WOOD)
                        .strength(2.5F, 10.0F)
                        .noOcclusion()
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING_TO_BASE,DRILL_FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        var clickedFace = blockPlaceContext.getClickedFace();
        var chosenFacing = clickedFace.getOpposite();
        return defaultBlockState().setValue(FACING_TO_BASE, chosenFacing).setValue(DRILL_FACING, clickedFace);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        if (!level.isClientSide()) {
            var selfFacing = state.getValue(FACING_TO_BASE);
            if (!fromPos.equals(pos.relative(selfFacing))) {
                return;
            }
            var probablyDrillState = level.getBlockState(fromPos);
            if (probablyDrillState.getBlock() != ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_BORE_BASE()) {
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }else if (probablyDrillState.getValue(FACING_TO_DRILL).getOpposite() != selfFacing) {
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }
        }
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (LevelBlockEntityAccessing.getExistingBlockEntity(level, blockPos.relative(blockState.getValue(FACING_TO_BASE))) instanceof ArcaneBoreBlockEntity arcaneBore) {
                openExtendedMenu(serverPlayer,arcaneBore);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        var level = useOnContext.getLevel();
        var clientSideFlag = level.isClientSide();
        if (level.isClientSide()) {return InteractionResult.sidedSuccess(clientSideFlag);}
        var pos = useOnContext.getClickedPos();
        var drillState = level.getBlockState(pos);
        level.setBlockAndUpdate(pos, drillState.setValue(DRILL_FACING,useOnContext.getClickedFace()));
        if (LevelBlockEntityAccessing.getExistingBlockEntity(level, pos.relative(drillState.getValue(FACING_TO_BASE))) instanceof ArcaneBoreBlockEntity arcaneBore) {
            arcaneBore.clearRotation();
        }
        level.playSound(
                null,
                pos,
                ThaumcraftSounds.TOOL,
                SoundSource.BLOCKS,
                0.5F,
                1.9F + level.random.nextFloat() * 0.2F
        );
        var player = useOnContext.getPlayer();
        if (player != null){
            player.swing(player.getUsedItemHand());
        }
        return InteractionResult.sidedSuccess(clientSideFlag);
    }
}
