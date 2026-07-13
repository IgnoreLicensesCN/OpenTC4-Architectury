package com.linearity.opentc4.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.entities.abstracts.IBrainGoalsOverrideVillager;
import thaumcraft.common.entities.abstracts.ITaintConvertableEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINTED_VILLAGER;
import static thaumcraft.common.entities.ThaumcraftEntities.usualCanConvertToTaintedMob;
import static thaumcraft.common.entities.ThaumcraftEntities.usualTaintedMobConversion;

@Mixin(Villager.class)
public class VillagerMixin implements ITaintConvertableEntity {
    @WrapMethod(
            method = "registerBrainGoals"
    )
    private void opentc4$registerBrainGoals(Brain<Villager> brain, Operation<Void> original) {
        if (this instanceof IBrainGoalsOverrideVillager villager) {
            villager.registerBrainGoalsRewritten(brain, original);
        }
    }

    @Override
    public boolean canConvertToTaintedMob() {
        return usualCanConvertToTaintedMob((LivingEntity)(Object)this);
    }

    @Override
    public void convertToTaintedMob() {
        usualTaintedMobConversion((LivingEntity)(Object)this,TAINTED_VILLAGER());
    }
}
