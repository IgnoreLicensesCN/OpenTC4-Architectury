package com.linearity.opentc4.mixinaccessors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.runicshield.RunicShieldInfo;
import thaumcraft.common.runicshield.IRunicShieldInfoOwnerLivingEntity;

public interface PlayerRunicShieldInfoMixinAccessor extends IRunicShieldInfoOwnerLivingEntity {
    RunicShieldInfo opentc4$getPlayerRunicShieldInfo();
    void opentc4$setPlayerRunicShieldInfo(RunicShieldInfo info);

    @Override
    default @Nullable RunicShieldInfo getRunicShieldInfo(){
        return  opentc4$getPlayerRunicShieldInfo();
    }

    @Override
    default void setPlayerRunicShieldInfo(@NotNull RunicShieldInfo info){
        opentc4$setPlayerRunicShieldInfo(info);
    }
}
