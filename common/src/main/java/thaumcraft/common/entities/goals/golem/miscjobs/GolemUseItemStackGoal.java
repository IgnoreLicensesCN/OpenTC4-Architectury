package thaumcraft.common.entities.goals.golem.miscjobs;

import it.unimi.dsi.fastutil.bytes.ByteArraySet;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.goals.golem.AbstractGolemGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemHelper;
import thaumcraft.common.entities.golems.Marker;

import java.util.UUID;

import static com.linearity.opentc4.OpenTC4.platformUtils;
import static thaumcraft.common.entities.goals.golem.transportresources.itemstackinteraction.GolemUseCorePlaceItemStackToHomeGoal.findSomethingUseCoreReturnMarker;
import static thaumcraft.common.entities.golems.configuration.GolemConfigurationKey.Default.USE_CORE_DO_SNEAKING;
import static thaumcraft.common.entities.golems.configuration.GolemConfigurationKey.Default.USE_CORE_LEFT_CLICK;

public class GolemUseItemStackGoal extends AbstractGolemGoal {
    public static final UUID USE_ITEM_STACK_GOAL_FAKE_PLAYER_UUID = UUID.fromString("447044f9-8c22-455b-ba38-d684425d9028");
    public GolemUseItemStackGoal(BaseGolemEntity golem) {
        super(golem);
    }

    protected @Nullable Marker marker = null;
    protected @Nullable BlockState usingAtBlockState = null;
    protected ByteSet colorsCache = new ByteArraySet(17);
    protected int nextTickToCheckRun = 0;
    protected int remainingTicks = 0;
    
    @Override
    public boolean canUse() {
        if (golem.tickCount >= nextTickToCheckRun && golem.getNavigation().isDone()) {
            nextTickToCheckRun = golem.tickCount + golem.getRandom().nextInt(4)+2;
            marker = findSomethingUseCoreReturnMarker(golem, golem.getMainHandItem(), colorsCache);
            return marker != null;
        }
        return false;
    }

    protected int getReachRange() {
        return 2;
    }
    
    protected int getReachRangeSqr() {
        return getReachRange() * getReachRange();
    }

    @Override
    public boolean canContinueToUse() {
        return remainingTicks > 0 
                && golem.getNavigation().isInProgress() 
                && (
                        marker != null 
                                && golem
                                .level()
                                .getBlockState(marker.getPos())
                                .equals(usingAtBlockState)
        );
    }

    @Override
    public void tick() {
        super.tick();
        remainingTicks -= 1;
        if (marker != null) {
            golem.lookAt(EntityAnchorArgument.Anchor.EYES,marker.getPos().getCenter());
            if (marker.getPos().distToCenterSqr(golem.position()) < getReachRangeSqr()) {
                useAtMarker();
            }
        }else {
            remainingTicks = 0;
        }
    }

    @Override
    public void stop() {
        super.stop();
        remainingTicks = 0;
        golem.getNavigation().stop();
    }

    @Override
    public void start() {
        super.start();
        if (marker != null) {
            remainingTicks = 200;
            var navi = golem.getNavigation();
            BlockPos gotoPos = marker.getPos();
            navi.moveTo(navi.createPath(gotoPos,Math.max(1,getReachRange()/2)),golem.getMoveControl().getSpeedModifier());
            usingAtBlockState = golem.level().getBlockState(gotoPos);
        }
    }

    protected ServerPlayer getFakePlayer(ServerLevel level){
        var fakePlayerInstance = platformUtils.getFakePlayer(level,USE_ITEM_STACK_GOAL_FAKE_PLAYER_UUID);
        var sneakingFlag = golem.configurationProvidersView.contains(USE_CORE_DO_SNEAKING)
                ? USE_CORE_DO_SNEAKING.provider.getBooleanFrom(golem)
                :USE_CORE_DO_SNEAKING.provider.defaultValue;
        fakePlayerInstance.moveTo(golem.position());
        fakePlayerInstance.setShiftKeyDown(sneakingFlag);
        fakePlayerInstance.getInventory().clearContent();
        fakePlayerInstance.setItemInHand(InteractionHand.MAIN_HAND,golem.getMainHandItem());
        return fakePlayerInstance;
    }

    protected void dropFakePlayerContents(ServerPlayer fakePlayer) {
        GolemHelper.dropFakePlayerContentsBelongsToGolem(golem, fakePlayer);
    }
    
    protected void useAtMarker() {
        if (!(golem.level() instanceof ServerLevel level)){
            return;
        }
        if (marker == null){
            remainingTicks = 0;
            return;
        }
        var fakePlayer = getFakePlayer(level);
//        boolean ignoreItem = false;
//        Direction facing = Direction.getOrientation(golem.homeFacing);
//        int cX = home.posX - facing.offsetX;
//        int cY = home.posY - facing.offsetY;
//        int cZ = home.posZ - facing.offsetZ;
//        TileEntity tile = golem.level().getTileEntity(cX, cY, cZ);
//        if (!(tile instanceof IInventory)) {
//            ignoreItem = true;
//        }
        BlockPos pos = marker.getPos();
        var useOnAir = level.getBlockState(pos).isAir();
        var isLeftClick = golem.configurationProvidersView.contains(USE_CORE_LEFT_CLICK)
                ? USE_CORE_LEFT_CLICK.provider.getBooleanFrom(golem)
                : USE_CORE_LEFT_CLICK.provider.defaultValue;
        if (!isLeftClick){
            if (useOnAir) {
                fakePlayer.gameMode.useItem(
                        fakePlayer, level, fakePlayer.getMainHandItem(), InteractionHand.MAIN_HAND
                );
            } else {
                fakePlayer.gameMode.useItemOn(
                        fakePlayer, level, fakePlayer.getMainHandItem(), InteractionHand.MAIN_HAND,
                        new BlockHitResult(pos.getCenter(), marker.getSide(), pos, false)
                );
            }
        } else {
            fakePlayer.gameMode.handleBlockBreakAction(
                    pos,
                    ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK,
                    marker.getSide(),
                    level.getMaxBuildHeight(),Integer.MIN_VALUE
            );
        }
        dropFakePlayerContents(fakePlayer);
        remainingTicks = 0;
    }

}
