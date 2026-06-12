package thaumcraft.api.listeners.aspects.entity.basic;

import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.UnmodifiableAspectView;
import thaumcraft.api.aspects.aspectlists.baseimpl.HashAspectList;

public class EntityBasicAspectGetterManager {

    public static final ListenerManager<EntityBasicAspectGetter> ENTITY_BASIC_ASPECT_GETTERS = new ListenerManager<>();

    //TODO:Entity bonus aspects
    public static @Unmodifiable AspectList<Aspect> getAspectsForEntity(Entity entity){
        var result = new HashAspectList<Aspect>();
        for (var getter:ENTITY_BASIC_ASPECT_GETTERS.getListeners()){
            getter.onGetBasicAspect(entity,result);
        }
        return new UnmodifiableAspectView<>(result);
    }
}
