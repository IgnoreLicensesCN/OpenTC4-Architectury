package thaumcraft.api.visnet;

import com.linearity.opentc4.annotations.forvalue.CentiVisAmount;
import com.linearity.opentc4.mixinaccessors.CoordLookupOwnerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import thaumcraft.api.aspects.Aspect;

import java.util.concurrent.atomic.AtomicInteger;

import static thaumcraft.api.visnet.VisNetNodeBlockEntity.VisNetNodeType.ALL_VIS_NET_NODE_TYPES;
import static thaumcraft.common.lib.utils.Utils.generateVisEffect;

public class VisNetHandler {
//    public static final Collection<
//            Map<
//                    VisNetNodeTypeResourceLocation,
//                    Map<
//                            Level,
//                            CoordLookup<VisNetNodeBlockEntity>
//                            >
//                    >
//            > visNetNodeLookups
//            = new ArrayList<>();

    // NODE DRAINING

    public static @CentiVisAmount int drainCentiVis(Level world, BlockPos pos, Aspect aspect, int amount){
        return drainCentiVis(world,pos,aspect,amount,8);
    }
    /**
     * This method drains vis from a relay or source near the passed in
     * location. The amount received can be less than the amount requested so
     * take that into account.
     *
     * @param world      node at world
     * @param pos        the position from the draining block or entity
     * @param aspect     what aspect to drain
     * @param amount     how much to drain
     * @param drainRange range to drain
     * @return how much was actually drained
     */
    public static @CentiVisAmount int drainCentiVis(Level world, BlockPos pos, Aspect aspect, int amount, int drainRange) {
        long drainRangeSqr = ((long) drainRange) * drainRange;
        var drainedAmount = new AtomicInteger(0);
        for (var type : ALL_VIS_NET_NODE_TYPES) {
            for (var lookup : ((CoordLookupOwnerLevel)world).opentc4$getCoordLookupsForType(type)) {

                var iter = lookup.forItemsNearPosWithRangeIterator(pos);
                while (iter.hasNext()) {
                    var netNodes = iter.next();
                    for (var node:netNodes) {
                        var range = node.getRange();
                        var nodePos = node.getBlockPos();
                        if (Math.min(drainRangeSqr,((long)range) * range) >= nodePos.distSqr(pos)) {
                            int requiredAmount = amount - drainedAmount.get();
                            int a = node.consumeCentiVis(aspect, requiredAmount);
                            requiredAmount = amount - drainedAmount.get();
                            drainedAmount.addAndGet(a);
                            if (a > 0) {
                                int color = aspect.color;
                                generateVisEffect(world, pos, nodePos, color);
                            }
                            if (requiredAmount <= 0){
                                break;
                            }
                        }
                    }
                }
            }
        }
        return drainedAmount.get();
    }

    public static boolean canNodeBeSeen(VisNetNodeBlockEntity source, VisNetNodeBlockEntity target) {
        Level level = source.getLevel();
        if (level == null) return false;

        Vec3 start = Vec3.atCenterOf(source.getBlockPos());
        Vec3 end = Vec3.atCenterOf(target.getBlockPos());
        BlockPos targetPos = target.getBlockPos();

        Boolean result = BlockGetter.traverseBlocks(
                start,
                end,
                false, // context = 是否被阻挡
                (blocked, pos) -> {

                    // 到达目标
                    if (pos.equals(targetPos)) {
                        return false; // 不阻挡
                    }

                    BlockState state = level.getBlockState(pos);

                    // 没有碰撞箱 → 不阻挡
                    return !state.getCollisionShape(level, pos)
                            .isEmpty();

                    // 有碰撞箱 → 阻挡
                },
                blocked -> false // miss
        );

        return !result;
    }

}
