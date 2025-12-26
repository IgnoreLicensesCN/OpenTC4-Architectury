package thaumcraft.api.wands;

import com.linearity.opentc4.OpenTC4CommonProxy;
import com.linearity.opentc4.utils.StatCollector;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.expands.wandconsumption.ConsumptionModifierCalculator;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WandUtils {

    //generate capacity? or whatever you like
    public static Map<Aspect, Integer> getPrimalAspectMapWithValue(int value) {
        return Aspect.getPrimalAspects().stream().collect(Collectors.toMap(a -> a, a -> value));
    }

    public static final DecimalFormat decimalFormat = new DecimalFormat("#######.##");
    public static void appendWandHoverText(Item wandItem, ItemStack wandStack, @Nullable Level level, List<Component> list, TooltipFlag flag, LivingEntity livingEntity) {
        int pos = list.size();
        String tt2 = "";
        boolean shiftKeyDownFlag = OpenTC4CommonProxy.INSTANCE.isShiftKeyDown();
        if (wandItem instanceof IVisContainer IVisContainer) {
            StringBuilder tt = new StringBuilder();

            var visOwning = IVisContainer.getAllVisOwning(wandStack);
            var visCapacity = IVisContainer.getAllVisCapacity(wandStack);


            for (var entry : visOwning.entrySet()) {
                var aspect = entry.getKey();
                String amountString = String.valueOf(((float)entry.getValue()) / 100.0F);
                String capacityString = String.valueOf(((float)visCapacity.getOrDefault(entry.getKey(),0)) / 100.0F);
                float mod = ConsumptionModifierCalculator.getConsumptionModifier(
                        wandItem,
                        wandStack,
                        livingEntity,
                        aspect,
                        false);
                String consumptionString = decimalFormat.format(mod * 100.0F);
                String focusConsumptionString = "";
                if (wandItem instanceof IWandFocusEngine engine && engine.canApplyFocus()) {
                    var focusStack = engine.getFocusItemStack(wandStack);
                    var focusItem = focusStack.getItem();
                    if (focusItem instanceof IWandFocusItem wandFocusItem) {
                        int amt = wandFocusItem.getVisCost(focusStack,wandStack).getAmount(aspect);
                        if (amt > 0) {
                            focusConsumptionString = "§r, " + decimalFormat.format((float) amt * mod / 100.0F) + " " + StatCollector.translateToLocal(wandFocusItem.isVisCostPerTick() ? "wandItem.Focus.cost2" : "wandItem.Focus.cost1");
                        }
                    }
                }
                if (shiftKeyDownFlag) {
                    list.add(Component.literal(
                            " §" + aspect.getChatcolor() + aspect.getName()
                                    + "§r x " + amountString + "/" + capacityString
                                    + ", §o(" + consumptionString + "% " + StatCollector.translateToLocal("tc.vis.cost") + ")"
                                    + focusConsumptionString)
                    );
                } else {
                    if (!tt.isEmpty()) {
                        tt.append(" | ");
                    }

                    tt.append("§").append(aspect.getChatcolor()).append(amountString).append("§r");
                }
            }

            if (!shiftKeyDownFlag){
                list.add(Component.literal(tt.toString()));
            }
//            for (Aspect aspect : Aspect.getPrimalAspects()) {
//                if (tag.hasKey(aspect.getTag())) {
//                    String amount = decimalFormat.format((float) tag.getInteger(aspect.getTag()) / 100.0F);
//
//                    String consumption = decimalFormat.format(mod * 100.0F);
//                    ++num;
//                    tot = (int) ((float) tot + mod * 100.0F);
//                    String text = "";
//                    ItemStack focus = this.getFocusItem(wandStack);
//                    if (focus != null) {
//                        int amt = ((ItemFocusBasic) focus.getItem()).getVisCost(focus).getAmount(aspect);
//                        if (amt > 0) {
//                            text = "§r, " + decimalFormat.format((float) amt * mod / 100.0F) + " " + StatCollector.translateToLocal(((ItemFocusBasic) focus.getItem()).isVisCostPerTick(focus) ? "wandItem.Focus.cost2" : "wandItem.Focus.cost1");
//                        }
//                    }
//
//                    if (Thaumcraft.proxy.isShiftKeyDown()) {
//                        list.add(" §" + aspect.getChatcolor() + aspect.getName() + "§r x " + amount + ", §o(" + consumption + "% " + StatCollector.translateToLocal("tc.vis.cost") + ")" + text);
//                    } else {
//                        if (tt.length() > 0) {
//                            tt.append(" | ");
//                        }
//
//                        tt.append("§").append(aspect.getChatcolor()).append(amount).append("§r");
//                    }
//                }
//            }

//            if (!Thaumcraft.proxy.isShiftKeyDown() && num > 0) {
//                list.add(tt.toString());
//                tot /= num;
//                tt2 = " (" + tot + "% " + StatCollector.translateToLocal("tc.vis.costavg") + ")";
//            }
        }

//        list.add(pos, ChatFormatting.GOLD + StatCollector.translateToLocal("wandItem.capacity.text") + " " + this.getMaxVis(wandStack) / 100 + "§r" + tt2);

        if (wandItem instanceof IWandFocusEngine engine && engine.canApplyFocus()) {
            var focus = engine.getFocusItemStack(wandStack);
            var focusItem = focus.getItem();
            if (focusItem instanceof IWandFocusItem wandFocusItem) {

                list.add(Component.literal(ChatFormatting.BOLD + "" + ChatFormatting.ITALIC + ChatFormatting.GREEN + focusItem.getName(focus)));
                if (OpenTC4CommonProxy.INSTANCE.isShiftKeyDown()){
                    addFocusInformation(wandFocusItem,focus,list,flag);
                }
            }
        }

    }

    public static void addFocusInformation(IWandFocusItem focus,ItemStack focusstack, List<Component> list, TooltipFlag flag) {
		for (var entry:focus.getAppliedWandUpgrades(focusstack).entrySet()) {
            FocusUpgradeType type = entry.getKey();
            var id = type.id();
            var lvl = entry.getValue();
            list.add(Component.literal(
                    ChatFormatting.DARK_PURPLE
                            + type.getLocalizedName()
                            + (lvl>1?" "+StatCollector.translateToLocal("enchantment.level." + lvl):"")));
		}
	}
}
