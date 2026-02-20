package thaumcraft.api.aspects;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

/**
 * 
 * @author azanor
 * 
 * Used by blocks like the crucible and alembic to hold their aspects. 
 * Tiles extending this interface will have their aspects show up when viewed by goggles of aspect revealing
 *
 *<p>frok IgnoreLicensesCN:</p>
 * Node will no longer use this.they will go to {@link IWorldlyCentiVisContainerBlockEntity}.
 * Vis and aspect(for infusion) should be different!
 */
public interface IAspectContainerBlockEntity<Asp extends Aspect>
		extends IAspectOutBlockEntity<Asp>,
		IAspectInBlockEntity<Asp>
{

	@UnmodifiableView
	@NotNull
	AspectList<Asp> getAspects();
	int getAspectTypeSize();//1 for jar and Integer.MAX_VALUE for ESSENTIA_RESERVOIR
	int getAspectMaxSize();//64 for jar and 256 for ESSENTIA_RESERVOIR
	void setAspects(AspectList<Asp> aspects);
//	/**
//	 * removes a bunch of aspect different aspects and amounts from the tile entity.
//	 * @param ot the ObjectTags object that contains the aspects and their amounts.
//	 * @return true if all the aspects and their amounts were available and successfully removed
//	 *
//	 * Going away in the next major patch
//	 */
//	@Deprecated
//    boolean takeFromContainer(AspectList<Asp> ot);


//	/**
//	 * Checks if the tile entity contains all the listed aspects and their amounts
//	 * @param ot the ObjectTags object that contains the aspects and their amounts.
//	 * @return
//	 *
//	 * Going away in the next major patch
//	 */
//	@Deprecated
//    boolean doesContainerContain(AspectList<Asp> ot);
	
}



