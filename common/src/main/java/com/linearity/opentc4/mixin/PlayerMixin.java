package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinaccessors.PlayerRunicShieldInfoMixinAccessor;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.runicshield.PlayerRunicShieldInfo;

@Mixin(Player.class)
public class PlayerMixin implements PlayerRunicShieldInfoMixinAccessor {
    @Unique
    private final PlayerRunicShieldInfo opentc4$playerRunicShieldInfo = new PlayerRunicShieldInfo((Player)(Object)this);

    @Override
    public PlayerRunicShieldInfo opentc4$getPlayerRunicShieldInfo() {
        return opentc4$playerRunicShieldInfo;
    }
}
