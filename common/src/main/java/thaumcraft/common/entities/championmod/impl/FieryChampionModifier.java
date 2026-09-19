package thaumcraft.common.entities.championmod.impl;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.entities.championmod.ChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.modifier.IAttackListenerChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.modifier.IClientTickableChampionModifier;
import thaumcraft.common.lib.resourcelocations.ChampionModifierResourceLocation;

import java.util.UUID;

public class FieryChampionModifier extends ChampionModifier implements IClientTickableChampionModifier, IAttackListenerChampionModifier {

    public FieryChampionModifier(UUID uuid, ChampionModifierResourceLocation id) {
        super(uuid, id);
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
                    ClientFXUtils.drawGenericParticles(
                            clientLevel,
                            boundingBox.minX + (double)w,
                            boundingBox.minY + (double)h,
                            boundingBox.minZ + (double)d,
                            0.0F, 0.03, 0.0F,
                            random.nextFloat() * 0.1F+0.9F,
                            1,
                            1,
                            0.7F, false, 160, 10, 1,
                            8 + random.nextInt(4),
                            0,
                            0.7F + random.nextFloat() * 0.2F
                    );
                }
            }
        }
    }

    @Override
    public void championModifierOnAttack(LivingEntity victim,LivingEntity attacker, DamageSource source, float amountNotReduced, float amount) {
        var random = attacker.getRandom();
        if (random.nextFloat() < 0.4F) {
            victim.setSecondsOnFire(4);
        }
    }

}
