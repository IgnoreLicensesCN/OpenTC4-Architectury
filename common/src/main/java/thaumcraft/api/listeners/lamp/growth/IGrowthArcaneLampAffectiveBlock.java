package thaumcraft.api.listeners.lamp.growth;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.listeners.lamp.growth.apply.GrowthLampAffectContext;

public interface IGrowthArcaneLampAffectiveBlock {
    void growthLampAffect(GrowthLampAffectContext context);
}
