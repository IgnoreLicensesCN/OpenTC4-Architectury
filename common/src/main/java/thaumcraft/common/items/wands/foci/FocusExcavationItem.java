package thaumcraft.common.items.wands.foci;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.mixinaccessors.DropExperienceBlockAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.*;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.LinkedHashAspectList;
import thaumcraft.api.aspects.aspectlists.LinkedTreeCentiVisList;
import thaumcraft.api.aspects.aspectlists.UnmodifiableAspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.IWandFocusItem;
import thaumcraft.api.wands.ICentiVisContainerItem;
import thaumcraft.api.wands.IWandFocusEngineItem;
import thaumcraft.client.fx.migrated.beams.FXBeamWand;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.lib.resourcelocations.FocusUpgradeTypeResourceLocation;
import thaumcraft.common.lib.utils.BlockUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class FocusExcavationItem extends FocusBasicItem {
    public static final CentiVisList<Aspect> wandCost = LinkedTreeCentiVisList.of(Aspects.EARTH, 15);

    public static final CentiVisList<Aspect> wandCostWithSilkTouchOrDowsing;
    static {
        LinkedHashAspectList<Aspect> wandCostWithSilkTouchOrDowsingInternal;
        wandCostWithSilkTouchOrDowsingInternal = new LinkedHashAspectList<>(6 + wandCost.size(),1);
        wandCostWithSilkTouchOrDowsingInternal.addAll(Aspects.AIR, 1);
        wandCostWithSilkTouchOrDowsingInternal .addAll(Aspects.FIRE, 1);
        wandCostWithSilkTouchOrDowsingInternal .addAll(Aspects.EARTH, 1);
        wandCostWithSilkTouchOrDowsingInternal .addAll(Aspects.WATER, 1);
        wandCostWithSilkTouchOrDowsingInternal .addAll(Aspects.ORDER, 1);
        wandCostWithSilkTouchOrDowsingInternal .addAll(Aspects.ENTROPY, 1);
        wandCostWithSilkTouchOrDowsingInternal .addAll(wandCost);
        wandCostWithSilkTouchOrDowsing = CentiVisList.fromAspectVisList(
                wandCostWithSilkTouchOrDowsingInternal
        );
    }
    public static final FocusUpgradeType dowsing = new FocusUpgradeType(
            FocusUpgradeTypeResourceLocation.of(Thaumcraft.MOD_ID,"dowsing"),
            new ResourceLocation("thaumcraft", "textures/foci/dowsing.png"),
            "focus.upgrade.dowsing.name",
            "focus.upgrade.dowsing.text",
            UnmodifiableAspectList.of(Aspects.MINE, 1)
    );

    public FocusExcavationItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public boolean canApplyUpgrade(ItemStack focusstack, Player player, FocusUpgradeType type, int rank) {
        return super.canApplyUpgrade(focusstack, player, type, rank);
    }

    @Override
    public CentiVisList<Aspect> getCentiVisCost(ItemStack focusstack, @Nullable ItemStack wandStack) {
        if (!(focusstack.getItem() instanceof IWandFocusItem<? extends Aspect> wandFocusItem)) {
            return wandCost;
        }
        var upgrades = wandFocusItem.getWandUpgradesWithWandModifiers(focusstack,wandStack);
        if (upgrades.getOrDefault(FocusUpgradeType.silktouch,0) > 0
                || upgrades.getOrDefault(dowsing,0) > 0
        ) {
            return wandCostWithSilkTouchOrDowsing;
        }
        return wandCost;
    }

    @Override
    public int getActivationCooldown(ItemStack focusstack) {
        return 0;
    }

    @Override
    public int getMaxAreaSize(ItemStack focusstack) {
        return 0;
    }

    public static final List<FocusUpgradeType> RANK_0_UPGRADES =
            List.of(FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgradeType.treasure);

    public static final List<FocusUpgradeType> RANK_1_UPGRADES =
            List.of(FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgradeType.enlarge);

    public static final List<FocusUpgradeType> RANK_2_UPGRADES =
            List.of(FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgradeType.treasure, dowsing);

    public static final List<FocusUpgradeType> RANK_3_UPGRADES =
            List.of(FocusUpgradeType.frugal, FocusUpgradeType.frugal, FocusUpgradeType.enlarge);

    public static final List<FocusUpgradeType> RANK_4_UPGRADES =
            List.of(FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgradeType.treasure, FocusUpgradeType.silktouch);



    @Override
    public List<FocusUpgradeType> getPossibleUpgradesByRank(ItemStack focusstack) {
        if (focusstack == null) {return List.of();}
        var item = focusstack.getItem();
        if (item instanceof IWandFocusItem<? extends Aspect> focusItem) {
            int rank = focusItem.getAppliedWandUpgrades(focusstack)
                    .values()
                    .stream()
                    .mapToInt(Integer::intValue)
                    .sum();

            if (rank == 0) return RANK_0_UPGRADES;
            if (rank == 1) return RANK_1_UPGRADES;
            if (rank == 2) return RANK_2_UPGRADES;
            if (rank == 3) return RANK_3_UPGRADES;
            if (rank >= 4) return RANK_4_UPGRADES;
        }
        return List.of();
    }

    @Override
    public String getSortingHelper(ItemStack focusstack) {
        return "BE" + super.getSortingHelper(focusstack);
    }

    @Override
    public boolean isCentiVisCostPerTick() {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> onFocusRightClick(ItemStack wandstack, ItemStack focusStack, Level world, Player player, InteractionHand interactionHand) {
        player.startUsingItem(interactionHand);
        return new InteractionResultHolder<>(InteractionResult.CONSUME, wandstack);
    }

    private static final Map<Entity, BlockPos> lastUsingAtCoords = new MapMaker().weakKeys().concurrencyLevel(2).makeMap();
    private static final Map<Entity, Long> soundDelay = new MapMaker().weakKeys().concurrencyLevel(2).makeMap();
    private static final Map<Entity, FXBeamWand> beam = new MapMaker().weakKeys().concurrencyLevel(2).makeMap();
    private static final Map<Entity, Float> breakCount = new MapMaker().weakKeys().concurrencyLevel(2).makeMap();
    private static final BlockPos ZERO = new BlockPos(0, 0, 0);
    private static final BlockPos MAX = new BlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

    @Override
    @SuppressWarnings("unchecked")
    public void onUsingFocusTick(ItemStack usingWand, ItemStack focusStack, LivingEntity user, int count) {
        var wandItem = usingWand.getItem();
        var focusItem = focusStack.getItem();
        if (!((wandItem instanceof ICentiVisContainerItem<? extends Aspect> containerNotCasted) && (focusItem instanceof IWandFocusItem<? extends Aspect> wandFocusItem))
        ){
            user.stopUsingItem();
            return;
        }
        var container = (ICentiVisContainerItem<Aspect>) containerNotCasted;
        var level = user.level();
        var serverSideFlag = !level.isClientSide();
        if (!(container.consumeAllCentiVis(
                usingWand
                ,user, getCentiVisCost(focusStack,usingWand),false,false,serverSideFlag))
        ){
            user.stopUsingItem();
            return;
        }
        {

            soundDelay.putIfAbsent(user, 0L);
            breakCount.putIfAbsent(user, 0.0F);
            lastUsingAtCoords.putIfAbsent(user, ZERO);

            HitResult mop = BlockUtils.getTargetBlock(level, user, false);
            Vec3 v = user.getLookAngle();
            double tx = user.getX() + v.x() * (double)10.0F;
            double ty = user.getY() + v.y() * (double)10.0F;
            double tz = user.getZ() + v.z() * (double)10.0F;
            int impact = 0;

            if (mop == null && level instanceof ClientLevel clientLevel) {
                lastUsingAtCoords.put(user,MAX);
                breakCount.put(user,0.F);
                soundDelay.put(user, 0L);
                beam.put(user, ClientFXUtils.beamCont(clientLevel, user, tx, ty, tz, 2, 65382, false, 0.0F, beam.get(user), impact));
                return;
            }
            if (mop == null) {
                return;
            }
            var mopVec = mop.getLocation();
            tx = mopVec.x();
            ty = mopVec.y();
            tz = mopVec.z();
            var mopBlockPos = new BlockPos((int)mopVec.x(),(int) mopVec.y(),(int)mopVec.z());
            impact = 5;
            if (serverSideFlag && soundDelay.get(user) < System.currentTimeMillis()) {
                level.playSound(user,mopBlockPos, ThaumcraftSounds.RUMBLE, SoundSource.BLOCKS,.3F,1.F);
                soundDelay.put(user, System.currentTimeMillis() + 1200L);
            }
            if (level instanceof ClientLevel clientLevel) {
                beam.put(user, ClientFXUtils.beamCont(clientLevel, user, tx, ty, tz, 2, 65382, false, 2.0F, beam.get(user), impact));
            }

            if (mop.getType() != HitResult.Type.BLOCK || !(user instanceof Player player)){
                lastUsingAtCoords.put(user,MAX);
                breakCount.put(user,0.F);
                return;
            }

            if (level.mayInteract(player, mopBlockPos)) {
                BlockState blockState = level.getBlockState(mopBlockPos);
                float hardness = blockState.getDestroySpeed(level, mopBlockPos);
                if (hardness >= 0.0F) {
                    int pot = wandFocusItem.getWandUpgradesWithWandModifiers(focusStack,usingWand).getOrDefault(FocusUpgradeType.potency, 0);
                    float speed = 0.05F + (float)pot * 0.1F;
                    if (blockState.is(BlockTags.BASE_STONE_OVERWORLD) ||
                            blockState.is(BlockTags.DIRT)  ||
                            blockState.is(BlockTags.SAND)) {

                        speed = 0.25F + pot * 0.25F;
                    }
                    if (blockState.is(Blocks.OBSIDIAN)) {
                        speed *= 3.0F;
                    }

                    if (Objects.equals(mopBlockPos,lastUsingAtCoords.getOrDefault(user,MAX))
                    ) {
                        float bc = breakCount.get(user);
                        if (!serverSideFlag && bc > 0.0F && !blockState.isAir()) {
                            int progress = (int)(bc / hardness * 9.0F);
                            ClientFXUtils.excavateFX(mopBlockPos, user, blockState.getBlock().arch$registryName(), progress);
                        }

                        if (!serverSideFlag) {
                            if (bc >= hardness) {
                                breakCount.put(user, 0.0F);
                            } else {
                                breakCount.put(user, bc + speed);
                            }
                        } else if (bc >= hardness && container.consumeAllCentiVis(usingWand, user, (CentiVisList<Aspect>)wandFocusItem.getCentiVisCost(focusStack,usingWand), true, false,serverSideFlag)) {
                            if (this.excavate(level, usingWand, user, blockState, mopBlockPos)) {
                                for(int a = 0; a < wandFocusItem.getWandUpgradesWithWandModifiers(focusStack,usingWand).getOrDefault(FocusUpgradeType.enlarge,0); ++a) {
                                    if (container.consumeAllCentiVis(usingWand, user, (CentiVisList<Aspect>)wandFocusItem.getCentiVisCost(focusStack,usingWand), false, false,serverSideFlag)
                                            && this.breakNeighbour(user, mopBlockPos, blockState, usingWand)) {
                                        container.consumeAllCentiVis(usingWand, user, (CentiVisList<Aspect>)wandFocusItem.getCentiVisCost(focusStack,usingWand), true, false,serverSideFlag);
                                    }
                                }
                            }
                            lastUsingAtCoords.put(user,MAX);
                            breakCount.put(user, 0.0F);
                        } else {
                            breakCount.put(user, bc + speed);
                        }
                    } else {
                        lastUsingAtCoords.put(user,MAX);
                        breakCount.put(user, 0.0F);
                    }
                }
            } else {
                lastUsingAtCoords.put(user,MAX);
                breakCount.put(user,0.F);
            }

        }
    }



    private boolean excavate(Level level, ItemStack usingWand, LivingEntity user, BlockState state,BlockPos pos) {
        if (!(user instanceof Player player)) {return false;}
        GameType gt = GameType.SURVIVAL;

        if (player instanceof ServerPlayer sp) {
            gt = sp.gameMode.getGameModeForPlayer();
        }

        if (!player.mayInteract(level, pos)) {
            return false;
        }
        var wandItem = usingWand.getItem();
        if (!(
                (wandItem instanceof ICentiVisContainerItem<? extends Aspect> centiVisContainer)
                && (wandItem instanceof IWandFocusEngineItem focusEngine)
        )){
            return false;
        }
        if (!focusEngine.canApplyFocus()){
            return false;
        }
        var focusStack = focusEngine.getFocusItemStack(usingWand);
        if (focusStack.isEmpty()) {
            return false;
        }
        var fItem = focusStack.getItem();
        if (!(fItem instanceof IWandFocusItem<? extends Aspect> wandFocusItem)){
            return false;
        }
        var wandUpgrades = wandFocusItem.getWandUpgradesWithWandModifiers(focusStack,usingWand);
        {
            int fortune = wandUpgrades.getOrDefault(FocusUpgradeType.treasure, 0);
            boolean silk = wandUpgrades.getOrDefault(FocusUpgradeType.silktouch, 0) > 0;
            boolean canHarvest =
                    !state.requiresCorrectToolForDrops()
                            || player.hasCorrectToolForDrops(state);
            boolean canSilk = silk && canHarvest &&
                    !state.getBlock().asItem().canBeDepleted() &&
                    !state.requiresCorrectToolForDrops();
            getResourceFromBlock(level, state, pos, silk, canSilk, fortune,stack -> Block.popResource(level, pos, stack));

            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

            SoundEvent soundEvent = state.getBlock().getSoundType(state).getBreakSound();
            level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
            return true;
        }
    }
    public static void getResourceFromBlockCanHarvest(
            Level level,
            BlockState state,
            BlockPos pos,
            boolean silk,
            int fortune,
            Consumer<ItemStack> resourceConsumer
    ){
        boolean canSilk = silk &&
                !state.getBlock().asItem().canBeDepleted() &&
                !state.requiresCorrectToolForDrops();
        getResourceFromBlock(level, state, pos, silk, canSilk, fortune,resourceConsumer);
    }
    public static void getResourceFromBlock(
            Level level,
            BlockState state,
            BlockPos pos,
            boolean silk,
            boolean canSilk,
            int fortune,
            Consumer<ItemStack> resourceConsumer
    ) {
        if (silk && canSilk
        ) {
            if (!level.isClientSide) {
                Block.popResource(
                        level, pos, state.getBlock().asItem().getDefaultInstance());
            }
//                //TODO:Multi-platform
//                ForgeEventFactory.fireBlockHarvesting(items, world, state, x, y, z, md, 0, 1.0F, true, player);

        } else {
            //x,y,z -> pos(BlockPos)
            //md:metadata(removed,dont care)
            //fortune:fortune level
            //calculate mine block drop (including drop exp) with fortune
            if (level instanceof ServerLevel serverLevel) {
                var block = state.getBlock();
                LootTable lootTable = serverLevel.getServer().getLootData()
                        .getLootTable(state.getBlock().getLootTable());
                LootParams.Builder builder = new LootParams.Builder(serverLevel)
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                        .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                        .withOptionalParameter(LootContextParams.BLOCK_STATE, state)
                        .withLuck(fortune);
                var lootParams = builder.create(LootContextParamSets.BLOCK);

                lootTable.getRandomItems(lootParams,resourceConsumer);
                if (block instanceof DropExperienceBlockAccessor accessor) {
                    if (serverLevel.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
                        ExperienceOrb.award(serverLevel, Vec3.atCenterOf(pos), (accessor).opentc4$getRandomXp(
                                state, level, level.getRandom(), pos, fortune,0));
                    }
                }
            }
        }
    }

    protected boolean breakNeighbour(LivingEntity p, BlockPos pos, BlockState state, ItemStack usingWand) {
        List<Direction> directions = Arrays.asList(Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
        Collections.shuffle(directions, ThreadLocalRandom.current());

        for(Direction dir : directions) {
            if (p.level().getBlockState(pos) == state
                    && excavate(p.level(), usingWand, p, state, pos.offset(dir.getNormal()))
            ) {
                return true;
            }
        }

        return false;
    }


}
