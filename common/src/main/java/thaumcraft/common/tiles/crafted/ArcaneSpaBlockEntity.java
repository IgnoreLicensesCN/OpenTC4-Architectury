package thaumcraft.common.tiles.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.blocks.liquid.ThaumcraftFluids;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.menu.menu.ArcaneSpaMenu;
import thaumcraft.common.tiles.IThaumcraftBEWithMenu;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.abstracts.IDefaultWorldlyContainer;
import thaumcraft.common.tiles.abstracts.SingleFluidContainerBlockEntity;

import static com.linearity.opentc4.Consts.ArcaneSpaBlockEntityTagAccessors.STORED_ITEM;

//TODO:GUI
public class ArcaneSpaBlockEntity extends SingleFluidContainerBlockEntity
    implements IDefaultWorldlyContainer,
        IThaumcraftBEWithMenu<ArcaneSpaMenu,ArcaneSpaBlockEntity>
{
    public ArcaneSpaBlockEntity(BlockEntityType<? extends ArcaneSpaBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public ArcaneSpaBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ARCANE_SPA, blockPos, blockState);
    }
    protected final NonNullList<ItemStack> items = NonNullList.withSize(1,ItemStack.EMPTY);

    @Override
    public void readCustomNBT(CompoundTag tag) {
        super.readCustomNBT(tag);
        items.set(0,STORED_ITEM.readFromCompoundTag(tag));
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        STORED_ITEM.writeToCompoundTag(compoundTag,items.getFirst());
    }

    @Override
    public NonNullList<ItemStack> getInventory() {
        return items;
    }
    public static final int[] SLOTS = new int[] {0};

    @Override
    public int[] getSlots() {
        return SLOTS;
    }

    public static final int LIQUID_CAPACITY = 5000;

    @Override
    public int getLiquidCapacity() {
        return LIQUID_CAPACITY;
    }
    private int tickCount = 0;
    public boolean shouldPause(){
        if (this.level == null){
            return true;
        }
        return this.level.hasNeighborSignal(getBlockPos());
    }
    public void serverTick(){
        if (this.level == null) return;
        if (this.tickCount++ % 40 == 0
                && !shouldPause()
                && hasEnoughMaterial()
        ) {
            var selfPos = getBlockPos();
            for (int xOffset = -2;xOffset <= 2;xOffset++){
                for (int zOffset = -2;zOffset <= 2;zOffset++){
                    if (canPlaceAtLocation(selfPos.offset(xOffset,1,zOffset))
                    && costEnoughMaterial()){
                        this.level.setBlockAndUpdate(selfPos, ThaumcraftFluids.PURE_FLUID_FLOWING.defaultFluidState().createLegacyBlock());
                    }
                }
            }
        }
    }

    @Override
    public boolean canAcceptFluid(@NotNull Fluid fluid) {
        return fluid.isSame(ThaumcraftFluids.PURE_FLUID_SOURCE) || fluid.isSame(Fluids.WATER);
    }

    public boolean hasEnoughMaterial(){
        if (getFluidAmount() >= getFluidCost()){
            var fluid = getFluidStack().getFluid();
            if (fluid == ThaumcraftFluids.PURE_FLUID_SOURCE) {
                return true;
            }else if (fluid == Fluids.WATER) {
                var item = getItem(0);
                return !item.isEmpty() && item.getItem() == ThaumcraftItems.BATH_SALTS;
            }
        }
        return false;
    }
    public boolean costEnoughMaterial(){
        if (getFluidAmount() >= getFluidCost()){
            var fluid = getFluidStack().getFluid();
            if (fluid == ThaumcraftFluids.PURE_FLUID_SOURCE) {
                decreaseFluid(getFluidCost());
                return true;
            }else if (fluid == Fluids.WATER) {
                var item = getItem(0);
                if( !item.isEmpty() && item.getItem() == ThaumcraftItems.BATH_SALTS){
                    decreaseFluid(getFluidCost());
                    item.shrink(1);
                    return true;
                }
            }
        }
        return false;
    }
    public int getFluidCost(){
        return 1000;
    }

    protected boolean canPlaceAtLocation(BlockPos placeTo){
        if (this.level == null) {
            return false;
        }
        var bstate = this.level.getBlockState(placeTo);
        var fstate = bstate.getFluidState();
        if (bstate.is(Blocks.WATER)) {
            return true;
        }
        else return fstate.is(ThaumcraftFluids.PURE_FLUID_FLOWING) && !fstate.isSource();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.thaumcraft.arcane_spa");
    }

    private final IThaumcraftBEWithMenu.IThaumcraftBEWithMenuFactory<ArcaneSpaMenu,ArcaneSpaBlockEntity> menuFactory
            = ArcaneSpaMenu::new;
    @Override
    public @NotNull IThaumcraftBEWithMenuFactory<ArcaneSpaMenu, ArcaneSpaBlockEntity> getMenuFactory() {
        return menuFactory;
    }
}
