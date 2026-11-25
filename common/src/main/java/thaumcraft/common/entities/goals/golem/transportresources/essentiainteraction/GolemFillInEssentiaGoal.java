package thaumcraft.common.entities.goals.golem.transportresources.essentiainteraction;

import net.minecraft.sounds.SoundEvents;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportInBlockEntity;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.core.EssentiaGolemCoreItem;

public class GolemFillInEssentiaGoal extends AbstractGolemSearchToFillInEssentiaGoal {
    public GolemFillInEssentiaGoal(BaseGolemEntity golem) {
        super(golem);
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
