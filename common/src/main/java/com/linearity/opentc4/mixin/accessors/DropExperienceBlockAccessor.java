package com.linearity.opentc4.mixin.accessors;

import com.linearity.opentc4.mixinattachments.DropExperienceBlockXpOwner;
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
public interface DropExperienceBlockAccessor extends DropExperienceBlockXpOwner {
    @Accessor("xpRange")
    default IntProvider getXpRange(){
        throw new IllegalStateException("should not reach here");
    }
}
