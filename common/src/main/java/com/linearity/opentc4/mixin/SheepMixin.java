package com.linearity.opentc4.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.entities.abstracts.ITaintConvertableEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINTED_SHEEP;
import static thaumcraft.common.entities.ThaumcraftEntities.usualCanConvertToTaintedMob;
import static thaumcraft.common.entities.ThaumcraftEntities.usualTaintedMobConversion;

@Mixin(Sheep.class)
public class SheepMixin implements ITaintConvertableEntity {

    @Override
    public boolean canConvertToTaintedMob() {
        return usualCanConvertToTaintedMob((LivingEntity)(Object)this);
    }

    @Override
    public void convertToTaintedMob() {
        usualTaintedMobConversion((LivingEntity)(Object)this,TAINTED_SHEEP());
    }
}
