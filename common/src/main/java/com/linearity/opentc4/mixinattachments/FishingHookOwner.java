package com.linearity.opentc4.mixinattachments;

import com.linearity.annotations.Modifiable;
import net.minecraft.world.entity.projectile.FishingHook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Supplier;

public interface FishingHookOwner {
    void opentc4$onUpdateFishingHookOwning(FishingHook fishingHook);
    @NotNull
    @Modifiable
    Set<Supplier<@Nullable FishingHook>> opentc4$getFishingHooks();
    void opentc4$maybeClearFishingHooksOnRemoved();
}
