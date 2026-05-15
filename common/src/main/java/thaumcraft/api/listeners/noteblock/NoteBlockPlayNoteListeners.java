package thaumcraft.api.listeners.noteblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static thaumcraft.common.tiles.crafted.ArcaneEarBlockEntity.earsForTriggerEvent;

public enum NoteBlockPlayNoteListeners {
    ARCANE_EAR_EVENTS(new NoteBlockPlayNoteListener(0) {
        @Override
        public void onNotePlayed(Entity entity, BlockState state, Level level, BlockPos pos) {
            var lookup = earsForTriggerEvent.get(level);
            if (lookup != null) {
                lookup.forItemsNearPos(
                        pos, ear -> ear.onNoteBlockEvent(entity, state, level, pos)
                );
            }
        }
    })
    ;
    public final NoteBlockPlayNoteListener listener;

    NoteBlockPlayNoteListeners(NoteBlockPlayNoteListener listener) {
        this.listener = listener;
    }
}
