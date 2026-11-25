package thaumcraft.common.entities.goals.golem.transportresources.liquidinteraction;

import dev.architectury.fluid.FluidStack;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.goals.golem.AbstractGolemGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemHelper;
import thaumcraft.common.entities.golems.Marker;
import thaumcraft.common.items.golem.upgrade.core.LiquidGolemCoreItem;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import static com.linearity.opentc4.OpenTC4.platformUtils;

public abstract class AbstractGolemFluidGoal extends AbstractGolemGoal {
    public AbstractGolemFluidGoal(BaseGolemEntity golem) {
        super(golem);
    }

    public double reachRange(){
        return 2.25;
    }
    public double reachRangeSqr(){
        return reachRange() * reachRange();
    }

    public @Nullable("empty -> any,null -> no") Set<Fluid> getMissingFluid(BaseGolemEntity golem, Set<Fluid> fluidFilterCacheSet){
        return getMissingFluidForGolem(golem, fluidFilterCacheSet);
    }

    public static @Nullable("empty -> any,null -> no") Set<Fluid> getMissingFluidForGolem(BaseGolemEntity golem, Set<Fluid> fluidFilterCacheSet){
        fluidFilterCacheSet.clear();
        var holdingStack = LiquidGolemCoreItem.GolemHoldingFluidFakeField.getHoldingFluid(golem);
        var fluidInHand = holdingStack.isEmpty()? Fluids.EMPTY:holdingStack.getFluid();

        for (var stack:golem.inventory){//filters, TODO:[maybe wont finished]special way to set fluid filters instead of unnecessary itemstack in inventory
            if (!stack.isEmpty()){
                var fluidFilter = platformUtils.getFluidInteractionManager().getFluidFromItemStack(stack);
                if (fluidFilter != Fluids.EMPTY){
                    if (fluidInHand == fluidFilter){
                        fluidFilterCacheSet.clear();
                        fluidFilterCacheSet.add(fluidInHand);
                        return fluidFilterCacheSet;
                    } else {
                        if (fluidInHand != Fluids.EMPTY) {
                            return null;
                        } else {
                            fluidFilterCacheSet.add(fluidFilter);
                        }
                    }
                }
            }
        }
        if (fluidInHand != Fluids.EMPTY){
            fluidFilterCacheSet.add(fluidInHand);
        }
        return fluidFilterCacheSet;
    }

    public static class FluidHandlerInfo {
        public Marker marker;
    }
    public FluidStack extractFluidFrom(Level level,BlockPos pos,Direction direction,long expectedAmount,boolean doit){
        return extractFluidFromPos(level,pos,direction,expectedAmount,doit);
    }
    public static FluidStack extractFluidFromPos(Level level, BlockPos pos, Direction direction, long expectedAmount, boolean doit){
        return platformUtils.getFluidInteractionManager().extractFluidFromPos(level, pos, direction, expectedAmount, doit);
    }
    public boolean isFluidContainer(Level level, BlockPos pos, Direction direction){
        return isPositionFluidContainer(level, pos, direction);
    }
    public static boolean isPositionFluidContainer(Level level, BlockPos pos, Direction direction){
        return platformUtils.getFluidInteractionManager().getFluidTanksFromPos(level, pos, direction) > 0;
    }
    public @Nullable FluidStack getFluidFrom(ItemStack stack){
        return getFluidFromItemStack(stack);
    }
    public static @Nullable FluidStack getFluidFromItemStack(ItemStack stack){
        return platformUtils.getFluidInteractionManager().copyFluidStackFromItemStack(stack);
    }
    public FluidStack getFluidDirectlyFrom(Level level,BlockPos pos){
        return getFluidDirectlyFromBlock(level,pos);
    }
    public static FluidStack getFluidDirectlyFromBlock(Level level, BlockPos pos){
        var blockState = level.getBlockState(pos);


        var fluidState = blockState.getFluidState();
        if (!fluidState.isEmpty() && fluidState.isSource() && blockState.getBlock() instanceof BucketPickup){
            var fluid = fluidState.getType();
            var fluidBucket = fluid.getBucket();
            if (fluidBucket != Items.AIR) {
                var fluidBucketStack = fluidBucket.getDefaultInstance();
                return platformUtils.getFluidInteractionManager().copyFluidStackFromItemStack(fluidBucketStack);
            }
        }
        return FluidStack.empty();
    }
    public static final UUID EXTRACT_FLUID_PLAYER_UUID = UUID.fromString("a6287143-cbce-49a0-ba43-c9c84c618b40");
    public FluidStack extractFluidDirectlyFrom(Level level, BlockPos pos,Direction direction){
        return extractFluidDirectlyFromBlock(level, pos, direction);
    }
    public static FluidStack extractFluidDirectlyFromBlock(Level level, BlockPos pos,Direction direction){
        var blockState = level.getBlockState(pos);
        var fluidState = blockState.getFluidState();
        if (!fluidState.isEmpty() && fluidState.isSource() && blockState.getBlock() instanceof BucketPickup bucketPickupBlock && level instanceof ServerLevel serverLevel){
            var fluid = fluidState.getType();
            var fluidBucket = fluid.getBucket();
            if (fluidBucket != Items.AIR) {
                var bucketItem = Items.BUCKET;
                var fakePlayer = platformUtils.getFakePlayer(serverLevel,EXTRACT_FLUID_PLAYER_UUID);
                var usingSimulateStack = bucketItem.getDefaultInstance();
                fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, usingSimulateStack);
                fakePlayer.moveTo(pos.getX(),pos.getY(),pos.getZ());
                blockState.use(serverLevel,fakePlayer,InteractionHand.MAIN_HAND,new BlockHitResult(pos.getCenter(), direction, pos,false));
                var mainHandStack = fakePlayer.getItemInHand(InteractionHand.MAIN_HAND);
                fakePlayer.setItemInHand(InteractionHand.MAIN_HAND,ItemStack.EMPTY);
                return platformUtils.getFluidInteractionManager().copyFluidStackFromItemStack(mainHandStack);
            }
        }
        return FluidStack.empty();
    }

    //wont find which is at home
    @RequiredArgsConstructor
    public static class MarkedFluidHandlerIterator implements Iterator<FluidHandlerInfo>, GolemHelper.Resettable {
        public final BaseGolemEntity golem;
        protected Iterator<Marker> markerIterator = golem.markers.iterator();
        public FluidHandlerInfo info = new FluidHandlerInfo();
        public @Nullable("when !hasNext()") FluidHandlerInfo resultCache = null;

        @Override
        public void reset() {
            markerIterator = golem.markers.iterator();
        }

        @Override
        public boolean hasNext() {
            if (resultCache != null){
                return true;
            }
            while (markerIterator.hasNext()){
                Marker marker = markerIterator.next();
                if (marker.getPos().equals(golem.getRestrictCenter())){
                    continue;
                }
                if (isValidMarker(marker)){
                    info.marker = marker;
                    resultCache = info;
                    return true;
                }
            }
            return false;
        }

        protected boolean isValidMarker(Marker marker) {
            return isPositionFluidContainer(golem.level(), marker.getPos(), marker.getSide());
        }

        @Override
        public FluidHandlerInfo next() {
            if (resultCache == null){
                if (!hasNext()){
                    throw new IllegalStateException("call hasNext() first");
                }
            }
            var result = resultCache;
            resultCache = null;
            return result;
        }
    }
}
