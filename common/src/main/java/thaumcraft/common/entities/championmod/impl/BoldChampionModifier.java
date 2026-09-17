package thaumcraft.common.entities.championmod.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import thaumcraft.client.fx.migrated.particles.FXSpark;
import thaumcraft.common.entities.championmod.ChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.modifier.IClientTickableChampionModifier;
import thaumcraft.common.lib.resourcelocations.ChampionModifierResourceLocation;

import java.util.UUID;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_BASE;
public class BoldChampionModifier extends ChampionModifier implements IClientTickableChampionModifier {
    public BoldChampionModifier(UUID uuid, ChampionModifierResourceLocation id) {
        super(uuid, id);
    }

    public static final UUID BOLD_BUFF_MODIFIER_UUID = UUID.fromString("4b1edd33-caa9-47ae-a702-d86c05701037");

    public static AttributeModifier getNewBoldBuffModifier() {
        return new AttributeModifier(BOLD_BUFF_MODIFIER_UUID, "Bold speed boost", 0.3, MULTIPLY_BASE);
    }
    @Override
    public void attachToEntity(LivingEntity living) {
        super.attachToEntity(living);

        var attr = living.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr == null) {
            return;
        }
        attr.removeModifier(BOLD_BUFF_MODIFIER_UUID);
        attr.addPermanentModifier(getNewBoldBuffModifier());
    }

    @Override
    public void detachFromEntity(LivingEntity living) {
        super.detachFromEntity(living);
        var attr = living.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr == null) {
            return;
        }
        attr.removeModifier(BOLD_BUFF_MODIFIER_UUID);
    }

    @Override
    public void onClientTick(LivingEntity living) {
        if (living.level().isClientSide()) {
            if (living.level() instanceof ClientLevel clientLevel){
                var random = living.getRandom();
                if (!random.nextBoolean()) {
                    var width = living.getBbWidth();
                    var height = living.getBbHeight();
                    float w = random.nextFloat() * width;
                    float d = random.nextFloat() * width;
                    float h = random.nextFloat() * height / 3.0F;
                    FXSpark ef = new FXSpark
                            (clientLevel,
                                    living.getBoundingBox().minX + (double)w,
                                    living.getBoundingBox().minY + (double)h, 
                                    living.getBoundingBox().minZ + (double)d, 0.2F);
                    ef.setRBGColorF(0.3F - random.nextFloat() * 0.1F, 0.0F, 0.8F + random.nextFloat() * 0.2F);
                    Minecraft.getInstance().particleEngine.add(ef);

                }
            }
        }
    }
}
