package thaumcraft.common.items.wands.render.waveanimations.impl;

import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.render.waveanimations.AbstractWandWaveAnimation;
import thaumcraft.common.lib.resourcelocations.WandWaveAnimationResourceLocation;

public class WandWaveAnimation extends AbstractWandWaveAnimation {
    public static final WandWaveAnimation INSTANCE = new WandWaveAnimation(WandWaveAnimationResourceLocation.of(Thaumcraft.MOD_ID,"wave"));
    protected WandWaveAnimation(WandWaveAnimationResourceLocation id) {
        super(id);
    }
}
