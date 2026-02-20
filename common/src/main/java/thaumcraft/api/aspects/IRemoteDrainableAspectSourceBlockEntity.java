package thaumcraft.api.aspects;


/**
 * @author Azanor
 * <p>
 * This interface is implemented by tile entites (or possibly anything else) like jars
 * so that they can act as an essentia source for blocks like the infusion altar.
 *
 * <p>yes i renamed from IAspectSource --IgnoreLicensesCN </p>
 */
public interface IRemoteDrainableAspectSourceBlockEntity<Asp extends Aspect>
        /*extends IAspectContainerBlockEntity<Asp>*/ {
    //return how much can provide.
    int canProvideAspectAmountForRemoteDrain(Asp aspect);
    //check with method above first.
    //@return false if drain failed
    boolean drainAspectRemote(Asp aspect, int amount);
}
