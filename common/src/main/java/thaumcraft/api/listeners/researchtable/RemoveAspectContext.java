package thaumcraft.api.listeners.researchtable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.research.ResearchNoteData;
import thaumcraft.common.lib.utils.HexCoord;

public class RemoveAspectContext {
    public final @NotNull Level atLevel;
    public final @NotNull BlockPos tablePos;
    public final @NotNull ItemStack writeToolStack;
    public final @NotNull ItemStack noteStack;
    public final @NotNull ServerPlayer player;
    public final @NotNull("null->empty") Aspect aspectToRemove;
    public final @NotNull HexCoord coordToRemove;
    public final ResearchNoteData noteData;
    public boolean doReturnAspect = true;


    public RemoveAspectContext(
            @NotNull Level atLevel,
            @NotNull BlockPos tablePos,
            @NotNull ItemStack writeToolStack,
            @NotNull ItemStack noteStack,
            @NotNull ServerPlayer player,
            @NotNull("null->empty") Aspect aspect,
            @NotNull HexCoord placedAt,
            @NotNull ResearchNoteData noteData
    ){
        this.atLevel = atLevel;
        this.tablePos = tablePos;
        this.writeToolStack = writeToolStack;
        this.noteStack = noteStack;
        this.player = player;
        this.aspectToRemove = aspect;
        this.coordToRemove = placedAt;
        this.noteData = noteData;
    }
}
