package thaumcraft.common.lib.utils;

import com.linearity.opentc4.mixinaccessors.ExtendedItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.listeners.croputils.CropHarvester;
import thaumcraft.api.listeners.croputils.DirectlyBreakHarvestableCropChecker;
import thaumcraft.api.listeners.croputils.ReplantPlaceableCropItemGetter;
import thaumcraft.api.listeners.croputils.UseHarvestableCropChecker;

import java.util.UUID;

import static com.linearity.opentc4.OpenTC4.platformUtils;
import static thaumcraft.api.listeners.croputils.CropHarvester.CROP_HARVESTERS;

public class CropUtils {

    public static boolean isHarvestableCrop(ServerLevel world, BlockPos pos,@Nullable Entity harvesterEntity) {
        for (var harvester:CROP_HARVESTERS.getListeners()){
            if (harvester.canHarvestWithThis(world, pos, harvesterEntity)){
                return true;
            }
        }
        return false;
    }


    public static final UUID USE_CROP_PLAYER_UUID = UUID.fromString("e0dea31d-ebf0-4745-8409-552bfef51e1d");
    public static void harvestCropUsingRightClick(ServerLevel world, BlockPos pos,@Nullable Entity harvester) {
        useWithFakePlayerAt(ItemStack.EMPTY,world, pos, Direction.DOWN);
    }

    public static void useWithFakePlayerAt(ItemStack usingStack,ServerLevel world, BlockPos pos,Direction direction) {
        var fakePlayer = platformUtils.getFakePlayer(world,USE_CROP_PLAYER_UUID);
        fakePlayer.moveTo(pos,0,0);
        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, usingStack);
        fakePlayer.setItemInHand(InteractionHand.OFF_HAND,ItemStack.EMPTY);
        world.getBlockState(pos).use(world,fakePlayer, InteractionHand.MAIN_HAND,new BlockHitResult(pos.getCenter(), direction, pos,false));
        for (var stack:fakePlayer.getInventory().items){
            if (!stack.isEmpty()){
                fakePlayer.spawnAtLocation(ExtendedItemStack.splitAll(stack));
            }
        }
        fakePlayer.moveTo(0,32768,0,0,0);
    }



    public static void init(){
        DirectlyBreakHarvestableCropChecker.init();
        UseHarvestableCropChecker.init();
        ReplantPlaceableCropItemGetter.init();
        CropHarvester.init();
    }

    public static boolean tryHarvest(
            ServerLevel world,
            BlockPos pos,
            @Nullable("golem in vanilla tc4 usage but idk if you want more") Entity harvesterEntity
    ) {
        for (var harvester:CROP_HARVESTERS.getListeners()){
            if (harvester.canHarvestWithThis(world, pos, harvesterEntity)){
                harvester.tryHarvest(world, pos, harvesterEntity);
                return true;
            }
        }
        return false;
    }
}
