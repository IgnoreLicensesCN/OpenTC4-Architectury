package thaumcraft.common.tiles.crafted.vis.visnet;

import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.visnet.IVisNetChargeRelayChargeableContainer;
import thaumcraft.common.items.abstracts.wandabstraction.wand.ICentiVisContainerItem;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;

public class VisNetChargeRelayBlockEntity extends VisNetRelayBlockEntity {
    public VisNetChargeRelayBlockEntity(BlockEntityType<? extends VisNetChargeRelayBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public VisNetChargeRelayBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.VIS_CHARGE_RELAY(),blockPos, blockState);
    }

    public BlockPos getPosBeingCharged() {
        return this.getBlockPos().below();
    }
    public static final int CHARGE_RATE_CENTIVIS = 5;
    public int getCentiVisChargeRate(){
        return CHARGE_RATE_CENTIVIS;//call someone to create a new charger with higher value
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level == null) {return;}
        if (!this.level.isClientSide) {
            var be = LevelBlockEntityAccessing.getExistingBlockEntity(level, getPosBeingCharged());
            if (be instanceof IVisNetChargeRelayChargeableContainer chargeableContainer) {
                var stackToCharge = chargeableContainer.getStackToCharge();
                if (!stackToCharge.isEmpty()) {
                    if (stackToCharge.getItem() instanceof ICentiVisContainerItem<? extends Aspect> centiVisContainerNotCasted) {
                        var centiVisContainer = (ICentiVisContainerItem<Aspect>)centiVisContainerNotCasted;
                        var aspRoomRemaining = centiVisContainer.getAspectsWithRoomRemaining(stackToCharge);
                        if (!aspRoomRemaining.isEmpty()) {
                            aspRoomRemaining.forEach(
                                    (aspToCharge,aspAmountCanCharge) -> {
                                        int drain = Math.min(getCentiVisChargeRate(),aspAmountCanCharge);
                                        if (drain > 0) {
                                            var canProvide = this.consumeCentiVis(aspToCharge,drain);
                                            centiVisContainer.addCentiVis(stackToCharge,aspToCharge,canProvide,true);
                                        }
                                    }
                            );
                        }
                    }
                }
            }
        }
    }
}
