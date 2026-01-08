package com.linearity.opentc4.mixinaccessors;

public interface AbstractFurnaceBlockEntityAccessor {
    int opentc4$getLitTime();
    int opentc4$getLitDuration();
    int opentc4$getCookingProgress();
    int opentc4$getCookingTotalTime();
    void opentc4$setLitTime(int litTime);
    void opentc4$setLitDuration(int litDuration);
    void opentc4$setCookingProgress(int cookingProgress);
    void opentc4$setCookingTotalTime(int cookingTotalTime);
    boolean opentc4$isLit();
    int opentc4$getTickCount();
    void opentc4$setTickCount(int tickCount);
    int opentc4$getAndIncrementLoopingCounter();
    int opentc4$incrementAndGetLoopingCounter();
    int opentc4$decrementAndGetDirectionLoopingCounter();
    int opentc4$getDirectionLoopingCounter();
}
