package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinaccessors.SlimeMoveControlAccessorGetter;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Slime.class)
public class SlimeMixin implements SlimeMoveControlAccessorGetter {


    @Override
    public SlimeMoveControlAccessor opentc4$getSlimeMoveControl() {
        return ((Slime)(Object)this).getMoveControl() instanceof SlimeMoveControlAccessor moveControl
                ? moveControl
                :null;
    }

    @Mixin(targets = "net.minecraft.world.entity.monster.Slime$SlimeMoveControl")
    public interface SlimeMoveControlAccessor {

        @Accessor
        float getYRot();
        @Accessor
        void setYRot(float yRot);
        @Accessor
        int getJumpDelay();
        @Accessor
        void setJumpDelay(int jumpDelay);
        @Accessor
        boolean getIsAggressive();
        @Accessor
        void setIsAggressive(boolean aggressive);
        @Accessor
        Slime getSlime();

        @Invoker("setDirection")
        void opentc4$setDirection(float f, boolean bl);
        @Invoker("setWantedMovement")
        void opentc4$setWantedMovement(double d);
        @Invoker("tick")
        void opentc4$tick();
    }
}
