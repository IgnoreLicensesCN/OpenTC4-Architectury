package thaumcraft.common.tiles.crafted.vis;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.LinkedHashCentiVisList;
import thaumcraft.api.aspects.aspectlists.UnmodifiableCentiVisList;
import thaumcraft.api.visnet.VisNetHandler;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.api.wands.focus.IWandFocusItem;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;
import thaumcraft.common.tiles.IThaumcraftBEWithMenu;
import thaumcraft.common.tiles.TileThaumcraftWithMenu;
import thaumcraft.common.menu.menu.FocalManipulatorMenu;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.abstracts.IDefaultWorldlyContainer;

import static com.linearity.opentc4.Consts.FocalManipulatorBlockEntityTagAccessors.*;

public class FocalManipulatorBlockEntity
        extends TileThaumcraftWithMenu<FocalManipulatorMenu,FocalManipulatorBlockEntity>
        implements IDefaultWorldlyContainer
{
    public FocalManipulatorBlockEntity(
            BlockEntityType<? extends TileThaumcraftWithMenu<FocalManipulatorMenu,
                    FocalManipulatorBlockEntity>> blockEntityType,
            BlockPos blockPos,
            BlockState blockState,
            IThaumcraftBEWithMenu.IThaumcraftBEWithMenuFactory<
                    FocalManipulatorMenu,
                    FocalManipulatorBlockEntity> menuFactory
    ) {
        super(blockEntityType, blockPos, blockState, menuFactory);
    }
    public FocalManipulatorBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.FOCAL_MANIPULATOR, blockPos, blockState, FocalManipulatorMenu::new);
    }

    protected final CentiVisList<Aspect> centiVisRequiring = new LinkedHashCentiVisList<>();
    protected @NotNull FocusUpgradeTypeResourceLocation upgradeToApply = FocusUpgradeTypeResourceLocation.EMPTY;
    protected final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    @Override
    public void readCustomNBT(CompoundTag tag) {
        super.readCustomNBT(tag);
        centiVisRequiring.clear();
        CENTIVIS_REQUIRING.readFromCompoundTagInto(tag, centiVisRequiring);
        upgradeToApply = FocusUpgradeTypeResourceLocation.of(UPGRADE_TP_APPLY.readFromCompoundTag(tag));
        items.set(0,STORED_ITEM.readFromCompoundTag(tag));
    }

    @Override
    public void writeCustomNBT(CompoundTag tag) {
        super.writeCustomNBT(tag);
        CENTIVIS_REQUIRING.writeToCompoundTag(tag, centiVisRequiring);
        UPGRADE_TP_APPLY.writeToCompoundTag(tag, upgradeToApply);
        STORED_ITEM.writeToCompoundTag(tag,items.getFirst());
    }

    public final @UnmodifiableView CentiVisList<Aspect> centiVisListView = UnmodifiableCentiVisList.of(centiVisRequiring);
    public static final int[] SLOTS = {0};


    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.thaumcraft.focal_manipulator");
    }

    private int tickCount = 0;
    public void serverTick(){
        if (this.level == null){
            return;
        }
        tickCount+=1;
        if (tickCount%5==0){
            var stack = getItem(0);
            if (!(stack.getItem() instanceof IWandFocusItem<? extends Aspect> focus)){
                this.centiVisRequiring.clear();
                this.upgradeToApply = FocusUpgradeTypeResourceLocation.EMPTY;
                return;
            }
            if (this.centiVisRequiring.isEmpty()){//finish upgrading
                FocusUpgradeType upgrade = FocusUpgradeType.getType(upgradeToApply);
                if (upgrade != null) {
                    focus.addWandUpgrade(stack, upgrade);
                    this.centiVisRequiring.clear();
                    this.upgradeToApply = FocusUpgradeTypeResourceLocation.EMPTY;
                    this.level.playSound(null,getBlockPos(), ThaumcraftSounds.WAND,SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                markDirtyAndUpdateSelf();
                return;
            }
            //drain centivis
            CentiVisList<Aspect> drained = new LinkedHashCentiVisList<>(this.centiVisRequiring.size(),1);
            this.centiVisRequiring.forEach(
                    (aspectRequiring,amountRequiring) -> {
                        int amountDrained =
                                VisNetHandler.drainCentiVis(
                                        this.level,
                                        getBlockPos(),
                                        aspectRequiring,
                                        Math.min(getMaxCentiVisPerDrain(),amountRequiring)
                                );
                        if (amountDrained > 0){
                            drained.addAll(aspectRequiring,amountDrained);
                        }
                    }
            );
            if (!drained.isEmpty()){
                drained.forEach(this.centiVisRequiring::reduceAndRemoveIfNotPositive);
                markDirtyAndUpdateSelf();
            }
        }
    }

    public int getMaxCentiVisPerDrain(){
        return 100;
    }

    public void startManipulation(FocusUpgradeTypeResourceLocation upgradeToApply, Player player) {
        if (this.level == null) return;
        if (!this.centiVisRequiring.isEmpty()) return;
        var stack = getItem(0);
        if (stack.isEmpty()) return;
        if (!(stack.getItem() instanceof IWandFocusItem<? extends Aspect> focus)) return;
        FocusUpgradeType upgrade = FocusUpgradeType.getType(upgradeToApply);
        if (upgrade == null) return;
        int rank = focus.getRank(stack);
        if (player.experienceLevel < getXPLvlNeededForRank(rank)) {
            return;
        }
        if (!focus.canApplyUpgrade(stack,player,upgrade,rank)){
            return;
        }
        this.centiVisRequiring.addAll(upgrade.getCentiVisRequiring(stack,focus,rank));
        this.upgradeToApply = upgradeToApply;
        player.giveExperienceLevels(-getXPLvlNeededForRank(rank));
        markDirtyAndUpdateSelf();
        this.level.playSound(null,getBlockPos(), ThaumcraftSounds.CRAFT_START, SoundSource.BLOCKS, 0.25F, 1.0F);
    }

    public int getXPLvlNeededForRank(int rank){
        return rank * 8;
    }


    @Override
    public NonNullList<ItemStack> getInventory() {
        return items;
    }

    @Override
    public int[] getSlots() {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
        return itemStack.getItem() instanceof IWandFocusItem;
    }

    @Override
    public boolean canTakeItem(Container container, int i, ItemStack itemStack) {
        return centiVisRequiring.isEmpty();
    }
}
