package thaumcraft.api.visnet;

import com.linearity.opentc4.utils.CubeChunkedWeakLookups;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.resourcelocations.VisNetNodeTypeResourceLocation;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static thaumcraft.common.lib.utils.Utils.generateVisEffect;

public class VisNetHandler {
	public static final Collection<Map<Level,Map<VisNetNodeTypeResourceLocation,CubeChunkedWeakLookups<VisNetNodeBlockEntity>>>> visNetNodeLookups
			= new ArrayList<>();

	// NODE DRAINING
	/**
	 * This method drains vis from a relay or source near the passed in
	 * location. The amount received can be less than the amount requested so
	 * take that into account.
	 *
	 * @param world node at world
	 * @param pos the position from the draining block or entity
	 * @param aspect what aspect to drain
	 * @param amount how much to drain
	 * @return how much was actually drained
	 */
	public static int drainVis(Level world, BlockPos pos, Aspect aspect, int amount){
		var drainedAmount = new AtomicInteger(0);
		for (var map : visNetNodeLookups) {
			var lookupMap = map.get(world);
			if (lookupMap != null) {
				for (var lookup : lookupMap.values()) {
					lookup.forItemsNearPosWithBreak(
							pos, nodeBE -> {
								if (nodeBE instanceof VisNetNodeBlockEntity netNode) {
									var range = netNode.getRange();
									var nodePos = netNode.getBlockPos();
									if (range * range >= nodePos.distSqr(pos)) {
										int requiredAmount = amount - drainedAmount.get();
										int a = netNode.consumeCentiVis(aspect, requiredAmount);
										requiredAmount = amount - drainedAmount.get();
										drainedAmount.addAndGet(a);
										if (a > 0) {
											int color = aspect.color;
											generateVisEffect(world, pos, nodePos, color);
										}
										return requiredAmount <= 0;
									}
								}
								return false;
							}
					);
				}
			}
		}
		return drainedAmount.get();
	}

	 public static boolean canNodeBeSeen(VisNetNodeBlockEntity source,VisNetNodeBlockEntity target)
	 {
		 Level level = source.getLevel();
		 if (level == null) return false;

		 Vec3 start = Vec3.atCenterOf(source.getBlockPos());
		 Vec3 end   = Vec3.atCenterOf(target.getBlockPos());
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
