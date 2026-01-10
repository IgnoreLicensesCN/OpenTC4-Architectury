package thaumcraft.common.tiles.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.concurrent.atomic.AtomicBoolean;

public class HungryChestBlockEntity extends ChestBlockEntity {
    private NonNullList<ItemStack> items;
    private final HungryContainerOpenersCounter openersCounter;
    public AtomicBoolean eating = new AtomicBoolean(false);


    protected HungryChestBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.items = NonNullList.withSize(27, ItemStack.EMPTY);
        this.openersCounter = new HungryContainerOpenersCounter(this);
    }

    public HungryChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.HUNGRY_CHEST,blockPos, blockState);
    }

    static void playSound(Level level, BlockPos blockPos, BlockState blockState, SoundEvent soundEvent) {
        ChestType chestType = blockState.getValue(ChestBlock.TYPE);
        if (chestType != ChestType.LEFT) {
            double d = blockPos.getX() + 0.5;
            double e = blockPos.getY() + 0.5;
            double f = blockPos.getZ() + 0.5;
            if (chestType == ChestType.RIGHT) {
                Direction direction = ChestBlock.getConnectedDirection(blockState);
                d += direction.getStepX() * 0.5;
                f += direction.getStepZ() * 0.5;
            }

            level.playSound(null, d, e, f, soundEvent, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    @Override
    public void startOpen(Player player) {
        var level = this.getLevel();
        if (level == null) return;

        if (!this.remove && (player == null || !player.isSpectator())) {
            this.openersCounter.incrementOpeners(player, level, this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void stopOpen(@Nullable Player player) {
        var level = this.getLevel();
        if (level == null) return;
        if (!this.remove && (player == null || !player.isSpectator())) {
            this.openersCounter.decrementOpeners(player, level, this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.items = nonNullList;
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return ChestMenu.threeRows(i, inventory, this);
    }

    public void recheckOpen() {
        var level = this.getLevel();
        if (level == null){
            return;
        }
        if (!this.remove) {
            this.openersCounter.recheckOpeners(level, this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public int getContainerSize() {
        return super.getContainerSize();
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("block.thaumcraft.hungry_chest");
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(compoundTag)) {
            ContainerHelper.loadAllItems(compoundTag, this.items);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (!this.trySaveLootTable(compoundTag)) {
            ContainerHelper.saveAllItems(compoundTag, this.items);
        }
    }

    public ItemStack addItem(ItemStack stack) {
        if (stack.isEmpty()) return ItemStack.EMPTY;

        NonNullList<ItemStack> items = this.getItems();

        // 1️⃣ 先尝试合并
        for (ItemStack slot : items) {
            if (!slot.isEmpty()
                    && ItemStack.isSameItemSameTags(slot, stack)
                    && slot.getCount() < slot.getMaxStackSize()) {

                int canMove = Math.min(
                        stack.getCount(),
                        slot.getMaxStackSize() - slot.getCount()
                );

                slot.grow(canMove);
                stack.shrink(canMove);

                if (stack.isEmpty()) {
                    this.setChanged();
                    return ItemStack.EMPTY;
                }
            }
        }

        // 2️⃣ 再找空槽
        for (int i = 0; i < items.size(); i++) {
            ItemStack slot = items.get(i);
            if (slot.isEmpty()) {
                items.set(i, stack.copy());
                stack.setCount(0);
                this.setChanged();
                return ItemStack.EMPTY;
            }
        }

        // 3️⃣ 放不下，返回剩余
        return stack;
    }

    protected int eatingCooldownCounter = 0;
    public boolean eatingCooldown(){
        if (eatingCooldownCounter > 0){
            eatingCooldownCounter -= 1;
        }
        return eatingCooldownCounter == 0;
    }
    public void addEatingCooldownForEating() {
        eatingCooldownCounter += 5;
    }


    public static class HungryContainerOpenersCounter {
        private static final int CHECK_TICK_DELAY = 5;
        private int openCount;

        public void incrementOpeners(Player player, Level level, BlockPos blockPos, BlockState blockState) {
            int i = this.openCount++;
            if (i == 0) {
                this.onOpen(level, blockPos, blockState);
                level.gameEvent(player, GameEvent.CONTAINER_OPEN, blockPos);
                scheduleRecheck(level, blockPos, blockState);
            }

            this.openerCountChanged(level, blockPos, blockState, i, this.openCount);
        }

        public void decrementOpeners(Player player, Level level, BlockPos blockPos, BlockState blockState) {
            int i = this.openCount--;
            if (this.openCount == 0) {
                this.onClose(level, blockPos, blockState);
                level.gameEvent(player, GameEvent.CONTAINER_CLOSE, blockPos);
            }

            this.openerCountChanged(level, blockPos, blockState, i, this.openCount);
        }

        private int getOpenCount(Level level, BlockPos blockPos) {
            int i = blockPos.getX();
            int j = blockPos.getY();
            int k = blockPos.getZ();
            AABB aABB = new AABB(i - 5.0F, j - 5.0F, k - 5.0F, i + 1 + 5.0F, j + 1 + 5.0F, k + 1 + 5.0F);
            return level.getEntities(EntityTypeTest.forClass(Player.class), aABB, this::isOwnContainer).size();
        }

        public void recheckOpeners(Level level, BlockPos blockPos, BlockState blockState) {
            int i = this.getOpenCount(level, blockPos);
            int j = this.openCount;
            if (j != i) {
                boolean bl = i != 0;
                boolean bl2 = j != 0;
                if (bl && !bl2) {
                    this.onOpen(level, blockPos, blockState);
                    level.gameEvent(null, GameEvent.CONTAINER_OPEN, blockPos);
                } else if (!bl) {
                    this.onClose(level, blockPos, blockState);
                    level.gameEvent(null, GameEvent.CONTAINER_CLOSE, blockPos);
                }

                this.openCount = i;
            }

            this.openerCountChanged(level, blockPos, blockState, j, i);
            if (i > 0) {
                scheduleRecheck(level, blockPos, blockState);
            }
        }

        public int getOpenerCount() {
            return this.openCount;
        }

        private static void scheduleRecheck(Level level, BlockPos blockPos, BlockState blockState) {
            level.scheduleTick(blockPos, blockState.getBlock(), CHECK_TICK_DELAY);
        }
        private final HungryChestBlockEntity chest;
        public HungryContainerOpenersCounter(HungryChestBlockEntity chest) {
            super();
            this.chest = chest;
        }
        protected void onOpen(Level level, BlockPos blockPos, BlockState blockState) {
            if (chest.eating.getAndSet(false)){
                playSound(level, blockPos, blockState, SoundEvents.GENERIC_EAT);
            }else {
                playSound(level, blockPos, blockState, SoundEvents.CHEST_OPEN);
            }
        }

        protected void onClose(Level level, BlockPos blockPos, BlockState blockState) {
            if (chest.eating.getAndSet(false)){
                playSound(level, blockPos, blockState, SoundEvents.GENERIC_EAT);
            }else {
                playSound(level, blockPos, blockState, SoundEvents.CHEST_CLOSE);
            }
        }

        protected void openerCountChanged(Level level, BlockPos blockPos, BlockState blockState, int i, int j) {
            signalOpenCount(level, blockPos, blockState, i, j);
        }

        protected boolean isOwnContainer(@Nullable Player player) {
            if (player == null) {return false;}
            if (!(player.containerMenu instanceof ChestMenu)) {
                return false;
            } else {
                Container container = ((ChestMenu)player.containerMenu).getContainer();
                return container == chest
                        || (container instanceof CompoundContainer compoundContainer && compoundContainer.contains(chest));
            }
        }
        protected void signalOpenCount(Level level, BlockPos blockPos, BlockState blockState, int i, int j) {
            Block block = blockState.getBlock();
            level.blockEvent(blockPos, block, 1, j);
        }
    }



}
