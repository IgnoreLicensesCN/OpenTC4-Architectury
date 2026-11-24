package com.linearity.opentc4.mixin.thaumcraftspecial;

import com.linearity.opentc4.mixinattachments.supercallproviders.entity.ThaumcraftFishingHookSuperAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.entities.golems.ThaumcraftFishingHook;

@Mixin(ThaumcraftFishingHook.class)
public class ThaumcraftFishingHookMixin extends FishingHook implements ThaumcraftFishingHookSuperAccessor {
    private ThaumcraftFishingHookMixin(EntityType<? extends FishingHook> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean opentc4$fishingHookSuper$canHitEntity(Entity beingHit) {
        return super.canHitEntity(beingHit);
    }

}
