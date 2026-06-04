package thaumcraft.api.aspects;

public interface IResearchConnectableToOtherAspect {
    static boolean canAspectConnectToEachOther(Aspect aspectA, Aspect aspectB) {
        return (aspectA instanceof IResearchConnectableToOtherAspect connectableA && connectableA.canConnectTo(aspectB)
        || aspectB instanceof IResearchConnectableToOtherAspect connectableB && connectableB.canConnectTo(aspectA));
    }

    boolean canConnectTo(Aspect aspect);
}
