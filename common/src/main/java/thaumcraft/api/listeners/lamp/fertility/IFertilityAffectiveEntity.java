package thaumcraft.api.listeners.lamp.fertility;

import net.minecraft.world.level.Level;
import thaumcraft.api.listeners.lamp.fertility.apply.FertilityLampAffectContext;

public interface IFertilityAffectiveEntity {
    boolean fertilityLampAffect(FertilityLampAffectContext context);
}
