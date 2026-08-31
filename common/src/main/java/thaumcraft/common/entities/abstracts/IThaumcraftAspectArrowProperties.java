package thaumcraft.common.entities.abstracts;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import thaumcraft.common.entities.projectile.AspectArrowEntity;

public interface IThaumcraftAspectArrowProperties {
    public static final IThaumcraftAspectArrowProperties EMPTY = new IThaumcraftAspectArrowProperties() {};
    default double getDamageMultiplier() {
        return 1;
    }
    default ResourceKey<DamageType> getModifiedDamageType(ResourceKey<DamageType> type) {
        return type;
    }
    default void onArrowHitEntity(AspectArrowEntity arrow, Entity victim) {

    }
}
