package thaumcraft.api.listeners.lamp.growth.apply;

import lombok.RequiredArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@RequiredArgsConstructor
public class GrowthLampAffectContext {
    public final Level level;
    public final BlockPos pos;
    public final BlockState state;
    public boolean endAffect = false;
}
