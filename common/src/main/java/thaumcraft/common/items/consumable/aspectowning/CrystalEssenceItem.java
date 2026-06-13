package thaumcraft.common.items.consumable.aspectowning;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.*;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.listeners.aspects.item.bonus.IBonusAspectOwnerItem;

import static com.linearity.opentc4.Consts.CrystalEssenceItemTagAccessors.OWNING_ASPECT;

public class CrystalEssenceItem extends Item implements IBonusAspectOwnerItem<Aspect>, IAspectDisplayItem<Aspect> {
    public CrystalEssenceItem(Properties properties) {
        super(properties);
    }
    public CrystalEssenceItem() {
        this(new Properties());
    }
    public ItemStack ofAspect(Aspect aspect) {
        var result = new ItemStack(this);
        var tag = result.getOrCreateTag();
        OWNING_ASPECT.writeToCompoundTag(tag, aspect);
        return result;
    }

    @Override
    public @Unmodifiable @NotNull AspectList<Aspect> getOwningBonusAspects(ItemStack stack) {
        return UnmodifiableAspectList.of(getOwningAspect(stack));
    }

    public Aspect getOwningAspect(ItemStack stack) {
        if (!stack.hasTag()){
            return Aspects.EMPTY;
        }
        var tag = stack.getTag();
        if (tag == null){
            return Aspects.EMPTY;
        }
        var aspect = OWNING_ASPECT.readFromCompoundTag(tag);
        if (aspect.isEmpty()){
            return Aspects.EMPTY;
        }
        return aspect;
    }
    public void setOwningAspect(ItemStack stack, Aspect aspect) {
        var tag = stack.getOrCreateTag();
        OWNING_ASPECT.writeToCompoundTag(tag, aspect);
    }

    @Override
    public @NotNull @UnmodifiableView AspectList<Aspect> getAspectsToDisplay(ItemStack stack) {
        return getOwningBonusAspects(stack);
    }
}
