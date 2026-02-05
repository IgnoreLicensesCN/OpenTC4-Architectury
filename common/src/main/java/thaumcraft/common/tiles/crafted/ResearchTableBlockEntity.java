package thaumcraft.common.tiles.crafted;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.researchtable.IResearchNoteDataOwner;
import thaumcraft.api.researchtable.IResearchTableAspectWriteTool;
import thaumcraft.api.researchtable.IResearchTableWriteAspectListener;
import thaumcraft.api.tile.TileThaumcraftWithMenu;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.gui.menu.ResearchTableMenu;
import thaumcraft.common.items.misc.ItemResearchNotes;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ResearchNoteData;
import thaumcraft.common.lib.utils.HexCoordUtils;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class ResearchTableBlockEntity extends TileThaumcraftWithMenu<ResearchTableMenu, ResearchTableBlockEntity> implements WorldlyContainer {
    public static final int[] SLOTS = new int[]{0, 1};
    public static final int INK_SLOT = 0;
    public static final int RESEARCH_NOTE_SLOT = 1;
    public final NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);


    public boolean isInventoryIndexOutOfBound(int slot) {
        return slot < 0 || slot >= SLOTS.length;
    }

    public void ensureInventoryIndexInBound(int slot) {
        if (isInventoryIndexOutOfBound(slot)) {
            throw new IndexOutOfBoundsException("Index: " + slot);
        }
    }

    public ResearchTableBlockEntity(BlockPos pos, BlockState state) {
        this(ThaumcraftBlockEntities.RESEARCH_TABLE, pos, state, ResearchTableMenu::new);
    }

    public ResearchTableBlockEntity(BlockEntityType<ResearchTableBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState, TileThaumcraftWithMenuFactory<ResearchTableMenu, ResearchTableBlockEntity> menuFactory) {
        super(blockEntityType, blockPos, blockState, menuFactory);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.thaumcraft.research_table");//TODO:Separate a new name
    }

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        ContainerHelper.loadAllItems(compoundTag, inventory);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, inventory);
    }


    @Override
    public int getContainerSize() {
        return SLOTS.length;
    }

    @Override
    public boolean isEmpty() {
        for (var stack : inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        ensureInventoryIndexInBound(slot);
        return inventory.get(slot);
    }

    @Override
    @NotNull
    public ItemStack removeItem(int slot, int amount) {
        ensureInventoryIndexInBound(slot);
        ItemStack stack = getItem(slot);
        if (stack.getCount() <= amount) {
            setItem(slot, ItemStack.EMPTY);
            setChanged();
            return stack;
        } else {
            stack.shrink(amount);
            stack = stack.copy();
            stack.setCount(amount);
            setChanged();
            return stack;
        }
    }

    @Override
    @NotNull
    public ItemStack removeItemNoUpdate(int i) {
        var stack = getItem(i);
        setItem(i, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        inventory.set(i, itemStack);
        setChanged();

        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction direction) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        var item = itemStack.getItem();
        if (item instanceof IResearchTableAspectWriteTool && slot == INK_SLOT) {
            return true;
        }
        return item instanceof IResearchNoteDataOwner && slot == RESEARCH_NOTE_SLOT;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        var item = itemStack.getItem();
        if (item instanceof IResearchTableAspectWriteTool && slot == INK_SLOT) {
            return true;
        }
        return item instanceof IResearchNoteDataOwner && slot == RESEARCH_NOTE_SLOT;
    }

    @Override
    public boolean canTakeItem(Container container, int i, ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return true;
    }

    public @Nullable ResearchNoteData getResearchNoteData() {
        var probablyNoteStack = inventory.get(RESEARCH_NOTE_SLOT);
        if (probablyNoteStack.getItem() instanceof IResearchNoteDataOwner noteDataOwner){
            return noteDataOwner.getResearchNoteData(probablyNoteStack);
        }
        return null;
    }
    public boolean canWriteAspect(HexCoordUtils.HexCoord hexCoord, Aspect aspect, Player player) {
        for (var stack : inventory) {
            if (stack.getItem() instanceof IResearchTableWriteAspectListener writeListener){
                if (writeListener.canWriteAspect(
                        this.getLevel(),
                        this.getBlockPos(),
                        probablyInkStack,
                        probablyNoteStack,
                        player,
                        aspect,
                        hexCoord)){
                    continue;
                }
            }
            return false;
        }
        return true;
    }

    public void placeAspect(HexCoordUtils.HexCoord hexCoord, Aspect aspect, Player player) {
        var canWriteResult = canWriteAspect(hexCoord, aspect, player);
        if (canWriteResult) {
            var writeToolStack = inventory.get(RESEARCH_NOTE_SLOT);
            var dataOwnerStack = inventory.get(RESEARCH_NOTE_SLOT);
            if (!(dataOwnerStack.getItem() instanceof IResearchNoteDataOwner dataOwner)) {
                return;
            }
            var data = dataOwner.getResearchNoteData(dataOwnerStack);
            if (data != null) {
                //TODO:WriteAspect
                for (var stack : inventory) {
                    if (stack.getItem() instanceof IResearchTableWriteAspectListener writeListener){
                        writeListener.beforeWriteAspect(
                                this.getLevel(),
                                this.getBlockPos(),
                                writeToolStack,
                                dataOwnerStack,
                                player,
                                aspect,
                                hexCoord
                        );
                    }
                }
                if (dataOwner.onWriteAspect(
                        this.getLevel(),
                        this.getBlockPos(),
                        writeToolStack,
                        dataOwnerStack,
                        player,
                        aspect,
                        hexCoord
                )){
                    for (var stack : inventory) {
                        if (stack.getItem() instanceof IResearchTableWriteAspectListener writeListener){
                            writeListener.afterWriteAspect(
                                    this.getLevel(),
                                    this.getBlockPos(),
                                    writeToolStack,
                                    dataOwnerStack,
                                    player,
                                    aspect,
                                    hexCoord
                            );
                        }
                    }
                }
            }
            this.markDirtyAndUpdateSelf();
        }

        if (ResearchManager.consumeInkFromTable(this.contents[0], false)) {
            if (this.contents[1] != null && this.contents[1].getItem() instanceof ItemResearchNotes && this.data != null && this.contents[1].getItemDamage() < 64) {
                boolean r1 = ResearchManager.isResearchComplete(player.getCommandSenderName(), "RESEARCHER1");
                boolean r2 = ResearchManager.isResearchComplete(player.getCommandSenderName(), "RESEARCHER2");
                HexCoordUtils.HexCoord hex = new HexCoordUtils.HexCoord(q, r);
                ResearchManager.HexEntry he = null;
                if (aspect != null) {
                    he = new ResearchManager.HexEntry(aspect, 2);
                    if (r2 && this.level().rand.nextFloat() < 0.1F) {
                        this.level()
                                .playSoundAtEntity(
                                        player, "random.orb", 0.2F, 0.9F + player.level().rand.nextFloat() * 0.2F);
                    } else if (Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(
                            player.getCommandSenderName(), aspect) <= 0) {
                        this.bonusAspects.reduceAndRemoveIfNegative(aspect, 1);
                        player.level()
                                .markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                        this.markDirty();
                    } else {
                        Thaumcraft.proxy.playerKnowledge.addAspectPool(
                                player.getCommandSenderName(), aspect, (short) -1);
                        ResearchManager.scheduleSave(player);
                        PacketHandler.INSTANCE.sendTo(
                                new PacketAspectPool(
                                        aspect.getAspectKey(), (short) 0,
                                        Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(
                                                player.getCommandSenderName(),
                                                aspect
                                        )
                                ), (ServerPlayer) player
                        );
                    }
                }
                else {
                    float f = this.level().rand.nextFloat();
                    if (this.data.hexEntries.get(
                            hex.toString()).aspect != null && (r1 && f < 0.25F || r2 && f < 0.5F)) {
                        this.level()
                                .playSoundAtEntity(
                                        player, "random.orb", 0.2F, 0.9F + player.level().rand.nextFloat() * 0.2F);
                        Thaumcraft.proxy.playerKnowledge.addAspectPool(
                                player.getCommandSenderName(), this.data.hexEntries.get(hex.toString()).aspect,
                                (short) 1
                        );
                        ResearchManager.scheduleSave(player);
                        PacketHandler.INSTANCE.sendTo(
                                new PacketAspectPool(
                                        this.data.hexEntries.get(hex.toString()).aspect.getAspectKey(),
                                        (short) 0,
                                        Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(
                                                player.getCommandSenderName(),
                                                this.data.hexEntries.get(hex.toString()).aspect
                                        )
                                ), (ServerPlayer) player
                        );
                    }

                    he = new ResearchManager.HexEntry(null, 0);
                }

                this.data.hexEntries.put(hex.toString(), he);
                this.data.hexes.put(hex.toString(), hex);
                ResearchManager.updateData(this.contents[1], this.data);
                ResearchManager.consumeInkFromTable(this.contents[0], true);
                if (Platform.getEnvironment() == Env.SERVER && ResearchManager.checkResearchCompletion(
                        this.contents[1], this.data, player.getCommandSenderName())) {
                    this.contents[1].setItemDamage(64);
                    this.level()
                            .addBlockEvent(this.xCoord, this.yCoord, this.zCoord, ConfigBlocks.blockTable, 1, 1);
                }
            }

        }
    }
}
