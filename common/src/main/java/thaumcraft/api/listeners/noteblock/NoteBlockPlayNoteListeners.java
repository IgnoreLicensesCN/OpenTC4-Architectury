package thaumcraft.api.listeners.noteblock;

import com.linearity.opentc4.mixinaccessors.CoordLookupOwnerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static thaumcraft.common.tiles.crafted.ArcaneEarBlockEntity.COORD_LOOKUP_KEY_FOR_ARCANE_EAR;

public enum NoteBlockPlayNoteListeners {
    ARCANE_EAR_EVENTS(new NoteBlockPlayNoteListener(0) {
        @Override
        public void onNotePlayed(Entity entity, BlockState state, Level level, BlockPos pos) {
            for (var lookup:((CoordLookupOwnerLevel)level).opentc4$getCoordLookupsForType(COORD_LOOKUP_KEY_FOR_ARCANE_EAR)){
                lookup.forItemsNearPos(
                        pos, ear -> ear.onNoteBlockEvent(entity, state, level, pos)
                );
                break;
            }
        }
    })
    ;
    public final NoteBlockPlayNoteListener listener;

    NoteBlockPlayNoteListeners(NoteBlockPlayNoteListener listener) {
        this.listener = listener;
    }
}
