package thaumcraft.api;

import com.linearity.opentc4.annotations.forvalue.PercentageIntValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Azanor
 * ItemArmor with this interface will grant a discount to the vis cost of actions the wearer performs with casting wands.
 * The amount returned is the percentage by which the cost is discounted. There is a built-int max discount of 50%, but
 * individual items really shouldn't have a discount more than 5%
 */
public interface IVisDiscountGear {
	//if you dont want to mixin a interface,put it here
	Map<Item,IVisDiscountGear> VIS_DISCOUNT_GEAR_ADDITIONS = new HashMap<>();

	static @Nullable IVisDiscountGear getDiscountGearHandlerForItem(Item item) {
		var gear = VIS_DISCOUNT_GEAR_ADDITIONS.get(item);
		if (gear == null && item instanceof IVisDiscountGear visDiscountGear) {
			gear = visDiscountGear;
		}
		return gear;
	}

	//add cost if positive and decrease post if negative
	@PercentageIntValue
	int getVisDiscount(ItemStack stack, LivingEntity living, Aspect aspect);

}
