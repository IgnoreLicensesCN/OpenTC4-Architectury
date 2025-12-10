package thaumcraft.api.wands;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.linearity.opentc4.recipeclean.blockmatch.BlockMatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class serves a similar function to IWandable in that it allows wands to interact
 * with object in the world. In this case it is most useful for adding interaction with non-mod
 * blocks where you can't control what happens in their code.
 * Example where it is used is in crafting the thaumonomicon from a bookshelf and the
 * crucible from a cauldron
 * 
 * @author azanor
 *
 * However if you can i prefer something like WandInteractableBlock and write logic there
 * --IgnoreLicensesCN
 */
public class WandTriggerRegistry {

	//Map<ModID(String),HashMap<>>
	private static final Map<String, Multimap<BlockMatcher, IWandTriggerManager>> triggers = new ConcurrentHashMap<>();
	private static final String DEFAULT = "default";

	/**
	 * Registers an action to perform when a casting wand right clicks on a specific block. 
	 * A manager class needs to be created that implements IWandTriggerManager.
	 * @param manager
	 * @param blockMatcher matches interacted block
	 * @param modid a unique identifier. It is best to register your own triggers using your mod id to avoid conflicts with mods that register triggers for the same block
	 */
	public static void registerWandBlockTrigger(IWandTriggerManager manager, BlockMatcher blockMatcher, String modid) {
		Multimap<BlockMatcher,IWandTriggerManager> temp = triggers.computeIfAbsent(modid, k -> HashMultimap.create());
		temp.put(blockMatcher,manager);
	}
	
	/**
	 * for legacy support
	 */
	public static void registerWandBlockTrigger(IWandTriggerManager manager, BlockMatcher block) {
		registerWandBlockTrigger(manager, block, DEFAULT);
	}

	public static List<IWandTriggerManager> matchesTrigger(BlockState blockState) {
		List<IWandTriggerManager> result = new ArrayList<>();
		for (var matcherToTriggers : triggers.values()) {
			for (var matcher : matcherToTriggers.keySet()) {
				if (matcher.match(blockState)){
					result.addAll(matcherToTriggers.get(matcher));
				}
			}
		}
		return result;
	}

	public static List<IWandTriggerManager> matchesTrigger(BlockState blockState,String modid) {
		List<IWandTriggerManager> result = new ArrayList<>();
		var matcherToTriggers = triggers.get(modid);

		for (var matcher : matcherToTriggers.keySet()) {
			if (matcher.match(blockState)){
				result.addAll(matcherToTriggers.get(matcher));
			}
		}
		return result;
	}
	/**
	 * Checks all trigger registries if one exists for the given block and meta
	 */
	public static boolean hasTrigger(BlockMatcher matcher) {
		for (String modid:triggers.keySet()) {
			if (triggers.get(modid).containsKey(matcher)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * modid sensitive version
	 */
	public static boolean hasTrigger(BlockMatcher matcher, String modid) {
		if (!triggers.containsKey(modid)) return false;
        return triggers.get(modid).containsKey(matcher);
    }

//	/**
//	 * This is called by the onItemUseFirst function in wands.
//	 * Parameters and return value functions like you would expect for that function.
//	 * @param world
//	 * @param wand
//	 * @param player
//	 * @param x
//	 * @param y
//	 * @param z
//	 * @param side
//	 * @return true if consumed trigger
//	 */
//	public static boolean performTrigger(Level world, ItemStack wand, Player player,
//			int x, int y, int z, Direction side) {
//		BlockState state = world.getBlockState(new BlockPos(x, y, z));
//		for (String modid:triggers.keySet()) {
//			Multimap<BlockMatcher,IWandTriggerManager> matcherAndListeners = triggers.get(modid);
//
//			Collection<IWandTriggerManager> managers;
//			for (BlockMatcher matcher:matcherAndListeners.keySet()) {
//				if (matcher.match(state)){
//					managers = matcherAndListeners.get(matcher);
//					for (IWandTriggerManager manager:managers) {
//						if (manager.performTrigger(world, wand, player, x, y, z, side)){
//							return true;
//						}
//					}
//				}
//			}
//		}
//		return false;
//	}
//
//	/**
//	 * modid sensitive version
//	 */
//	public static boolean performTrigger(Level world, ItemStack wand, Player player,
//										 int x, int y, int z, Direction side, String modid) {
//		if (!triggers.containsKey(modid)) return false;
//		BlockState state = world.getBlockState(new BlockPos(x, y, z));
//		Multimap<BlockMatcher,IWandTriggerManager> matcherAndListeners = triggers.get(modid);
//
//		Collection<IWandTriggerManager> managers;
//		for (BlockMatcher matcher:matcherAndListeners.keySet()) {
//			if (matcher.match(state)){
//				managers = matcherAndListeners.get(matcher);
//				for (IWandTriggerManager manager:managers) {
//					if (manager.performTrigger(world, wand, player, x, y, z, side)){
//						return true;
//					}
//				}
//			}
//		}
//		return false;
//	}
}
