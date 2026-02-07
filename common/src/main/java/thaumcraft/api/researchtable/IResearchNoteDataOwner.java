package thaumcraft.api.researchtable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.expands.listeners.researchtable.RemoveAspectContext;
import thaumcraft.api.expands.listeners.researchtable.WriteAspectContext;
import thaumcraft.common.lib.research.ResearchNoteData;

//for research note and maybe you can do something else?
public interface IResearchNoteDataOwner extends IResearchTableEditAspectListener {
    default boolean canPlaceIntoResearchTable(
            Level atLevel,
            BlockPos tablePos,
            ItemStack noteStack){
        return true;
    }
    boolean onWriteAspect(WriteAspectContext context);
    boolean onRemoveAspect(RemoveAspectContext context);
    @Nullable ResearchNoteData getResearchNoteData(ItemStack researchNoteStack);
    boolean canCopyResearchNote(ItemStack researchNoteStack, ServerPlayer player);
    void copyResearchNote(ItemStack researchNoteStack, ServerPlayer player);
}
