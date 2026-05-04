package thaumcraft.common.tiles.crafted.pipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.crafted.pipes.EssentiaTubeValveBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class EssentiaTubeValveBlockEntity extends EssentiaTubeBlockEntity {
    public EssentiaTubeValveBlockEntity(BlockEntityType<? extends EssentiaTubeValveBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public EssentiaTubeValveBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ESSENTIA_TUBE_VALVE,blockPos, blockState);
    }

    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        if (useOnContext.getPlayer() instanceof ServerPlayer serverPlayer
        && serverPlayer.isShiftKeyDown()) {
            setBlockStateAndUpdate(
                    ThaumcraftBlocks.ESSENTIA_TUBE_VALVE.changeStateForDirection(
                            getBlockState(),
                            useOnContext.getClickedFace().getOpposite()
                    )
            );
            return InteractionResult.SUCCESS;
        }
        return super.useOnWandInteractable(useOnContext);
    }
    public Direction getFacing() {
        return getBlockState().getValue(EssentiaTubeValveBlock.FACING);
    }
    public boolean isPowered() {
        return getBlockState().getValue(EssentiaTubeValveBlock.POWERED);
    }

    @Override
    public boolean isConnectable(Direction face) {
        if (face == getFacing()) {
            return false;
        }
        return super.isConnectable(face);
    }

    @Override
    public void setSuction(Aspect aspect, int amount) {
        if (this.level != null && isPowered()) {
            return;
        }
        super.setSuction(aspect, amount);
    }


}
