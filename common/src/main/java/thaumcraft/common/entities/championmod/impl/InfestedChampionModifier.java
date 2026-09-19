package thaumcraft.common.entities.championmod.impl;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.championmod.ChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.modifier.IClientTickableChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.modifier.IDamageListenerChampionModifier;
import thaumcraft.common.entities.monster.tainted.converted.TaintedSpiderEntity;
import thaumcraft.common.lib.resourcelocations.ChampionModifierResourceLocation;

import java.util.UUID;

public class InfestedChampionModifier extends ChampionModifier implements IClientTickableChampionModifier, IDamageListenerChampionModifier {

    public InfestedChampionModifier(UUID uuid, ChampionModifierResourceLocation id) {
        super(uuid, id);
    }


    @Override
    public void onClientTick(LivingEntity living) {
        if (living.getRandom().nextBoolean()) {
            ClientFXUtils.slimeJumpFX(living, 0);
        }
    }

    @Override
    public void championModifierOnDamage(LivingEntity victim, DamageSource source, float amountNotReduced, float amount) {
        var level = victim.level();
        var random = victim.getRandom();
        if (random.nextFloat() < 0.4F && !level.isClientSide) {
            var spider = new TaintedSpiderEntity(level);
            spider.setPos(
                    victim.getX(),
                    victim.getEyeY(),
                    victim.getZ()
            );
            spider.setXRot(random.nextFloat() * 360.0F);
            spider.setYRot(0);
            level.addFreshEntity(spider);
            victim.playSound(ThaumcraftSounds.GORE, 0.5F, 1.0F);
        }
    }
}
