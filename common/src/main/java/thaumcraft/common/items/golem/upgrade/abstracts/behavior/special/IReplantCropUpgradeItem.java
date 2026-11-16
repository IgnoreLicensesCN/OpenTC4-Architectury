package thaumcraft.common.items.golem.upgrade.abstracts.behavior.special;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import thaumcraft.api.listeners.croputils.ReplantPlaceableCropItemGetter;
import thaumcraft.common.entities.golems.BaseGolemEntity;

import java.util.Objects;

import static com.linearity.opentc4.utils.consts.EntityTypeTests.ITEM_ENTITY_TEST;

public interface IReplantCropUpgradeItem extends BaseGolemEntity.IGolemUpgradeItem{
    default boolean canTryReplant(BaseGolemEntity golem, ItemStack upgradeStack, BlockPos atPos) {
        return golem.level().getBlockState(atPos).isAir();
    }
    default void checkSeedNearbyAndReplant(BaseGolemEntity golem, ItemStack upgradeStack, BlockPos pos) {
        var level = golem.level();
        if (level instanceof ServerLevel serverLevel){
            BlockState stateBefore = level.getBlockState(pos);
            for (var itemEntity:level.getEntities(ITEM_ENTITY_TEST, AABB.ofSize(pos.getCenter(),6,6,6),(_ignore) -> true)) {
                var itemPos = itemEntity.position();
                var golemPos = golem.position();
                if (itemEntity.tickCount < 2) {
                    adjustItemEntitySpeed(itemEntity, itemPos, golemPos);
                }
                ItemStack stack = itemEntity.getItem();
                ReplantPlaceableCropItemGetter.getReplantWayFromItem(level,pos,golem,stack).opentc4$replantAt(serverLevel, pos, stack,golem);
                if (!Objects.equals(stateBefore, level.getBlockState(pos))) {
                    break;
                }
            }
        }
    }

    static void adjustItemEntitySpeed(ItemEntity itemEntity, Vec3 itemPos, Vec3 golemPos) {
        var xFromGolemToItem = itemPos.x - golemPos.x;
        var yFromGolemToItem = itemPos.y - golemPos.y;
        var zFromGolemToItem = itemPos.z - golemPos.z;
        var length = Math.sqrt(xFromGolemToItem * xFromGolemToItem + yFromGolemToItem * yFromGolemToItem + zFromGolemToItem * zFromGolemToItem);
        if (length > 1E-4) {

            xFromGolemToItem /= length;
//                    yFromGolemToItem /= length;
            zFromGolemToItem /= length;
            itemEntity.setDeltaMovement(
                    -xFromGolemToItem / 4,
                    0.075,
                    -zFromGolemToItem / 4
            );
        }
    }
}
