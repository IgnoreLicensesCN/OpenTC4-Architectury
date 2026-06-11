package thaumcraft.api.listeners.elementalhoe.apply;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ElementalHoeAffectContext {
    public final Level level;
    public final BlockPos pos;
    public final BlockState state;
    public boolean endAffect = false;

    public ElementalHoeAffectContext(Level level, BlockPos pos, BlockState state) {
        this.level = level;
        this.pos = pos;
        this.state = state;
    }
}
