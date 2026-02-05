package thaumcraft.common.gui.slot;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.playerdata.PacketAspectPoolS2C;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.crafted.DeconstructionTableBlockEntity;

import java.util.Optional;

import static thaumcraft.api.aspects.Aspects.ASPECT_RESOURCE_LOCATION_TO_ITEM_MAP;

public class DeconstructionTableResultSlot extends Slot {
    DeconstructionTableBlockEntity deconstructionTable;
    public DeconstructionTableResultSlot(DeconstructionTableBlockEntity deconstructionTableBlockEntity, int index, int xPosition, int yPosition) {
        super(deconstructionTableBlockEntity, index, xPosition, yPosition);
        this.deconstructionTable = deconstructionTableBlockEntity;
    }

    @Override
    protected void onQuickCraft(ItemStack itemStack, int i) {

    }

    @Override
    public void onQuickCraft(ItemStack itemStack, ItemStack itemStack2) {
    }

    @Override
    protected void onSwapCraft(int i) {
    }

    @Override
    protected void checkTakeAchievements(ItemStack itemStack) {
    }

    @Override
    public void onTake(Player player, ItemStack itemStack) {
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean mayPickup(Player player) {
        return false;
    }

    @Override
    public @NotNull Optional<ItemStack> tryRemove(int i, int j, Player player) {
        onClick(player);
        return Optional.empty();
    }

    @Override
    public @NotNull ItemStack safeTake(int i, int j, Player player) {
        return ItemStack.EMPTY;
    }


    @Override
    public @NotNull ItemStack getItem() {
        var aspect = deconstructionTable.storingAspect;
        if (aspect.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return ASPECT_RESOURCE_LOCATION_TO_ITEM_MAP.get(aspect.aspectKey)
                .getDefaultInstance();
    }

    @Override
    public void setByPlayer(ItemStack itemStack) {

    }

    @Override
    public void set(ItemStack itemStack) {
    }


    public void onClick(Player p) {
        var aspect = deconstructionTable.storingAspect;
        if (aspect.isEmpty()) {
            return;
        }
        if (p instanceof ServerPlayer serverPlayer) {
            deconstructionTable.storingAspect = Aspects.EMPTY;
            Thaumcraft.playerKnowledge.addAspectPool(p.getGameProfile().getName(), aspect, (short)1);
            ResearchManager.scheduleSave(p.getGameProfile().getName());
            new PacketAspectPoolS2C(aspect.getAspectKey(),
                    (short) 1, Thaumcraft.playerKnowledge.getAspectPoolFor(p.getGameProfile().getName(), aspect)).sendTo(serverPlayer);
            setChanged();
        }
    }
}
