package thaumcraft.api;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemRunic extends Item implements IRunicArmor  {
	public static final Item.Properties properties = new Item.Properties();

	int charge;
	
	public ItemRunic (int charge)
    {
        super(properties);
        this.charge = charge;
    }
			
	@Override
	public int getRunicCharge(ItemStack itemstack) {
		return charge;
	}
	
}
