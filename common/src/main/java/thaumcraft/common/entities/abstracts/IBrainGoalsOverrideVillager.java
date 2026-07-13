package thaumcraft.common.entities.abstracts;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;

public interface IBrainGoalsOverrideVillager {
    void registerBrainGoalsRewritten(Brain<? extends Villager> brain, Operation<Void> originalMethod);
}
