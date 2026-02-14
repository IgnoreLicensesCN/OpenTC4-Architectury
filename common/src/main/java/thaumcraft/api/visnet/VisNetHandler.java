package thaumcraft.api.visnet;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.api.WorldCoordinates;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static thaumcraft.common.lib.utils.Utils.generateVisEffect;

public class VisNetHandler {

	public static int drainVis(Level world, BlockPos pos, Aspect aspect, int amount){
		return drainVis(world,pos.getX(),pos.getY(),pos.getZ(),aspect,amount);
	}
	// NODE DRAINING
	/**
	 * This method drains vis from a relay or source near the passed in
	 * location. The amount received can be less than the amount requested so
	 * take that into account.
	 * 
	 * @param world node at world
	 * @param x the x position ofAspectVisList the draining block or entity
	 * @param y the y position ofAspectVisList the draining block or entity
	 * @param z the z position ofAspectVisList the draining block or entity
	 * @param aspect what aspect to drain
	 * @param amount how much to drain
	 * @return how much was actually drained
	 */
	public static int drainVis(Level world, int x, int y, int z, Aspect aspect, int amount) {

		int drainedAmount = 0;

		WorldCoordinates drainer = new WorldCoordinates(x, y, z,
				world.dimension().location().toString());
		if (!nearbyNodes.containsKey(drainer)) {
			calculateNearbyNodes(world, x, y, z);
		}

		ArrayList<WeakReference<VisNetNodeBlockEntity>> nodes = nearbyNodes.get(drainer);
		if (nodes!=null && !nodes.isEmpty()) {
			for (WeakReference<VisNetNodeBlockEntity> noderef : nodes) {

				VisNetNodeBlockEntity node = noderef.get();

				if (node == null) continue;

				int a = node.consumeVis(aspect, amount);
				drainedAmount += a;
				amount -= a;
				if (a > 0) {
					int color = aspect.color;
					var pos = node.getBlockPos();
					generateVisEffect(world.dimension(), x, y, z, pos.getX(),pos.getY(),pos.getZ(), color);
				}
				if (amount <= 0) {
					break;
				}
			}
		}
		
		return drainedAmount;
	}

	public static Map<ResourceKey<Level>, HashMap<WorldCoordinates, WeakReference<VisNetNodeBlockEntity>>> sources = new HashMap<>();

	public static void addSource(Level world, VisNetNodeBlockEntity vs) {
		ResourceKey<Level> key = world.dimension();
		HashMap<WorldCoordinates, WeakReference<VisNetNodeBlockEntity>> sourcelist = sources
				.get(key);
		if (sourcelist == null) {
			sourcelist = new HashMap<>();
		}
		sourcelist.put(vs.getLocation(), new WeakReference<>(vs));
		sources.put(key, sourcelist);
		nearbyNodes.clear();
	}

//	public static boolean isNodeValid(WeakReference<VisNetNodeBlockEntity> node) {
//		if (node == null){
//			return false;
//		}
//		VisNetNodeBlockEntity visNode = node.get();
//        return isNodeValid(visNode);
//    }
//	public static boolean isNodeValid(VisNetNodeBlockEntity visNode) {
//		if (visNode == null){
//			return false;
//		}
//		return !visNode.isInvalid();
//	}

	public static WeakReference<VisNetNodeBlockEntity> addNode(Level world, VisNetNodeBlockEntity vn) {
		WeakReference<VisNetNodeBlockEntity> ref = new WeakReference<>(vn);

		HashMap<WorldCoordinates, WeakReference<VisNetNodeBlockEntity>> sourcelist = sources
				.get(world.dimension());
		if (sourcelist == null) {
			sourcelist = new HashMap<>();
			return null;
		}

		ArrayList<Object[]> nearby = new ArrayList<>();

		for (WeakReference<VisNetNodeBlockEntity> root : sourcelist.values()) {

			VisNetNodeBlockEntity source = root.get();
			if (source == null) {
				continue;
			}
			float r = inRange(world, vn.getLocation(), source.getLocation(),
					vn.getRange());
			if (r > 0) {
				nearby.add(new Object[] { source, r - vn.getRange() * 2 });
			}
			
			nearby = findClosestNodes(vn, source, nearby);
			cache.clear();
		}

		float dist = Float.MAX_VALUE;
		VisNetNodeBlockEntity closest = null;
		if (!nearby.isEmpty()) {
			for (Object[] o : nearby) {
				if ((Float) o[1] < dist &&
					(vn.getAttunement() == -1 || ((VisNetNodeBlockEntity) o[0]).getAttunement() == -1 || 
						vn.getAttunement() == ((VisNetNodeBlockEntity) o[0]).getAttunement())//) {
					 && canNodeBeSeen(vn,(VisNetNodeBlockEntity)o[0])) {
					dist = (Float) o[1];
					closest = (VisNetNodeBlockEntity) o[0];					
				}
			}
		}
		if (closest != null) {
			closest.getChildren().add(ref);
			nearbyNodes.clear();
			return new WeakReference<>(closest);
		}

		return null;
	}

	public static final List<WorldCoordinates> cache = new ArrayList<>();
	public static ArrayList<Object[]> findClosestNodes(VisNetNodeBlockEntity target,
			VisNetNodeBlockEntity parent, ArrayList<Object[]> in) {
		
		if (cache.size() > 512 || cache.contains(new WorldCoordinates(parent))) return in;
		cache.add(new WorldCoordinates(parent));
		
		for (WeakReference<VisNetNodeBlockEntity> childWR : parent.getChildren()) {
			VisNetNodeBlockEntity child = childWR.get();

			if (child != null && !child.equals(target) && !child.equals(parent)) {
				float r2 = inRange(child.getLevel(), child.getLocation(),
						target.getLocation(), target.getRange());
				if (r2 > 0) {
					in.add(new Object[] { child, r2 });
				}
				
				in = findClosestNodes(target, child, in);
			}
		}
		return in;
	}

	private static float inRange(Level world, WorldCoordinates cc1,
			WorldCoordinates cc2, int range) {
		float distance = cc1.getDistanceSquaredToWorldCoordinates(cc2);
		return distance > range * range ? -1 : distance;
	}

	public static final Map<WorldCoordinates, ArrayList<WeakReference<VisNetNodeBlockEntity>>> nearbyNodes = new ConcurrentHashMap<>();

	private static void calculateNearbyNodes(Level world, int x, int y, int z) {

		HashMap<WorldCoordinates, WeakReference<VisNetNodeBlockEntity>> sourcelist = sources
				.get(world.dimension());
		if (sourcelist == null) {
			sourcelist = new HashMap<>();
			return;
		}

		ArrayList<WeakReference<VisNetNodeBlockEntity>> cn = new ArrayList<>();
		WorldCoordinates drainer = new WorldCoordinates(x, y, z,
				world.dimension().toString());

		ArrayList<Object[]> nearby = new ArrayList<>();

		for (WeakReference<VisNetNodeBlockEntity> root : sourcelist.values()) {

			VisNetNodeBlockEntity source = root.get();
			if (source == null)
				continue;
			
			VisNetNodeBlockEntity closest = null;
			float range = Float.MAX_VALUE;

			float r = inRange(world, drainer, source.getLocation(),
					source.getRange());
			if (r > 0) {
				range = r;
				closest = source;
			}
			
			List<WeakReference<VisNetNodeBlockEntity>> children = new ArrayList<>();
			children = getAllChildren(source,children);
			
			for (WeakReference<VisNetNodeBlockEntity> child : children) {
				VisNetNodeBlockEntity n = child.get();
				if (n != null && !n.equals(source)) {
					
					float r2 = inRange(n.getLevel(), n.getLocation(),
							drainer, n.getRange());
					if (r2 > 0 && r2 < range) {
						range = r2;
						closest = n;
					}
				}
			}

			if (closest != null) {
				
				cn.add(new WeakReference<>(closest));
			}
		}

		nearbyNodes.put(drainer, cn);
	}
	
	private static List<WeakReference<VisNetNodeBlockEntity>> getAllChildren(VisNetNodeBlockEntity source, List<WeakReference<VisNetNodeBlockEntity>> list) {
		for (WeakReference<VisNetNodeBlockEntity> child : source.getChildren()) {
			VisNetNodeBlockEntity n = child.get();
			
			if (n != null && n.getLevel()!=null && isChunkLoaded(
					n.getLevel(),
					n.getBlockPos().getX(),
					n.getBlockPos().getZ()
			)) {
				list.add(child);
				list = getAllChildren(n,list);
			}
		}
		return list;
	}
	
	public static boolean isChunkLoaded(Level world, int x, int z) {
        return Utils.isChunkLoaded(world, x, z);
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
		 //old method RIP

//		 Level world = source.getLevel();
//		 Vec3 v1 = new Vec3((double) source.xCoord + 0.5D, (double) source.yCoord + 0.5D, (double) source.zCoord + 0.5D);
//		 Vec3 v2 = new Vec3((double) target.xCoord + 0.5D, (double) target.yCoord + 0.5D, (double) target.zCoord + 0.5D);
//		 if (Double.isNaN(v1.xCoord) || Double.isNaN(v1.yCoord) || Double.isNaN(v1.zCoord)) return true;
//		 if (Double.isNaN(v2.xCoord) || Double.isNaN(v2.yCoord) || Double.isNaN(v2.zCoord)) return true;
//		 int x2 = MathHelper.floor_double(v2.xCoord);
//		 int y2 = MathHelper.floor_double(v2.yCoord);
//		 int z2 = MathHelper.floor_double(v2.zCoord);
//		 int x1 = MathHelper.floor_double(v1.xCoord);
//		 int y1 = MathHelper.floor_double(v1.yCoord);
//		 int z1 = MathHelper.floor_double(v1.zCoord);
//		 int maxStep = source.getRange() * 5; // mathematician please help. likely not * 5...
//
//		 while (maxStep-- >= 0) {
//			 if (Double.isNaN(v1.xCoord) || Double.isNaN(v1.yCoord) || Double.isNaN(v1.zCoord)) {
//				 return true;
//			 }
//
//			 if (x1 != x2 || y1 != y2 || z1 != z2) {
//				 boolean xDiff = true;
//				 boolean yDIff = true;
//				 boolean zDiff = true;
//				 double x0 = 999.0D;
//				 double y0 = 999.0D;
//				 double z0 = 999.0D;
//				 if (x2 > x1) {
//					 x0 = (double) x1 + 1.0D;
//				 } else if (x2 < x1) {
//					 x0 = (double) x1 + 0.0D;
//				 } else {
//					 xDiff = false;
//				 }
//
//				 if (y2 > y1) {
//					 y0 = (double) y1 + 1.0D;
//				 } else if (y2 < y1) {
//					 y0 = (double) y1 + 0.0D;
//				 } else {
//					 yDIff = false;
//				 }
//
//				 if (z2 > z1) {
//					 z0 = (double) z1 + 1.0D;
//				 } else if (z2 < z1) {
//					 z0 = (double) z1 + 0.0D;
//				 } else {
//					 zDiff = false;
//				 }
//
//				 double xpercent = 999.0D;
//				 double ypercent = 999.0D;
//				 double zpercent = 999.0D;
//				 double dx = v2.xCoord - v1.xCoord;
//				 double dy = v2.yCoord - v1.yCoord;
//				 double dz = v2.zCoord - v1.zCoord;
//				 if (xDiff) {
//					 xpercent = (x0 - v1.xCoord) / dx;
//				 }
//
//				 if (yDIff) {
//					 ypercent = (y0 - v1.yCoord) / dy;
//				 }
//
//				 if (zDiff) {
//					 zpercent = (z0 - v1.zCoord) / dz;
//				 }
//
//				 byte checkType;
//				 if (xpercent < ypercent && xpercent < zpercent) {
//					 // x changes next
//					 if (x2 > x1) {
//						 checkType = 4;
//					 } else {
//						 checkType = 5;
//					 }
//
//					 v1.xCoord = x0;
//					 v1.yCoord += dy * xpercent;
//					 v1.zCoord += dz * xpercent;
//				 } else if (ypercent < zpercent) {
//					 // y changes next
//					 if (y2 > y1) {
//						 checkType = 0;
//					 } else {
//						 checkType = 1;
//					 }
//
//					 v1.xCoord += dx * ypercent;
//					 v1.yCoord = y0;
//					 v1.zCoord += dz * ypercent;
//				 } else {
//					 // z changes next
//					 if (z2 > z1) {
//						 checkType = 2;
//					 } else {
//						 checkType = 3;
//					 }
//
//					 v1.xCoord += dx * zpercent;
//					 v1.yCoord += dy * zpercent;
//					 v1.zCoord = z0;
//				 }
//
//				 x1 = MathHelper.floor_double(v1.xCoord);
//				 if (checkType == 5) {
//					 --x1;
//				 }
//
//				 y1 = MathHelper.floor_double(v1.yCoord);
//				 if (checkType == 1) {
//					 --y1;
//				 }
//
//				 z1 = MathHelper.floor_double(v1.zCoord);
//				 if (checkType == 3) {
//					 --z1;
//				 }
//
//				 if (x1 == target.xCoord && y1 == target.yCoord && z1 == target.zCoord)
//					 return true;
//
//				 BlockState block = world.getBlockState(new BlockPos(x1, y1, z1));
////				 int meta = world.getBlockMetadata(x1, y1, z1);
//				 if (block.canCollideCheck(meta, false)) {
//					 if (block.getCollisionBoundingBoxFromPool(world, x1, y1, z1) != null) {
//						 HitResult HitResult1 = block.collisionRayTrace(world, x1, y1, z1, v1, v2);
//						 if (HitResult1 != null && HitResult1.typeOfHit != HitResult.MovingObjectType.MISS) {
//							 return false;
//						 }
//					 }
//				 }
//			 }
//		 }
//		 return true;

//		 HitResult mop = ThaumcraftApiHelper.rayTraceIgnoringSource(source.getLevel(),
//				 new Vec3(source.xCoord+.5, source.yCoord+.5,source.zCoord+.5),
//				 new Vec3(target.xCoord+.5, target.yCoord+.5,target.zCoord+.5),
//				 false, true, false);
//		 return  mop == null || (mop.typeOfHit==MovingObjectType.BLOCK &&
//				 mop.blockX==target.xCoord && mop.blockY==target.yCoord && mop.blockZ==target.zCoord);
	 }

	// public static HashMap<WorldCoordinates,WeakReference<VisNetNodeBlockEntity>>
	// noderef = new HashMap<WorldCoordinates,WeakReference<VisNetNodeBlockEntity>>();
	//
	// public static VisNetNodeBlockEntity getClosestNodeWithinRadius(Level world, int x,
	// int y, int z, int radius) {
	// VisNetNodeBlockEntity out = null;
	// WorldCoordinates wc = null;
	// float cd = Float.MAX_VALUE;
	// for (int sx = x - radius; sx <= x + radius; sx++) {
	// for (int sy = y - radius; sy <= y + radius; sy++) {
	// for (int sz = z - radius; sz <= z + radius; sz++) {
	// wc = new WorldCoordinates(sx,sy,sz,world.dimension());
	// if (noderef.containsKey(wc)) {
	// float d = wc.getDistanceSquared(x, y, z);
	// if (d<radius*radius && noderef.get(wc).get()!=null &&
	// !noderef.get(wc).get().isReceiver() &&
	// isNodeValid(noderef.get(wc).get().getParent())
	// ) {
	// out = noderef.get(wc).get();
	// cd = d;
	// }
	// }
	// }
	// }
	// }
	// return out;
	// }

}
