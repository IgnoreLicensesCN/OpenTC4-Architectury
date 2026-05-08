package thaumcraft.common.items.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.UnmodifiableAspectList;
import thaumcraft.api.listeners.aspects.item.bonus.IBonusAspectOwnerItem;

import java.util.List;

import static com.linearity.opentc4.Consts.CrystalEssenceItemTagAccessors.OWNING_ASPECT;
import static thaumcraft.api.aspects.AspectList.addAspectDescriptionToList;

public class CrystalEssenceItem extends Item implements IBonusAspectOwnerItem<Aspect> {
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
        if (!stack.hasTag()){
            return UnmodifiableAspectList.EMPTY;
        }
        var tag = stack.getTag();
        if (tag == null){
            return UnmodifiableAspectList.EMPTY;
        }
        var aspect = OWNING_ASPECT.readFromCompoundTag(tag);
        if (aspect.isEmpty()){
            return UnmodifiableAspectList.EMPTY;
        }
        return UnmodifiableAspectList.of(aspect);
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
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        if (level != null && level.isClientSide){
            addAspectDescriptionToList(getOwningBonusAspects(itemStack), Minecraft.getInstance().player, list);
        }
    }
}
