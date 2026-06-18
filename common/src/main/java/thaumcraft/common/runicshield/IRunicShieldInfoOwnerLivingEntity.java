package thaumcraft.common.runicshield;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IRunicShieldInfoOwnerLivingEntity {
    @Nullable RunicShieldInfo getRunicShieldInfo();
    void setPlayerRunicShieldInfo(@NotNull RunicShieldInfo info);
}
