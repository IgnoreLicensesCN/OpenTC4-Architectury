package thaumcraft.common.items.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.listeners.researchtable.RemoveAspectContext;
import thaumcraft.api.listeners.researchtable.WriteAspectContext;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.interfaces.IResearchNoteCopyable;
import thaumcraft.api.researchtable.IResearchNoteDataOwner;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.lib.network.playerdata.PacketAspectPoolS2C;
import thaumcraft.common.lib.research.HexEntry;
import thaumcraft.common.lib.research.HexType;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ResearchNoteData;
import thaumcraft.common.lib.utils.HexCoord;

import java.util.function.Predicate;

public class ResearchNoteItem extends Item implements IResearchNoteDataOwner {
    public ResearchNoteItem(Properties props) {
        super(props);
    }
    public ResearchNoteItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public boolean onWriteAspect(WriteAspectContext context) {
        if (context.aspectToWrite.isEmpty()) {return false;}
        var grid = context.noteData.hexGrid;
        var existence = grid.containsKey(context.coordToWrite);
        if (!existence) {return false;}
        var existing = grid.get(context.coordToWrite);
        if (existing.type() == HexType.GIVEN){
            return false;
        }
        if (existing.type() == HexType.WRITTEN && !existing.aspect().isEmpty()){
            return false;
        }
        grid.put(context.coordToWrite,new HexEntry(context.aspectToWrite, HexType.WRITTEN));
        return true;
    }

    @Override
    public boolean onRemoveAspect(RemoveAspectContext context) {
        if (context.aspectToRemove.isEmpty()) {return false;}
        var grid = context.noteData.hexGrid;
        var existence = grid.containsKey(context.coordToRemove);
        if (!existence) {return false;}
        var existing = context.noteData.hexGrid.get(context.coordToRemove);
        if (existing.type() == HexType.GIVEN || existing.type() == HexType.NONE){
            return false;
        }
        if (existing.type() == HexType.WRITTEN){
            if (existing.aspect().isEmpty()){
                return false;
            }
        }

        context.noteData.hexGrid.put(context.coordToRemove,HexEntry.EMPTY);
        if (context.noteData.checkResearchNoteCompletion(context.player)){
            context.noteData.completed = true;
        }
        updateResearchNoteData(context.noteStack,context.noteData);
        return true;
    }

    @Override
    public @Nullable ResearchNoteData getResearchNoteData(ItemStack researchNoteStack) {
        return ResearchNoteData.readFrom(researchNoteStack);
    }

    public static final Predicate<ItemStack> blackDyePredication = stack -> stack.is(Items.BLACK_DYE)
            || stack.is(ThaumcraftItems.ItemTags.BLACK_DYE_FORGE_TAG)
            || stack.is(ThaumcraftItems.ItemTags.BLACK_DYE_FABRIC_TAG);
    public static final Predicate<ItemStack> paperPredication = stack -> stack.is(Items.PAPER);//TODO:[maybe wont finished]Tag it?

    @Override
    public boolean canCopyResearchNote(ItemStack researchNoteStack, ServerPlayer player) {
        var data = getResearchNoteData(researchNoteStack);
        if (data == null){return false;}
        var researchItem = ResearchItem.getResearch(data.key);
        if (researchItem == null){return false;}
        if (!(researchItem instanceof IResearchNoteCopyable copyable)){return false;}
        if (!copyable.canPlayerCopyResearch(player.getGameProfile().getName())){return false;}

        boolean foundDyeBlack = false;
        boolean foundPaper = false;
        for (var i:player.getInventory().items){
            if (blackDyePredication.test(i)){
                foundDyeBlack = true;
            }
            if (paperPredication.test(i)){
                foundPaper = true;
            }
            if (foundDyeBlack && foundPaper){
                return true;
            }
        }
        return false;
    }

    @Override
    public void copyResearchNote(ItemStack researchNoteStack, ServerPlayer player) {
        var researchData = getResearchNoteData(researchNoteStack);
        if (researchData == null){return;}
        var researchItem = ResearchItem.getResearch(researchData.key);
        if (researchItem == null){return;}
        if (!(researchItem instanceof IResearchNoteCopyable copyable)){return;}

        var aspectsToCopy = copyable.getCopyResearchBaseAspects();
        var copiedCount = researchData.copiedCount;
        var playerName = player.getGameProfile().getName();
        var playerOwnedAspects = Thaumcraft.playerKnowledge.getAspectsDiscovered(player);

        //checkToConsume
        var playerInventory = player.getInventory();
        int paperIndex = -1;
        int dyeIndex = -1;
        int currentIndex = 0;
        for (var i:playerInventory.items){

            if (blackDyePredication.test(i)){
                paperIndex = currentIndex;
            }
            if (paperPredication.test(i)){
                dyeIndex = currentIndex;
            }
            if (paperIndex != -1 && dyeIndex != -1){
                break;
            }
            currentIndex += 1;
        }
        if (!(paperIndex != -1 && dyeIndex != -1)){
            return;
        }
        for (var entry:aspectsToCopy.entrySet()){
            var aspect = entry.getKey();
            var count = entry.getValue() + copiedCount;
            if (playerOwnedAspects.getOrDefault(aspect,0) < count){
                return;
            }
        }
        //checked
        //consume
        for (var entry:aspectsToCopy.entrySet()){
            var aspect = entry.getKey();
            var count = entry.getValue() + copiedCount;
            Thaumcraft.playerKnowledge.addAspectPool(player,aspect,-count);
            ResearchManager.scheduleSave(player);
            new PacketAspectPoolS2C(aspect.getAspectKey(), 0, Thaumcraft.playerKnowledge.getAspectPoolFor(player, aspect)).sendTo(player);
        }
        playerInventory.items.get(paperIndex).shrink(1);
        if (playerInventory.items.get(paperIndex).isEmpty()){
            playerInventory.items.set(paperIndex, ItemStack.EMPTY);
        }
        playerInventory.items.get(dyeIndex).shrink(1);
        if (playerInventory.items.get(dyeIndex).isEmpty()){
            playerInventory.items.set(dyeIndex, ItemStack.EMPTY);
        }
        //consumed

        player.level().playSound(player,player.blockPosition(), ThaumcraftSounds.LEARN, SoundSource.BLOCKS, 1.0F, 1.0F);

        researchData.copiedCount += 1;
        researchData.saveTo(researchNoteStack);
        var copied = researchNoteStack.copy();
        copied.setCount(1);
        currentIndex = 0;
        for (var itemStackInSlot : playerInventory.items){
            if (itemStackInSlot.isEmpty()){
                playerInventory.items.set(currentIndex, copied);
                return;
            }
            currentIndex += 1;
        }
        player.drop(copied, false);
    }

    public void updateResearchNoteData(@NotNull ItemStack researchNoteStack,@NotNull ResearchNoteData data) {
        if (researchNoteStack.isEmpty()) return;
        data.saveTo(researchNoteStack);
    }
    @Override
    public boolean canWriteAspect(
            Level atLevel,
            BlockPos researchTableBEPos,
            ItemStack writeToolStack,
            ItemStack researchNoteStack,
            Player player,
            Aspect aspect,
            HexCoord placedAt) {
        var data = getResearchNoteData(researchNoteStack);
        if (data == null) {return false;}
        return !data.completed;
    }

    @Override
    public void beforeWriteAspect(WriteAspectContext context) {

    }

    @Override
    public void afterWriteAspect(WriteAspectContext context) {

    }

    @Override
    public void beforeRemoveAspect(RemoveAspectContext context) {

    }

    @Override
    public void afterRemoveAspect(RemoveAspectContext context) {

    }

}
