package thaumcraft.api.listeners.elementalhoe.check;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ElementalHoeAffectiveContext {
    public final Level level;
    public final BlockPos pos;
    public final BlockState state;
    public boolean endCheck = false;
    public boolean recommendAffective = false;

    public ElementalHoeAffectiveContext(Level level, BlockPos pos, BlockState state) {
        this.level = level;
        this.pos = pos;
        this.state = state;
    }
}
