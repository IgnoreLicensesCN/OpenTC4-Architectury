package thaumcraft.common.entities.championmod.impl;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.championmod.ChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.modifier.IClientTickableChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.modifier.IDamageListenerChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.modifier.ITickableChampionModifier;
import thaumcraft.common.lib.network.fx.PacketFXShieldS2C;
import thaumcraft.common.lib.resourcelocations.ChampionModifierResourceLocation;

import java.util.UUID;

import static net.minecraft.tags.DamageTypeTags.DAMAGES_HELMET;
import static net.minecraft.tags.DamageTypeTags.IS_FALL;

public class WardedChampionModifier extends ChampionModifier
        implements
        IClientTickableChampionModifier,
        ITickableChampionModifier,
        IDamageListenerChampionModifier
{
    public WardedChampionModifier(UUID uuid, ChampionModifierResourceLocation id) {
        super(uuid, id);
    }

    @Override
    public void attachToEntity(LivingEntity living) {
        super.attachToEntity(living);

        AttributeInstance attrInstance = living.getAttribute(Attributes.MAX_HEALTH);
        if (attrInstance == null) {
            return;
        }
        int bh = (int) attrInstance.getBaseValue() / 2;
        living.setAbsorptionAmount(living.getAbsorptionAmount() + (float) bh);
    }
    @Override
    public void onClientTick(LivingEntity living) {
        if (living.level().isClientSide()) {
            if (living.level() instanceof ClientLevel clientLevel){
                var random = living.getRandom();
                if (!random.nextBoolean()) {
                    var width = living.getBbWidth();
                    var height = living.getBbHeight();
                    var boundingBox = living.getBoundingBox();
                    float w = random.nextFloat() * width;
                    float d = random.nextFloat() * width;
                    float h = random.nextFloat() * height;
                    ClientFXUtils.drawGenericParticles(clientLevel, boundingBox.minX + (double)w, boundingBox.minY + (double)h, boundingBox.minZ + (double)d, 0.0F, 0.0F, 0.0F, 0.5F + random.nextFloat() * 0.1F, 0.5F + random.nextFloat() * 0.1F, 0.5F + random.nextFloat() * 0.1F, 0.6F, true, 21, 4, 1, 4 + random.nextInt(4), 0, 0.8F + random.nextFloat() * 0.3F);
                }
            }
        }
    }

    @Override
    public void onTick(LivingEntity victim) {
        if (victim.hurtTime <= 0 && victim.tickCount % 25 == 0) {
            var attr = victim.getAttribute(Attributes.MAX_HEALTH);
            if (attr == null) {
                return;
            }
            int bh = (int)attr.getBaseValue() / 2;
            if (victim.getAbsorptionAmount() < (float)bh) {
                victim.setAbsorptionAmount(victim.getAbsorptionAmount() + 1.0F);
            }
        }
    }

    @Override
    public void championModifierOnDamage(LivingEntity victim, DamageSource source, float amountNotReduced, float amount) {
        performEldritchRunicEffectIfAbsorptionExists(victim,source);
    }

    public static void performEldritchRunicEffectIfAbsorptionExists(LivingEntity victim, DamageSource source) {
        if (victim.getAbsorptionAmount() > 0.0F && !victim.level().isClientSide) {
            performEldritchRunicShieldEffectForDamageSource(victim, source);
        }
    }

    public static void performEldritchRunicShieldEffectForDamageSource(LivingEntity victim, DamageSource source) {
        int target = -1;
        if (source.getEntity() != null) {
            target = source.getEntity().getId();
        }

        if (source.is(IS_FALL)) {
            target = -2;
        }

        if (source.is(DAMAGES_HELMET)) {
            target = -3;
        }

        if (victim.level() instanceof ServerLevel serverLevel){
            new PacketFXShieldS2C(victim.getId(), target).sendToAllAround(
                    serverLevel,
                    victim.position(),32*32.
            );
        }else {
            victim.playSound(
                    ThaumcraftSounds.RUNIC_SHIELD_EFFECT, 0.66F, 1.1F + victim.getRandom().nextFloat() * 0.1F
            );
        }
    }
}
