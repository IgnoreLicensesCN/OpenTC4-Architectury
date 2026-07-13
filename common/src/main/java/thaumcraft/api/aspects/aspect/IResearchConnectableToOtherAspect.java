package thaumcraft.api.aspects.aspect;

import thaumcraft.api.aspects.Aspect;

public interface IResearchConnectableToOtherAspect {
    static boolean canAspectConnectToEachOther(Aspect aspectA, Aspect aspectB) {
        return (aspectA instanceof IResearchConnectableToOtherAspect connectableA && connectableA.canConnectTo(aspectB)
        || aspectB instanceof IResearchConnectableToOtherAspect connectableB && connectableB.canConnectTo(aspectA));
    }

    //maybe there could be a "unify connector" aspect and hard(maybe) to get?
    //ate too much mana bean and get some?

    //now the logic comes to "I connect you" instead of "We connect to each other"
    boolean canConnectTo(Aspect aspect);
}
