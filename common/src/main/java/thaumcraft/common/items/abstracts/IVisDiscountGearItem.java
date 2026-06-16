package thaumcraft.common.items.abstracts;

import com.linearity.opentc4.annotations.forvalue.PercentageIntValue;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Azanor
 * <s>ItemArmor with this interface will grant a discount to the vis cost of actions the wearer performs with casting wands.
 * The amount returned is the percentage by which the cost is discounted. There is a built-int max discount of 50%, but
 * individual items really shouldn't have a discount more than 5%</s>
 * <p>Forget above --IgnoreLicensesCN</p>
 */
public interface IVisDiscountGearItem {
	//if you dont want to mixin a interface,put it here
	Map<Item, IVisDiscountGearItem> VIS_DISCOUNT_GEAR_ADDITIONS = new HashMap<>();

	static @Nullable IVisDiscountGearItem getDiscountGearHandlerForItem(Item item) {
		var gear = VIS_DISCOUNT_GEAR_ADDITIONS.get(item);
		if (gear == null && item instanceof IVisDiscountGearItem visDiscountGear) {
			gear = visDiscountGear;
		}
		return gear;
	}

	//changed:decrease cost if positive and add cost if negative
	@PercentageIntValue
	int getVisCostPercentDecrease(ItemStack stack, @Nullable LivingEntity living, @Nullable Aspect aspect);

	default void addVisDiscountToolTip(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag, @Nullable LivingEntity livingWatchingThis, @Nullable Aspect aspect) {
		var costDecrease = this.getVisCostPercentDecrease(stack, livingWatchingThis, aspect);
		if (costDecrease != 0){
			var componentToAdd = Component.translatable("tc.visdiscount",
							": " + costDecrease + "%")
					.withStyle(ChatFormatting.DARK_PURPLE);
			if (aspect != null) {
				componentToAdd = aspect.getImageComponent().copy().append(aspect.getNameColored()).append(componentToAdd);
			}
			tooltip.add(componentToAdd);
		}
	}
}
