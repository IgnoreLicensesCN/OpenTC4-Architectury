package thaumcraft.common.tiles.crafted.essentiabe.pipes;

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
import thaumcraft.common.blocks.crafted.essentia.pipes.EssentiaTubeValveBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class EssentiaTubeValveBlockEntity extends EssentiaTubeBlockEntity {
    public EssentiaTubeValveBlockEntity(BlockEntityType<? extends EssentiaTubeValveBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public EssentiaTubeValveBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.ESSENTIA_TUBE_VALVE,blockPos, blockState);
    }

    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        if (
                useOnContext.getPlayer() instanceof ServerPlayer serverPlayer
                        && serverPlayer.isShiftKeyDown()) {
            setBlockStateAndUpdate(
                    ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_TUBE_VALVE.changeStateForDirection(
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
    public boolean shouldCutFlow() {
        var state = getBlockState();
        return state.getValue(EssentiaTubeValveBlock.POWERED)
                || state.getValue(EssentiaTubeValveBlock.FORCE_CUT_FLOW);
    }

    @Override
    public boolean isConnectable(@NotNull Direction face) {
        if (face == getFacing()) {
            return false;
        }
        return super.isConnectable(face);
    }

    @Override
    public void setSuction(@NotNull Aspect aspect, int amount) {
        if (this.level != null && shouldCutFlow()) {
            return;
        }
        super.setSuction(aspect, amount);
    }


}
