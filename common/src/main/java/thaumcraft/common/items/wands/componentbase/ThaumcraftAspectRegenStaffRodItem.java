package thaumcraft.common.items.wands.componentbase;

import com.linearity.opentc4.utils.collectionlike.ObjectIntPair;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.wands.IInventoryTickableComponentItem;

import java.util.ArrayList;
import java.util.List;

import static thaumcraft.common.items.wands.componentbase.ThaumcraftAspectRegenWandRodItem.regenCentiVisAsComponent;

public abstract class ThaumcraftAspectRegenStaffRodItem extends ThaumcraftStaffRodItem implements IInventoryTickableComponentItem {
    public ThaumcraftAspectRegenStaffRodItem(Properties properties, CentiVisList<Aspect> canRegenCentiVisAndValue) {
        super(properties);
        this.canRegenCentiVisAndValue = canRegenCentiVisAndValue;
        List<ObjectIntPair<Aspect>> canRegenList = new ArrayList<>(canRegenCentiVisAndValue.size());
        canRegenCentiVisAndValue.forEach(((aspect, value) -> canRegenList.add(new ObjectIntPair<>(aspect, value))));
        this.canRegenCentiVisAndValueAsList = canRegenList;
    }
    protected final CentiVisList<Aspect> canRegenCentiVisAndValue;
    protected final List<ObjectIntPair<Aspect>> canRegenCentiVisAndValueAsList;

    @Override
    public void tickAsComponent(@NotNull ItemStack finalParentStack, @NotNull ItemStack usingWand, @NotNull ItemStack selfStack, Level level, Entity owner, int finalParentAtContainerIndex, boolean parentSelected) {
        regenCentiVisAsComponent(usingWand,owner,canRegenCentiVisAndValue);
    }
}
