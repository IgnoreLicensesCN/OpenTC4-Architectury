package thaumcraft.api.nodes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.IWorldlyCentiVisContainerBlockEntity;
import thaumcraft.common.lib.resourcelocations.NodeLockResourceLocation;

public interface INodeBlockEntity extends IWorldlyCentiVisContainerBlockEntity<Aspect> {

	/**
	 * Unique identifier to distinguish nodes. Normal node id's are based on world id and coordinates
	 * @return
	 */
    String getId();
	
	AspectList<Aspect> getAspectsBase();
	
	/**
	 * Return the type of node
	 * @return
	 */
    NodeType getNodeType();

	/**
	 * Set the type of node
	 * @return
	 */
    void setNodeType(NodeType nodeType);

	/**
	 * Set the node modifier
	 * @return
	 */
    void setNodeModifier(NodeModifier nodeModifier);
	
	/**
	 * Return the node modifier
	 * @return
	 */
    NodeModifier getNodeModifier();
		
	/**
	 * Return the maximum capacity of each aspect the node can hold
	 * @return
	 */
    int getNodeVisBase(Aspect aspect);

	/**
	 * Set the maximum capacity of each aspect the node can hold
	 * @return
	 */
    void setNodeVisBase(Aspect aspect, short nodeVisBase);

	@Nullable INodeLockBlock getCurrentNodeLock();

	@NotNull BlockPos getNodeLockPos();

	Level getLevel();

	BlockPos getBlockPos();


	void setRegenerationTickPeriod(int regenerationTickPeriod);
	int getRegenerationTickPeriod();

	void setChanged();
	BlockState getBlockState();
	NodeLockResourceLocation getLockId();
	Aspect takeRandomPrimalFromSource();
	int getTickCount();
	void nodeChange();

	void setWait(int wait);
}
