package thaumcraft.common.entities.abstracts;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public interface IMobAttackDamageTypeReplaceable {
    ResourceKey<DamageType> replaceDamageTypeWith();
}
