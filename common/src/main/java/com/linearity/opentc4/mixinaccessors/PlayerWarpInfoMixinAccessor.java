package com.linearity.opentc4.mixinaccessors;

import org.jetbrains.annotations.Nullable;
import thaumcraft.api.warp.IWarpInfoOwnerLivingEntity;
import thaumcraft.api.warp.WarpInfo;

public interface PlayerWarpInfoMixinAccessor extends IWarpInfoOwnerLivingEntity {
    WarpInfo opentc4$getWarpInfo();//TODO:[maybe wont finished]also for maid(wtf you want) but it may not possible unless im not so lazy to sync warp
    void opentc4$setWarpInfo(WarpInfo warpInfo);

    @Override
    default @Nullable WarpInfo getWarpInfo(){
        return  opentc4$getWarpInfo();
    }
    default void setWarpInfo(@Nullable WarpInfo warpInfo){
        opentc4$setWarpInfo(warpInfo);
    }
}
