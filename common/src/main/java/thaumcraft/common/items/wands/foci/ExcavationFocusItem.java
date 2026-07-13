package thaumcraft.common.items.wands.foci;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.annotations.Modifiable;
import com.linearity.opentc4.mixinaccessors.DropExperienceBlockAccessor;
import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.*;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableCentiVisList;
import thaumcraft.api.listeners.wandconsumption.ThaumcraftWandConsumptionTypes;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.items.abstracts.wandabstraction.focus.IWandFocusItem;
import thaumcraft.common.items.abstracts.wandabstraction.wand.ICentiVisContainerItem;
import thaumcraft.common.items.abstracts.wandabstraction.wand.IWandFocusEngineItem;
import thaumcraft.client.fx.migrated.beams.FXBeamWand;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.items.wands.render.waveanimations.AbstractWandWaveAnimation;
import thaumcraft.common.items.wands.render.waveanimations.ThaumcraftWandWaveAnimations;

import java.util.*;
import java.util.function.Consumer;

import static com.linearity.opentc4.utils.consts.DirectionShuffles.DIRECTIONS_SHUFFLED;
import static net.minecraft.world.level.block.Block.getDrops;
import static thaumcraft.api.listeners.wandconsumption.ThaumcraftWandConsumptionTypes.CONSUMPTION_FOCUS;
import static thaumcraft.api.wands.focus.upgrade.ThaumcraftFocusUpgradeTypes.*;
import static thaumcraft.common.lib.enchantment.ThaumcraftEnchantments.ThaumcraftEnchantmentInstances.DOWSING;

public class ExcavationFocusItem extends BasicFocusItem {
    public static final CentiVisList<Aspect> BASIC_COST = UnmodifiableCentiVisList.of(Aspects.EARTH, 15);

    public static final CentiVisList<Aspect> WITH_SILKTOUCH_OR_DOWSING = UnmodifiableCentiVisList.of(
            Aspects.AIR, 1,
            Aspects.FIRE, 1,
            Aspects.EARTH, 16,
            Aspects.WATER, 1,
            Aspects.ORDER, 1,
            Aspects.ENTROPY, 1
    );

    public ExcavationFocusItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public CentiVisList<Aspect> getCentiVisCost(ItemStack focusStack, Object2IntMap<FocusUpgradeType> upgrades) {
        if (isUpgradedWith(focusStack,SILKTOUCH,upgrades)
                || isUpgradedWith(focusStack,DOWSING,upgrades)
        ) {
            return WITH_SILKTOUCH_OR_DOWSING;
        }
        return BASIC_COST;
    }

    @Override
    public int getActivationCooldownTicks(ItemStack focusStack, @NotNull ItemStack wandStack) {
        return 0;
    }

    public static final List<FocusUpgradeType> RANK_0_UPGRADES =
            List.of(FRUGAL, POTENCY, TREASURE);

    public static final List<FocusUpgradeType> RANK_1_UPGRADES =
            List.of(FRUGAL, POTENCY, ENLARGE);

    public static final List<FocusUpgradeType> RANK_2_UPGRADES =
            List.of(FRUGAL, POTENCY, TREASURE, DOWSING);

    public static final List<FocusUpgradeType> RANK_3_UPGRADES =
            List.of(FRUGAL, POTENCY, ENLARGE);

    public static final List<FocusUpgradeType> RANK_4_UPGRADES =
            List.of(FRUGAL, POTENCY, TREASURE, SILKTOUCH);



    @Override
    public @NotNull List<FocusUpgradeType> getPossibleUpgradesByRank(ItemStack focusStack, int rank) {
        if (focusStack == null) {return List.of();}
        if (rank == 0) return RANK_0_UPGRADES;
        if (rank == 1) return RANK_1_UPGRADES;
        if (rank == 2) return RANK_2_UPGRADES;
        if (rank == 3) return RANK_3_UPGRADES;
        if (rank == 4) return RANK_4_UPGRADES;
        return List.of();
    }

//    @Override
//    public String getSortingHelper(ItemStack focusstack) {
//        return "BE" + super.getSortingHelper(focusstack);
//    }

    @Override
    public boolean isCentiVisCostPerTick(ItemStack focusStack, @Nullable ItemStack wandStack) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> onFocusRightClick(ItemStack wandStack, ItemStack focusStack, Level level, LivingEntity user, InteractionHand interactionHand) {
        user.startUsingItem(interactionHand);
        return new InteractionResultHolder<>(InteractionResult.CONSUME, wandStack);
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

        if (!checkAndSetCooldown(focusStack,usingWand,user)) {
            user.stopUsingItem();
            return;
        }

        var wandItem = usingWand.getItem();
        var focusItem = focusStack.getItem();
        if (!((wandItem instanceof ICentiVisContainerItem<? extends Aspect> containerNotCasted) && (focusItem instanceof IWandFocusItem<? extends Aspect> wandFocusItem))
        ){
            user.stopUsingItem();
            return;
        }
        var upgrades = getFocusUpgradesWithWandModifiers(focusStack, usingWand);
        var container = (ICentiVisContainerItem<Aspect>) containerNotCasted;
        var level = user.level();
        var serverSideFlag = !level.isClientSide();
        if (!(container.consumeAllCentiVis(
                usingWand
                ,user, getCentiVisCost(focusStack,upgrades),false, CONSUMPTION_FOCUS,serverSideFlag))
        ){
            user.stopUsingItem();
            return;
        }
        {

            soundDelay.putIfAbsent(user, 0L);
            breakCount.putIfAbsent(user, 0.0F);
            lastUsingAtCoords.putIfAbsent(user, ZERO);

            HitResult mop = user.pick(5, 0, false);
            Vec3 v = user.getLookAngle();
            double tx = user.getX() + v.x() * (double)10.0F;
            double ty = user.getY() + v.y() * (double)10.0F;
            double tz = user.getZ() + v.z() * (double)10.0F;
            int impact = 0;

            if (mop.getType() == HitResult.Type.MISS && level.isClientSide) {
                if (level instanceof ClientLevel clientLevel) {
                    lastUsingAtCoords.put(user, MAX);
                    breakCount.put(user, 0.F);
                    soundDelay.put(user, 0L);
                    beam.put(user, ClientFXUtils.beamCont(clientLevel, user, tx, ty, tz, 2, 65382, false, 0.0F, beam.get(user), impact));
                }
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
                    int pot = wandFocusItem.getFocusUpgradesWithWandModifiers(focusStack,usingWand).getOrDefault(POTENCY, 0);
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
                        } else if (bc >= hardness
                                && container.consumeAllCentiVis(usingWand, user, getCentiVisCost(focusStack,upgrades),
                                true, CONSUMPTION_FOCUS, true)) {
                            if (this.excavate(level, usingWand, user, blockState, mopBlockPos)) {
                                for(int a = 0; a < wandFocusItem.getFocusUpgradesWithWandModifiers(focusStack,usingWand).getOrDefault(ENLARGE,0); ++a) {
                                    if (container.consumeAllCentiVis(usingWand, user, getCentiVisCost(focusStack,upgrades), false, CONSUMPTION_FOCUS, true)
                                            && this.breakNeighbour(user, mopBlockPos, blockState, usingWand)) {
                                        container.consumeAllCentiVis(usingWand, user, getCentiVisCost(focusStack,upgrades), true, CONSUMPTION_FOCUS, true);
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

    protected boolean excavate(Level level, ItemStack usingWand, LivingEntity user, BlockState state,BlockPos pos) {
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
        {
            getResourceFromBlock(focusStack,usingWand,level, state, pos,stack -> Block.popResource(level, pos, stack));

            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

            SoundEvent soundEvent = state.getBlock().getSoundType(state).getBreakSound();
            level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
            return true;
        }
    }
    protected ItemStack getFakePickaxeStackForFocus(ItemStack focusStack,@Nullable ItemStack wandStack) {
        return getFakePickaxeStackForFocusFromPickaxe(focusStack,wandStack,new ItemStack(Items.DIAMOND_PICKAXE));
    }
    protected ItemStack getFakePickaxeStackForFocusFromPickaxe(ItemStack focusStack,@Nullable ItemStack wandStack,ItemStack pickaxeStack) {
        ItemStack fakeDiamondPickaxeStack = pickaxeStack.copy();
        var enchantments = new HashMap<>(EnchantmentHelper.getEnchantments(focusStack));
        getEnchantmentsFromFocus(focusStack,wandStack).forEach((enchantment, level) -> enchantments.merge(enchantment,level,Integer::sum));
        EnchantmentHelper.setEnchantments(enchantments,fakeDiamondPickaxeStack);
        return fakeDiamondPickaxeStack;
    }
    protected @Modifiable Map<Enchantment,Integer> getEnchantmentsFromFocus(ItemStack focusStack,@Nullable ItemStack wandStack){
        var wandUpgrades = this.getFocusUpgradesWithWandModifiers(focusStack,wandStack);
        int fortune = wandUpgrades.getOrDefault(TREASURE, 0);
        int silk = wandUpgrades.getOrDefault(SILKTOUCH, 0);
        int dowsing = wandUpgrades.getOrDefault(DOWSING,0);

        Map<Enchantment,Integer> enchantments = new HashMap<>();
        if (fortune != 0){
            enchantments.put(Enchantments.BLOCK_FORTUNE, fortune);
            enchantments.put(Enchantments.SILK_TOUCH, silk);
            enchantments.put(DOWSING(), dowsing);
        }
        return enchantments;
    }
    public void getResourceFromBlock(
            ItemStack focusStack,
            @Nullable ItemStack wandStack,
            Level level,
            BlockState state,
            BlockPos pos,
            ItemStack pickaxeStack,
            Consumer<ItemStack> resourceConsumer
    ){
        getResourceFromBlock(level,state,pos,getFakePickaxeStackForFocusFromPickaxe(focusStack,wandStack,pickaxeStack),resourceConsumer);
    }
    public void getResourceFromBlock(
            ItemStack focusStack,
            @Nullable ItemStack wandStack,
            Level level,
            BlockState state,
            BlockPos pos,
            Consumer<ItemStack> resourceConsumer
    ){
        getResourceFromBlock(level,state,pos,getFakePickaxeStackForFocus(focusStack,wandStack),resourceConsumer);
    }
    public static void getResourceFromBlock(
            Level level,
            BlockState state,
            BlockPos pos,
            ItemStack pickaxeStack,
            Consumer<ItemStack> resourceConsumer
    ) {
        if (level instanceof ServerLevel serverLevel) {
            var drops = getDrops(state, serverLevel, pos, LevelBlockEntityAccessing.getExistingBlockEntity(serverLevel, pos), null, pickaxeStack);
            drops.forEach(resourceConsumer);

            var block = state.getBlock();
            if (block instanceof DropExperienceBlockAccessor accessor) {
                var enchantments = EnchantmentHelper.getEnchantments(pickaxeStack);
                if (serverLevel.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
                    ExperienceOrb.award(serverLevel, Vec3.atCenterOf(pos), (accessor).opentc4$getRandomXp(
                            state,
                            level,
                            level.getRandom(),
                            pos,
                            enchantments.getOrDefault(Enchantments.BLOCK_FORTUNE,0),
                            enchantments.getOrDefault(Enchantments.SILK_TOUCH,0)));
                }
            }
        }
    }

    protected int easyLCGState = 0;
    protected static final int[] LCGNeededPrimesExample = {
            3877, 6737, 7237, 62327, 39439, 53791, 53549, 16759,
            1987, 35897, 46589, 59369, 26647, 56629, 26387, 1931,
            43451, 4409, 823, 14947, 22907, 9533, 36343, 46601,
            36833, 26903, 1667, 4519, 53777, 38917, 37181, 56417,
            14923, 42989, 58481, 12577, 54151, 18691, 44927, 47591,
            29569, 54499, 16223, 63997, 12149, 6551, 59341, 30553,
            58909, 34883, 1759, 11093, 13873, 64621, 13367, 16741,
            14221, 28429, 50873, 9127, 54721, 2447, 16057, 28183
    };
    protected int LCGStep = LCGNeededPrimesExample[Math.abs(System.identityHashCode(this)% LCGNeededPrimesExample.length)];

    protected boolean breakNeighbour(LivingEntity p, BlockPos pos, BlockState state, ItemStack usingWand) {
        easyLCGState = (easyLCGState + LCGStep) % 720;
        for(var dir:DIRECTIONS_SHUFFLED[easyLCGState]) {
            if (p.level().getBlockState(pos.relative(dir)) == state && excavate(p.level(), usingWand, p, state, pos.relative(dir))
            ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AbstractWandWaveAnimation getWaveAnimation(ItemStack focusStack,@Nullable ItemStack wandStack) {
        return ThaumcraftWandWaveAnimations.CHARGE;
    }
}
