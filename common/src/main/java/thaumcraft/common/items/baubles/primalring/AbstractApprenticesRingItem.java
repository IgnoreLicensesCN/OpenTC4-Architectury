package thaumcraft.common.items.baubles.primalring;

import io.wispforest.accessories.api.AccessoryItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.listeners.aspects.item.bonus.IBonusAspectOwnerItem;
import thaumcraft.common.items.abstracts.IVisDiscountGearItem;
import thaumcraft.common.runicshield.IAugmentationRunicShieldProviderItem;

import java.util.List;

public class AbstractApprenticesRingItem extends AccessoryItem 
        implements 
        IVisDiscountGearItem, 
        IBonusAspectOwnerItem<Aspect>,
        IAugmentationRunicShieldProviderItem
{
    protected final Aspect aspect;
    public AbstractApprenticesRingItem(Properties properties,Aspect aspect) {
        super(properties);
        this.aspect = aspect;
    }
    public AbstractApprenticesRingItem(Aspect aspect) {
        this(new Properties().stacksTo(1),aspect);
    }

    @Override
    public @Unmodifiable @NotNull AspectList<Aspect> getOwningBonusAspects(ItemStack stack) {
        return UnmodifiableAspectList.of(aspect);
    }

    @Override
    public int getVisCostPercentDecrease(ItemStack stack, @Nullable LivingEntity living, @Nullable Aspect aspect) {
        if (aspect == this.aspect) {
            return 1;
        }
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        addVisDiscountToolTip(itemStack,level,list,tooltipFlag,null,aspect);
    }
}
