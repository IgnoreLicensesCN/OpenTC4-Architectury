package thaumcraft.common.entities.ai.goals.golem.transportresources.essentiainteraction;

import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemEssentiaJarSearchingHelper;
import thaumcraft.common.items.golem.upgrade.core.EssentiaGolemCoreItem;

import static thaumcraft.common.entities.golems.GolemEssentiaJarSearchingHelper.getBestEssentiaContainerWithRoomForConnectedJars;

public abstract class AbstractGolemSearchToFillInEssentiaGoal extends AbstractGolemEssentiaGoal{
    public AbstractGolemSearchToFillInEssentiaGoal(BaseGolemEntity golem) {
        super(golem);
    }

    protected GolemEssentiaJarSearchingHelper.BestEssentiaContainerResultReceiver searchResultReceiver =
            new GolemEssentiaJarSearchingHelper.BestEssentiaContainerResultReceiver();

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
}
