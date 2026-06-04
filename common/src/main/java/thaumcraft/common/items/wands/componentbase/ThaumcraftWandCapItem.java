package thaumcraft.common.items.wands.componentbase;

import com.linearity.opentc4.annotations.forvalue.PercentageFloatValue;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMaps;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.IWandCapPropertiesOwnerComponent;
import thaumcraft.api.wands.IWandComponentNameOwnerItem;

import java.util.Collections;
import java.util.Map;

//i say why not set wand cap properties here?you just put resourceKey into wand then find item.
//but in fact the key part is WandCapPropertiesOwner not this class.that provides some apis.
public abstract class ThaumcraftWandCapItem extends Item implements IWandCapPropertiesOwnerComponent, IWandComponentNameOwnerItem /*, WandSpellEventListenable(we don't use this in vanilla tc4)*/ {
    public ThaumcraftWandCapItem(Properties properties) {
        super(properties);
    }

    @Override
    public @PercentageFloatValue float getBaseCostModifier() {
        return 0;
    }

    @Override
    public @NotNull Object2FloatMap<Aspect> getSpecialCostModifierAspects() {
        return Object2FloatMaps.emptyMap();
    }

}
