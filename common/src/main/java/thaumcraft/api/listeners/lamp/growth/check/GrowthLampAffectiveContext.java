package thaumcraft.api.listeners.lamp.growth.check;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class GrowthLampAffectiveContext {
    public final Level level;
    public final BlockPos pos;
    public final BlockState state;
    public boolean endCheck = false;
    public boolean recommendAffective = false;

    public GrowthLampAffectiveContext(Level level, BlockPos pos, BlockState state) {
        this.level = level;
        this.pos = pos;
        this.state = state;
    }
}
