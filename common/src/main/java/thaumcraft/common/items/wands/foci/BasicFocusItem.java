package thaumcraft.common.items.wands.foci;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.items.abstracts.wandabstraction.focus.IWandFocusItem;
import thaumcraft.common.items.wands.WandCooldownManager;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.linearity.opentc4.Consts.FocusUpgradeCompoundTagAccessors.FOCUS_UPGRADE_ACCESSOR;

public abstract class BasicFocusItem extends Item implements IWandFocusItem<Aspect> {
    public BasicFocusItem(Properties properties) {
        super(properties);
    }

    public void addFocusInformation(ItemStack focusstack, List<Component> list) {
        for (var entry: this.getAppliedFocusUpgrades(focusstack).object2IntEntrySet()) {
            var upgradeType = FocusUpgradeType.getType(entry.getKey().id());
            list.add(
                    upgradeType.name().copy()
                            .append(" ")
                            .append("enchantment.level." + entry.getIntValue())
                                    .withStyle(style -> style.withColor(ChatFormatting.DARK_PURPLE))
            );
        }
    }

    @Override
    public List<FocusUpgradeType> getAppliedFocusUpgradesWithOrder(ItemStack focusStack) {
        var tag = focusStack.getOrCreateTag();
        if (!FOCUS_UPGRADE_ACCESSOR.compoundTagHasKey(tag)){
            FOCUS_UPGRADE_ACCESSOR.writeToCompoundTag(tag,List.of());
            return List.of();
        }
        return FOCUS_UPGRADE_ACCESSOR.readFromCompoundTag(focusStack.getOrCreateTag())
                .stream()
                .map(FocusUpgradeType::getType)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void storeFocusUpgrades(ItemStack stack, List<FocusUpgradeType> wandUpgrades) {
        var tag = stack.getOrCreateTag();
        FOCUS_UPGRADE_ACCESSOR.writeToCompoundTag(tag,wandUpgrades.stream().map(FocusUpgradeType::id).collect(Collectors.toList()));
    }


    @Override
    public void addFocusUpgrade(ItemStack stack, FocusUpgradeType type) {
        var tag = stack.getOrCreateTag();
        var upgrades = FOCUS_UPGRADE_ACCESSOR.readFromCompoundTag(tag);
        upgrades.add(type.id());
        FOCUS_UPGRADE_ACCESSOR.writeToCompoundTag(tag,upgrades);
    }

    @Override
    public void appendHoverText(ItemStack focusStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        AspectList<Aspect>al = this.getCentiVisCost(focusStack,null);
        if (al!=null && !al.isEmpty()) {
            list.add(Component.translatable(isCentiVisCostPerTick(focusStack,null)?"item.Focus.cost2":"item.Focus.cost1"));
            for (var aspect:al.getAspectsSorted()) {
                DecimalFormat myFormatter = new DecimalFormat("#####.##");
                String amount = myFormatter.format(al.get(aspect)/100f);
                list.add(aspect.getName().copy()
                        .withStyle(style -> style.withColor(TextColor.fromRgb(aspect.getColor())))
                        .append(Component.literal(" x " + amount))
                );
            }
        }
        addFocusInformation(focusStack,list);
    }

    public boolean checkAndSetCooldown(ItemStack focusStack,LivingEntity user){
        var cooldownManager = WandCooldownManager.getFromLiving(user);
        if (cooldownManager == null) {
            return false;
        }
        boolean cooldownMeets = !cooldownManager.isOnCooldown(user);
        if (!cooldownMeets) {
            return false;
        }
        cooldownManager.setCooldown(user,getActivationCooldownTicks(focusStack));
        return true;
    }
}
