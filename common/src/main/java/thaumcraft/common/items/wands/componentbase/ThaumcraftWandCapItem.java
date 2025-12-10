package thaumcraft.common.items.wands;

import com.linearity.opentc4.datautils.SimplePair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.WandCapPropertiesOwner;
import thaumcraft.api.wands.WandComponentNameOwner;
import thaumcraft.api.wands.WandSpellEventListenable;
import thaumcraft.api.wands.WandSpellEventType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

//i say why not set wand cap properties here?you just put resourceKey into wand then find item.
//but in fact the key part is WandCapPropertiesOwner not this class.that provides some apis.
public abstract class ThaumcraftWandCapItem extends Item implements WandCapPropertiesOwner, WandComponentNameOwner /*, WandSpellEventListenable(we don't use this in vanilla tc4)*/ {
    public ThaumcraftWandCapItem(Properties properties) {
        super(properties);
    }

    @Override
    public float getBaseCostModifier() {
        return 1;
    }

    @Override
    public @NotNull Map<Aspect,Float> getSpecialCostModifierAspects() {
        return Collections.emptyMap();
    }

}
