package thaumcraft.common.entities.championmod.impl;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.entities.championmod.ChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.modifier.IClientTickableChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.modifier.IDamageModifierChampionModifier;
import thaumcraft.common.lib.resourcelocations.ChampionModifierResourceLocation;

import java.util.UUID;

public class ArmoredChampionModifier extends ChampionModifier implements IDamageModifierChampionModifier, IClientTickableChampionModifier {
    public ArmoredChampionModifier(UUID uuid, ChampionModifierResourceLocation id) {
        super(uuid, id);
    }

    @Override
    public float championModifierOnModifyDamage(LivingEntity victim, DamageSource damageSource, float amount) {
        if (
                !(
                        !(damageSource.is(DamageTypeTags.IS_EXPLOSION)
                                || damageSource.is(DamageTypeTags.IS_FIRE))
                                && damageSource.is(DamageTypeTags.BYPASSES_ARMOR)
                )
        ) {
            float f1 = amount * 19.0F;
            amount = f1 / 25.0F;
        }
        return amount;
    }

    @Override
    public void onClientTick(LivingEntity living) {
        var level = living.level();
        if (level.isClientSide()) {
            if (level instanceof ClientLevel clientLevel) {
                var random = living.getRandom();
                if (random.nextInt(4) == 0) {
                    var width = living.getBbWidth();
                    var height = living.getBbHeight();
                    float w = random.nextFloat() * width;
                    float d = random.nextFloat() * height;
                    float h = random.nextFloat() * height;
                    ClientFXUtils.drawGenericParticles(
                            clientLevel,
                            living.getBoundingBox().minX + (double) w,
                            living.getBoundingBox().minY + (double) h,
                            living.getBoundingBox().minZ + (double) d,
                            0.0F, 0.0F, 0.0F,
                            0.9F, 0.9F,
                            0.9F + random.nextFloat() * 0.1F, 0.7F,
                            false,
                            112,
                            9,
                            1,
                            5 + random.nextInt(4), 0, 0.6F + random.nextFloat() * 0.2F
                    );
                }
            }
        }
    }
}
