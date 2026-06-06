package thaumcraft.common.runicshield.shieldtypes;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.resourcelocations.RunicShieldTypeResourceLocation;

public class CommonRunicShield extends AbstractRunicShieldType<Void> {
    public CommonRunicShield(RunicShieldTypeResourceLocation shieldTypeResourceLocation, int priority) {
        super(shieldTypeResourceLocation, priority);
    }

    @Override
    public @Nullable CompoundTagAccessor<Void> getOwningTagAccessor() {
        return null;
    }
}
