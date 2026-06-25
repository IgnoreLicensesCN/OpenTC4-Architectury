package com.linearity.opentc4.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.entities.abstracts.IBrainGoalsOverrideVillager;

@Mixin(Villager.class)
public class VillagerMixin {
    @WrapMethod(
            method = "registerBrainGoals"
    )
    private void opentc4$registerBrainGoals(Brain<Villager> brain, Operation<Void> original) {
        if (this instanceof IBrainGoalsOverrideVillager villager) {
            villager.registerBrainGoalsRewritten(brain, original);
        }
    }
}
