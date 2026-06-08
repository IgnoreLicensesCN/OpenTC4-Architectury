package thaumcraft.common.tiles.crafted.mirror;

import com.linearity.opentc4.annotations.Modifiable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.essentiabe.IRemoteEssentiaDrainerBlockEntity;
import thaumcraft.api.aspects.essentiabe.IRemoteDrainableEssentiaSourceBlockEntity;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.Set;

public class EssentiaMirrorBlockEntity extends AbstractMirrorBlockEntity
        implements
        IRemoteEssentiaDrainerBlockEntity,
        IRemoteDrainableEssentiaSourceBlockEntity {
    public EssentiaMirrorBlockEntity(BlockEntityType<? extends EssentiaMirrorBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public EssentiaMirrorBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ESSENTIA_MIRROR, blockPos, blockState);
    }

    @Override
    public int getDrainRange() {
        return 8;
    }
    public static final int MAX_DRAIN_AMOUNT = 1;
    protected int getMaxDrainAmount(){
        return MAX_DRAIN_AMOUNT;
    }

    @Override
    public int drainEssentiaRemote(Aspect aspect, int amount, int range, Set<IRemoteEssentiaDrainerBlockEntity> drainerMet) {
        if (getValidLinkedMirror() instanceof EssentiaMirrorBlockEntity linkedMirror && linkedMirror.checkHasValidLinkTo(this)) {
            return linkedMirror.drainEssentiaRemoteAsProxy(aspect, amount, range, drainerMet);
        }
        return 0;
    }

    protected int drainEssentiaRemoteAsProxy(Aspect aspect, int amount, int range, Set<IRemoteEssentiaDrainerBlockEntity> drainerMet) {
        return IRemoteEssentiaDrainerBlockEntity.super.drainEssentiaRemote(aspect, Math.clamp(amount,0,getMaxDrainAmount()), range, drainerMet);
    }

    @Override
    public int drainEssentiaRemote(Aspect aspect, int amount, @Modifiable Set<IRemoteEssentiaDrainerBlockEntity> metDrainers) {
        return drainEssentiaRemote(aspect, amount, getDrainRange(), metDrainers);
    }
}
