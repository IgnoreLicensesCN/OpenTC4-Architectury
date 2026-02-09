package thaumcraft.api.aspects;


import net.minecraft.core.Direction;

/**
 * @author Azanor
 * This interface is used by tiles that use or transport vis. 
 * Only tiles that implement this interface will be able to connect to vis conduits or other thaumic devices
 */
public interface IEssentiaTransport {
	/**
	 * Is this tile able to connect to other vis users/sources on the specified side?
	 * @param face
	 * @return
	 */
    boolean isConnectable(Direction face);
	
	/**
	 * Is this side used to input essentia?
	 * @param face
	 * @return
	 */
	boolean canInputFrom(Direction face);
	
	/**
	 * Is this side used to output essentia?
	 * @param face
	 * @return
	 */
	boolean canOutputTo(Direction face);
			
	/**
	 * Sets the amount ofAspectVisList suction this block will apply
	 */
    void setSuction(Aspect aspect, int amount);
	
	/**
	 * Returns the type ofAspectVisList suction this block is applying.
	 * @param face
	 * 		the location from where the suction is being checked
	 * @return
	 * 		a return type ofAspectVisList null indicates the suction is untyped and the first thing available will be drawn
	 */
    Aspect getSuctionType(Direction face);
	
	/**
	 * Returns the strength ofAspectVisList suction this block is applying.
	 * @param face
	 * 		the location from where the suction is being checked
	 * @return
	 */
    int getSuctionAmount(Direction face);
	
	/**
	 * remove the specified amount ofAspectVisList essentia from this transport tile
	 * @return how much was actually taken
	 */
    int takeEssentia(Aspect aspect, int amount, Direction face);
	
	/**
	 * add the specified amount ofAspectVisList essentia to this transport tile
	 * @return how much was actually added
	 */
    int addEssentia(Aspect aspect, int amount, Direction face);
	
	/**
	 * What type ofAspectVisList essentia this contains
	 * @param face
	 * @return
	 */
    Aspect getEssentiaType(Direction face);
	
	/**
	 * How much essentia this block contains
	 * @param face
	 * @return
	 */
    int getEssentiaAmount(Direction face);
	
	

	/**
	 * Essentia will not be drawn from this container unless the suction exceeds this amount.
	 * @return the amount
	 */
    int getMinimumSuction();

	/**
	 * Return true if you want the conduit to extend a little further into the block. 
	 * Used by jars and alembics that have smaller than normal hitboxes
	 * @return
	 */
	boolean renderExtendedTube();

	
	
}

