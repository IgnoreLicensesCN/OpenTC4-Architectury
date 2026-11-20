package thaumcraft.common.entities.ai.goals.golem.transportresources.essentiainteraction;

import thaumcraft.common.entities.ai.goals.golem.AbstractGolemGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;

//TODO:[maybe wont finished]essentia container interaction logic API (more than IEssentiaTransportInBE)
public abstract class AbstractGolemEssentiaGoal extends AbstractGolemGoal {

    public AbstractGolemEssentiaGoal(BaseGolemEntity golem) {
        super(golem);
    }

    public double reachRange() {
        return 2;
    }

    public double reachRangeSqr(){
        return reachRange() * reachRange();
    }
}
