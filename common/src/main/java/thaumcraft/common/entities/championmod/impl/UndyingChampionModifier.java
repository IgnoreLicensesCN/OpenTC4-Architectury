package thaumcraft.common.entities.championmod.impl;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.entities.championmod.ChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.modifier.IClientTickableChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.modifier.IDamageListenerChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.modifier.ITickableChampionModifier;
import thaumcraft.common.lib.resourcelocations.ChampionModifierResourceLocation;

import java.util.UUID;

public class UndyingChampionModifier extends ChampionModifier implements IClientTickableChampionModifier, ITickableChampionModifier {

    public UndyingChampionModifier(UUID uuid, ChampionModifierResourceLocation id) {
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
                            random.nextFloat() * 0.1F+0.1F,
                            random.nextFloat() * 0.8F+0.2F,
                            random.nextFloat() * 0.1F+0.1F,
                            0.9F, true, 21, 4, 1,
                            4 + random.nextInt(4),
                            0,
                            0.5F + random.nextFloat() * 0.2F
                    );
                }
            }
        }
    }

    @Override
    public void onTick(LivingEntity living) {
        if (living.tickCount % 20 == 0) {
            living.heal(1);
        }
    }
}
