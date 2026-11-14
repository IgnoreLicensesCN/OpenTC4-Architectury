package thaumcraft.common.lib.utils;

import com.linearity.opentc4.mixinaccessors.ExtendedItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.phys.BlockHitResult;

import java.util.UUID;

import static com.linearity.opentc4.OpenTC4.platformUtils;
import static net.minecraft.world.level.block.SweetBerryBushBlock.AGE;
import static thaumcraft.common.blocks.ThaumcraftBlocks.Tags.*;

public class CropUtils {

    public static boolean isHarvestableCrop(Level world, BlockPos pos) {
        return isDirectlyBreakHarvestableCrop(world, pos) || isUseBlockHarvestableCrop(world, pos);
    }
    //also mixin point
    public static boolean isDirectlyBreakHarvestableCrop(Level world, BlockPos pos) {
        var state = world.getBlockState(pos);
        var block = state.getBlock();
        if (state.is(NOT_BREAK_HARVESTABLE)) {
            return false;
        }
        if (block instanceof CropBlock cropBlock && cropBlock.isMaxAge(state)){
            return true;
        }
        if (state.is(REED_LIKE_DIRECTLY_BREAK_HARVESTABLE)){
            return world.getBlockState(pos.below()).getBlock() == block;
        }
        return state.is(DIRECTLY_BREAK_HARVESTABLE);
    }
    //also mixin point
    public static boolean isUseBlockHarvestableCrop(Level world, BlockPos pos) {
        var state = world.getBlockState(pos);
        var block = state.getBlock();
        if (block instanceof SweetBerryBushBlock && state.getValue(AGE) >= SweetBerryBushBlock.MAX_AGE) {
            return true;
        }
        return block instanceof CaveVines && CaveVines.hasGlowBerries(state);
    }
    public static final UUID USE_CROP_PLAYER_UUID = UUID.fromString("e0dea31d-ebf0-4745-8409-552bfef51e1d");
    public static void harvestCropUsingRightClick(ServerLevel world, BlockPos pos) {
        var fakePlayer = platformUtils.getFakePlayer(world,USE_CROP_PLAYER_UUID);
        fakePlayer.moveTo(pos,0,0);
        world.getBlockState(pos).use(world,fakePlayer, InteractionHand.MAIN_HAND,new BlockHitResult(pos.getCenter(), Direction.DOWN,pos,false));
        for (var stack:fakePlayer.getInventory().items){
            if (!stack.isEmpty()){
                fakePlayer.spawnAtLocation(ExtendedItemStack.splitAll(stack));
            }
        }
        fakePlayer.moveTo(0,32768,0,0,0);
    }
}
