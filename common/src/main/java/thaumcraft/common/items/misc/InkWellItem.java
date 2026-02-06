package thaumcraft.common.items.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.expands.listeners.researchtable.RemoveAspectContext;
import thaumcraft.api.expands.listeners.researchtable.WriteAspectContext;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.researchtable.IResearchTableAspectEditTool;
import thaumcraft.api.researchtable.ResearchCreateReason;
import thaumcraft.common.lib.utils.HexCoord;

import java.util.Set;

import static thaumcraft.api.researchtable.ResearchCreateReasons.*;

public class InkWellItem extends Item implements IResearchTableAspectEditTool {
    public static final int INK_WELL_MAX_DURABILITY = 100;
    public InkWellItem(Properties properties) {
        super(properties);
    }
    public InkWellItem() {
        super(new Properties().durability(INK_WELL_MAX_DURABILITY));
    }

    public boolean durabilityEnough(ItemStack stack) {
        var dmgValue = stack.getDamageValue();
        var maxDmg = stack.getMaxDamage();
        return maxDmg - dmgValue > 0;
    }
    @Override
    public boolean canWriteAspect(Level atLevel, BlockPos researchTableBEPos, ItemStack writeToolStack, ItemStack researchNoteStack, Player player, Aspect aspect, HexCoord placedAt) {
        return durabilityEnough(writeToolStack);
    }

    @Override
    public void beforeWriteAspect(WriteAspectContext context) {

    }

    @Override
    public void afterWriteAspect(WriteAspectContext context) {
        if (context.writeToolStack.getItem() == this){
            context.writeToolStack.hurt(1,context.atLevel.random,context.player);
        }
    }

    @Override
    public void beforeRemoveAspect(RemoveAspectContext context) {

    }

    @Override
    public void afterRemoveAspect(RemoveAspectContext context) {
        if (context.writeToolStack.getItem() == this){
            context.writeToolStack.hurt(1,context.atLevel.random,context.player);
        }
    }

    @Override
    public ResearchCreateReason canCreateResearchNote(
            Level atLevel,
            Player player,
            ItemStack writeToolStack,
            ResearchItem researchItem) {
        if (!researchItem.canPlayerCreateResearchNote(player.getGameProfile().getName())){
            return NO_PREREQUISITES;
        }
        if (!durabilityEnough(writeToolStack) || !player.getInventory().hasAnyOf(Set.of(Items.PAPER))){
            return NO_INK_OR_PAPER;
        }
        return CAN_CREATE;
    }

    @Override
    public void createResearchNote(Level atLevel, ServerPlayer player, ItemStack writeToolStack, ResearchItem researchItem) {
        var inv = player.getInventory();
        var paperSlot = inv.findSlotMatchingItem(Items.PAPER.getDefaultInstance());
        writeToolStack.hurt(1,atLevel.random,player);
        inv.getItem(paperSlot).shrink(1);
        if (inv.getItem(paperSlot).isEmpty()) {
            inv.setItem(paperSlot, ItemStack.EMPTY);
        }
        var noteStack = ResearchNoteItem.createResearchNote(atLevel.random,researchItem);
        if (inv.getFreeSlot() >= 0){
            inv.add(noteStack);
        }else {
            player.drop(noteStack, false);
        }
    }
}
