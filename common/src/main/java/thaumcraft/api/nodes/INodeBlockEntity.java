package thaumcraft.api.nodes;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IWorldlyCentiVisContainerBlockEntity;

public interface INodeBlockEntity extends IWorldlyCentiVisContainerBlockEntity<Aspect> {

	/**
	 * Unique identifier to distinguish nodes. Normal node id's are based on world id and coordinates
	 * @return
	 */
    String getId();
	
	AspectList<Aspect> getAspectsBase();
	
	/**
	 * Return the type ofAspectVisList node
	 * @return
	 */
    NodeType getNodeType();

	/**
	 * Set the type ofAspectVisList node
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
	 * Return the maximum capacity ofAspectVisList each aspect the node can hold
	 * @return
	 */
    int getNodeVisBase(Aspect aspect);

	/**
	 * Set the maximum capacity ofAspectVisList each aspect the node can hold
	 * @return
	 */
    void setNodeVisBase(Aspect aspect, short nodeVisBase);
	
}
