package thaumcraft.common.items.wands.render.waveanimations.impl;

import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.render.waveanimations.AbstractWandWaveAnimation;
import thaumcraft.common.lib.resourcelocations.WandWaveAnimationResourceLocation;

public class WandChargeAnimation extends AbstractWandWaveAnimation {
    public static final WandChargeAnimation INSTANCE = new WandChargeAnimation(WandWaveAnimationResourceLocation.of(Thaumcraft.MOD_ID,"charge"));
    protected WandChargeAnimation(WandWaveAnimationResourceLocation id) {
        super(id);
    }
}
