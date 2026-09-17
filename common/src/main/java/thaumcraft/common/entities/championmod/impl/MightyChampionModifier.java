package thaumcraft.common.entities.championmod.impl;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.entities.championmod.ChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.modifier.IClientTickableChampionModifier;
import thaumcraft.common.lib.resourcelocations.ChampionModifierResourceLocation;

import java.util.UUID;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_TOTAL;

public class MightyChampionModifier extends ChampionModifier implements IClientTickableChampionModifier {
    public MightyChampionModifier(UUID uuid, ChampionModifierResourceLocation id) {
        super(uuid, id);
    }

    public static final UUID MIGHTY_BUFF_MODIFIER_UUID = UUID.fromString("7163897f-07f5-49b3-9ce4-b74beb83d2d3");

    public static AttributeModifier getNewMightyBuffModifier() {
        return new AttributeModifier(MIGHTY_BUFF_MODIFIER_UUID, "Mighty damage boost", 3.0F, MULTIPLY_TOTAL);
    }
    @Override
    public void attachToEntity(LivingEntity living) {
        super.attachToEntity(living);

        var attr = living.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attr == null) {
            return;
        }
        attr.removeModifier(MIGHTY_BUFF_MODIFIER_UUID);
        attr.addPermanentModifier(getNewMightyBuffModifier());
    }

    @Override
    public void detachFromEntity(LivingEntity living) {
        super.detachFromEntity(living);
        var attr = living.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attr == null) {
            return;
        }
        attr.removeModifier(MIGHTY_BUFF_MODIFIER_UUID);
    }

    @Override
    public void onClientTick(LivingEntity living) {
        if (living.level().isClientSide()) {
            if (living.level() instanceof ClientLevel clientLevel){
                var random = living.getRandom();
                if (!(random.nextFloat() > 0.3F)) {
                    var width = living.getBbWidth();
                    var height = living.getBbHeight();
                    var boundingBox = living.getBoundingBox();
                    float w = random.nextFloat() * width;
                    float d = random.nextFloat() * width;
                    float h = random.nextFloat() * height;
                    int p = 176 + random.nextInt(4) * 3;
                    ClientFXUtils.drawGenericParticles(
                            clientLevel,
                            boundingBox.minX + (double)w, boundingBox.minY + (double)h, boundingBox.minZ + (double)d, 0.0F, 0.0F, 0.0F, 0.8F + random.nextFloat() * 0.2F, 0.8F + random.nextFloat() * 0.2F, 0.8F + random.nextFloat() * 0.2F, 0.7F, false, p, 3, 1, 4 + random.nextInt(3), 0, 1.0F + random.nextFloat() * 0.3F);
                }
            }
        }
    }
}
