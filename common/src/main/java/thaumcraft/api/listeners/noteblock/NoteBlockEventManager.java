package thaumcraft.api.listeners.noteblock;

import com.linearity.opentc4.simpleutils.ListenerManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class NoteBlockEventManager {
    public static final ListenerManager<NoteBlockPlayNoteListener> listeners = new ListenerManager<>();

    static {
        for (var listenerEnum : NoteBlockPlayNoteListeners.values()) {
            listeners.registerListener(listenerEnum.listener);
        }
    }
    public static void onPlayedNote(Entity entity, BlockState state, Level level, BlockPos pos){
        listeners.getListeners().forEach(listener -> listener.onNotePlayed(entity, state, level, pos));
    }
}
