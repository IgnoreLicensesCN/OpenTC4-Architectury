package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinaccessors.DropExperienceBlockAccessor;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.DropExperienceBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DropExperienceBlock.class)
public class DropExperienceBlockMixin implements DropExperienceBlockAccessor {
    @Shadow
    @Final
    private IntProvider xpRange;

    @Override
    public IntProvider opentc4$getXpRange() {
        return xpRange;
    }
}
