package thaumcraft.api.aspects;


/**
 * @author Azanor
 * This interface is used by tiles that use or transport vis. 
 * Only tiles that implement this interface will be able to connect to vis conduits or other thaumic devices
 * <p>change:Separated into {@link IEssentiaTransportOutBlockEntity} and {@link IEssentiaTransportInBlockEntity}</p>
 * <p>--IgnoreLicensesCN</p>
 */
public interface IEssentiaTransportBlockEntity extends IEssentiaTransportOutBlockEntity, IEssentiaTransportInBlockEntity {


//	/**
//	 * Used by jars and alembics that have smaller than normal hitboxes
//	 * @return true if you want the conduit to extend a little further into the block.
//	 */
//	@Deprecated(forRemoval = true)
//	boolean renderExtendedTube();



}

