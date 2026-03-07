package thaumcraft.common.tiles.crafted;

import com.google.common.collect.MapMaker;
import dev.architectury.fluid.FluidStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.CompoundAspect;
import thaumcraft.api.aspects.PrimalAspect;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.abstracts.ICrucibleAttachmentBlock;
import thaumcraft.common.blocks.liquid.FiniteLiquidBlock;
import thaumcraft.common.blocks.liquid.ThaumcraftFluids;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.awt.*;
import java.util.Map;

import static com.linearity.opentc4.Consts.CrucibleTagAccessors.*;
import static dev.architectury.fluid.FluidStack.create;
import static thaumcraft.api.listeners.aspects.item.bonus.ItemBonusAspectCalculator.getBonusAspects;

public class CrucibleBlockEntity extends TileThaumcraft
        implements
        IValueContainerBasedComparatorSignalProviderBlockEntity
{
    private long counter;
    public static final int ASPECT_CAPACITY = 100;
    public static final int BOILING_HEAT = 150;
    public static final int LIQUID_CAPACITY = 2000;

    public CrucibleBlockEntity(BlockEntityType<? extends CrucibleBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public CrucibleBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.CRUCIBLE, blockPos, blockState);
    }
    protected int tempCalculatedCapacity = getInitialAspectCapacity();
    protected int getInitialAspectCapacity() {
        return ASPECT_CAPACITY;
    }
    public int getAspectCapacity() {
        if (this.counter % 100 == 0){
            tempCalculatedCapacity = getInitialAspectCapacity();
            bellowOnCalculatingCrucibleCapacity();
        }
        return tempCalculatedCapacity;
    }
    public int getBoilingHeat() {
        return BOILING_HEAT;
    }
    public int getLiquidCapacity() {
        return LIQUID_CAPACITY;
    }


    public @NotNull FluidStack getFluidStack() {
        return fluidStack;
    }

    public long getFluidAmount() {
        return fluidStack.getAmount();
    }

    //return inserted
    public long insertFluid(Fluid fluid,long maxCanInsert){
        return insertFluid(fluid,maxCanInsert,true);
    }
    public long insertFluid(Fluid fluid,long maxCanInsert,boolean doIt) {
        if (!canAcceptFluid(fluid)) {
            return 0;
        }
        if (fluid != fluidStack.getFluid() && !fluidStack.isEmpty()) {
            return 0;
        }
        long currentAmount = fluidStack.getAmount();
        if (currentAmount < 0){
            currentAmount = 0;
            fluidStack.setAmount(0);
        }
        long spaceToInsert = getLiquidCapacity() - currentAmount;
        if (spaceToInsert < 0){
            spaceToInsert = 0;
            fluidStack.setAmount(getLiquidCapacity());
        }
        long inserted = Math.min(spaceToInsert, maxCanInsert);
        if (doIt){
            fluidStack.setAmount(currentAmount + inserted);
        }
        return inserted;
    }

    public long extractFluid(Fluid fluid, long maxCanExtract) {
        return 0L;//no out
    }
    protected void decreaseFluid(long amount) {
        this.fluidStack.setAmount(Math.max(0, this.fluidStack.getAmount() - amount));
    }

    public boolean canAcceptFluid(@NotNull Fluid fluid) {
        return fluid.isSame(Fluids.WATER);
    }


    public final AspectList<Aspect> owningAspects = new AspectList<>();
    public int heat = 0;
    protected @NotNull FluidStack fluidStack = create(Fluids.EMPTY, 0);//keep it a instance

    public void setFluidStack(@NotNull FluidStack fluidStack) {
        this.fluidStack = fluidStack;
    }

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        this.owningAspects.addAll(OWNING_ASPECTS.readFromCompoundTag(compoundTag));
        this.heat = HEAT.readFromCompoundTag(compoundTag);
        this.fluidStack = FLUID.readFromCompoundTag(compoundTag);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        OWNING_ASPECTS.writeToCompoundTag(compoundTag, owningAspects);
        HEAT.writeToCompoundTag(compoundTag, heat);
        FLUID.writeToCompoundTag(compoundTag, fluidStack);
    }

    public boolean isHeating() {
        if (level == null){
            return false;
        }
        var blockPosBelow = getBlockPos().below();
        var blockStateBelow = level.getBlockState(blockPosBelow);
//        var blockBelow = blockStateBelow.getBlock();
        if (blockStateBelow.is(ThaumcraftBlocks.Tags.CRUCIBLE_HEATER) 
            || (
                    blockStateBelow.is(ThaumcraftBlocks.Tags.REDSTONE_CONTROLLABLE_CRUCIBLE_HEATER) 
                            && level.hasNeighborSignal(blockPosBelow)
                )
        ){
            return true;
        }
        var fluidState = blockStateBelow.getFluidState();
        if (!fluidState.isEmpty()){
            return fluidState.is(ThaumcraftFluids.Tags.CRUCIBLE_HEATER);
        }
        return false;
    }
    
    public void bellowOnCrucibleHeating(){
        if (level == null){
            return;
        }
        for (Direction dir:Direction.Plane.HORIZONTAL){
            var probablyAttachmentPos = getBlockPos().relative(dir);
            var probablyAttachmentState = level.getBlockState(probablyAttachmentPos);
            if (probablyAttachmentState.getBlock() instanceof ICrucibleAttachmentBlock attachment){
                attachment.onCrucibleHeating(
                        level,
                        probablyAttachmentPos,
                        probablyAttachmentState,
                        this
                );
            }
        }
    }

    public void bellowOnCrucibleNotHeating(){
        if (level == null){
            return;
        }
        for (Direction dir:Direction.Plane.HORIZONTAL){
            var probablyAttachmentPos = getBlockPos().relative(dir);
            var probablyAttachmentState = level.getBlockState(probablyAttachmentPos);
            if (probablyAttachmentState.getBlock() instanceof ICrucibleAttachmentBlock attachment){
                attachment.onCrucibleNotHeating(
                        level,
                        probablyAttachmentPos,
                        probablyAttachmentState,
                        this
                );
            }
        }
    }

    public void bellowOnCalculatingCrucibleCapacity(){
        if (level == null){
            return;
        }
        for (Direction dir:Direction.Plane.HORIZONTAL){
            var probablyAttachmentPos = getBlockPos().relative(dir);
            var probablyAttachmentState = level.getBlockState(probablyAttachmentPos);
            if (probablyAttachmentState.getBlock() instanceof ICrucibleAttachmentBlock attachment){
                attachment.onCalculatingCrucibleCapacity(
                        level,
                        probablyAttachmentPos,
                        probablyAttachmentState,
                        this,
                        tempCalculatedCapacity
                );
            }
        }
    }
    
    public boolean boiled(){
        return this.heat > getBoilingHeat(); 
    }

    public void serverTick(){
//        if (this.bellows < 0) {
//            this.getBellows();
//        }
        var random = level != null ? level.random:RandomSource.createNewThreadLocalInstance();
        this.counter+=1;

        if (this.fluidStack.getAmount() <= 0) {
            if (this.heat > 0) {
                --this.heat;
            }
        } else {
            var heatingFlag = isHeating();
            if (heatingFlag) {
                if (this.heat < 200) {
                    boolean prevBoiled = boiled();
                    bellowOnCrucibleHeating();
                    this.heat += 1;
                    boolean currentBoiled = boiled();
                    if (!prevBoiled && currentBoiled) {
                        markDirtyAndUpdateSelf();
                    }
                }
            } else if (this.heat > 0) {
                boolean prevBoiled = boiled();
                --this.heat;
                bellowOnCrucibleNotHeating();
                boolean currentBoiled = boiled();
                if (prevBoiled && !currentBoiled) {
                    markDirtyAndUpdateSelf();
                }
            }
        }

        if (this.owningAspects.visSize() > getAspectCapacity() && this.counter % 5L == 0L) {
            AspectList<Aspect> tt = this.takeRandomFromSource(random);
            this.spill();
        }

        if (this.counter > 100L && this.heat > getBoilingHeat()) {
            this.counter = 0L;
            if (!this.owningAspects.isEmpty()) {
                Aspect a = this.owningAspects.randomAspect(random);
                if (a instanceof PrimalAspect) {
                    a = this.owningAspects.randomAspect(random);
                }

                this.decreaseFluid(2);
                this.owningAspects.reduceAndRemoveIfNotPositive(a, 1);
                if (a instanceof CompoundAspect compoundAspect) {
                    if (random.nextBoolean()) {
                        this.owningAspects.addAll(compoundAspect.components.aspectA(), 1);
                    } else {
                        this.owningAspects.addAll(compoundAspect.components.aspectB(), 1);
                    }
                } else {
                    this.spill();
                }
                markDirtyAndUpdateSelf();
            }
        }
    }


    public void spill() {
        if (this.level == null){
            return;
        }
        if (this.level.random.nextInt(4) == 0) {
            var posAbove = getBlockPos().above();
            var stateAbove = level.getBlockState(posAbove);
            var blockAbove = stateAbove.getBlock();
            if (stateAbove.isAir()) {
                if (this.level.random.nextBoolean()) {
                    this.level.setBlockAndUpdate(posAbove, ThaumcraftBlocks.FLUX_GAS.defaultBlockState());
                } else {
                    this.level.setBlockAndUpdate(posAbove, ThaumcraftBlocks.FLUX_GOO.defaultBlockState());
                }
            } else {
                if (blockAbove == ThaumcraftBlocks.FLUX_GAS || blockAbove == ThaumcraftBlocks.FLUX_GOO) {
                    //stacking flux
                    int fluidLevel = stateAbove.getValue(FiniteLiquidBlock.LEVEL);
                    if (fluidLevel < FiniteLiquidBlock.MAX_LEVEL){
                        this.level.setBlockAndUpdate(posAbove, stateAbove.setValue(FiniteLiquidBlock.LEVEL, fluidLevel + 1));
                    }
                } else {
                    int xOffset = -1 + this.level.random.nextInt(3);
                    int yOffset = -1 + this.level.random.nextInt(3);
                    int zOffset = -1 + this.level.random.nextInt(3);
                    var pickPos = getBlockPos().offset(xOffset, yOffset, zOffset);
                    var pickState = this.level.getBlockState(pickPos);
                    var pickBlock = pickState.getBlock();
                    if (pickState.isAir()){
                        //TODO:[maybe wont finished]set gas or goo(or more things such as tainted junks if you like) api
                        if (this.level.random.nextBoolean()) {
                            this.level.setBlockAndUpdate(posAbove, ThaumcraftBlocks.FLUX_GAS.defaultBlockState());
                        } else {
                            this.level.setBlockAndUpdate(posAbove, ThaumcraftBlocks.FLUX_GOO.defaultBlockState());
                        }
                    }
                    //i added this part.just think it's obviously reasonable
                    else if (pickBlock == ThaumcraftBlocks.FLUX_GAS || pickBlock == ThaumcraftBlocks.FLUX_GOO) {
                        //also stacking flux
                        int fluidLevel = stateAbove.getValue(FiniteLiquidBlock.LEVEL);
                        if (fluidLevel < FiniteLiquidBlock.MAX_LEVEL){
                            this.level.setBlockAndUpdate(posAbove, stateAbove.setValue(FiniteLiquidBlock.LEVEL, fluidLevel + 1));
                        }
                    }

                }
            }
        }

    }

    public void spillRemnants() {
        if (this.getFluidAmount() > 0 || !this.owningAspects.isEmpty()) {
            this.setFluidStack(FluidStack.create(Fluids.EMPTY, 0));

            for(int a = 0; a < this.owningAspects.visSize() / 2; ++a) {
                this.spill();
            }

            this.owningAspects.clear();
            this.level.blockEvent(getBlockPos(), ThaumcraftBlocks.CRUCIBLE, 2, 5);
        }

    }


    public AspectList<Aspect> takeRandomFromSource(RandomSource random) {
        AspectList<Aspect>output = new AspectList<>();
        if (!this.owningAspects.isEmpty()) {
            Aspect tag = this.owningAspects.randomAspect(random);
            output.addAll(tag, 1);
            this.owningAspects.reduceAndRemoveIfNotPositive(tag, 1);
        }

        markDirtyAndUpdateSelf();
        return output;
    }
    public void entityInside(@NotNull Entity entity) {
        if (level == null || level.isClientSide) {
            return;
        }
        if (entity instanceof ItemEntity itemEntity){
            attemptSmelt(itemEntity);
        } else {
            //magma block
            if (!entity.isSteppingCarefully() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
                entity.hurt(level.damageSources().hotFloor(), 1.0F);
                level.playSound(
                        null,
                        getBlockPos(),
                        SoundEvents.LAVA_EXTINGUISH,
                        SoundSource.BLOCKS,
                        0.4F,
                        2.0F + level.random.nextFloat() * 0.4F
                );
            }
        }
    }

    //TODO:Cache Recipe?
    public void attemptSmelt(ItemEntity itemEntity) {
        if (level == null){
            return;
        }
        boolean bubble = false;
        boolean event = false;
        ItemStack stack = itemEntity.getItem();
        var thrower = itemEntity.getOwner();
        int stacksize = stack.getCount();

        for(int a = 0; a < stacksize; ++a) {
            boolean burnIntoAspect = true;
            if (thrower instanceof Player player){

                int highestRecipeVisSize = Integer.MIN_VALUE;
                CrucibleRecipe recipeChosen = null;

                for (var recipe:CrucibleRecipe.getCrucibleRecipes()){
                    if (recipe.research.isPlayerCompletedResearch(player) && recipe.matches(owningAspects,stack)){
                        if (recipe.aspects.visSize() >= highestRecipeVisSize) {
                            highestRecipeVisSize = recipe.aspects.visSize();
                            recipeChosen = recipe;
                        }
                    }
                }
                if (recipeChosen != null && this.getFluidAmount() > 0) {
                    burnIntoAspect = false;
                    recipeChosen.removeMatching(this.owningAspects);
                    this.decreaseFluid(50);
                    event = true;
                    stacksize -= 1;
                    stack.shrink(1);
                    this.counter = -250L;

//                TODO:Crucible crafting API
//                if (p != null) {
//                    FMLCommonHandler.instance().firePlayerCraftingEvent(p, out, new InventoryFake(new ItemStack[]{stack}));
//                }
                }
            }
            if (burnIntoAspect){
                var burntInto = getBonusAspects(stack,!(level.isClientSide));
                if (burntInto.isEmpty()) {
                    itemEntity.addDeltaMovement(new Vec3(
                            (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F,
                                    .35F,
                            (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F
                    ));
                    this.level.playSound(
                            itemEntity,
                            getBlockPos(),
                            SoundEvents.ITEM_PICKUP,
                            SoundSource.BLOCKS,
                            0.2F,
                            1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.4F);
                    return;
                }
                this.owningAspects.addAll(burntInto);
                bubble = true;
                --stacksize;
                this.counter = -150L;
            }
        }

        if (bubble) {
            this.level.playSound(null,getBlockPos(), ThaumcraftSounds.BUBBLE,SoundSource.BLOCKS, 0.2F, 1.0F + this.level.random.nextFloat() * 0.4F);
            markDirtyAndUpdateSelf();
            this.level.blockEvent(getBlockPos(), ThaumcraftBlocks.CRUCIBLE, 2, 1);
        }

        if (event) {
            markDirtyAndUpdateSelf();
            this.level.blockEvent(getBlockPos(), ThaumcraftBlocks.CRUCIBLE, 2, 5);
        }

        if (stack.isEmpty()) {
            itemEntity.discard();
        } else {
            stack.setCount(stacksize);
            itemEntity.setItem(stack);
        }
        this.setChanged();
    }
    public void ejectItem(ItemStack items) {
        boolean first = true;

        do {
            ItemStack spitout = items.copy();
            if (spitout.getCount() > spitout.getMaxStackSize()) {
                spitout.setCount(spitout.getMaxStackSize());
            }
            items.setCount(items.getCount() - spitout.getCount());

            //TODO:SpecialItemEntity
            EntitySpecialItem entityitem = new EntitySpecialItem(this.level(), (float)this.xCoord + 0.5F, (float)this.yCoord + 0.71F, (float)this.zCoord + 0.5F, spitout);
            entityitem.motionY = 0.1F;
            entityitem.motionX = first ? (double)0.0F : (double)((this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.01F);
            entityitem.motionZ = first ? (double)0.0F : (double)((this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.01F);
            this.level.spawnEntityInWorld(entityitem);
            first = false;
        } while(!items.isEmpty());

    }
    
    public void clientTick(){
        ClientTickContext.tick(this);
    }

    @Override
    public int currentComparatorSignalValue() {
        return Math.min(owningAspects.visSize(),getAspectCapacity());
    }

    @Override
    public int comparatorSignalCapacity() {
        return getAspectCapacity();
    }

    public static class ClientTickContext {
//        int prevcolor;
        int prevx;
        int prevy;
        private static final Map<CrucibleBlockEntity, ClientTickContext> contexts =
                new MapMaker().weakKeys().makeMap();
        public static void tick(CrucibleBlockEntity be){
            if (!(be.level instanceof ClientLevel level)){return;}
            var ctx = contexts.computeIfAbsent(be,c->new ClientTickContext());
            var pos = be.getBlockPos();
            var xCoord = pos.getX();
            var yCoord = pos.getY();
            var zCoord = pos.getZ();
            if (be.heat > be.getBoilingHeat()) {
                ClientFXUtils.crucibleFroth(
                        level,
                        (float)xCoord + 0.2F + level.random.nextFloat() * 0.6F,
                        (float)yCoord + getFluidHeight(be),
                        (float)zCoord + 0.2F + level.random.nextFloat() * 0.6F
                );
                if (be.owningAspects.visSize() > be.getAspectCapacity()) {
                    for(int a = 0; a < 2; ++a) {
                        ClientFXUtils.crucibleFrothDown(level, (float)xCoord, (float)(yCoord + 1), (float)zCoord + level.random.nextFloat());
                        ClientFXUtils.crucibleFrothDown(level, (float)(xCoord + 1), (float)(yCoord + 1), (float)zCoord + level.random.nextFloat());
                        ClientFXUtils.crucibleFrothDown(level, (float)xCoord + level.random.nextFloat(), (float)(yCoord + 1), (float)zCoord);
                        ClientFXUtils.crucibleFrothDown(level, (float)xCoord + level.random.nextFloat(), (float)(yCoord + 1), (float)(zCoord + 1));
                    }
                }
            }

            if (level.random.nextInt(6) == 0 && !be.owningAspects.isEmpty()) {
                int color = be.owningAspects.randomAspect(level.random).getColor() - 16777216;
                int x = 5 + level.random.nextInt(22);
                int y = 5 + level.random.nextInt(22);
//                ctx.delay = level.random.nextInt(10);
//                ctx.prevcolor = color;
                ctx.prevx = x;
                ctx.prevy = y;
                Color c = new Color(color);
                float r = (float)c.getRed() / 255.0F;
                float g = (float)c.getGreen() / 255.0F;
                float b = (float)c.getBlue() / 255.0F;
                ClientFXUtils.crucibleBubble(
                        level,
                        (float)xCoord + (float)x / 32.0F + 0.015625F,
                        (float)yCoord + 0.05F + getFluidHeight(be),
                        (float)zCoord + (float)y / 32.0F + 0.015625F,
                        r, g, b
                );
            }
        }
        public static float getFluidHeight(CrucibleBlockEntity be) {
            float base = 0.3F + 0.5F * ((float) be.fluidStack.getAmount() / (float)be.getLiquidCapacity());
            float out = base + (float)be.owningAspects.visSize() / 100.0F * (1.0F - base);
            if (out > 1.0F) {
                out = 1.001F;
            }

            if (out == 1.0F) {
                out = 0.9999F;
            }

            return out;
        }

    }

}
