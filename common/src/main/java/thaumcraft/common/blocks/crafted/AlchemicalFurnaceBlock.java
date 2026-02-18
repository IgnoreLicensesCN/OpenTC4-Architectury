package thaumcraft.common.blocks.crafted;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.crafted.AlchemicalFurnaceBlockEntity;

import static dev.architectury.registry.menu.MenuRegistry.openExtendedMenu;

public class AlchemicalFurnaceBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = AbstractFurnaceBlock.FACING;
    public static final BooleanProperty LIT = AbstractFurnaceBlock.LIT;
    public static final BooleanProperty HAS_ASPECT = BooleanProperty.create("has_aspect");
    public AlchemicalFurnaceBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH)
                .setValue(LIT, false)
                .setValue(HAS_ASPECT, false)
        );
    }
    public AlchemicalFurnaceBlock(){
        this(Properties.copy(Blocks.FURNACE).strength(3,25).lightLevel(state -> state.getValue(LIT) ? 12 : 0));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext arg) {
        return this.defaultBlockState().setValue(FACING, arg.getHorizontalDirection().getOpposite());
    }


    @Override
    public RenderShape getRenderShape(BlockState arg) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState arg, Rotation arg2) {
        return arg.setValue(FACING, arg2.rotate(arg.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState arg, Mirror arg2) {
        return arg.rotate(arg2.getRotation(arg.getValue(FACING)));
    }


    @Override
    public @NotNull InteractionResult use(
            BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult
    ) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity be = level.getBlockEntity(blockPos);
            if (be instanceof AlchemicalFurnaceBlockEntity alchemicalFurnace && player instanceof ServerPlayer serverPlayer) {
                openExtendedMenu(serverPlayer,alchemicalFurnace);
            }
            return InteractionResult.CONSUME;
        }
    }
    
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == this){
            new AlchemicalFurnaceBlockEntity(blockPos,blockState);
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (
                Platform.getEnvironment() == Env.SERVER
                        && blockState.getBlock() == this
                        && blockEntityType == ThaumcraftBlockEntities.ALCHEMICAL_FURNACE
        ) {
            return (level1, blockPos, blockState2, blockEntity) ->{
                if (blockEntity instanceof AlchemicalFurnaceBlockEntity alchemicalFurnace) {
                    alchemicalFurnace.serverTick();
                }
            };

        }
        return null;
    }

    @Override
    public void animateTick(BlockState blockState, Level w, BlockPos blockPos, RandomSource r) {
        var i = blockPos.getX();
        int j = blockPos.getY();
        int k = blockPos.getZ();
        float f = (float)i + 0.5F;
        float f1 = (float)j + 0.2F + r.nextFloat() * 5.0F / 16.0F;
        float f2 = (float)k + 0.5F;
        float f3 = 0.52F;
        float f4 = r.nextFloat() * 0.5F - 0.25F;
        w.addParticle(ParticleTypes.SMOKE, f - f3, f1, f2 + f4, 0.0F, 0.0F, 0.0F);
        w.addParticle(ParticleTypes.FLAME, f - f3, f1, f2 + f4, 0.0F, 0.0F, 0.0F);
        w.addParticle(ParticleTypes.SMOKE, f + f3, f1, f2 + f4, 0.0F, 0.0F, 0.0F);
        w.addParticle(ParticleTypes.FLAME, f + f3, f1, f2 + f4, 0.0F, 0.0F, 0.0F);
        w.addParticle(ParticleTypes.SMOKE, f + f4, f1, f2 - f3, 0.0F, 0.0F, 0.0F);
        w.addParticle(ParticleTypes.FLAME, f + f4, f1, f2 - f3, 0.0F, 0.0F, 0.0F);
        w.addParticle(ParticleTypes.SMOKE, f + f4, f1, f2 + f3, 0.0F, 0.0F, 0.0F);
        w.addParticle(ParticleTypes.FLAME, f + f4, f1, f2 + f3, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState arg) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState arg, Level arg2, BlockPos arg3) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(arg2.getBlockEntity(arg3));
    }
}
