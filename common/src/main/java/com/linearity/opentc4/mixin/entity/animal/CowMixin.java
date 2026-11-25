package com.linearity.opentc4.mixin.entity.animal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.entities.abstracts.ITaintConvertableEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINTED_COW;
import static thaumcraft.common.entities.ThaumcraftEntities.usualCanConvertToTaintedMob;
import static thaumcraft.common.entities.ThaumcraftEntities.usualTaintedMobConversion;

@Mixin(Cow.class)
public class CowMixin implements ITaintConvertableEntity {

    @Override
    public boolean opentc4$canConvertToTaintedMob() {
        return usualCanConvertToTaintedMob((LivingEntity)(Object)this);
    }

    @Override
    public void opentc4$convertToTaintedMob() {
        usualTaintedMobConversion((LivingEntity)(Object)this,TAINTED_COW());
    }
}
