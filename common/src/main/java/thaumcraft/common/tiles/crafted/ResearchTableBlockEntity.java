package thaumcraft.common.tiles.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.listeners.researchtable.RemoveAspectContext;
import thaumcraft.api.listeners.researchtable.WriteAspectContext;
import thaumcraft.api.listeners.researchtable.WriteAspectManager;
import thaumcraft.api.researchtable.IResearchNoteDataOwner;
import thaumcraft.api.researchtable.IResearchTableAspectEditTool;
import thaumcraft.api.researchtable.IResearchTableEditAspectListener;
import thaumcraft.api.tile.TileThaumcraftWithMenu;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.menu.menu.ResearchTableMenu;
import thaumcraft.common.lib.research.ResearchNoteData;
import thaumcraft.common.lib.utils.HexCoord;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.Set;

import static com.linearity.opentc4.Consts.ResearchTableBlockEntityTagAccessors.BONUS_ASPECT_ACCESSOR;
import static com.linearity.opentc4.Consts.ResearchTableBlockEntityTagAccessors.TICK_COUNT_ACCESSOR;
import static thaumcraft.api.aspects.Aspects.ALL_ASPECTS;

public class ResearchTableBlockEntity
        extends TileThaumcraftWithMenu<ResearchTableMenu, ResearchTableBlockEntity>
        implements WorldlyContainer {
    public static final Set<Block> CONSIDERED_REDSTONE_COMPONENTS = Set.of(
            Blocks.REDSTONE_WIRE,
            Blocks.REDSTONE_TORCH,
            Blocks.REDSTONE_WALL_TORCH,
            Blocks.REPEATER,
            Blocks.COMPARATOR,
            Blocks.REDSTONE_BLOCK,

            Blocks.LEVER,

            Blocks.STONE_BUTTON,
            Blocks.POLISHED_BLACKSTONE_BUTTON,
            Blocks.OAK_BUTTON,
            Blocks.SPRUCE_BUTTON,
            Blocks.BIRCH_BUTTON,
            Blocks.JUNGLE_BUTTON,
            Blocks.ACACIA_BUTTON,
            Blocks.DARK_OAK_BUTTON,
            Blocks.MANGROVE_BUTTON,
            Blocks.CHERRY_BUTTON,
            Blocks.BAMBOO_BUTTON,
            Blocks.CRIMSON_BUTTON,
            Blocks.WARPED_BUTTON,

            Blocks.STONE_PRESSURE_PLATE,
            Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE,
            Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE,
            Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE,
            Blocks.OAK_PRESSURE_PLATE,
            Blocks.SPRUCE_PRESSURE_PLATE,
            Blocks.BIRCH_PRESSURE_PLATE,
            Blocks.JUNGLE_PRESSURE_PLATE,
            Blocks.ACACIA_PRESSURE_PLATE,
            Blocks.DARK_OAK_PRESSURE_PLATE,
            Blocks.MANGROVE_PRESSURE_PLATE,
            Blocks.CHERRY_PRESSURE_PLATE,
            Blocks.BAMBOO_PRESSURE_PLATE,
            Blocks.CRIMSON_PRESSURE_PLATE,
            Blocks.WARPED_PRESSURE_PLATE,

            Blocks.PISTON,
            Blocks.STICKY_PISTON,
            Blocks.PISTON_HEAD,
            Blocks.MOVING_PISTON,

            Blocks.DISPENSER,
            Blocks.DROPPER,
            Blocks.HOPPER,
            Blocks.OBSERVER,
            Blocks.DAYLIGHT_DETECTOR,
            Blocks.SCULK_SENSOR,
            Blocks.CALIBRATED_SCULK_SENSOR,

            Blocks.TRAPPED_CHEST,
            Blocks.POWERED_RAIL,
            Blocks.DETECTOR_RAIL,
            Blocks.ACTIVATOR_RAIL,

            Blocks.REDSTONE_LAMP,
            Blocks.NOTE_BLOCK,
//            Blocks.TNT,
            Blocks.IRON_DOOR,
            Blocks.IRON_TRAPDOOR
    );

    public static final int[] SLOTS = new int[]{0, 1};
    public static final int INK_SLOT = 0;
    public static final int RESEARCH_NOTE_SLOT = 1;
    public final NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    public final AspectList<Aspect> bonusAspects = new AspectList<>();
    public int tickCounter = 0;
    public static final int BONUS_ASPECT_REGEN_PERIOD = 600;

    public void serverTick() {
        tickCounter++;
        if (tickCounter >= BONUS_ASPECT_REGEN_PERIOD) {
            tickCounter -= BONUS_ASPECT_REGEN_PERIOD;
            gainBonusAspect();
        }
    }

    //TODO:[maybe wont finished]API to gain this.(do we really care about so little aspects?)
    public void gainBonusAspect() {
        var level = getLevel();
        if (level == null) return;
        var pos = getBlockPos();
        var posAbove = pos.above();
        var heightMax = level.getMaxBuildHeight();
        var heightMin = level.getMinBuildHeight();//when heightMax==heightMin,i should be out ofAspectVisList world
        double heightAtPercent = ((double) (pos.getY() - heightMin)) / (heightMax - heightMin);

        if (!level.isDay()
                && level.getBrightness(LightLayer.BLOCK, posAbove) < 4
                && !level.canSeeSky(posAbove)
                && level.random.nextInt(20) == 0
        ) {
            bonusAspects.mergeWithHighest(Aspects.ENTROPY, 1);
        }
        if (heightAtPercent > 0.5
                && level.random.nextInt(20) == 0) {
            bonusAspects.mergeWithHighest(Aspects.AIR, 1);
        }
        if (heightAtPercent > 0.66
                && level.random.nextInt(20) == 0) {
            bonusAspects.mergeWithHighest(Aspects.AIR, 1);
        }
        if (heightAtPercent > 0.75
                && level.random.nextInt(20) == 0) {
            bonusAspects.mergeWithHighest(Aspects.AIR, 1);
        }
        for (int xOffset = -8; xOffset <= 8; xOffset++) {
            for (int yOffset = -8; yOffset <= 8; yOffset++) {
                for (int zOffset = -8; zOffset <= 8; zOffset++) {
                    var pickBlockPos = pos.offset(xOffset, yOffset, zOffset);
                    var pickBlockState = level.getBlockState(pickBlockPos);
                    var pickBlock = pickBlockState.getBlock();
                    if (
                            pickBlock == ThaumcraftBlocks.AIR_INFUSED_STONE
                                    || pickBlock == ThaumcraftBlocks.AIR_CRYSTAL
                    ) {
                        if (level.random.nextInt(20) == 0) {
                            bonusAspects.mergeWithHighest(Aspects.AIR, 1);
                        }
                    }
                    if (
                            pickBlock == ThaumcraftBlocks.FIRE_CRYSTAL
                                    || pickBlock == ThaumcraftBlocks.FIRE_INFUSED_STONE
                                    || pickBlockState.is(BlockTags.FIRE)
                                    || pickBlockState.getFluidState().is(FluidTags.LAVA)
                    ) {
                        if (level.random.nextInt(20) == 0) {
                            bonusAspects.mergeWithHighest(Aspects.FIRE, 1);
                        }
                    }
                    if (
                            pickBlock == ThaumcraftBlocks.EARTH_CRYSTAL
                                    || pickBlock == ThaumcraftBlocks.EARTH_INFUSED_STONE
                                    || pickBlockState.is(BlockTags.DIRT)
                                    || pickBlockState.is(BlockTags.SAND)

                    ) {
                        if (level.random.nextInt(20) == 0) {
                            bonusAspects.mergeWithHighest(Aspects.EARTH, 1);
                        }
                    }

                    if (
                            pickBlock == ThaumcraftBlocks.WATER_CRYSTAL
                                    || pickBlock == ThaumcraftBlocks.WATER_INFUSED_STONE
                                    || pickBlockState.getFluidState().is(FluidTags.WATER)

                    ) {
                        if (level.random.nextInt(20) == 0) {
                            bonusAspects.mergeWithHighest(Aspects.WATER, 1);
                        }
                    }

                    if (
                            pickBlock == ThaumcraftBlocks.ORDER_CRYSTAL
                            || pickBlock == ThaumcraftBlocks.ORDER_INFUSED_STONE
                            || CONSIDERED_REDSTONE_COMPONENTS.contains(pickBlock)
                    ) {
                        if (level.random.nextInt(20) == 0) {
                            bonusAspects.mergeWithHighest(Aspects.WATER, 1);
                        }
                    }

                    if (
                            pickBlock == ThaumcraftBlocks.ENTROPY_CRYSTAL
                                    || pickBlock == ThaumcraftBlocks.ENTROPY_INFUSED_STONE
                    ) {
                        if (level.random.nextInt(20) == 0) {
                            bonusAspects.mergeWithHighest(Aspects.ENTROPY, 1);
                        }
                    }
                    if ((pickBlock == Blocks.BOOKSHELF && level.random.nextInt(300) == 0)
                    || (pickBlockState.is(ThaumcraftBlocks.Tags.JAR_BLOCK) && level.random.nextInt(200) == 0)) {
                        var allAspects = ALL_ASPECTS.values().toArray(new Aspect[0]);
                        bonusAspects.mergeWithHighest(allAspects[level.random.nextInt(allAspects.length)],1);
                    }

                }
            }
        }

    }

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

    public ResearchTableBlockEntity(BlockEntityType<? extends ResearchTableBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState, TileThaumcraftWithMenuFactory<ResearchTableMenu, ResearchTableBlockEntity> menuFactory) {
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
        this.bonusAspects.addAll(BONUS_ASPECT_ACCESSOR.readFromCompoundTag(compoundTag));
        this.tickCounter = TICK_COUNT_ACCESSOR.readFromCompoundTag(compoundTag);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, inventory);
        BONUS_ASPECT_ACCESSOR.writeToCompoundTag(compoundTag, bonusAspects);
        TICK_COUNT_ACCESSOR.writeToCompoundTag(compoundTag, tickCounter);
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
        if (item instanceof IResearchTableAspectEditTool && slot == INK_SLOT) {
            return true;
        }
        return item instanceof IResearchNoteDataOwner && slot == RESEARCH_NOTE_SLOT;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        var item = itemStack.getItem();
        if (item instanceof IResearchTableAspectEditTool && slot == INK_SLOT) {
            return true;
        }
        return item instanceof IResearchNoteDataOwner && slot == RESEARCH_NOTE_SLOT;
    }

    @Override
    public boolean canTakeItem(Container container, int i, ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        if (direction != Direction.DOWN){return slot == INK_SLOT;}
        return slot == RESEARCH_NOTE_SLOT;//then we have hopper in hopper out
    }

    public @Nullable ResearchNoteData getResearchNoteData() {
        var probablyNoteStack = inventory.get(RESEARCH_NOTE_SLOT);
        if (probablyNoteStack.getItem() instanceof IResearchNoteDataOwner noteDataOwner) {
            return noteDataOwner.getResearchNoteData(probablyNoteStack);
        }
        return null;
    }

    public boolean canWriteAspect(HexCoord hexCoord, Aspect aspect, Player player) {
        var probablyInkStack = inventory.get(INK_SLOT);
        var probablyNoteStack = inventory.get(RESEARCH_NOTE_SLOT);
        for (var stack : inventory) {
            if (stack.getItem() instanceof IResearchTableEditAspectListener writeListener) {
                if (writeListener.canWriteAspect(
                        this.getLevel(),
                        this.getBlockPos(),
                        probablyInkStack,
                        probablyNoteStack,
                        player,
                        aspect,
                        hexCoord
                )) {
                    continue;
                }
            }
            return false;
        }
        return true;
    }

    public void placeAspect(HexCoord hexCoord, Aspect aspect, ServerPlayer player) {
        var level = this.getLevel();
        if (level == null) {return;}
        var canWriteResult = canWriteAspect(hexCoord, aspect, player);
        if (canWriteResult) {
            var writeToolStack = inventory.get(INK_SLOT);
            var dataOwnerStack = inventory.get(RESEARCH_NOTE_SLOT);
            if (!(dataOwnerStack.getItem() instanceof IResearchNoteDataOwner dataOwner)) {
                return;
            }
            var data = dataOwner.getResearchNoteData(dataOwnerStack);
            if (data != null) {
                if (!aspect.isEmpty()){
                    var writeContext = new WriteAspectContext(
                            level,
                            this.getBlockPos(),
                            writeToolStack,
                            dataOwnerStack,
                            player,
                            aspect,
                            hexCoord,
                            data
                    );
                    WriteAspectManager.beforeWriteAspect(writeContext);
                    for (var stack : inventory) {
                        if (stack.getItem() instanceof IResearchTableEditAspectListener writeListener) {
                            writeListener.beforeWriteAspect(writeContext);
                        }
                    }
                    if (dataOwner.onWriteAspect(writeContext)) {
                        WriteAspectManager.afterWriteAspect(writeContext);
                        for (var stack : inventory) {
                            if (stack.getItem() instanceof IResearchTableEditAspectListener writeListener) {
                                writeListener.afterWriteAspect(writeContext);
                            }
                        }
                    }
                }else {
                    var removeContext = new RemoveAspectContext(
                            level,
                            this.getBlockPos(),
                            writeToolStack,
                            dataOwnerStack,
                            player,
                            data.hexGrid.get(hexCoord).aspect(),
                            hexCoord,
                            data
                    );
                    WriteAspectManager.beforeRemoveAspect(removeContext);
                    for (var stack : inventory) {
                        if (stack.getItem() instanceof IResearchTableEditAspectListener writeListener) {
                            writeListener.beforeRemoveAspect(removeContext);
                        }
                    }
                    if (dataOwner.onRemoveAspect(removeContext)) {
                        WriteAspectManager.afterRemoveAspect(removeContext);
                        for (var stack : inventory) {
                            if (stack.getItem() instanceof IResearchTableEditAspectListener writeListener) {
                                writeListener.afterRemoveAspect(removeContext);
                            }
                        }
                    }
                }
            }
            this.markDirtyAndUpdateSelf();
        }
    }

    public void copyResearch(ServerPlayer player){
        var researchStack = inventory.get(RESEARCH_NOTE_SLOT);
        if (researchStack.isEmpty()){return;}
        if (researchStack.getItem() instanceof IResearchNoteDataOwner dataOwner) {
            if (dataOwner.canCopyResearchNote(researchStack, player)){
                dataOwner.copyResearchNote(researchStack, player);
            }
        }
    }

}
