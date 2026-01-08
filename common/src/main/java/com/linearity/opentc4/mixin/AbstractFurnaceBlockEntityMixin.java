package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinaccessors.AbstractFurnaceBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.blocks.abstracts.IFurnaceAttachmentBlock;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin implements AbstractFurnaceBlockEntityAccessor {
    @Shadow int litTime;
    @Shadow int litDuration;
    @Shadow int cookingProgress;
    @Shadow int cookingTotalTime;
    @Shadow protected abstract boolean isLit();

    @Override
    public int opentc4$getLitTime() {
        return litTime;
    }

    @Override
    public int opentc4$getLitDuration() {
        return litDuration;
    }

    @Override
    public int opentc4$getCookingProgress() {
        return cookingProgress;
    }

    @Override
    public int opentc4$getCookingTotalTime() {
        return cookingTotalTime;
    }

    @Override
    public void opentc4$setCookingProgress(int cookingProgress) {
        this.cookingProgress = cookingProgress;
    }

    @Override
    public void opentc4$setCookingTotalTime(int cookingTotalTime) {
        this.cookingTotalTime = cookingTotalTime;
    }

    @Override
    public void opentc4$setLitDuration(int litDuration) {
        this.litDuration = litDuration;
    }

    @Override
    public void opentc4$setLitTime(int litTime) {
        this.litTime = litTime;
    }

    @Override
    public boolean opentc4$isLit() {
        return isLit();
    }

    @Unique int opentc4$tickCount = 0;
    @Override
    public int opentc4$getTickCount() {
        return opentc4$tickCount;
    }

    @Override
    public void opentc4$setTickCount(int tickCount) {
        this.opentc4$tickCount = tickCount;
    }

    @Unique
    private final AtomicInteger opentc4$directionLoopingCounter = new AtomicInteger(0);

    @Override
    public int opentc4$incrementAndGetLoopingCounter(){
        return opentc4$directionLoopingCounter.incrementAndGet();
    }
    public int opentc4$getAndIncrementLoopingCounter(){
        return opentc4$directionLoopingCounter.getAndIncrement();
    }
    @Override
    public int opentc4$decrementAndGetDirectionLoopingCounter(){
        return opentc4$directionLoopingCounter.decrementAndGet();
    }
    @Override
    public int opentc4$getDirectionLoopingCounter(){
        return opentc4$directionLoopingCounter.get();
    }


    @Inject(method = "serverTick",at=@At("TAIL"))
    private static void opentc4$serverTick(Level level, BlockPos blockPos, BlockState blockState, AbstractFurnaceBlockEntity abstractFurnaceBlockEntity, CallbackInfo ci) {
        AbstractFurnaceBlockEntityAccessor accessor = (AbstractFurnaceBlockEntityAccessor) abstractFurnaceBlockEntity;
        if (accessor.opentc4$getAndIncrementLoopingCounter() == 0){
            accessor.opentc4$setTickCount(accessor.opentc4$getTickCount() + 1);
        }
        for (Direction direction : Direction.values()) {
            BlockPos expectedAttachmentPos = blockPos.relative(direction);
            BlockState expectedAttachmentState = level.getBlockState(expectedAttachmentPos);
            if (expectedAttachmentState.getBlock() instanceof IFurnaceAttachmentBlock attachmentBlock) {
                attachmentBlock.onFurnaceBurning(level,abstractFurnaceBlockEntity,blockState,blockPos,expectedAttachmentState,expectedAttachmentPos,ci);
            }
        }
        accessor.opentc4$decrementAndGetDirectionLoopingCounter();
    }
}

