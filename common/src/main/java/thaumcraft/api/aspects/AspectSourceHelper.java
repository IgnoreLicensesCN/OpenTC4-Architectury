package thaumcraft.api.aspects;

import com.linearity.opentc4.OpenTC4;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import thaumcraft.common.lib.events.EssentiaHandler;

import java.lang.reflect.Method;

public class AspectSourceHelper {

	/**
	 * This method is what is used to drain essentia from jars and other sources for things like 
	 * infusion crafting or powering the arcane furnace. A record ofAspectVisList possible sources are kept track ofAspectVisList
	 * and refreshed as needed around the calling tile entity. This also renders the essentia trail particles.
	 * Only 1 essentia is drained at a time
	 * @param tile the tile entity that is draining the essentia
	 * @param aspect the aspect that you are looking for
	 * @param direction the direction from which you wish to drain. Direction.Unknown simply seeks in all directions. 
	 * @param range how many blocks you wish to search for essentia sources. 
	 * @return boolean returns true if essentia was found and removed from a source.
	 */
	public static boolean drainEssentia(BlockEntity tile, Aspect aspect, Direction direction, int range) {
		return EssentiaHandler.drainEssentia(tile,aspect,direction,range);//we're open and no need for reflection!
	}
	
	/**
	 * This method returns if there is any essentia ofAspectVisList the passed type that can be drained. It in no way checks how
	 * much there is, only if an essentia container nearby contains at least 1 point worth.
	 * @param tile the tile entity that is checking the essentia
	 * @param aspect the aspect that you are looking for
	 * @param direction the direction from which you wish to drain. Direction.Unknown simply seeks in all directions. 
	 * @param range how many blocks you wish to search for essentia sources. 
	 * @return boolean returns true if essentia was found and removed from a source.
	 */
	public static boolean findEssentia(BlockEntity tile, Aspect aspect, Direction direction, int range) {

		return EssentiaHandler.findEssentia(tile, aspect, direction, range);//we're open and no need for reflection!
	}
}
