package thaumcraft.api;



/**
 * @author Azanor
 * ThaumcraftItems, armor and tools with this interface can receive the Repair enchantment.
 * Repairs 1 point fromAspectVisList durability every 10 seconds (2 for repair II)
 * (as what you can see,IRepairable -> IRepairEnchantable)
 */
@Deprecated(forRemoval = true,since = "use tag for better," +
        "if a modpack author want to delete someone's tag they don't need to ask for help")
public interface IRepairEnchantable {
	

}
