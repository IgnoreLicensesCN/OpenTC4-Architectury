package thaumcraft.common.entities.championmod.impl;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.entities.championmod.ChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.modifier.IClientTickableChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.modifier.IDamageListenerChampionModifier;
import thaumcraft.common.lib.resourcelocations.ChampionModifierResourceLocation;

import java.util.UUID;

public class SpineChampionModifier extends ChampionModifier implements IDamageListenerChampionModifier, IClientTickableChampionModifier {
    public SpineChampionModifier(UUID uuid, ChampionModifierResourceLocation id) {
        super(uuid, id);
    }

    @Override
    public void championModifierOnDamage(LivingEntity victim, DamageSource source, float amountNotReduced, float amount) {
        var target = source.getEntity();
        if (target instanceof LivingEntity && !source.is(DamageTypes.THORNS)) {
            target.hurt(target.level().damageSources().thorns(victim), (float)(1 + victim.getRandom().nextInt(3)));
            target.playSound(SoundEvents.THORNS_HIT, 0.5F, 1.0F);
        }
    }

    @Override
    public void onClientTick(LivingEntity living) {
        var level = living.level();
        if (level.isClientSide()) {
            if (level instanceof ClientLevel clientLevel){
                var random = living.getRandom();
                if (random.nextBoolean()) {
                    var width = living.getBbWidth();
                    var height = living.getBbHeight();
                    var boundingBox = living.getBoundingBox();
                    float w = random.nextFloat() * width;
                    float d = random.nextFloat() * width;
                    float h = random.nextFloat() * height;
                    int p = 176 + random.nextInt(4) * 3;
                    ClientFXUtils.drawGenericParticles(
                            clientLevel,
                            boundingBox.minX + (double)w,
                            boundingBox.minY + (double)h,
                            boundingBox.minZ + (double)d,
                            0.0F, 0.0F, 0.0F,
                            0.5F + random.nextFloat() * 0.2F,
                            0.1F + random.nextFloat() * 0.2F,
                            0.1F + random.nextFloat() * 0.2F,
                            0.7F,
                            false,
                            p,
                            3, 1, 3,
                            0,
                            1.2F + random.nextFloat() * 0.3F
                    );
                }
            }
        }
    }
}
