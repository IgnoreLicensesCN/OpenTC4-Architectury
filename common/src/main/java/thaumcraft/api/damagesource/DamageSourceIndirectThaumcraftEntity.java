package thaumcraft.api.damagesource;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class DamageSourceIndirectThaumcraftEntity /*extends EntityDamageSourceIndirect*/ {

    public static final ResourceKey<DamageType> TC_INDIRECT_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.tryParse("thaumcraft:indirect_damage"));

}
