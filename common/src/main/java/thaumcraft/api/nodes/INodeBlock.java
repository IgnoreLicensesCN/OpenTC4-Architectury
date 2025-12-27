package thaumcraft.api.nodes;

public interface INodeBlock {
    default boolean preventAttackFromAnotherNode(){
        return false;
    };
}
