package thaumcraft.common.entities.ai.goals.golem.transportresources.essentiainteraction;

import net.minecraft.sounds.SoundEvents;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportInBlockEntity;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemEssentiaJarSearchingHelper;
import thaumcraft.common.items.golem.upgrade.core.EssentiaGolemCoreItem;

import static thaumcraft.common.entities.golems.GolemEssentiaJarSearchingHelper.getBestEssentiaContainerWithRoomForConnectedJars;

public class GolemFillInEssentiaGoal extends AbstractGolemEssentiaGoal {
    protected GolemEssentiaJarSearchingHelper.BestEssentiaContainerResultReceiver searchResultReceiver =
            new GolemEssentiaJarSearchingHelper.BestEssentiaContainerResultReceiver();

    public GolemFillInEssentiaGoal(BaseGolemEntity golem) {
        super(golem);
    }
    @Override
    public boolean canUse() {
        var holding = EssentiaGolemCoreItem.GolemHoldingEssentiaFakeField.getHolding(golem);
        if (holding.aspect.isEmpty() || holding.amount <= 0) {
            return false;
        }
        for (var marker:golem.markers){
            searchResultReceiver.reset();
            getBestEssentiaContainerWithRoomForConnectedJars(
                    golem,
                    marker.getPos(),
                    holding.aspect,
                    holding.amount,
                    searchResultReceiver
                    );
            var atPos = searchResultReceiver.getBlockPos();
            if (searchResultReceiver.isFoundResult()
                    && !(atPos.getX() == Integer.MAX_VALUE || atPos.getY() == Integer.MAX_VALUE || atPos.getZ() == Integer.MAX_VALUE)
                    && atPos.distToCenterSqr(golem.position()) <= reachRange()
            ){
                return true;
            }
        }
        return false;
    }

    @Override
    public void start() {
        super.start();
        var be = searchResultReceiver.getBe();
        var dir = searchResultReceiver.getDir();
        var holding = EssentiaGolemCoreItem.GolemHoldingEssentiaFakeField.getHolding(golem);
        if (be != null && dir != null
        ) {
            if (be instanceof IEssentiaTransportInBlockEntity inBE){
                if (inBE.canInputFrom(dir) && !inBE.isFullFor(holding.aspect,dir)){
                    int added = inBE.addEssentia(holding.aspect,holding.amount,dir);
                    if (added > 0){
                        golem.playSound(SoundEvents.GENERIC_SWIM,0.2F, golem.getRandom().nextFloat()*0.6F + 0.7F);
                    }
                    holding.amount -= added;
                    holding.verify();
                }
            }
        }

    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }
}
