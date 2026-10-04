package com.linearity.opentc4.mixin;

import thaumcraft.common.entities.abstracts.StayableEntity;
import net.minecraft.world.entity.TamableAnimal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TamableAnimal.class)
public abstract class TamableAnimalMixin implements StayableEntity {

    @Shadow
    public abstract boolean isInSittingPose();

    @Shadow
    public abstract void setInSittingPose(boolean bl);
    @Override
    public void thaumcraft$setStay(boolean stay) {
        setInSittingPose(stay);
    }

    @Override
    public boolean thaumcraft$getStay() {
        return isInSittingPose();
    }
}
