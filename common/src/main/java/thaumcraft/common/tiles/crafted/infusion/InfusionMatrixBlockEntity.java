package thaumcraft.common.tiles.crafted.infusion;

import com.google.common.collect.MapMaker;
import com.linearity.colorannotation.annotation.RGBColor;
import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.annotations.Modifiable;
import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import com.linearity.opentc4.mixinaccessors.clientbe.InfusionMatrixBlockEntityClientAccessor;
import com.linearity.opentc4.simpleutils.ObjectIntPair;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IAspectDisplayBlockEntity;
import thaumcraft.api.aspects.essentiabe.IEssentiaForceInBlockEntity;
import thaumcraft.api.aspects.essentiabe.IRemoteEssentiaDrainerBlockEntity;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.LinkedHashAspectList;
import thaumcraft.api.aspects.aspectlists.UnmodifiableAspectView;
import thaumcraft.api.crafting.infusion.InfusionRecipe;
import thaumcraft.api.listeners.infusion.instabilityevent.InfusionInstabilityEventListener;
import thaumcraft.api.listeners.infusion.instabilityevent.InfusionInstabilityEventManager;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.api.wands.IWandInteractableBlockOrBlockEntity;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.abstracts.IInfusionStabilizerBlock;
import thaumcraft.common.lib.network.fx.PacketFXInfusionSourceS2C;
import thaumcraft.common.lib.resourcelocations.InfusionRecipeResourceLocation;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.abstracts.IInfusionCenterItemStackProvider;
import thaumcraft.common.tiles.abstracts.IInfusionComponentStackProvider;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.linearity.opentc4.Consts.InfusionMatrixBlockEntityTagAccessors.*;
import static thaumcraft.api.crafting.infusion.InfusionRecipe.*;
import static thaumcraft.common.blocks.crafted.infusion.InfusionMatrixBlock.LIT;
import static thaumcraft.common.tiles.crafted.infusion.ArcanePedestalBlockEntity.INFUSION_COMPONENT_PROVIDERS;

public class InfusionMatrixBlockEntity
        extends TileThaumcraft
        implements
        IRemoteEssentiaDrainerBlockEntity,
        IAspectDisplayBlockEntity<Aspect>,
        IWandInteractableBlockOrBlockEntity,
        IEssentiaForceInBlockEntity<Aspect>
{
    //should serialize
    protected final AspectList<Aspect> aspectsRequiring = new LinkedHashAspectList<>();
    protected final List<ItemStack> itemStacksRequiring = new LinkedList<>();
    protected final List<ItemStack> itemStacksReturning = new LinkedList<>();
    protected @NotNull("not empty when crafting") ItemStack infusionCenterStack = ItemStack.EMPTY;
    protected @NotNull("not empty when crafting") ItemStack infusionResult = ItemStack.EMPTY;
    protected @Nullable("only when not crafting") String playerLaunchedCrafting = null;
    protected @Nullable("only when not crafting") InfusionRecipeResourceLocation craftingRecipeID = null;
    protected int instability = 0;
    protected boolean crafting = false;

    @Override
    public void readCustomNBT(CompoundTag tag) {
        super.readCustomNBT(tag);
        REQUIRING_ASPECTS.readFromCompoundTagInto(tag,aspectsRequiring);
        REQUIRING_ITEMS.readFromCompoundTagInto(tag,itemStacksRequiring);
        infusionCenterStack = CENTER_STACK.readFromCompoundTag(tag);
        infusionResult = RESULT.readFromCompoundTag(tag);
        playerLaunchedCrafting = PLAYER_LAUNCHED_CRAFTING.readFromCompoundTag(tag);
        craftingRecipeID = InfusionRecipeResourceLocation.of(INFUSION_RECIPE_LOCATION.readFromCompoundTag(tag));
        instability = INSTABILITY.readIntFromCompoundTag(tag);
        crafting = CRAFTING.readBooleanFromCompoundTag(tag);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        REQUIRING_ASPECTS.writeToCompoundTag(compoundTag,aspectsRequiring);
        REQUIRING_ITEMS.writeToCompoundTag(compoundTag,itemStacksRequiring);
        RETURNING_ITEMS.writeToCompoundTag(compoundTag,itemStacksReturning);
        CENTER_STACK.writeToCompoundTag(compoundTag,infusionCenterStack);
        RESULT.writeToCompoundTag(compoundTag,infusionResult);
        PLAYER_LAUNCHED_CRAFTING.writeToCompoundTag(compoundTag,playerLaunchedCrafting==null?"":playerLaunchedCrafting);
        INFUSION_RECIPE_LOCATION.writeToCompoundTag(compoundTag,craftingRecipeID==null?InfusionRecipeResourceLocation.EMPTY:craftingRecipeID);
        INSTABILITY.writeIntToCompoundTag(compoundTag,instability);
        CRAFTING.writeBooleanToCompoundTag(compoundTag,crafting);
    }

    public static int getTickDelay() {
        return 10;
    }

    //should not serialize
    public final AspectList<Aspect> aspectsRequiringView = new UnmodifiableAspectView<>(aspectsRequiring);
    private int tickCount = System.identityHashCode(this) & 63;

    private @Nullable("only when not crafting") InfusionRecipe craftingRecipe = null;

    public InfusionMatrixBlockEntity(BlockEntityType<? extends InfusionMatrixBlockEntity> type,BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public InfusionMatrixBlockEntity(BlockPos pos, BlockState state) {
        this(ThaumcraftBlockEntities.BlockEntityTypeInstances.INFUSION_MATRIX, pos, state);
    }

    public void serverTick() {
        this.tickCount+=1;
        if (this.crafting) {
            if (this.tickCount % getTickDelay() == 0) {
                craftingCycle();
            }
        }
    }

    protected void craftingCycle() {
        if (this.craftingRecipeID != null && this.craftingRecipe == null) {
            this.craftingRecipe = INFUSION_RECIPES_VIEW.get(this.craftingRecipeID);
        }
        if (this.craftingRecipe == null){
            OpenTC4.LOGGER.warn(new IllegalStateException("craftingRecipeID cannot be find:" + craftingRecipeID));
            clearCraftingInfoAndStop();
            return;
        }
        if (!isCenterStackValid()){
            clearCraftingInfoAndStop();
            return;
        }
        if (checkAndDrainEssentia()){
            return;
        }
        if (checkComponentStacks()){
            return;
        }
        
        finishCrafting(this.craftingRecipe,this.infusionCenterStack);
    }
    
    //true if drained essentia false if can goto next step
    protected boolean checkAndDrainEssentia(){
        if (this.level == null || this.craftingRecipe == null){
            return false;
        }
        while (!this.aspectsRequiring.isEmpty()){
            var aspect = this.aspectsRequiring.getFirstAspect();
            if (aspect.isEmpty()){
                this.aspectsRequiring.remove(aspect);
            }else {
                break;
            }
        }
        if (!this.aspectsRequiring.isEmpty()){
            var aspect = this.aspectsRequiring.getFirstAspect();
            if (aspect.isEmpty()){
                this.aspectsRequiring.remove(aspect);
                return true;
            }
            var drained = drainEssentiaRemote(aspect,getDrainRange());
            if (drained != 0){
                aspectsRequiring.reduceAndRemoveIfNotPositive(aspect,drained);
                markDirtyAndUpdateSelf();
            } else {
                int chanceDenominator = 100 - this.craftingRecipe.getInstabilityExample() * 3;
                if (chanceDenominator > 1){
                    if (this.level.random.nextInt(chanceDenominator) == 0) {
                        ++this.instability;
                    }
                }else {
                    ++this.instability;
                }
                checkInstability();
            }
            return true;
        }
        return false;
    }
    //true if drained component false if can goto next step

    private int drainItemTimes = -1;
    protected boolean checkComponentStacks(){
        if (this.level == null){
            return false;
        }
        if (this.itemStacksRequiring.isEmpty()){
            return false;
        }
        ItemStack consumingStack = this.itemStacksRequiring.getFirst();
        if (consumingStack == null || consumingStack.isEmpty()){
            this.itemStacksRequiring.removeFirst();
            this.itemStacksReturning.removeFirst();
            return true;
        }

        boolean consumed = false;
        for (var offset : getComponentProviderPosOffsets()){
            var realProviderPos = offset.offset(getBlockPos());
            if (this.level.getBlockEntity(realProviderPos) instanceof IInfusionComponentStackProvider provider){
                for (int i=0; i<provider.getContainerSize(); i++){
                    var providingStack = provider.getItem(i);
                    if (ItemStack.isSameItemSameTags(providingStack,consumingStack)){//can consume
                        if (this.drainItemTimes < 0) {
                            this.drainItemTimes = 5;
                            if (this.level instanceof ServerLevel serverLevel){
                                var selfBEPos = getBlockPos();
                                new PacketFXInfusionSourceS2C(
                                        selfBEPos.getX(), selfBEPos.getY(),selfBEPos.getZ(),
                                        (byte) (-offset.getX()), (byte) (-offset.getY()), (byte) (-offset.getZ()),
                                        0
                                ).sendToAllAround(serverLevel,selfBEPos,32*32);
                            }
                            return true;
                        } else if (this.drainItemTimes-- <= 1) {
                            int maxConsumeAmount = Math.min(consumingStack.getCount(),providingStack.getCount());
                            providingStack.split(maxConsumeAmount);
                            consumingStack.split(maxConsumeAmount);
                            provider.setChanged();
                            this.markDirtyAndUpdateSelf();
                            consumed = true;
                            if (providingStack.isEmpty()){
                                provider.setItem(i, ItemStack.EMPTY);
                            }
                            if (consumingStack.isEmpty()){
                                this.itemStacksRequiring.removeFirst();
                                var returning = this.itemStacksReturning.removeFirst();
                                if (!returning.isEmpty()){
                                    var remaining = provider.storeReturningStack(returning);
                                    if (!remaining.isEmpty()){
                                        Containers.dropItemStack(
                                                this.level,
                                                realProviderPos.getX() + 0.5,
                                                realProviderPos.getY() + 0.5,
                                                realProviderPos.getZ() + 0.5,
                                                remaining
                                        );
                                    }
                                }
                                return true;
                            }
                        }
                    }
                }
                if (consumed){
                    return true;
                }
            }
        }
        if (this.level.random.nextInt(1 + this.itemStacksRequiring.size()) == 0) {//i dont want to store index so changed this from "1 + componentOrder" to "1 + (totalComponents - componentOrder)"
            addInstabilityAndRequiredAspect();
        }
        return true;
    }

    protected void checkInstability() {
        if (this.level == null){
            return;
        }
        if (this.instability > 0){
            if (this.instability > 25) {
                this.instability = 25;
            }
            if (this.level.random.nextInt(500) < this.instability) {
                InfusionInstabilityEventManager.pickAndTriggerEvent(
                        new InfusionInstabilityEventListener.InfusionInstabilityEventContext(
                                this,
                                getBlockPos(),
                                this.level,
                                playerLaunchedCrafting)
                );
            }
        }
    }
    private void addInstabilityAndRequiredAspect() {
        if (this.level == null){
            return;
        }
        Aspect aspToAdd = this.aspectsRequiring.randomAspect(this.level.random);
        if (aspToAdd == null){
            return;
        }
        this.aspectsRequiring.addAll(aspToAdd, 1);
        
        int chanceDenominator = 50 - (this.craftingRecipe == null ? 0:this.craftingRecipe.getInstabilityExample()) * 2;
        if (chanceDenominator > 1) {
            if (this.level.random.nextInt(chanceDenominator) == 0) {
                ++this.instability;
            }
        }else {
            ++this.instability;
        }

        checkInstability();
    }
    
    public Collection<BlockPos> getComponentProviderPosOffsets(){
        if (this.level == null){
            return Collections.emptyList();
        }
        
        return componentProviderChecker.checkComponentProviderNearby(_ignored -> {},this.level,getBlockPos()).obj();
    }

    protected boolean isCenterStackValid() {
        return ItemStack.isSameItemSameTags(getInfusionCenterStack(),this.infusionCenterStack);
    }

    public void clientTick() {
        ClientTickContext.tick(this);
    }
    public static class ClientTickContext{
        public Map<ObjectIntPair<BlockPos>,SourceFX> sourceFX = new MapMaker().concurrencyLevel(2).makeMap();
        private int craftCount = 0;
        private float startUp = 0;
        public ClientTickContext(InfusionMatrixBlockEntity be) {

        }
        public static void tick(InfusionMatrixBlockEntity be) {
            var context = ((InfusionMatrixBlockEntityClientAccessor)be).opentc4$getClientTickContext();
            var level = be.getLevel();
            if (!(level instanceof ClientLevel clientLevel)){
                return;
            }
            var pos = be.getBlockPos();
            var centerContainerPos = be.getCenterPedestalPos();
            if (be.crafting) {
                if (context.craftCount == 0) {
                    level.playSound(null,pos, ThaumcraftSounds.INFUSER_START,SoundSource.BLOCKS, 0.5F, 1.0F);
                } else if (context.craftCount % 65 == 0) {
                    level.playSound(null,pos, ThaumcraftSounds.INFUSER,SoundSource.BLOCKS, 0.5F, 1.0F);
                }

                context.craftCount += 1;
                
                ClientFXUtils.blockRunes(
                        clientLevel,
                        centerContainerPos.getX(),
                        centerContainerPos.getY(),
                        centerContainerPos.getZ(),
                        0.5F + clientLevel.random.nextFloat() * 0.2F, 0.1F, 0.7F + clientLevel.random.nextFloat() * 0.3F, 25, -0.03F);
            } else if (context.craftCount > 0) {
                context.craftCount -= 2;
                if (context.craftCount < 0) {
                    context.craftCount = 0;
                }

                if (context.craftCount > 50) {
                    context.craftCount = 50;
                }
            }

            boolean matrixActivated = be.getBlockState().getValue(LIT);
            if (matrixActivated && context.startUp != 1.0F) {
                if (context.startUp < 1.0F) {
                    context.startUp += Math.max(context.startUp / 10.0F, 0.001F);
                }

                if ((double) context.startUp > 0.999) {
                    context.startUp = 1.0F;
                }
            }

            if (!matrixActivated && context.startUp > 0.0F) {

                context.startUp -= context.startUp / 10.0F;


                if ((double) context.startUp < 0.001) {
                    context.startUp = 0.0F;
                }
            }

            Collection<ObjectIntPair<BlockPos>> posToRemove = new LinkedList<>();
            for (var entry : context.sourceFX.entrySet()) {
                SourceFX fx = entry.getValue();
                if (fx.ticks <= 0) {
                    posToRemove.add(entry.getKey());
                    continue;
                } 
                {
                    if (fx.loc.equals(pos)) {
                        Entity player = clientLevel.getEntity(fx.color);//wtf
                        if (player != null) {
                            for (int a = 0; a < ClientFXUtils.particleCount(2); ++a) {
                                ClientFXUtils.drawInfusionParticles4(
                                        clientLevel,
                                        player.getX() + (double) ((clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * player.getBbWidth()),
                                        player.getBoundingBox().minY + (double) (clientLevel.random.nextFloat() * player.getEyeHeight()),
                                        player.getZ() + (double) ((clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * player.getBbWidth()),
                                        pos.getX(),pos.getY(),pos.getZ()
                                );
                            }
                        }
                    } else {
                        if (clientLevel.getBlockEntity(fx.loc) instanceof IInfusionComponentStackProvider provider) {
                            ItemStack is = provider.getItem(0);
                            if (!is.isEmpty()) {
                                if (clientLevel.random.nextInt(3) == 0) {
                                    ClientFXUtils.drawInfusionParticles3(
                                            clientLevel,
                                            (float) fx.loc.getX() + clientLevel.random.nextFloat(),
                                            (float) fx.loc.getY() + clientLevel.random.nextFloat() + 1.0F,
                                            (float) fx.loc.getZ() + clientLevel.random.nextFloat(),
                                            pos.getX(),pos.getY(),pos.getZ());
                                } else {
                                    Item bi = is.getItem();
                                    if (/*is.getItemSpriteNumber() == 0 && */bi instanceof BlockItem blockItem) {
                                        for (int a = 0; a < ClientFXUtils.particleCount(2); ++a) {
                                            ClientFXUtils.drawInfusionParticles2(clientLevel,
                                                    (float) fx.loc.getX() + clientLevel.random.nextFloat(),
                                                    (float) fx.loc.getY() + clientLevel.random.nextFloat() + 1.0F,
                                                    (float) fx.loc.getZ() + clientLevel.random.nextFloat(),
                                                    pos.getX(),pos.getY(),pos.getZ(), blockItem.getBlock()
                                            );
                                        }
                                    } else {
                                        for (int a = 0; a < ClientFXUtils.particleCount(2); ++a) {
                                            ClientFXUtils.drawInfusionParticles1(
                                                    clientLevel,
                                                    (float) fx.loc.getX() + 0.4F + clientLevel.random.nextFloat() * 0.2F,
                                                    (float) fx.loc.getY() + 1.23F + clientLevel.random.nextFloat() * 0.2F,
                                                    (float) fx.loc.getZ() + 0.4F + clientLevel.random.nextFloat() * 0.2F,
                                                    pos.getX(),pos.getY(),pos.getZ(), bi
                                            );
                                        }
                                    }
                                }
                            }
                        } else {
                            fx.ticks = 0;
                        }
                    }
                    --fx.ticks;
                }
            }
            for (var toRemove: posToRemove) {
                context.sourceFX.remove(toRemove);
            }

            if (be.crafting
                    && be.instability > 0
                    && clientLevel.random.nextInt(200) <= be.instability
            ) {
                ClientFXUtils.nodeBolt(
                        clientLevel,
                        (float) pos.getX() + 0.5F,
                        (float) pos.getY() + 0.5F,
                        (float) pos.getZ() + 0.5F,
                        pos.getX() + 0.5F + (clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * 2.0F,
                        pos.getY() + 0.5F + (clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * 2.0F,
                        pos.getZ() + 0.5F + (clientLevel.random.nextFloat() - clientLevel.random.nextFloat()) * 2.0F
                );
            }
        }

        public static class SourceFX {
            public BlockPos loc;
            public int ticks;
            public @RGBColor int color;
            public int entity;
    
            public SourceFX(BlockPos loc, int ticks, int color) {
                this.loc = loc;
                this.ticks = ticks;
                this.color = color;
            }
        }
    }
    public void cancelCrafting(){
        clearCraftingInfoAndStop();
        if (this.level != null){
            this.level.playSound(null,getBlockPos(), ThaumcraftSounds.CRAFT_FAIL, SoundSource.BLOCKS, 1.0F, 0.6F);
        }
    }

    public boolean isCrafting() {
        return crafting;
    }

    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        if (this.level == null || this.level.isClientSide) {
            return InteractionResult.PASS;
        }
        var player = useOnContext.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }
        if (crafting) {
            return InteractionResult.PASS;
        }
        var centerStack = getInfusionCenterStack();
        if (centerStack.isEmpty()) {
            return InteractionResult.PASS;
        }
        var componentStacksAndInstability = componentProviderChecker.getInfusionComponentStacksAndInstability(this.level,getBlockPos());
        var componentStacks = componentStacksAndInstability.obj();
        var matchedRecipe = tryMatchRecipe(centerStack,componentStacks,player);
        if (matchedRecipe == null) {
            return InteractionResult.PASS;
        }


        startCrafting(player.getGameProfile().getName(),matchedRecipe,centerStack,componentStacks,matchedRecipe.getRemainingStacks(componentStacks),componentStacksAndInstability.value());

        return InteractionResult.SUCCESS;
    }

    public void startCrafting(String playerName,InfusionRecipe recipe,ItemStack centerStack,List<ItemStack> componentStacks,List<ItemStack> remainingStacks,int pedestalInstability){
        if (this.level == null){
            return;
        }
        if (componentStacks.size() != remainingStacks.size()){
            throw new IllegalArgumentException("componentStacks and remainingStacks must have same size");
        }
        this.playerLaunchedCrafting = playerName;
        recipe.onInfusionStart(this.level,getBlockPos(),this.playerLaunchedCrafting,centerStack);
        this.craftingRecipeID = recipe.recipeID;
        this.craftingRecipe = INFUSION_RECIPES_VIEW.get(this.craftingRecipeID);
        this.aspectsRequiring.clear();
        this.aspectsRequiring.addAll(recipe.getAspects(centerStack));
        this.infusionCenterStack = centerStack;
        this.infusionResult = recipe.getRecipeOutput(centerStack).copy();
        this.itemStacksRequiring.clear();
        componentStacks.forEach(stack -> this.itemStacksRequiring.add(stack.copy()));
        this.itemStacksReturning.clear();
        remainingStacks.forEach(stack -> this.itemStacksReturning.add(stack.copy()));
        this.crafting = true;
        this.instability = recipe.getInstabilityExample() + pedestalInstability
                -surroundingStabilityProviderChecker.recalculateStabilityProviding(this.level,getBlockPos());

        markDirtyAndUpdateSelf();
    }
    public void finishCrafting(InfusionRecipe recipe,ItemStack infusionCenterStack){
        if (this.level != null){
            if (level.getBlockEntity(getCenterPedestalPos()) instanceof IInfusionCenterItemStackProvider centerItemStackProvider){
                if (!this.infusionResult.isEmpty()){
                    centerItemStackProvider.setCenterStack(this.infusionResult);
                }else {
                    OpenTC4.LOGGER.warn("empty infusionResult found,this might be error or this infusion's onInfusionEnd matters");
                }
                recipe.onInfusionEnd(this.level,getBlockPos(),this.playerLaunchedCrafting,infusionCenterStack);
                clearCraftingInfoAndStop();
            }
        }
    }
    public void clearCraftingInfoAndStop(){
        aspectsRequiring.clear();
        itemStacksRequiring.clear();
        itemStacksReturning.clear();
        infusionCenterStack = ItemStack.EMPTY;
        infusionResult = ItemStack.EMPTY;
        playerLaunchedCrafting = null;
        craftingRecipeID = null;
        craftingRecipe = null;
        instability = 0;
        crafting = false;
    }

    protected @NotNull ItemStack getInfusionCenterStack() {
        if (this.level == null) {
            return ItemStack.EMPTY;
        }
        var pedestalPos = getCenterPedestalPos();
        if (level.getBlockEntity(pedestalPos) instanceof IInfusionCenterItemStackProvider centerProvider){
            return centerProvider.getCenterStack();

        }
        return ItemStack.EMPTY;
    }

    protected @NotNull ComponentProviderChecker componentProviderChecker = new ComponentProviderChecker();
    @UtilityLikeAbstraction(reason = "avoid messy")
    protected static class ComponentProviderChecker {
        protected final Set<BlockPos> infusionComponentStackProviderPositionOffsets = new HashSet<>();

        //int instability
        protected ObjectIntPair<Collection<BlockPos>> checkInstabilityForComponentProvider(Level level,BlockPos matrixPos){
            return checkComponentProviderNearby(_ignored -> {},level,matrixPos);
        }

        //List<ItemStack> component stacks,int instability
        protected @NotNull ObjectIntPair<List<ItemStack>> getInfusionComponentStacksAndInstability(Level level, BlockPos matrixPos){
            var result = new ArrayList<ItemStack>(16);
            int instability = checkComponentProviderNearby(result::add,level,matrixPos).value();
            return new ObjectIntPair<>(result,instability);
        }

        //Collection<BlockPos> got provider pos,int instability
        protected ObjectIntPair<Collection<BlockPos>> checkComponentProviderNearby(Consumer<ItemStack> forStackInContainer, Level level,BlockPos matrixPos) {
            final Set<BlockPos> infusionComponentStackProviderPositionOffsets = new HashSet<>();
            if (level == null) {
                return new ObjectIntPair<>(infusionComponentStackProviderPositionOffsets,0);
            }
            var componentProviderLookup = INFUSION_COMPONENT_PROVIDERS.get(level);
            if (componentProviderLookup == null) {
                return new ObjectIntPair<>(infusionComponentStackProviderPositionOffsets,0);
            }
            AtomicInteger componentProviderInstability = new AtomicInteger();
            componentProviderLookup.forItemsNearPosWithRange(matrixPos,componentProvider -> {
                var posOffset = getComponentProviderOffsetToSelf(componentProvider,matrixPos);
                if (posOffset != null && posOffset.getY() != 0){
                    if (infusionComponentStackProviderPositionOffsets.contains(posOffset)) {
                        return;
                    }
                    infusionComponentStackProviderPositionOffsets.add(posOffset);

                    var posOffsetTransformed = new BlockPos(-posOffset.getX(), posOffset.getY(), -posOffset.getZ());
                    var beInTransformedPos = level.getBlockEntity(posOffsetTransformed.offset(matrixPos));

                    int instabilityChange = 20;

                    if (!componentProvider.isEmpty()){
                        boolean empty = true;
                        for (int i=0;i<componentProvider.getContainerSize();i++){
                            var stackInProvider = componentProvider.getItem(i);
                            if (!stackInProvider.isEmpty()){
                                empty = false;
                            }
                            forStackInContainer.accept(stackInProvider);
                        }
                        if (empty){
                            instabilityChange += 10;
                        }
                    }

                    if (beInTransformedPos instanceof IInfusionComponentStackProvider componentProviderInTransformedPos){
                        infusionComponentStackProviderPositionOffsets.add(posOffsetTransformed);
                        instabilityChange -= 20;
                        boolean empty = true;
                        for (int i=0;i<componentProviderInTransformedPos.getContainerSize();i++){
                            var stackInProvider = componentProviderInTransformedPos.getItem(i);
                            if (!stackInProvider.isEmpty()){
                                empty = false;
                            }
                            forStackInContainer.accept(stackInProvider);
                        }
                        if (empty){
                            instabilityChange -= 10;
                        }
                    }

                    componentProviderInstability.addAndGet(Math.abs(instabilityChange));

                }
            },25);
            return new ObjectIntPair<>(infusionComponentStackProviderPositionOffsets,componentProviderInstability.get());
        }


        protected @Nullable("if not in range") BlockPos getComponentProviderOffsetToSelf(IInfusionComponentStackProvider componentProvider,BlockPos selfPos) {
            var posOffset = componentProvider.getBlockPos().offset(selfPos.multiply(-1));
            var xOffset = posOffset.getX();
            var zOffset = posOffset.getZ();
            if (Math.abs(xOffset) > 12 || Math.abs(zOffset) > 12) {
                return null;
            }
            var yOffset = posOffset.getY();
            if (!(-10 <= yOffset && yOffset <= 5)) {
                return null;
            }
            if (xOffset == 0 && zOffset == 0) {
                return null;
            }
            return posOffset;
        }
    }

    protected @NotNull SurroundingStabilityProviderChecker surroundingStabilityProviderChecker = new SurroundingStabilityProviderChecker();
    @UtilityLikeAbstraction(reason = "avoid messy")
    public static class SurroundingStabilityProviderChecker{

        public static final Object2IntMap<Block> block2StabilityMap = new Object2IntOpenHashMap<>();
        static {
            block2StabilityMap.put(Blocks.SKELETON_SKULL, 1);
            block2StabilityMap.put(Blocks.SKELETON_WALL_SKULL, 1);
            block2StabilityMap.put(Blocks.WITHER_SKELETON_SKULL, 1);
            block2StabilityMap.put(Blocks.WITHER_SKELETON_WALL_SKULL, 1);
            block2StabilityMap.put(Blocks.CREEPER_HEAD, 1);
            block2StabilityMap.put(Blocks.CREEPER_WALL_HEAD, 1);
            block2StabilityMap.put(Blocks.DRAGON_HEAD, 1);
            block2StabilityMap.put(Blocks.DRAGON_WALL_HEAD, 1);
            block2StabilityMap.put(Blocks.PIGLIN_HEAD, 1);
            block2StabilityMap.put(Blocks.PIGLIN_WALL_HEAD, 1);
            block2StabilityMap.put(Blocks.PLAYER_HEAD, 1);
            block2StabilityMap.put(Blocks.PLAYER_WALL_HEAD, 1);
            block2StabilityMap.put(Blocks.ZOMBIE_HEAD, 1);
            block2StabilityMap.put(Blocks.ZOMBIE_WALL_HEAD, 1);
        }
        public static final Object2IntMap<TagKey<Block>> tag2StabilityMap = new Object2IntOpenHashMap<>();

        //mixin before new api?
        protected int getStabilityFromBlock(@NotNull Level level,BlockState bState, BlockPos pos,BlockPos matrixPos){
            var block = bState.getBlock();
            if (block instanceof IInfusionStabilizerBlock stabilizer) {
                return stabilizer.getInfusionStabilizationPower(level, bState, pos, matrixPos);
            }
            int blockTypedStability = block2StabilityMap.getOrDefault(block, 0);
            if (blockTypedStability != 0) {
                return blockTypedStability;
            }
            var maxTag = bState.getTags().max(Comparator.comparingInt(tagA -> tag2StabilityMap.getOrDefault(tagA, 0)));
            if (maxTag.isEmpty()) {
                return 0;
            }
            return tag2StabilityMap.getOrDefault(maxTag, 0);
        }

        public int recalculateStabilityProviding(Level level,BlockPos matrixPos){
            int stabilityProviding = 0;
            for (int yOffset=-10;yOffset<=5;yOffset++){
                for (int xOffset = -12;xOffset <= 12;xOffset++) {
                    for (int zOffset = 0;zOffset <= 12;zOffset++) {
                        if (xOffset == 0 && zOffset == 0) {
                            continue;
                        }
                        var blockPosOffset = new BlockPos(xOffset,yOffset,zOffset);
                        var blockPosOffsetTransformed = new BlockPos(-xOffset,yOffset,-zOffset);
                        int stabilityA = getStabilityFromBlock(level,level.getBlockState(blockPosOffset),matrixPos.offset(blockPosOffset),matrixPos);
                        int stabilityB = getStabilityFromBlock(level,level.getBlockState(blockPosOffsetTransformed),matrixPos.offset(blockPosOffsetTransformed),matrixPos);
                        if (stabilityB != stabilityA) {
                            stabilityProviding -= (stabilityB+stabilityA);
                        }else {
                            stabilityProviding+= (stabilityB+stabilityA);
                        }
                    }
                }
            }
            return stabilityProviding;
        }
    }


    protected BlockPos getCenterPedestalPos() {
        return this.getBlockPos().below(2);
    }

    //TODO:Faster recipe match
    protected @Nullable InfusionRecipe tryMatchRecipe(ItemStack centerStack, List<ItemStack> componentStacks, Player player) {
        if (this.level == null) {
            return null;
        }
        InfusionRecipe chosenRecipe = null;
        for (var infusionRecipe : unmodifiableInfusionRecipes){
            if (infusionRecipe.matches(componentStacks,centerStack,this.level,player,getBlockPos())){
                if (chosenRecipe == null){
                    chosenRecipe = infusionRecipe;
                }else if (chosenRecipe.getAspectsExample().visSize() < infusionRecipe.getAspectsExample().visSize()){
                    chosenRecipe = infusionRecipe;
                }
            }
        }
        return chosenRecipe;
    }

    @Override
    public boolean canDrainEssentiaFromPos(Level level, BlockPos pos, Aspect aspect, @Modifiable Set<IRemoteEssentiaDrainerBlockEntity> metDrainers) {
        if (!aspectsRequiring.containsKey(aspect)){
            return false;
        }
        return IRemoteEssentiaDrainerBlockEntity.super.canDrainEssentiaFromPos(level, pos, aspect, metDrainers);
    }

    @Override
    public int getDrainRange() {
        return 12;
    }

    @Override
    public @NotNull @UnmodifiableView AspectList<Aspect> getAspectsToDisplay() {
        return aspectsRequiringView;
    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return aspectsRequiring.containsKey(aspect);
    }

    @Override
    public int addIntoContainer(Aspect aspect, int amount) {
        int requiringAmount = aspectsRequiring.get(aspect);
        if (requiringAmount > 0) {
            int canDrainAmount = Math.min(amount, requiringAmount);
            aspectsRequiring.reduceAndRemoveIfNotPositive(aspect,canDrainAmount);
            markDirtyAndUpdateSelf();
            return amount-canDrainAmount;
        }
        return amount;
    }
}
