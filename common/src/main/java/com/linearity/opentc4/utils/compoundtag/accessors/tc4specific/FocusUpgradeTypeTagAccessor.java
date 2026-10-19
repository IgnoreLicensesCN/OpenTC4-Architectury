package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific;

import com.linearity.opentc4.utils.compoundtag.accessors.CacheableCompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.FocusUpgradeTypeResourceLocationTagAccessor;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;

public class FocusUpgradeTypeTagAccessor extends CacheableCompoundTagAccessor<FocusUpgradeType> {
    private final FocusUpgradeTypeResourceLocationTagAccessor internalAccessor;
    public FocusUpgradeTypeTagAccessor(String tagKey) {
        super(tagKey);
        this.internalAccessor = new FocusUpgradeTypeResourceLocationTagAccessor(tagKey);
    }

    @Override
    public FocusUpgradeType readFromCompoundTagWithoutCache(CompoundTag tag) {
        var loc = internalAccessor.readFromCompoundTag(tag);
        var upgrade = FocusUpgradeType.getType(loc);
        if (upgrade == null) {
            upgrade = FocusUpgradeType.EMPTY;
        }
        return upgrade;
    }

    @Override
    public void writeToCompoundTagWithoutCache(CompoundTag tag, FocusUpgradeType value) {
        internalAccessor.writeToCompoundTag(tag,value.id());
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return internalAccessor.compoundTagHasKey(tag);
    }
}
