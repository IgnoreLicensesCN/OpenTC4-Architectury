package thaumcraft.common.tiles.crafted.infernalfurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportInBlockEntity;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportOutBlockEntity;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import static thaumcraft.common.blocks.multipartcomponent.infernalfurnace.InfernalFurnaceLavaBlock.SELF_POS_1_1_1;

public class InfernalFurnaceNozzleBlockEntity extends TileThaumcraft implements IEssentiaTransportInBlockEntity {
    public InfernalFurnaceNozzleBlockEntity(BlockEntityType<? extends InfernalFurnaceNozzleBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public InfernalFurnaceNozzleBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.INFERNAL_FURNACE_NOZZLE, blockPos, blockState);
    }

    int drawDelay = 0;

    public void serverTick(){
        if (this.level == null) return;
        drawDelay += 1;
        if ((drawDelay & 3) == 0){
            var core = getCore();
            if (core != null){
                if (core.speedyTime < 60){
                    var selfPos = getBlockPos();
                    var facing = ThaumcraftBlocks.INFERNAL_FURNACE_SIDE.getFacingFromState(this.level,getBlockState(),selfPos);
                    var facingBE = level.getBlockState(selfPos.relative(facing));
                    if (facingBE instanceof IEssentiaTransportOutBlockEntity outBE){
                        if (outBE.canOutputTo(facing.getOpposite())){
                            boolean inBECondition = true;
                            int selfSuction = this.getSuctionAmount(facing);
                            if (outBE instanceof IEssentiaTransportInBlockEntity inBE){
                                if (inBE.getSuctionAmount(facing.getOpposite()) >= selfSuction){
                                    inBECondition = false;
                                }
                            }
                            if (inBECondition && outBE.getMinimumSuctionToDrainOut() <= selfSuction){
                                if (outBE.takeEssentia(Aspects.FIRE, 1, facing.getOpposite()) > 0){
                                    core.speedyTime += 600;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected Direction getFacing(){
        return ThaumcraftBlocks.INFERNAL_FURNACE_SIDE.getFacingFromState(this.level,getBlockState(),getBlockPos());
    }

    @Override
    public boolean canInputFrom(@NotNull Direction face) {
        return isConnectable(face);
    }

    @Override
    public void setSuction(@NotNull Aspect aspect, int amount) {

    }

    @Override
    public int getSuctionAmount(@NotNull Direction face) {
        var furnaceCore = getCore();
        if (furnaceCore != null) {
            return furnaceCore.speedyTime < 40?128:0;
        }
        return 0;
    }

    protected @Nullable InfernalFurnaceBlockEntity getCore(){
        if (this.level == null) return null;
        var pos = this.getBlockPos();
        var state = this.getBlockState();
        var selfPosRelated = ThaumcraftBlocks.INFERNAL_FURNACE_SIDE.findSelfPosRelatedInMultipart(this.level,state,pos);
        var furnaceCorePos = pos.offset(selfPosRelated.multiply(-1)).offset(SELF_POS_1_1_1);
        if (level.getBlockEntity(furnaceCorePos) instanceof InfernalFurnaceBlockEntity furnaceCore) {
            return furnaceCore;
        }
        return null;
    }

    @Override
    public @NotNull Aspect getSuctionType(@NotNull Direction face) {
        return Aspects.FIRE;
    }

    @Override
    public int addEssentia(@NotNull Aspect aspect, int amount, @NotNull Direction fromDirection) {
        return 0;
    }

    @Override
    public @NotNull Aspect getEssentiaType(@NotNull Direction face) {
        return Aspects.FIRE;
    }

    @Override
    public int getEssentiaAmount(@NotNull Direction face) {
        return 0;
    }

    @Override
    public boolean isConnectable(@NotNull Direction face) {
        if (this.level == null) return false;
        return face == ThaumcraftBlocks.INFERNAL_FURNACE_SIDE.getFacingFromState(this.level,getBlockState(),getBlockPos());
    }
}
