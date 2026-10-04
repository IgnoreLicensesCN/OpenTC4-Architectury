package com.linearity.opentc4.mixinaccessors;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

public interface InteractionOverridenMob {
    InteractionResult thaumcraft$interact(Player player, InteractionHand interactionHand);
}
