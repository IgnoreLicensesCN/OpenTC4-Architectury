package thaumcraft.common.entities.abstracts;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

public interface IExplodeOverrideCreeper {
    default void explodeCreeperRewritten(Operation<Void> originalMethod){
        originalMethod.call();
    }
    default void spawnLingeringCloudRewritten(Operation<Void> originalMethod){
        originalMethod.call();
    }
}
