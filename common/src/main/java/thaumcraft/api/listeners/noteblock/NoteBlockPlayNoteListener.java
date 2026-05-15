package thaumcraft.api.listeners.noteblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class NoteBlockPlayNoteListener implements Comparable<NoteBlockPlayNoteListener> {
    public final int weight;
    public NoteBlockPlayNoteListener(int weight) {
        this.weight = weight;
    }
    public abstract void onNotePlayed(Entity entity, BlockState state, Level level, BlockPos pos);

    @Override
    public int compareTo(@NotNull NoteBlockPlayNoteListener o) {
        return Integer.compare(this.weight, o.weight);
    }
}
