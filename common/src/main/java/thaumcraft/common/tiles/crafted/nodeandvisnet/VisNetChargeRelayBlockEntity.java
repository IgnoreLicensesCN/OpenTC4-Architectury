package thaumcraft.common.tiles.crafted.nodeandvisnet;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.visnet.IVisNetChargeRelayChargeableContainer;
import thaumcraft.api.wands.ICentiVisContainer;
import thaumcraft.common.items.wands.wandtypes.WandCastingItem;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class VisNetChargeRelayBlockEntity extends VisNetRelayBlockEntity{
    public VisNetChargeRelayBlockEntity(BlockEntityType<? extends VisNetChargeRelayBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public VisNetChargeRelayBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ThaumcraftBlockEntities.VIS_CHARGE_RELAY,blockPos, blockState);
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
        if (Platform.getEnvironment() == Env.SERVER) {
            var be = level.getBlockEntity(getPosBeingCharged());
            if (be instanceof IVisNetChargeRelayChargeableContainer chargeableContainer) {
                var stackToCharge = chargeableContainer.getStackToCharge();
                if (!stackToCharge.isEmpty()) {
                    if (stackToCharge.getItem() instanceof ICentiVisContainer<? extends Aspect> centiVisContainerNotCasted) {
                        var centiVisContainer = (ICentiVisContainer<Aspect>)centiVisContainerNotCasted;
                        var aspRoomRemaining = centiVisContainer.getAspectsWithRoomRemaining(stackToCharge);
                        if (!aspRoomRemaining.isEmpty()) {
                            for(var entry : aspRoomRemaining.entrySet()) {
                                var aspToCharge = entry.getKey();
                                var aspAmountCanCharge = entry.getValue();
                                int drain = Math.min(getCentiVisChargeRate(),aspAmountCanCharge);
                                if (drain > 0) {
                                    var canProvide = this.consumeCentiVis(aspToCharge,drain);
                                    centiVisContainer.addCentiVis(stackToCharge,aspToCharge,canProvide,true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
