package thaumcraft.common.entities.championmod;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import thaumcraft.common.lib.resourcelocations.ChampionModifierResourceLocation;

import java.util.UUID;

import static thaumcraft.common.lib.utils.EntityUtils.ThaumcraftAttributeCategoryInstances.CHAMPION_MOD;

public abstract class ChampionModifier {
    public final UUID uuid;
    public final ChampionModifierResourceLocation id;
    protected Component modifierName;
    public ChampionModifier(UUID uuid, ChampionModifierResourceLocation id) {
        this.uuid = uuid;
        this.id = id;
        this.modifierName = Component.translatable("champion_modifier."+id.getNamespace()+"."+id.getPath());
    }
    public Component getModNameLocalized() {
        return modifierName;
    }

    public void attachToEntity(LivingEntity living){
        var attributeInstance = living.getAttribute(CHAMPION_MOD());
        if (attributeInstance != null) {
            attributeInstance.addPermanentModifier(
                    new AttributeModifier(
                            uuid,modifierName.getString(),0,AttributeModifier.Operation.ADDITION
                    )
            );
        }
    }
    public void detachFromEntity(LivingEntity living){
        var attributeInstance = living.getAttribute(CHAMPION_MOD());
        if (attributeInstance != null) {
            attributeInstance.removeModifier(
                    uuid
            );
        }
    }

    public boolean championModifierExistsOnEntity(LivingEntity living) {
        var attributeInstance = living.getAttribute(CHAMPION_MOD());
        if (attributeInstance != null) {
            return attributeInstance.getModifier(uuid) != null;
        }
        return false;
    }
}
