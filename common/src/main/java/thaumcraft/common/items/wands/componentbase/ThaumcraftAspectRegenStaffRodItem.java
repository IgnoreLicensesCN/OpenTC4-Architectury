package thaumcraft.common.items.wands.componentbase;

import com.linearity.opentc4.utils.collectionlike.ObjectIntPair;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.common.items.abstracts.wandabstraction.component.IInventoryTickableComponentItem;

import java.util.ArrayList;
import java.util.List;

import static thaumcraft.common.items.wands.componentbase.ThaumcraftAspectRegenWandRodItem.regenCentiVisAsComponent;

public abstract class ThaumcraftAspectRegenStaffRodItem extends ThaumcraftStaffRodItem implements IInventoryTickableComponentItem {
    public ThaumcraftAspectRegenStaffRodItem(Properties properties, CentiVisList<Aspect> canRegenCentiVisAndValue) {
        super(properties);
        this.canRegenCentiVisAndValue = canRegenCentiVisAndValue;
    }
    protected final @Unmodifiable CentiVisList<Aspect> canRegenCentiVisAndValue;

    @Override
    public void tickAsComponent(@NotNull ItemStack finalParentStack, @NotNull ItemStack usingWand, @NotNull ItemStack selfStack, Level level, Entity owner, int finalParentAtContainerIndex, boolean parentSelected) {
        regenCentiVisAsComponent(usingWand,owner,canRegenCentiVisAndValue);
    }
}
