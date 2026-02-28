package thaumcraft.api.aspects;

//which can be read with Goggles
//e.g. infusion matrix and jar and things
public interface IAspectDisplayBlockEntity<Asp extends Aspect> {
    AspectList<Asp> getAspectsToDisplay();
}
