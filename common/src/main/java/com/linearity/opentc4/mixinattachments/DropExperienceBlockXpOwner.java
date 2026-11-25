package com.linearity.opentc4.mixinattachments;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Unique;

public interface DropExperienceBlockXpOwner {
    IntProvider getXpRange();

    default int opentc4$getRandomXp(
            BlockState state,
            LevelReader level,
            RandomSource randomSource,
            BlockPos pos,
            int fortuneLevel,
            int silkTouchLevel
    ) {
        return silkTouchLevel == 0 ? getXpRange().sample(randomSource) : 0;
    }
}
