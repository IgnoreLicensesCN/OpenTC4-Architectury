package thaumcraft.common.items.wands;

import com.linearity.opentc4.annotations.Modifiable;
import com.linearity.opentc4.annotations.ModifiableCopy;
import io.wispforest.accessories.api.AccessoryItem;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import thaumcraft.common.items.abstracts.wandabstraction.focus.IWandFocusBagItem;
import thaumcraft.common.items.abstracts.wandabstraction.focus.IWandFocusItem;
import thaumcraft.common.items.abstracts.IBundleLikeItem;

import java.util.List;

public class FocusPouchItem extends AccessoryItem implements IBundleLikeItem, IWandFocusBagItem {
    public FocusPouchItem(Properties properties) {
        super(properties);
    }
    public FocusPouchItem() {
        this(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack itemStack, Slot slot, ClickAction clickAction, Player player) {
        return bundleOverrideStackedOnOther(itemStack, slot, clickAction, player);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack itemStack, ItemStack itemStack2, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        return bundleOverrideOtherStackedOnMe(itemStack, itemStack2, slot, clickAction, player, slotAccess);
    }

    @Override
    public int getBundleMaxItemCount(ItemStack bundleStack) {
        return 18;
    }

    @Override
    public boolean canInsertStackToBundle(ItemStack bundleStack, ItemStack stackToInsert) {
        return stackToInsert.getItem() instanceof IWandFocusItem<?>;
    }

    @Override
    public ItemStack extractStackAtFocusBag(ItemStack bagStack, int slot) {
        return extractStackAtBundle(bagStack, slot);
    }

    @Override
    public void insertStackToFocusBag(ItemStack bagStack, @Modifiable ItemStack stackToInsert) {
        insertStackToBundle(bagStack, stackToInsert);
    }

    @Override
    public @ModifiableCopy List<ItemStack> getStacksWithSlotOrder(ItemStack bagStack) {
        return getStacksInsideBundle(bagStack);
    }
}
