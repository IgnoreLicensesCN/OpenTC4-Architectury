package thaumcraft.api.aspects.aspect;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;

public interface IScanDiscoverableAspect {
    boolean canLivingDiscover(LivingEntity living);
    @Nullable AspectResourceLocation getOneOfAspectRequiredToDiscover(LivingEntity living);
}
