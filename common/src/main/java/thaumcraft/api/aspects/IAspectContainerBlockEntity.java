package thaumcraft.api.aspects;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.LinkedTreeAspectList;
import thaumcraft.api.listeners.aspects.item.bonus.IBonusAspectOwnerItem;

/**
 * 
 * @author azanor
 * 
 * Used by blocks like the crucible and alembic to hold their aspects. 
 * Tiles extending this interface will have their aspects show up when viewed by goggles of aspect revealing
 *
 * <p>renamed from IAspectContainer</p>
 * <p>change:Node will no longer use this.they will go to {@link IWorldlyCentiVisContainerBlockEntity}.</p>
 * <p>Vis and aspect(for infusion) should be different!</p>
 * <p>Separated into {@link IAspectOutBlockEntity} and {@link IAspectInBlockEntity}</p>
 * <p>for items contains bonus aspects use {@link IBonusAspectOwnerItem}</p>
 * <p>for BEs needs to show aspect use {@link IAspectDisplayBlockEntity}</p>
 * <p>for itemss needs to show aspect use {@link IAspectDisplayItem}</p>
 * <p>--IgnoreLicensesCN</p>
 */
@Deprecated(forRemoval = true)
public interface IAspectContainerBlockEntity<Asp extends Aspect>
//		extends /*IAspectOutBlockEntity<Asp>, */IAspectInBlockEntity<Asp>
{

	@UnmodifiableView
	@NotNull
    AspectList<Asp> getAspects();
	int getAspectTypeSize();
	int getAspectMaxSize();
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



