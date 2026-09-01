package thaumcraft.common.entities.abstracts;

public interface ISpiderWithoutSkeletonRiding {
    default boolean canBeRiddenBySkeletonWhenSpawn(){
        return false;
    }
}
