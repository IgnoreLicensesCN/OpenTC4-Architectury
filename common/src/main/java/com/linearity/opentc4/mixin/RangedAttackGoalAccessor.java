package com.linearity.opentc4.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RangedAttackGoal.class)
public interface RangedAttackGoalAccessor {
    @Accessor("attackTime")
    int getAttackTime();

    @Accessor("attackTime")
    void setAttackTime(int value);

    @Accessor("mob")
    Mob getMob();

    @Accessor("rangedAttackMob")
    RangedAttackMob getRangedAttackMob();

    @Accessor("attackIntervalMax")
    int getAttackIntervalMax();
}
