package thaumcraft.common.entities.championmod.abstracts.entity;

import net.minecraft.world.entity.LivingEntity;
import thaumcraft.common.entities.championmod.ChampionModifier;

//todo:for boss
public interface IChampionModifiedNamingRuleOwner {
    void setNameWhenModified(LivingEntity living, ChampionModifier modifier);
}
