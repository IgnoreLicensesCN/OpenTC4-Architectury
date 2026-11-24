package com.linearity.opentc4.mixin.accessors;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DropExperienceBlock.class)
public interface DropExperienceBlockAccessor {
    @Accessor("xpRange")
    default IntProvider getXpRange(){
        throw new IllegalStateException("should not reach here");
    }

    @Unique
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
