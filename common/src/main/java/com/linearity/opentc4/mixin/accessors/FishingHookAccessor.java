package com.linearity.opentc4.mixin.accessors;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FishingHook.class)
public interface FishingHookAccessor {
    @Invoker("shouldStopFishing")
    boolean shouldStopFishing(Player player);
    @Invoker("pullEntity")
    void pullEntity(Entity entity);
    @Accessor("nibble")
    int getNibble();
    @Accessor("luck")
    int getLuck();
    @Accessor("luck")
    void setLuck(int luck);
    @Accessor("lureSpeed")
    void setLureSpeed(int lureSpeed);
    @Accessor("syncronizedRandom")
    RandomSource getSyncronizedRandom();
    @Accessor("life")
    int getLife();
    @Accessor("life")
    void setLife(int life);
    @Invoker("checkCollision")
    void checkCollision();
    @Invoker("setHookedEntity")
    void setHookedEntity(Entity entity);
    @Accessor("timeUntilHooked")
    int getTimeUntilHooked();
    @Accessor("openWater")
    boolean getOpenWater();
    @Accessor("openWater")
    void setOpenWater(boolean openWater);
    @Accessor("outOfWaterTime")
    int getOutOfWaterTime();
    @Accessor("outOfWaterTime")
    void setOutOfWaterTime(int outOfWaterTime);
    @Invoker("calculateOpenWater")
    boolean calculateOpenWater(BlockPos blockPos);
    @Accessor("biting")
    boolean getBiting();
    @Invoker("catchingFish")
    void catchingFish(BlockPos blockPos);
    @Invoker("retrieve")
    int retrieve(ItemStack itemStack);
}
