package com.linearity.opentc4.mixinattachments.overrides.entity.fishinghook;

import com.linearity.annotations.InterfaceShouldAttachTo;
import com.linearity.opentc4.mixin.accessors.EntityAccessor;
import com.linearity.opentc4.mixin.accessors.FishingHookAccessor;
import com.linearity.opentc4.mixinattachments.ILuckOwnerEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;

@InterfaceShouldAttachTo(FishingHook.class)
public interface FishingHookRetrieveOverride {
    default float opentc4$fishingHook$getLuckFrom(Entity entity) {
        if (entity instanceof ILuckOwnerEntity luckOwner) {
            return luckOwner.getLuck();
        }
        if (entity instanceof LivingEntity livingEntity) {
            var attrInstance = livingEntity.getAttribute(Attributes.LUCK);
            if (attrInstance != null) {
                return (float) attrInstance.getValue();
            }
        }
        return 0;
    }

    default int opentc4$fishingHook$retrieve(
            ItemStack fishingRod,
            Operation<Integer> /*(Player playerUsingFishingHook)*/ original /*super*/
    ) {
        var thiz = (FishingHook) this;
        {
            var playerOwner = thiz.getPlayerOwner();
            if (playerOwner != null) {
                return original.call(fishingRod);
            }
        }
        if (thiz instanceof FishingHookShouldStopFishingOverride shouldStopFishingOverride) {
            var owner = thiz.getOwner();
            var accessor = (FishingHookAccessor) thiz;
            if (!thiz.level().isClientSide
                    && owner != null
                    && !shouldStopFishingOverride.opentc4$fishingHook$shouldStopFishing(
                    owner
            )
            ) {
                int i = 0;
                var hookedIn = thiz.getHookedIn();
                if (hookedIn != null) {
                    accessor.pullEntity(hookedIn);
//                    CriteriaTriggers.FISHING_ROD_HOOKED.trigger(owner, fishingRod, thiz, Collections.emptyList());
                    thiz.level().broadcastEntityEvent(thiz, (byte) 31);
                    i = hookedIn instanceof ItemEntity ? 3 : 5;
                } else if (((FishingHookAccessor) thiz).getNibble() > 0) {
                    LootParams lootParams = new LootParams.Builder((ServerLevel) thiz.level())
                            .withParameter(LootContextParams.ORIGIN, thiz.position())
                            .withParameter(LootContextParams.TOOL, fishingRod)
                            .withParameter(LootContextParams.THIS_ENTITY, thiz)
                            .withParameter(LootContextParams.KILLER_ENTITY, owner)/*forge part*/
                            .withLuck(accessor.getLuck() + opentc4$fishingHook$getLuckFrom(owner))
                            .create(LootContextParamSets.FISHING);
                    LootTable lootTable = thiz.level().getServer().getLootData().getLootTable(BuiltInLootTables.FISHING);
                    List<ItemStack> list = lootTable.getRandomItems(lootParams);
                    //TODO:[maybe wont finished] make fishing trigger useable for as many entities as possible
//                    CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer)player, itemStack, thiz, list);

                    for (ItemStack itemStack2 : list) {
                        ItemEntity itemEntity = new ItemEntity(thiz.level(), thiz.getX(), thiz.getY(), thiz.getZ(), itemStack2);
                        double d = owner.getX() - thiz.getX();
                        double e = owner.getY() - thiz.getY();
                        double f = owner.getZ() - thiz.getZ();
                        double g = 0.1;
                        itemEntity.setDeltaMovement(d * 0.1, e * 0.1 + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08, f * 0.1);
                        thiz.level().addFreshEntity(itemEntity);
                        owner.level().addFreshEntity(
                                new ExperienceOrb(
                                        owner.level(),
                                        owner.getX(),
                                        owner.getY() + 0.5,
                                        owner.getZ() + 0.5,
                                        ((EntityAccessor) thiz).opentc4$Entity$getRandom().nextInt(6) + 1));
//                        if (itemStack2.is(ItemTags.FISHES)) {
//                            owner.awardStat(Stats.FISH_CAUGHT, 1);/*player's method*/
//                        }
                    }

                    i = 1;
                }

                if (thiz.onGround()) {
                    i = 2;
                }

                thiz.discard();
                return i;
            }
        }
        return 0;
    }
}
