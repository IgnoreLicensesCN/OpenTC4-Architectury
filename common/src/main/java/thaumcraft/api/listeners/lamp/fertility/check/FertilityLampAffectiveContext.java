package thaumcraft.api.listeners.lamp.fertility.check;

import com.linearity.opentc4.utils.collectionlike.parameterizedtypechaos.EntityCategoryToEntitiesMap;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.entity.Entity;

@RequiredArgsConstructor
public class FertilityLampAffectiveContext {
    public final Entity entity;
    public final EntityCategoryToEntitiesMap entityMap;
    public boolean endCheck = false;
    public boolean recommendAffective = false;

}
