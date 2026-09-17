package thaumcraft.common.entities.championmod.abstracts.modifier;

import net.minecraft.world.entity.LivingEntity;

public interface IClientTickableChampionModifier {
    void onClientTick(LivingEntity living);
}
