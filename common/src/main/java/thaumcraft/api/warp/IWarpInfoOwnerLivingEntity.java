package thaumcraft.api.warp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IWarpInfoOwnerLivingEntity {
    @Nullable WarpInfo getWarpInfo();
    void setWarpInfo(@NotNull WarpInfo warpInfo);
}
