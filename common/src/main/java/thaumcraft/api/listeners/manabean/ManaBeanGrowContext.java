package thaumcraft.api.listeners.manabean;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.tiles.generated.ManaBeanBlockEntity;

public class ManaBeanGrowContext {
    public @NotNull final ManaBeanBlockEntity manaBeanBE;
    public final int growthStage;
    public final int maxGrowthStage;
    public final int minGrowthStage;
    public final Direction facing;

    public ManaBeanGrowContext(@NotNull ManaBeanBlockEntity manaBeanBE, int growthStage, int minGrowthStage, int maxGrowthStage, Direction facing) {
        this.manaBeanBE = manaBeanBE;
        this.growthStage = growthStage;
        this.maxGrowthStage = maxGrowthStage;
        this.minGrowthStage = minGrowthStage;
        this.facing = facing;
    }
}
