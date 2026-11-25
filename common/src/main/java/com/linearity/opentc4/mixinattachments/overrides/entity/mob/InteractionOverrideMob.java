package com.linearity.opentc4.mixinattachments.overrides.entity.mob;

import com.linearity.annotations.InterfaceShouldAttachTo;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

@InterfaceShouldAttachTo(Mob.class)
public interface InteractionOverrideMob {
    InteractionResult thaumcraft$interact(Player player, InteractionHand interactionHand);
}
