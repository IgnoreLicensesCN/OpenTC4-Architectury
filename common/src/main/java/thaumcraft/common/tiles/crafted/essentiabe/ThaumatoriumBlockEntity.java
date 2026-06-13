package thaumcraft.common.tiles.crafted.essentiabe;

import com.linearity.opentc4.annotations.Modifiable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.api.aspects.*;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.LinkedHashAspectList;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportInBlockEntity;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportOutBlockEntity;
import thaumcraft.api.crafting.crucible.CrucibleRecipe;
import thaumcraft.common.tiles.TileThaumcraftWithMenu;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.blocks.abstracts.IThaumatoriumAttachmentBlock;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.menu.menu.ThaumatoriumMenu;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.abstracts.IDefaultWorldlyContainer;
import thaumcraft.common.tiles.crafted.CrucibleBlockEntity;

import java.util.ArrayList;
import java.util.List;

import static com.linearity.opentc4.Consts.ThaumatoriumBlockEntityTagAccessors.OWNING_ASPECTS;
import static com.linearity.opentc4.Consts.ThaumatoriumBlockEntityTagAccessors.RECIPES;
import static thaumcraft.common.blocks.crafted.essentia.thaumatorium.ThaumatoriumBottomBlock.FACING;

public class ThaumatoriumBlockEntity extends TileThaumcraftWithMenu<ThaumatoriumMenu,ThaumatoriumBlockEntity> implements
        IDefaultWorldlyContainer,
        IEssentiaTransportInBlockEntity,
        IAspectDisplayBlockEntity<Aspect>,
        IValueContainerBasedComparatorSignalProviderBlockEntity
{
    public static final int DEFAULT_RECIPE_SIZE = 1;
    public ThaumatoriumBlockEntity(BlockEntityType<? extends ThaumatoriumBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState,ThaumatoriumMenu::new);
    }
    public ThaumatoriumBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.THAUMATORIUM(), blockPos, blockState);
    }
    public static final int INPUT_SLOT = 0;
    public static final int[] SLOTS = new int[]{INPUT_SLOT};//output ItemEntity
    public @NotNull NonNullList<ItemStack> inventory = NonNullList.withSize(SLOTS.length,ItemStack.EMPTY);
    public final @Modifiable @NotNull List<CrucibleRecipe> rememberedRecipes = new ArrayList<>();
    public final AspectList<Aspect> aspectsOwning = new LinkedHashAspectList<>();

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        ContainerHelper.loadAllItems(compoundTag, inventory);
        rememberedRecipes.clear();
        rememberedRecipes.addAll(RECIPES.readFromCompoundTag(compoundTag));
        aspectsOwning.clear();
        aspectsOwning.addAll(OWNING_ASPECTS.readFromCompoundTag(compoundTag));
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        ContainerHelper.saveAllItems(compoundTag, inventory);
        RECIPES.writeToCompoundTag(compoundTag, rememberedRecipes);
        OWNING_ASPECTS.writeToCompoundTag(compoundTag, aspectsOwning);
    }

    public int currentCraftingIndexCache = Integer.MIN_VALUE;
    protected AspectList<Aspect> aspectRequiredCache = new LinkedHashAspectList<>();

    //returns >= 0(recipe index)if input matched some recipe
    protected int checkAndCalculateIndexCache() {
        if (this.rememberedRecipes.isEmpty()) {
            this.currentCraftingIndexCache = Integer.MIN_VALUE;
            aspectRequiredCache.clear();
            return this.currentCraftingIndexCache;
        }
        if (this.currentCraftingIndexCache >= rememberedRecipes.size()){
            this.currentCraftingIndexCache = Integer.MIN_VALUE;
        }
        if (this.currentCraftingIndexCache < 0){
            for (int recipeIndex = 0;recipeIndex < rememberedRecipes.size(); recipeIndex++) {
                var recipe = rememberedRecipes.get(recipeIndex);
                if (recipe.catalystMatches(getCatalyst())){
                    this.currentCraftingIndexCache = recipeIndex;
                    recalculateAspectRequirement();
                    return this.currentCraftingIndexCache;
                }
            }
        }
        aspectRequiredCache.clear();
        return this.currentCraftingIndexCache;
    }
    
    public boolean isHeating(){
        if (this.level == null) {
            return false;
        }
        if (level.getBlockEntity(getBlockPos().below()) instanceof CrucibleBlockEntity crucible) {
            return crucible.isHeating();
        }
        return false;
    }

    public int getRecipeSizeLimit(){
        int additionalSize = 0;
        if (this.level != null) {
            final var abovePos = this.getBlockPos().above();
            final var selfPos = this.getBlockPos();
            final var facing = getFacing();
            for (var dir : Direction.values()) {
                if (dir == facing) {
                    continue;
                }
                var probablyAttachmentPos = selfPos.relative(dir);
                var probablyAttachment = this.level.getBlockState(probablyAttachmentPos);
                var probablyAttachmentPos2 = abovePos.relative(dir);
                var probablyAttachment2 = this.level.getBlockState(probablyAttachmentPos2);
                if (probablyAttachment.getBlock() instanceof IThaumatoriumAttachmentBlock attachment) {
                    additionalSize += attachment.getAdditionalRecipeSize(
                            this.level,
                            probablyAttachmentPos,
                            probablyAttachment,
                            selfPos
                    );
                }
                if (probablyAttachment2.getBlock() instanceof IThaumatoriumAttachmentBlock attachment) {
                    additionalSize += attachment.getAdditionalRecipeSize(
                            this.level,
                            probablyAttachmentPos2,
                            probablyAttachment2,
                            selfPos
                    );
                }
            }
        }
        return DEFAULT_RECIPE_SIZE + additionalSize;
    }

    public void cutRecipesToSize(){
        rememberedRecipes.subList(getRecipeSizeLimit(), rememberedRecipes.size()).clear();
    }

    public Direction getFacing() {
        return getBlockState().getValue(FACING);
    }

    protected int tickCount = System.identityHashCode(this) & 63;

    public int getRunningTickDelay(){
        return 5;
    }

    public void serverTick() {
        if (this.level == null) {
            return;
        }
        if (tickCount % 20 == 0) {
            cutRecipesToSize();
        }
        if (tickCount % getRunningTickDelay() == 0 && this.isHeating() && !level.hasNeighborSignal(getBlockPos())) {
            if (this.getCatalyst().isEmpty()){
                //but do not clear owning aspects here
                return;
            }
            int currentRecipeIndex = checkAndCalculateIndexCache();
            if (currentRecipeIndex < 0){
                return;
            }

            var outResult = checkAndCompleteRecipe();
            if (outResult != null) {
                var facing = getFacing();
                if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof Container container){
                    outResult = InventoryUtils.placeItemStackIntoInventory(outResult, container, facing.getOpposite(), true);
                }
                if (!outResult.isEmpty()) {
                    dropOutputAsEntity(outResult);
                }
            }

        }
        tickCount += 1;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        if (direction == null){
            return true;
        }
        for (var recipe:rememberedRecipes) {
            if (recipe.catalystMatches(itemStack)) {
                return true;
            }
        }
        return false;
    }

    public ItemStack getCatalyst(){
        return getItem(INPUT_SLOT);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.thaumcraft.thaumatorium");
    }

    @Override
    public boolean isConnectable(@NotNull Direction face) {
        return face != getFacing();
    }

    @Override
    public boolean canInputFrom(@NotNull Direction face) {
        return isConnectable(face);
    }

    @Override
    public void setSuction(@NotNull Aspect aspect, int amount) {
        
    }

    @Override
    public int getSuctionAmount(@NotNull Direction face) {
        return !getSuctionType(face).isEmpty() ? 128 : 0;
    }

    public void recalculateAspectRequirement(){
        var recipeIndex = checkAndCalculateIndexCache();
        if (recipeIndex < 0){
            aspectRequiredCache.clear();
            return;
        }
        var recipe = rememberedRecipes.get(recipeIndex);
        var aspectsRequired = recipe.getAspectRequirement(getCatalyst());
        for (var aspectTypeRequired:aspectsRequired.keySet()){
            var aspectAmountRequired = aspectsRequired.get(aspectTypeRequired);
            int amountRemaining = aspectAmountRequired - aspectsOwning.get(aspectTypeRequired);
            aspectRequiredCache.put(aspectTypeRequired, amountRemaining);
        }
    }
    @Override
    public @NotNull Aspect getSuctionType(@NotNull Direction face) {
        if (!isConnectable(face)){
            return Aspect.EMPTY;
        }
        var recipeIndex = checkAndCalculateIndexCache();
        if (recipeIndex < 0){
            return Aspect.EMPTY;
        }
        for (var aspect:aspectRequiredCache.keySet()){
            return aspect;
        }
        return Aspects.EMPTY;
    }

    public int addEssentia(@NotNull Aspect aspect, int amount, @NotNull Direction fromDirection) {
        return this.canInputFrom(fromDirection) ? amount - this.addIntoContainer(aspect, amount) : 0;
    }
    //return remaining
    public int addIntoContainer(Aspect couldBeRequired, int amountCanAdd) {
        if (!this.isHeating()){
            return amountCanAdd;
        }
        var requiredAmount = aspectRequiredCache.get(couldBeRequired);
        if (!couldBeRequired.isEmpty() && requiredAmount > 0) {
            int added = Math.min(requiredAmount, amountCanAdd);
            this.aspectsOwning.addAll(couldBeRequired, added);
            markDirtyAndUpdateSelf();
            aspectRequiredCache.reduceAndRemoveIfNotPositive(couldBeRequired,added);
            return amountCanAdd - added;
        }
        return amountCanAdd;
    }

    @Override
    public @NotNull Aspect getEssentiaType(@NotNull Direction face) {
        return Aspects.EMPTY;
    }

    @Override
    public int getEssentiaAmount(@NotNull Direction face) {
        return 0;
    }

    @Override
    public @UnmodifiableView @NotNull AspectList<Aspect> getAspectsToDisplay() {
        return aspectsOwning;
    }
    
    public boolean addRecipe(CrucibleRecipe recipe,Player player){
        if (!recipe.research.isPlayerCompletedResearch(player)){
            return false;
        }
        if (rememberedRecipes.size() >= getRecipeSizeLimit()){
            return false;
        }
        rememberedRecipes.add(recipe);
        return true;
    }

    public boolean removeRecipe(CrucibleRecipe recipe){
        return rememberedRecipes.remove(recipe);
    }

    @Override
    public boolean triggerEvent(int i, int j) {
        if (this.level instanceof ClientLevel clientLevel && i > 0){
            float fx = 0.1F - this.level.random.nextFloat() * 0.2F;
            float fz = 0.1F - this.level.random.nextFloat() * 0.2F;
            float fy = 0.1F - this.level.random.nextFloat() * 0.2F;
            float fx2 = 0.1F - this.level.random.nextFloat() * 0.2F;
            float fz2 = 0.1F - this.level.random.nextFloat() * 0.2F;
            float fy2 = 0.1F - this.level.random.nextFloat() * 0.2F;
            int color = 16777215;
            var facing = getFacing();
            var facingOffsetX = facing.getStepX()/2.;
            var facingOffsetY = facing.getStepY()/2.;
            var facingOffsetZ = facing.getStepZ()/2.;
            var pos = getBlockPos().getCenter().add(facingOffsetX,facingOffsetY,facingOffsetZ);
            ClientFXUtils.drawVentParticles(clientLevel, pos.x + fx , pos.y + 0.5F + fy, pos.z + 0.5F + fz
                    , facingOffsetX/2 + fx2, fy2, facingOffsetZ/2 + fz2, color);
        }

        return super.triggerEvent(i, j);
    }

    public @Nullable ItemStack checkAndCompleteRecipe(){
        if (this.level == null){
            return null;
        }
        var recipeIndex = checkAndCalculateIndexCache();
        if (recipeIndex < 0){
            return null;
        }
        var recipe = rememberedRecipes.get(recipeIndex);
        var inStack = getCatalyst();
        if (!recipe.matches(aspectsOwning,inStack)){
            return null;
        }
        recipe.removeMatching(aspectsOwning,inStack);
        inStack.shrink(1);
        var out = recipe.getRecipeOutput(inStack);
        if (inStack.isEmpty()){
            currentCraftingIndexCache = Integer.MIN_VALUE;
        }
        markDirtyAndUpdateSelf();
        return out;
    }

    public void dropOutputAsEntity(ItemStack out){
        if (this.level == null){
            return;
        }
        var facing = getFacing();
        var selfPos = getBlockPos();
        var itemEntityPos = selfPos.getCenter().add(0.66 * facing.getStepX(), 0.33 * facing.getStepY(), 0.66 * facing.getStepZ());

        ItemEntity itemEntity = new ItemEntity(
                this.level,
                itemEntityPos.x,
                itemEntityPos.y,
                itemEntityPos.z,
                out
        );
        itemEntity.addDeltaMovement(new Vec3(0.075F*facing.getStepX(),0.025,0.075*facing.getStepZ()));
        this.level.addFreshEntity(itemEntity);
        this.level.blockEvent(selfPos,level.getBlockState(selfPos).getBlock(),1,1);
        this.level.playSound(
                null,
                selfPos,
                SoundEvents.LAVA_EXTINGUISH,
                SoundSource.BLOCKS,
                0.25F,
                2.6F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.8F
        );
    }
    public void fill() {
        if (this.level == null){
            return;
        }

        for(int y = 0; y <= 1; ++y) {
            for(Direction dir : Direction.values()) {
                if (dir != getFacing() && dir != Direction.DOWN && (y != 0 || dir != Direction.UP)) {
                    var te = level.getBlockState(getBlockPos().above(y).relative(dir));
                    if (te instanceof IEssentiaTransportOutBlockEntity outBE
                    ) {
                        var iterator = aspectRequiredCache.keySet().iterator();
                        var requirement = iterator.hasNext() ? iterator.next() : Aspects.EMPTY;
                        if (!requirement.isEmpty()) {
                            int ess = outBE.takeEssentiaWithSuction(
                                    this.getSuctionAmount(dir),
                                    requirement,
                                    1,
                                    dir.getOpposite()
                            );
                            if (ess > 0) {
                                this.addIntoContainer(requirement, ess);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public int currentValueForComparatorSignal() {
        var stack = getCatalyst();
        return stack.isEmpty()?0:stack.getCount();
    }

    @Override
    public int comparatorSignalCapacity() {
        return getCatalyst().getMaxStackSize();
    }

    @Override
    public @NotNull NonNullList<ItemStack> getInventory() {
        return inventory;
    }

    @Override
    public int[] getSlots() {
        return SLOTS;
    }
}
