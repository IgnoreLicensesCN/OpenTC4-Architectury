package thaumcraft.common.entities.golems;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class GolemBasicAbilityProperties {
    @Builder.Default
    public final int basicUpgradeSize = 1;
    @Builder.Default
    public final int carryPower = 1;
    @Builder.Default
    public final int regenDelay = 75;
    @Builder.Default
    public final int strength = 0;
    @Builder.Default
    public final int attackDelay = 20;
    @Builder.Default
    public final int regenAmount = 1;
    @Builder.Default
    public final int attackFireTicks = 0;
    @Builder.Default
    public final int homeRange = 16;
    @Builder.Default
    public final int basicInventorySize = 6;
    @Builder.Default
    public final int basicInventorySizeMultiplier = 1;
    @Builder.Default
    public final int inventorySizeMultiplier = 1;
    @Builder.Default
    public final int basicSpeedMultiplier = 1;
    @Builder.Default
    public final int basicHomeRange = 16;
}
